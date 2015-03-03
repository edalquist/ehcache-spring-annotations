## 1. What does this product do? ##

This project is intended to provide an easy way to incorporate caching semantics implemented with ` EhCache ` within a project already using the Spring Framework
for dependency injection.

If you are using this project and including Spring only because this project depends on Spring, you are doing it wrong. If you aren't already
using Spring to manage your project's beans and services, you should use ` EhCache ` API directly in your code.

On the other hand, if you have an existing Spring project and use Spring to wire your data-access-objects and services together, this project is a good fit. Adding this to your Spring project will let you add caching backed by ` EhCache ` without having to call the ` EhCache ` API directly within your code.

## 2. How does this product work? ##

Here's a short summary of how this works:

When ehcache-spring-annotations is initialized, it scans classes looking for ` @Cacheable `/` @TriggersRemove ` annotated methods on beans already loaded
by the Spring ` ApplicationContext `.
When it finds an annotated method on a Spring bean, it then looks for the interface that the class implements. It then generates a http://download.oracle.com/javase/6/docs/api/java/lang/reflect/Proxy.html that implements the same interface as your class.
The methods that do not have any annotations are implemented by delegating to your class; the methods that do have annotations apply the cache semantics you
specified in the annotation.

## 3. Where does @Cacheable go in my source? ##

**Requirement 1**: your class MUST implement some (any) interface. If your class does not implement an interface, this project will not
be able to create a Proxy to mimic your class and apply the cache semantics around your annotated methods.

**Requirement 2**: ` @Cacheable `/` @TriggersRemove ` only apply to methods, not to classes.

**Requirement 3**: self-invocation is not supported. If you annotate a method that is only called internally within the class, caching will not be applied (since 'this' doesn't invoke the proxy that was created around your annotated class).

**Strong Recommendation**: the arguments of the methods you annotate and the objects they return should have proper equals() and hashCode() implementations.

## 4. My method isn't being cached! ##

See the requirements in Answer 3. Most often people aren't a) implementing any interface or b) expecting self-invocation to work.

All else fails you can add debug logging for the project:
```
log4j.category.com.googlecode.ehcache.annotations=DEBUG,R
log4j.additivity.com.googlecode.ehcache.annotations=false
```

## 5. Can I see an example? ##

WeatherServiceExample

You can also browse the source and look at the many unit tests.

## 6. Do I have to implement hashCode() for beans that are used as arguments for ` @Cacheable `/` @TriggersRemove ` methods? ##

Example:
```

class MyBean {
  private String field1;
  private String field2;
   ....
}
interface MyDao {
  OtherObject getOtherObject(MyBean bean);
}

```

Technically no, however we strongly recommend that you do.
If you do not wish to implement ` hashCode ` on beans like ` MyBean `, you will need to enable the **useReflection** property on the [CacheKeyGenerator](CacheKeyGenerators.md) you're using. Enabling **useReflection** uses reflection to generate a key from from the fields in your class. Enabling **useReflection** drops key generation performance by 50%.

**Many IDEs have the capability to automatically generate proper equals(), hashCode() and toString() implementations for your Java beans, we strongly recommend using this feature.**


## 7. How can I generate a cache key that isn't based on the arguments? ##
This example generates a cache key that is a formatted string of the current date.

The CacheKeyGenerator implementationpackage com.example;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.aopalliance.intercept.MethodInvocation;

import com.googlecode.ehcache.annotations.key.CacheKeyGenerator;

public class DateCacheKeyGenerator implements CacheKeyGenerator<Serializable> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); 

    @Override
    public Serializable generateKey(MethodInvocation methodInvocation) {
        return this.generateKey((Object)methodInvocation);
    }

    @Override
    public Serializable generateKey(Object... data) {
        
        final Date now = new Date();
        
        synchronized (this.dateFormat) {
            return dateFormat.format(now);
        }
    }
}
[/code]

Referring to the custom CacheKeyGenerator in the @Cacheable annotation
[code]
@Cacheable(cacheName="exampleCache", 
    keyGenerator = @KeyGenerator (name = "com.example.DateCacheKeyGenerator")
    )
public ExampleData getData(String arg1, Object arg2)
[/code]

Now while this example provides a functional key generator it could be improved on a by doing things like using a Timer to generate the date string once per minute and storing it in a volatile field instead of generating the date string every time the cached method is requested. ```