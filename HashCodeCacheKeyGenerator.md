| **JavaDoc** | [HashCodeCacheKeyGenerator](http://ehcache-spring-annotations.googlecode.com/svn/site/current/apidocs/com/googlecode/ehcache/annotations/key/HashCodeCacheKeyGenerator.html) |
|:------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|

Generates a simple 64bit hash code based on the method arguments. The hash code generation algorithm is very close to [Arrays.deepHashCode](http://java.sun.com/j2se/1.5.0/docs/api/java/util/Arrays.html#deepHashCode(java.lang.Object%5B%5D)). The only major differences being the use of a long instead of an int and that Collections and Maps are recursed on the same way deepHashCode recurses on arrays.

### The Importance of hashCode ###
This key generator completely depends on all arguments implementing hashCode correctly. Most IDEs can auto-generate hashCode methods, for additional guidance on implementing hashCode reading [Chapter 3 of Effective Java](http://java.sun.com/developer/Books/effectivejava/Chapter3.pdf).

### Special Object Handling ###
The following objects have special handling for generation of their hash codes.
  * **Class** - Uses the hashCode of [Class.getName()](http://java.sun.com/j2se/1.5.0/docs/api/java/lang/Class.html#getName()) since Class just uses the Object identity hashCode.
  * **Enum**  - Uses the hashCode of Enum.getClass().getName() and of [Enum.name()](http://java.sun.com/j2se/1.5.0/docs/api/java/lang/Enum.html#name())

### Generator Notes ###
  * This is the fastest provided key generator.
  * With only 64 bits of key space there some potential for key collisions, especially since the Java hash code implementation is not a cryptographic hash code.
  * Keys generated should be consistent across JVM restarts or between JVM instances as long as all objects used as method arguments correctly implement hashCode.
  * If the **useReflection** property is set objects that don't implement hashCode are reflected on.

### Example Key ###
The key ` -78777307802699 ` would be generated for the following example call with the default options:
```
dao.getWeather("49931");
```

Example dao:
```
class WeatherDao {
    @Cacheable(cacheName="weatherCache", @KeyGenerator(name="HashCodeCacheKeyGenerator"))
    Weather getWeather(String zip) {
        ...
    }
}
```