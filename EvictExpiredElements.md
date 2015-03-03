# Preface #

Ehcache does not automatically remove elements from your cache that have exceeded timeToLive and timeToIdle. These elements remain in the cache until either:

  * [Ehcache#evictExpiredElements()](http://ehcache.org/apidocs/net/sf/ehcache/Ehcache.html#evictExpiredElements()) is called
  * Or a get or remove operation is called on the cache that passes in the same key as the expired element


# Configuring evict-expired-elements #
ehcache-spring-annotations contains a feature in version 1.1 and later that allows you to configure periodic invocation of [Ehcache#evictExpiredElements()](http://ehcache.org/apidocs/net/sf/ehcache/Ehcache.html#evictExpiredElements()).

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

    <ehcache:annotation-driven cache-manager="ehCacheManager" />
    <ehcache:config cache-manager="ehCacheManager">
      <!-- interval is in minutes -->
      <ehcache:evict-expired-elements interval="20"/>
    </ehcache:config>

    <bean id="ehCacheManager" class="org.springframework.cache.ehcache.EhCacheManagerFactoryBean"/>
</beans>
```

When this context file is processed by spring, ehcache-spring-annotations will setup a
[TimerTask](http://java.sun.com/javase/6/docs/api/java/util/TimerTask.html) and a [Timer](http://java.sun.com/javase/6/docs/api/java/util/Timer.html) to invoke evictExpiredElements on all caches with the interval you specified (in minutes).

# Include/Exclude caches #

The 'ehcache:evict-expired-elements' element allows you to list names and/or patterns that match cache names to include or exclude from this feature.
The following example will only call evictExpiredElements on the cache named "foo", the cache named "bar", and any cache that starts with "jpa.":

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

    <ehcache:annotation-driven cache-manager="ehCacheManager" />
    <ehcache:config cache-manager="ehCacheManager">
      <ehcache:evict-expired-elements interval="20">
        <ehcache:include name="foo"/>
        <ehcache:include name="bar"/>
        <ehcache:include pattern="jpa\..*"/>
      </ehcache:evict-expired-elements>
    </ehcache:config>

    <bean id="ehCacheManager" class="org.springframework.cache.ehcache.EhCacheManagerFactoryBean"/>
</beans>
```

The next example will include all caches except the cache named "bucky" and the caches that end with "badger":

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

    <ehcache:annotation-driven cache-manager="ehCacheManager" />
    <ehcache:config cache-manager="ehCacheManager">
      <ehcache:evict-expired-elements interval="20">
        <ehcache:exclude name="bucky"/>
        <ehcache:exclude pattern=".*badger$"/>
      </ehcache:evict-expired-elements>
    </ehcache:config>

    <bean id="ehCacheManager" class="org.springframework.cache.ehcache.EhCacheManagerFactoryBean"/>
</beans>
```

# Caveats #

  * If you specify "exclude" elements without any preceding "include" elements, an "include All caches" directive is added at the front of the list.
  * You can create more complex rules using includes and excludes together, however you must specify ALL includes before any excludes.