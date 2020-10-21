# config-service
Config service is a demo of spring cloud config server offering the flexibility to test all the existing external databases implementation like Redis, MongoDB, MySql. 
And providing a simple way to convert and load local configuration (.yaml or .properties) into the chosen DB. 

# setup:
For running docker DBs instances (redis, MongoDB and MySql) We should fire up the command:
- docker-compose up -d

After that go to the desired branch and run the app using maven command:
- mvn spring-boot:run

# microservices properties setup:
If you already has a couple of microservices configurations (.yml or.properties) on your local environment and you want to load them in a specific DB:
- go to /resources/application.properties and change the "config.native.location" variable. 
- send a GET request to http://localhost:8888/properties/load-to-db/{service-name}/profile/{service-profile}?type=(properties or yaml)
