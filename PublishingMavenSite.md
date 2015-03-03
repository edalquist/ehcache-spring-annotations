# Introduction #

To help reduce space consumption in the SVN repository for the project the following approach to uploading the generated site should be used.

# Details #

After running ` release:perform ` build the site from the tagged project.
```
cd target/checkout/core/src/etc/
ant create-site -DoldSitePath=site/1.1.2 -DnewSitePath=site/1.1.3
ant update-current-site -DnewSitePath=site/1.1.3
```