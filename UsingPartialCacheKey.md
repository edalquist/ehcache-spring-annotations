# Using ` @PartialCacheKey ` #

` @PartialCacheKey ` is a parameter level annotation that can be used in conjunction with [@Cacheable](UsingCacheable.md) and [@TriggersRemove](UsingTriggersRemove.md) to specify that only certain method arguments should be included in the cache key generation.

For example with the two methods defined by the ` WeatherDao ` interface getting [@Cacheable](UsingCacheable.md) and [@TriggersRemove](UsingTriggersRemove.md) annotations to work together would require a custom key generator without using ` @PartialCacheKey `. This is because the ` getWeather ` method defines a single ` String ` parameter for the zip code but ` updateWeather ` has both the zip code and the ` Weather ` object.

Using ` @PartialCacheKey ` on ` updateWeather ` tells the key generator to only include that
field in the generated key resulting in the two methods generating the same keys. Note that ` includeMethod ` is also disabled for key generation on both methods as by default keys are scoped to the annotated method.
```
public interface WeatherDao {
    
    public Weather getWeather(String zipCode);
    
    public void updateWeather(String zipCode, Weather weather);
}

public class DefaultWeatherDao implements WeatherDao {
    
    @Cacheable(cacheName="weatherCache", 
        keyGenerator = @KeyGenerator (
                name = "ListCacheKeyGenerator",
                properties = @Property( name="includeMethod", value="false"
            )
        )
    )
    public Weather getWeather(String zipCode) {
        //Some Code
    }
    
    @TriggersRemove(cacheName="weatherCache", 
        keyGenerator = @KeyGenerator (
                name = "ListCacheKeyGenerator",
                properties = @Property( name="includeMethod", value="false"
            )
        )
    )
    public void updateWeather(@PartialCacheKey String zipCode, Weather weather){
        //Some Code
    }
}
```