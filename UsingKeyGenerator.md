# Using ` @KeyGenerator ` #

[@Cacheable](UsingCacheable.md) and [@TriggersRemove](UsingTriggersRemove.md) annotations support a ` keyGenerator ` property. This allows for the inline configuration of a [CacheKeyGenerator](http://ehcache-spring-annotations.googlecode.com/svn/site/current/apidocs/com/googlecode/ehcache/annotations/key/CacheKeyGenerator.html). This approach is recommended over the ` keyGeneratorName ` annotation as more of the caching configuration is inline in the class.

Example inline key generator configuration:
```
@Cacheable(cacheName="weather", 
    keyGenerator = @KeyGenerator (
            name = "ListCacheKeyGenerator",
            properties = {
                    @Property( name="useReflection", value="true" ),
                    @Property( name="checkforCycles", value="true" ),
                    @Property( name="includeMethod", value="false" )
            }
        )
    )
public Weather getWeather(WeatherKey key);
```
This example will result in a [ListCacheKeyGenerator](http://ehcache-spring-annotations.googlecode.com/svn/site/1.1.0-RC1/apidocs/com/googlecode/ehcache/annotations/key/ListCacheKeyGenerator.html) being used for key generation for calls to the ` getWeather(WeatherKey) ` method. The [useReflection](http://ehcache-spring-annotations.googlecode.com/svn/site/1.1.0-RC1/apidocs/com/googlecode/ehcache/annotations/key/AbstractDeepCacheKeyGenerator.html#setUseReflection(boolean)) and [checkforCycles](http://ehcache-spring-annotations.googlecode.com/svn/site/1.1.0-RC1/apidocs/com/googlecode/ehcache/annotations/key/AbstractCacheKeyGenerator.html#setCheckforCycles(boolean)) properties will be set to ` true `. The [includeMethod](http://ehcache-spring-annotations.googlecode.com/svn/site/1.1.0-RC1/apidocs/com/googlecode/ehcache/annotations/key/AbstractCacheKeyGenerator.html#setIncludeMethod(boolean)) will be set to ` false `.


### @KeyGenerator Properties ###
| **Property** | **Type** | **Description** |
|:-------------|:---------|:----------------|
| name         | String   | The name of the CacheKeyGenerator class. If no package is specified the [com.googlecode.ehcache.annotations.key](http://ehcache-spring-annotations.googlecode.com/svn/site/current/apidocs/com/googlecode/ehcache/annotations/key/package-summary.html) package is assumed. |
| properties   | List<[@Property](http://ehcache-spring-annotations.googlecode.com/svn/site/1.1.0-RC1/apidocs/com/googlecode/ehcache/annotations/Property.html)> | A list of properties to set on the key generator. |

### @!Property Properties ###
| **Property** | **Type** | **Description** |
|:-------------|:---------|:----------------|
| name         | String   | The name of the Java Bean property to set |
| value        | String   | The value to set into the bean property, any String type conversion that Spring supports is supported here, if specified ` ref ` may not be set. |
| ref          | String   | The name of a bean in your application context, a reference to that bean is injected into the key generator, if specified ` value ` may not be set. |

### Lifecycle ###
CacheKeyGenerators configured in this manner are created as Spring beans in a child ApplicationContext of the ApplicationContext the ` <ehcache:annotation-driven/> ` element is in. There for CacheKeyGenerators can implement Spring context lifecycle interfaces such as InitializingBean, DisposableBean, and ApplicationContextAware.

CacheKeyGenerator implementations are assumed to be thread-safe. Only one instance of each bean will be created for each unique combination of @Property annotations.