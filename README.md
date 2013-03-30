# Bookshelf #

This is a web tool that provides a simple way to centralize the properties configuration of diferent applications in the same place.

![Alt text](https://lh3.googleusercontent.com/dKVIjWtwn7xKDTu0OUhUrBStgIae86SUJo526oEDWofVU1aH4P6T16I552Kl-CJEl-W5LnOndbc)

### Why? ###

For a long time, i was working with [ESCape](https://code.google.com/p/escservesconfig/) and the idea of this tools it's soung great. But today ESCape is no longer mantained and i feel that they have some missing features. Besides this the UI of ESCape it really sucks, it's too slow and legacy.
This why the main feature of Bookshelf it's his responsive UI and his ajax usage through Restful services. The second main feature is the posibility of link 2 properties, the common case of this is when we have a lot of applicaction that interact with each other and they need to shared some properties like; DB url, services endpoints, udp logger ip, etc ..
To simplify this you can create a common property and from your module link your property module with the common property even if the properties are from diferent enviroments. After that if someone change the value of the common property the property modules automatically reference the new value.

### Usange ###

Just start Bookshelf server, set your projects, modules and properties and you can access all of this data through the Restful services:

"http://[ip:port]/bookshelf/rest/enviroment"
"http://[ip:port]/bookshelf/rest/project"
"http://[ip:port]/bookshelf/rest/project/{id}/modules"
"http://[ip:port]/bookshelf/rest/module/{id}/properties"

or you can simply get the value of the properties of a module from a specific enviroment:

"http://[ip:port]/bookshelf/query/{project-name}/{module-name}/{enviroment-name}"

If you are working with **Java** and **Spring** you can try [Bookshelf Client](https://github.com/Jarlakxen/bookshelf-client). This client can put the properties in the server into the spring loading context.


### Installation ###

....

### Features ###

* RESTful API
* Multi-Enviroment
* Linked Properties
* NoSQL Storage with MongoDB
* Fast & Scalable

### Tecnologies ###

* Scala
* [Scalatra](http://www.scalatra.org/)
* [Salat](https://github.com/novus/salat)
* [Bootstrap](http://twitter.github.com/bootstrap/)
* [AngularJS](http://angularjs.org/)
* [AngularUI](http://angular-ui.github.com/)
