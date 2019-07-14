## Call Details Record API

This project follows Domain Driven Design.

### Technologies

* Spring Boot
* Spring Data
* MySQL
* Gradle
* flyway (Manage and automate database script)
* Spock (Testing and specification framework) 

### Requirements to run

* Java 11
* Docker

### How to run

Run MySQL: `docker-compose up`

Build the application: `./gradlew build`     

Run the API: `java -jar build/libs/call-detail-record-1.0.0-SNAPSHOT.jar`

### Endpoints

Recommendation:

See Endpoints and try it with Swagger UI at http://localhost:8080/swagger-ui.html 
 
Or import postman collection [call-detail-record.postman_collection.json](call-detail-record.postman_collection.json) 

| Method | Description |
|--------|-------------|
| POST `/call-details/upload` | Upload a CSV file with call-details data |
| GET `/caller/{callerId}/report` | Get a report from a callerId |
| GET `/caller/{callerId}/history`| Get the call history from a callerId |
| GET `/phonenumber/{phonenumber}/history` | Get the call history from a callerId or recipient |

### Assumption

* I created the /upload endpoint as an example, but I don't think this is the correct approach. Because, if the csv size is in the gigabyte order of magnitude, does not matter 
how much I optimize, it is still too big and can take hours to process and can still fail. I would take an approach of importing the file to some server or S3, and then I could 
process in parts and for how long it needs. 

* GBP is the API main currency, any other currency is considered local_currency and should have an exchange rate. 

### Improvements

Although the most critical parts have tests, there is a lot more test coverage to do. Specially, integration tests.  

Improve the error messages, specially if invalid request, it would be helpfull if anyone would consume this API. 

Improve CSV import performance. If the csv file is too big, will take a long time to process. Parsing data is a lot faster than insert in the DB, because of that I would focus on making DB inserts asynchronous. 

Add Querydsl would add typesafe and would help to simplify similar queries that only change a criteria.

### Deployment

After running `./gradlew build`, a fatJar will be created at build/libs/call-detail-record-1.0.0-SNAPSHOT.jar. It is just necessary to override the database configs (with environment or args) to use production's DB. 

For example:
```
java -jar build/libs/call-detail-record-1.0.0-SNAPSHOT.jar --spring.datasource.username=db_user --spring.datasource.password=db_password --spring.datasource.url='jdbc:mysql://localhost:3306/test?useSSL=false&rewriteBatchedStatements=true'
```

However, ideally, would be better to build a docker container to wrap the application and make easier to deploy.

And finally, set up a continous integration pipeline with Jenkins or Bamboo to automate the build (jar and docker) and the deployment to AWS ECS or similar.
