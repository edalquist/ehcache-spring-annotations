# Introduction #

The ` WeatherService ` example project can be found in the source repository under:

https://code.google.com/p/ehcache-spring-annotations/source/browse/#svn%2Fexamples%2Fehcache-spring-example-1%2Ftrunk

This project is a Spring 3 web application that includes a few example applications of the @[Cacheable](UsingCacheable.md) and @[TriggersRemove](UsingTriggersRemove.md) annotations.

# Requirements #

  1. Subversion client to check out the source tree
  1. Maven to build the project
  1. Tomcat 6 instance to deploy and run.

The authors use [Eclipse](http://eclipse.org) to develop the software along with the following plugins:
  * [Subclipse Subversion integration](http://subclipse.tigris.org/)
  * [m2eclipse Maven Integration](http://m2eclipse.sonatype.org/)
  * [Sysdeo Eclipse Tomcat Launcher](http://www.eclipsetotale.com/tomcatPlugin.html)

# Setup #

  1. Download and unzip a Tomcat 6 distribution
  1. Check out the project with:
```
svn co https://ehcache-spring-annotations.googlecode.com/svn/examples/ehcache-spring-example-1/trunk ehcache-spring-example-1
```
  1. Navigate to the ehcache-spring-example-1 directory, and build the project with:
```
mvn package
```
  1. Copy the file named ehcache-spring-example-1.war under neath the target sub directory to the webapps directory in your Tomcat 6 install.
  1. Start tomcat, and navigate to http://localhost:8080/ehcache-spring-example-1/

# Using the Example #

On http://localhost:8080/ehcache-spring-example-1/ you will find a simple web interface for creating, manipulating, and deleting Weather objects.
Enter a zip code and current temperature in the form at the top and click Submit. This triggers a call to ` WeatherService#createNewWeather() ` which has no caching annotations.
The weatherCache statistics will be unchanged.

If you click on the details link for your new weather object, you'll trigger a call to ` WeatherService#getWeather ` which has the @[Cacheable](UsingCacheable.md) annotation. After the first click of the details link for a zip code, when you return to the main view you'll see the cache statistics now shows 1 object in memory, 0 hits, and 1 miss. Clicking that same zip code's details link afterwards will result in cache hits.

If you update the temperature for an existing weather object, you'll trigger a call to ` WeatherService#updateWeather `, which is annotated with @[TriggersRemove](UsingTriggersRemove.md). Returning to the main you'll see that the weather object previously in memory is now gone from the cache statistics (memory store object count will show 1 less).
Clicking the zip code's details link will result in a get, which will result in a miss; clicking again afterwards will result in cache hits.

# Example Details #

This example is a basic web application that allows the visitor to create, update, retrieve and delete "Weather" data.

There is a simple Java Bean to represent a piece of Weather data and a CRUD-style interface for working with Weather beans:

https://code.google.com/p/ehcache-spring-annotations/source/browse/#svn%2Fexamples%2Fehcache-spring-example-1%2Ftrunk%2Fsrc%2Fmain%2Fjava%2Fcom%2Fgooglecode%2Fehcache%2Fannotations%2Fexamples

There are 3 simple implementations of ` WeatherService ` interface in the 'impl' package:

https://code.google.com/p/ehcache-spring-annotations/source/browse/#svn%2Fexamples%2Fehcache-spring-example-1%2Ftrunk%2Fsrc%2Fmain%2Fjava%2Fcom%2Fgooglecode%2Fehcache%2Fannotations%2Fexamples%2Fimpl

All three of these examples have @[Cacheable](UsingCacheable.md) and @[TriggersRemove](UsingTriggersRemove.md) annotations.
The Simple and ` SpringJdbc ` implementations are very similar, with the former using simply a Map and the latter depending on Spring's ` SimpleJdbcTemplate ` and an in-memory HSQLDB ` DataSource. `
The 'Slow' variant is implemented slightly differently to simulate an 'expensive' implementation (each method calls Thread.sleep()) and has the "selfPopulating" option enabled.

The 'web' package is a simple Spring 3 @Controller that interacts with the ` WeatherService ` implementation:

https://code.google.com/p/ehcache-spring-annotations/source/browse/#svn%2Fexamples%2Fehcache-spring-example-1%2Ftrunk%2Fsrc%2Fmain%2Fjava%2Fcom%2Fgooglecode%2Fehcache%2Fannotations%2Fexamples%2Fweb

To switch between the 3 ` WeatherService ` implementations, simply edit the line in the ` WeatherServiceController ` that looks like the following:

```
public void setWeatherService(@Qualifier("jdbc") WeatherService weatherService) {
```

Change the value of the @Qualifier annotation from "jdbc" to the value of the @Qualifier annotation on the desired ` WeatherService ` annotation (e.g. "simple" or "slow").

ehcache-spring-annotations is configured in the Spring applicationContext found in:

https://code.google.com/p/ehcache-spring-annotations/source/browse/examples/ehcache-spring-example-1/trunk/src/main/resources/contexts/applicationContext.xml

The critical line to activate ehcache-spring-annotations in that file looks like:
```
<ehcache:annotation-driven/>
```

With that line in place, ehcache-spring-annotations integration in the Spring applicationContext will wrap beans that you have annotated with @[Cacheable](UsingCacheable.md) and @[TriggersRemove](UsingTriggersRemove.md) annotations with Proxies that provide the desired ` EhCache ` integration.

# Enabling caching for JUnit tests #

This example also demonstrates how to enable ehcache-spring-annotations for JUnit tests:

https://code.google.com/p/ehcache-spring-annotations/source/browse/examples/ehcache-spring-example-1/trunk/src/test/java/com/googlecode/ehcache/annotations/examples/impl/SimpleWeatherServiceImplTest.java

This tests depends on the Spring applicationContext found in src/main/resources.