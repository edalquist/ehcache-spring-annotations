| **JavaDoc** | [ListCacheKeyGenerator](http://ehcache-spring-annotations.googlecode.com/svn/site/current/apidocs/com/googlecode/ehcache/annotations/key/ListCacheKeyGenerator.html) |
|:------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------|

Does a deep inspection converting arrays to Lists and returning an immutable, Serializable, List of the arguments as the key. Useful if complete equality assurance is required when doing key comparisons as the original arguments are used for every key comparison.

### The Importance of hashCode and equals ###
This key generator completely depends on all arguments implementing hashCode and equals correctly. Most IDEs can auto-generate hashCode and equals methods, for additional guidance on implementing hashCode and equals reading [Chapter 3 of Effective Java](http://java.sun.com/developer/Books/effectivejava/Chapter3.pdf).

### Serializable Arguments ###
For use of the disk store or clustering options in EhCache all arguments must be completely Serializable.

### Generator Notes ###
  * This generator creates keys quickly but comparing keys is much slower as the Lists have to be iterated every time equals is called.
  * The hashCode result is cached by the immutable List that is returned.
  * The argument values must not change in a way that affects their hashCode or equals operations. If they change the cached value will likely be 'lost' in the cache.
  * Keys generated should be consistent across JVM restarts or between JVM instances as long as all objects used as method arguments implement toString such that the object's identity is included.
  * If the **useReflection** property is set objects that don't implement hashCode or equals are reflected on.


### Example Key ###
The key following example call with the default options:
```
//@Cacheable annotated method
dao.getWeather("49931");
```

Example dao:
```
class WeatherDao {
    @Cacheable(cacheName="weatherCache", @KeyGenerator(name="ListCacheKeyGenerator"))
    Weather getWeather(String zip) {
        ...
    }
}
```

Would generate a key that would pass hashCode and equals comparisons with:
```
Arrays.asList(
    x.y.z.WeatherDaoImpl.class,
    "getWeather",
    x.y.z.Weather.class,
    Arrays.asList(String.class),
    Arrays.asList("49931"));
```