
Jtty
====

A really simple jar bootstrap around Jetty.

All of the Jetty classes plus a bootstrap class are included, so you just run:

    java -jar jtty.jar 8080 myapp.war

If you want JSP support, use `jttyp` instead:

    java -jar jttyp.jar 8080 myapp.war

If you have multiple apps:

    java -jar jtty.jar 8080 /context1,path/to/app1.war /context2,path/to/app2exploaded

If you want to do virtual hosts:

    java -jar jtty.jar 8080 app1.example.com,/,path/to/app1.war app2.example.com,/,path/to/app2.war

If you want to run this from an Eclipse launch target, just use `Jtty` (no package) as the main class and command line arguments like above, e.g. `8080 src/main/webapp`.

The idea is not to use this in production but in situations where you need to quickly deploy a simple webapp with as little downloads/command line/classpath fiddling as possible.

