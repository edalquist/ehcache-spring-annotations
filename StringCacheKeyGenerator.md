| **JavaDoc** | [StringCacheKeyGenerator](http://ehcache-spring-annotations.googlecode.com/svn/site/current/apidocs/com/googlecode/ehcache/annotations/key/StringCacheKeyGenerator.html) |
|:------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|

Uses an algorithm similar to [Arrays.deepToString](http://java.sun.com/j2se/1.5.0/docs/api/java/util/Arrays.html#deepToString(java.lang.Object%5B%5D)) to generate a generally human readable key.

### The Importance of toString ###
This key generator completely depends on all arguments implementing toString such that the object's identity is included. Most IDEs can auto-generate toString methods, for additional guidance on implementing toString reading [Chapter 3 of Effective Java](http://java.sun.com/developer/Books/effectivejava/Chapter3.pdf).

### Generator Notes ###
  * For arguments that implement toString in such a way that their identity is written out this generator is more collision resistant than the HashCodeCacheKeyGenerator
  * Keys generated should be consistent across JVM restarts or between JVM instances as long as all objects used as method arguments implement toString correctly.
  * If the **useReflection** property is set objects that don't implement toString are reflected on.

### Example Key ###
The key ` "[class x.y.z.WeatherDaoImpl, getWeather class x.y.z.Weather, [class java.lang.String], [49931]]" ` would be generated for the following example call with the default options:
```
//@Cacheable annotated method
dao.getWeather("49931");
```


Example dao:
```
class WeatherDao {
    @Cacheable(cacheName="weatherCache", keyGenerator=@KeyGenerator(name="StringCacheKeyGenerator"))
    Weather getWeather(String zip) {
        ...
    }
}
```