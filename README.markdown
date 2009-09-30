
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

The idea is not to use this in production but in situations where you need to quickly deploy a simple webapp with as little downloads/command line/classpath fiddling as possible.

Notes
=====

* No JSP support right now--would be easy to add, just need to include the right Jasper jars. might provide two different versions so the non-JSP can remain a lighter-weight download.

* Only one webapp can be deployed with the current bootstrap class--multiple could be supported, with context path encodings (e.g. `/foo=~/myapp.war`).

