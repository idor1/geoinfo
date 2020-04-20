# geoinfo
Mongodb and small java app to do some simple geo requests

As the prerequisite for the application start the *docker-compose*, *Java* (at least 8) and *Maven 3* must be installed.
To run and check the application, please, use the following:

* The application uses *spring-boot* for the Java server. At first, build the application with Maven. From the root of 
the project, where the *pom.xml* is, run the: `mvn clean install`. After the command you should be able to fin the 
*.jar* in the */target* directory.
* Build the Docker containers by running the `docker-compose build` command. It will create the Docker images with 
*MongoDb* and container with *Spring-boot* application. The application will be copied to the image from the */target*
directory.
* Start the system by running the `docker-compose up`. Very first start could take a while because of the DB 
preparations and waiting for the containers by the Java app. Please, also note, that `geo-import` container will exit 
after performing the actions.

To check the application you can access:
 * The *Swagger* with the description of the REST API you can access at the *http://localhost:8200/swagger-ui.html*
 * Simple maps application UI at the *http://localhost:8200/*. You may see several buttons above the map. Clicking the 
 'Show Lines' should draw the countries borders lines in the visible part of the map. Input box and the button below it
 will show you the events in the JS console if you will provide the ISO3 country code in the input box.