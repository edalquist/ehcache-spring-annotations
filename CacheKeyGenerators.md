# Introduction #

Every method invocation advised by [@Cacheable](UsingCacheable.md) or [@TriggersRemove](CacheableTriggersRemove.md) has to have a key generated for use when accessing the corresponding cache. The [CacheKeyGenerator](http://ehcache-spring-annotations.googlecode.com/svn/site/current/apidocs/com/googlecode/ehcache/annotations/key/CacheKeyGenerator.html) interface defines the API for generating these keys.

# Provided Cache Key Generators #
|**HashCodeCacheKeyGenerator**| The default key generator. Very similar to [Arrays.deepHashCode](http://java.sun.com/j2se/1.5.0/docs/api/java/util/Arrays.html#deepHashCode(java.lang.Object%5B%5D)) except generates 64 bit keys instead of 32 bit |
|:----------------------------|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|**StringCacheKeyGenerator**  | A nice human readable key generator. Very similar to [Arrays.deepToString](http://java.sun.com/j2se/1.5.0/docs/api/java/util/Arrays.html#deepToString(java.lang.Object%5B%5D))                                      |
|**ListCacheKeyGenerator**    | Does a deep inspection converting arrays to Lists and returning an immutable List of the key arguments. Useful if complete assurance is required when doing key comparisons                                         |
|**MessageDigestCacheKeyGenerator**| Similar to HashCodeCacheKeyGenerator does but generates the key using a [MessageDigest](http://java.sun.com/j2se/1.5.0/docs/api/java/security/MessageDigest.html) which can provide a cryptographically secure hash code with a much larger key space. |

# Key Consistency #
All of the included cache key generators will generate keys that are consistent between JVM executions and safe for distribution between JVMs in a distributed/clustered caching environment. The exception to this is if the method arguments don't fulfill the contract of the CacheKeyGenerator being used as many require correct implementation of hashCode, equals, and/or toString.

# Common CacheKeyGenerator Configuration #
All of the included [CacheKeyGenerator](http://ehcache-spring-annotations.googlecode.com/svn/site/current/apidocs/com/googlecode/ehcache/annotations/key/CacheKeyGenerator.html) implementations extend [AbstractDeepCacheKeyGenerator](http://ehcache-spring-annotations.googlecode.com/svn/site/current/apidocs/com/googlecode/ehcache/annotations/key/AbstractDeepCacheKeyGenerator.html) which provides the following common configuration options.

#### Check for Cycles ####
Default **false**: If true the key generator will track visited objects during recursion and if a cycle is detected handle it correctly. If false the exact behavior is undefined but many of the key generators will fail with StackOverflowError due to infinite recursion.

#### Include Method ####
Default **true**: If true the declaring class, method name and method return type are included in the generated cache key. The effect of this is that keys become scoped to the annotated method. Consider the following method definitions:
```
@Cacheable(cache="weatherCache")
public Weather getWeatherById(String locationId);

@Cacheable(cache="weatherCache")
public Weather getWeatherByZip(String zipCode);
```

If both of these methods are called with the argument "49931" and the include method property is true different cache keys will be created. If the include method property is false the keys would be the same and could result in a collision if different Weather objects exist for "49931" as the locationId versus "49931" as the zipCode.

#### Include Parameter Types ####
Default **true**: If true and **Include Method** is true the method parameter types are also included in key generation. This is broken out separately from the **Include Method** option because of the additional runtime cost. [Method.getParameterTypes()](http://java.sun.com/j2se/1.5.0/docs/api/java/lang/reflect/Method.html#getParameterTypes()) is used for including the parameter type data. This call results in the Class array being cloned on every call resulting in extra CPU and garbage collection costs for every key generated. Consider the following use case:
```
@Cacheable(cache="weatherCache")
public Weather getWeather(Long locationId);

@Cacheable(cache="weatherCache")
public Weather getWeather(String zipCode);
```

Even with **Include Method** true if both of these methods are called with ` null ` as the parameter the same cache key would be generated. By setting **Include Parameter Types** to true the parameter types will be included in the key and the collision will be avoided.

#### Use Reflection ####
Default **false**: If true reflection is used on objects that don't directly implement the identity method(s) required by the generator. For example HashCodeCacheKeyGenerator and MessageDigestCacheKeyGenerator both require objects implement hashCode to function correctly while StringCacheKeyGenerator requires toString be implemented. The reflection algorithm recurses on every non-transient member field applying the key generation algorithm to each.

This option is useful if a method invocation has arguments from an external library that do not implement hashCode, equals or toString. Using reflection results in a 50% decrease in key generation speed and should only be used when there is no other option. It is good practice to always implement hashCode, equals and toString.

# Performance #
Included in the unit tests for the project is the ` CacheKeyGeneratorPerformanceTest `. This does some very simple synthetic benchmarking of the included CacheKeyGenerators. The following ratios between key generation speed should be fairly consistent across hardware configurations. HashCodeCacheKeyGenerator is being used as the reference for the speed comparisons

| **Cache Key Generator** | **% Decrease** |
|:------------------------|:---------------|
| HashCodeCacheKeyGenerator |                |
| ListCacheKeyGenerator   | 59.88%         |
| StringCacheKeyGenerator | 74.10%         |
| MessageDigestCacheKeyGenerator (MD5) | 89.00%         |

Also the specific options used for key generation can have a significant effect on performance. All options disables is being used as the reference for this table.

| **Include Method** | **Include Parameter Types** | **Use Reflection** | **% Decrease** |
|:-------------------|:----------------------------|:-------------------|:---------------|
| No                 | No                          | No                 |                |
| Yes                | Yes                         | No                 | 64.91%         |
| Yes                | Yes                         | Yes                | 151.29%        |