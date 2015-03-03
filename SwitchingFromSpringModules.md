# Basic Configuration #

The Spring-Modules Cache approach
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xmlns:ehcache="http://www.springmodules.org/schema/ehcache"
     xsi:schemaLocation="
     http://www.springframework.org/schema/beans 
     http://www.springframework.org/schema/beans/spring-beans-2.5.xsd  
     http://www.springmodules.org/schema/ehcache 
     http://www.springmodules.org/schema/cache/springmodules-ehcache.xsd">

    <ehcache:config configLocation="classpath:ehcache.xml" />
    <ehcache:annotations>
        <ehcache:caching id="getTestCacheModel" cacheName="getTestCache" />
        <ehcache:flushing id="getTestFlushModel" cacheNames="getTestCache" />
    </ehcache:annotations>

    <bean id="customerManager" class="services.impl.CustomerManagerImpl"/>
</beans>
```
```
import org.springmodules.cache.annotations.Cacheable;
import org.springmodules.cache.annotations.CacheFlush;

public interface CustomerManager {
    @Cacheable(modelId="getTestCacheModel")
    public Customer load(long customerId);

    @CacheFlush(modelId="getTestFlushModel")
    public void add(Customer customer);
}
```
```
public class CustomerManagerImpl implements CustomerManager {
    public Customer load(long customerId) {
        //This part should normally call a DAO
        return new Customer("Rene", 34);
    }

    public void add(Customer customer) {
        //This part should normally call a DAO
    }
}
```

The Ehcache Spring Annotations approach
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xmlns:ehcache="http://ehcache-spring-annotations.googlecode.com/svn/schema/ehcache-spring"
     xsi:schemaLocation="
     http://www.springframework.org/schema/beans 
     http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
     http://ehcache-spring-annotations.googlecode.com/svn/schema/ehcache-spring
     http://ehcache-spring-annotations.googlecode.com/svn/schema/ehcache-spring/ehcache-spring-1.1.xsd">

    <ehcache:annotation-driven />
    
    <bean id="cacheManager" class="org.springframework.cache.ehcache.EhCacheManagerFactoryBean"/>
    
    <bean id="customerManager" class="services.impl.CustomerManagerImpl"/>
</beans>
```
```
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;

public interface CustomerManager {
    @Cacheable(cacheName="getTestCacheModel")
    public Customer load(long customerId);

    @TriggersRemove(cacheName="getTestFlushModel", removeAll=true)
    public void add(Customer customer);
}
```
```
public class CustomerManagerImpl implements CustomerManager {
    public Customer load(long customerId) {
        //This part should normally call a DAO
        return new Customer("Rene", 34);
    }

    public void add(Customer customer) {
        //This part should normally call a DAO
    }
}
```