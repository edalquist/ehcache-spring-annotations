# DEPRECATED #
ReflectionHashCodeCacheKeyGenerator is deprecated as of 1.1.0. All key generators support reflection in 1.1.0

| **JavaDoc** | [ReflectionHashCodeCacheKeyGenerator](http://ehcache-spring-annotations.googlecode.com/svn/site/current/apidocs/com/googlecode/ehcache/annotations/key/ReflectionHashCodeCacheKeyGenerator.html) |
|:------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Default Bean Name** | [ReflectionHashCodeCacheKeyGenerator.DEFAULT\_BEAN\_NAME](http://ehcache-spring-annotations.googlecode.com/svn/site/current/apidocs/com/googlecode/ehcache/annotations/key/ReflectionHashCodeCacheKeyGenerator.html#DEFAULT_BEAN_NAME) |

Does the same object graph traversal as HashCodeCacheKeyGenerator does but uses reflection to generate a hash code for objects that don't implement hashCode. For each object visited reflection is used to find the hashCode method. If the hashCode method only exists on the base Object reflection is then used to recurse on every non-transient, non-static field in the object and generate it's hashCode.

### Special Object Handling ###
The following objects have special handling for generation of their hash codes.
  * **Class** - Uses the hashCode of [Class.getName()](http://java.sun.com/j2se/1.5.0/docs/api/java/lang/Class.html#getName()) since Class just uses the Object identity hashCode.
  * **Enum**  - Uses the hashCode of Enum.getClass().getName() and of [Enum.name()](http://java.sun.com/j2se/1.5.0/docs/api/java/lang/Enum.html#name())

### Generator Notes ###
  * This key generator is the slowest included in the library.
  * With only 64 bits of key space there some potential for key collisions, especially since the Java hash code implementation is not a cryptographic hash code.
  * Keys generated should be consistent across JVM restarts or between JVM instances as long as all objects used as method arguments correctly implement hashCode.

### Example Key ###

The key ` -78777307802668 ` would be generated for the following example call with the default options:
```
private static class WeatherId {
    private final String id;

    public WeatherId(String id) {
        this.id = id;
    }
}

//@Cacheable annotated method
dao.getWeather(new WeatherId("49931"));
```

Example dao:
```
class WeatherDao {
    @Cacheable(cacheName="weatherCache", keyGeneratorName=HashCodeCacheKeyGenerator.DEFAULT_BEAN_NAME)
    Weather getWeather(String zip) {
        ...
    }
}
```

Note in the example WeatherId does not implement hashCode but a consistent key is still generated.