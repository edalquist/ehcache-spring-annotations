# Preface #

This project is the spiritual successor to the support for Ehcache in the [Spring Modules](https://springmodules.dev.java.net/) project. When the authors of this project found out that Spring Modules was deprecated and no support for Spring 3 was planned, we decided it was time to create this project.

The authors of this project both have extensive experience with Spring and Ehcache, so much of our documentation assumes you are already familiar with both.

# Intended Usage #

This project is intended to help you weave Ehcache aspects at runtime around your Data Access Objects within your Spring applications. These annotations have no effect unless you create the appropriate configuration.

The following steps are merely a suggestion. If you don't use Maven, you can certainly setup the project in another way, however you'll be responsible for sorting out all of the dependencies required for this project, Spring, and Ehcache. We highly suggest you use Maven in this regard.

  1. Create a new [Maven](http://maven.apache.org/) project.
  1. Add this project as a dependency (see the Project Home page for the recommended version) in the maven pom.xml. This will in turn add Spring 3 and Ehcache 2 to your project's dependencies.
  1. Write your Spring Application, including ` @Cacheable ` and ` @TriggersRemove ` annotations around the methods in your DAOs that you would like caching applied (see [UsingCacheAnnotations](UsingCacheAnnotations.md) and/or [UsingTriggersRemove](UsingTriggersRemove.md)).
  1. Add the required configuration for this project to your Spring configuration (see [UsingCacheAnnotations](UsingCacheAnnotations.md)).
  1. Create the Ehcache configuration and place it within the project classpath - http://ehcache.org/documentation/configuration.html


# Spring Reference Materials #

[Spring Documentation Home](http://www.springsource.org/documentation)

[Spring 3 Reference](http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/)

[Spring 3 Javadoc](http://static.springsource.org/spring/docs/3.0.x/javadoc-api/)

# Ehcache Reference Materials #

[Getting Started with Ehcache](http://ehcache.org/documentation/getting_started.html)

[Ehcache Configuration](http://ehcache.org/documentation/configuration.html)