
Spring Roo addon helps create client side Sencha Touch MVC code for CRUD operations ! 

About Spring Roo: This is server side web app development tool. Typically used to kickstart the project which uses Spring Framework. Spring Roo can generate JSON based REST API very easily. For more information, see http://projects.spring.io/spring-roo/

About Sencha Touch: Sencha Touch is full fledged javascript framework used to develop cross platform mobile apps using all web technolgies. For more information, http://www.sencha.com/products/touch/

It takes good amount of time to create Sencha Touch code to map with back-end database entities. Its time consuming duplication of effort to create same database entities clones on Sencha Touch MVC Model/Store/Form and lists. This addon for Spring Roo saves this time and kickstarts Sencha Touch development by creating scaffolded application.

This addon reads Spring Roo generated entities and it's attributes and will create its Sencha Touch clones in MVC pattern. It creates separte controller, form, model , store for each server entity. 

Once the scaffolded application is generated, developer can modifiy code further to add app specific functionality.

The supporting Sencha Touch Reference project is at https://github.com/sdharmadhikari/roo-addon-reference-sencha-project.
Sencha Touch code from this project will be used as code templates for this main project.

How To Install Addon :

Since generated addon is not yet part of Spring Roo's official repository, it needs to be installed manually into Spring Roo. This addon has dependency on following 3 osgi bundles which need to be installed first into Spring Roo container. 

Apache Commons Lang (2.4.0)
Apache Commons Collections (3.2.1)
Apache Velocity Engine (1.6.2)

You can find all 3 bundles in "lib" directory at this location.

osgi start --url file:///Users/sudhir/Projects/trials/spring-roo/addon-roo-sencha/target/com.softrism.roo.senchaaddon-1.0.jar
