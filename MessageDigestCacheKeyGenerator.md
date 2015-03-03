| **JavaDoc** | [MessageDigestCacheKeyGenerator](http://ehcache-spring-annotations.googlecode.com/svn/site/current/apidocs/com/googlecode/ehcache/annotations/key/MessageDigestCacheKeyGenerator.html) |
|:------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|

Does the same object graph traversal that HashCodeCacheKeyGenerator does but instead of accumulating the hash code in a long a [MessageDigest](http://java.sun.com/j2se/1.5.0/docs/api/java/security/MessageDigest.html) is used. This results in a much larger key space and the use of a cryptographic hashing function for better key distribution.

### The Importance of hashCode ###
This key generator completely depends on all arguments implementing hashCode correctly. Most IDEs can auto-generate hashCode methods, for additional guidance on implementing hashCode reading [Chapter 3 of Effective Java](http://java.sun.com/developer/Books/effectivejava/Chapter3.pdf).

### Special Object Handling ###
The following objects have special handling for generation of their hash codes.
  * **Class** - Uses the digest of [Class.getName()](http://java.sun.com/j2se/1.5.0/docs/api/java/lang/Class.html#getName()) since Class just uses the Object identity hashCode.
  * **Enum**  - Uses the digest of Enum.getClass().getName() and of [Enum.name()](http://java.sun.com/j2se/1.5.0/docs/api/java/lang/Enum.html#name())

### Generator Notes ###
  * Any supported [MessageDigest](http://java.sun.com/j2se/1.5.0/docs/api/java/security/MessageDigest.html) algorithm can be used. The result is always a URL safe base64 encoding of the hash bytes.
  * The default digest algorithm is SHA-1
  * Keys generated should be consistent across JVM restarts or between JVM instances as long as all objects used as method arguments correctly implement hashCode.
  * If the **useReflection** property is set objects that don't implement hashCode are reflected on.

### Example Key ###
The key ` "FKeW4z_I5_yc_z9J98GmaM4aWSU" ` would be generated for the following example call with the default options:
```
//@Cacheable annotated method
dao.getWeather("49931");
```

Example dao:
```
class WeatherDao {
    @Cacheable(cacheName="weatherCache", @KeyGenerator(name="MessageDigestCacheKeyGenerator"))
    Weather getWeather(String zip) {
        ...
    }
}
```