# Kairos - K. Upgrade

[![Build Status](https://travis-ci.com/alexibro/KairosKUP.svg?branch=master)](https://travis-ci.com/alexibro/KairosKUP)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=alexibro_KairosKUP&metric=alert_status)](https://sonarcloud.io/dashboard?id=alexibro_KairosKUP)

## Blog API (kairoskup)

Any registered user could send a new Article and even non-registered user can write new Comments on articles. 

Comments must be checked on a secondary API which will return a 400 HTTP Status in caso of a sweraring word found inside the comment.

**Default endpoint:** https://localhost:8443

**Login:**

* **Default credentials:** 

    * **Username:** user

    * **Password:** pass

  > POST /login

**Logout:**

  > DELETE /logout

**Articles (authentication non-required):**

* **Get all articles.**
  > GET /articles
* **Get a specific article.**
  > GET /articles/{articleId}

**Articles (authentication required):**

* **Create an article.**
  > POST /articles
* **Update an article.**
  > PUT /articles/{articleId}
* **Delete an article.**
  > DELETE /articles/{articleId}

**Comments (authentication non-required):**

* **Get all comments.**
  > GET /articles/{articleId}/comments
* **Get a specific comment.**
  > GET /articles/{articleId}/comments/{commentId}
* **Create a comment.**
  > POST /articles/{articleId}/comments

**Comments (authentication required):**

* **Update a comment.**
  > PUT /articles/{articleId}/comments/{commentId}
* **Delete a comments.**
  > DELETE /articles/{articleId}/comments/{commentId}
  
### Architecture and components:

**Each package** contains the repository, the service and the controller related to a specific entity.

* **Repository interfaces** establish connections and transact with the database.

* **Services** contain the logic of the API. These maintain independence between controllers and repositories.

* **Controllers** manage connections to the api.

* **Entities** represent the API data model.

**Security package** contains all classes related to API authentication and authorization.

**Swearing API Middleware** manages connections beetwen Blog API and Swearing API.

**Database loader** preloads test data.

## Swearing API

Simple API that is responsible for analyzing a text and determining if it contains swearing words.

**Example:**

- **Valid comment:**
```
POST /check

BODY: 
{ 
   "request": "hola"   
}
```

It returns

```
HTTP 200 OK

BODY:
{ 
   "response": "valid"
}
```

- **Invalid comment:**
```
POST /check

BODY: 
{ 
   "request": "lechuguino"
}
```

It returns

```
HTTP 200 OK

BODY: 
{ 
   "response": "invalid"
}
```


**Default endpoint:** http://localhost:8080

:warning: **It has one preloaded swearing word:** lechuguino

## Deployment:

### Multi-stage Dockerfile

Each API has a **multi-stage Dockerfile**:

* **Builds the application JAR**
* **Executes the JAR and expose it**

*Blog API has a "Dockerfile.prebuilded" to build the docker image from a compiled JAR.*

### Docker-compose

* **DB service:** Downloads MySQL image, initializes and configures it; and connects it to internat network.
* **Swearing API service:** Builds image from Dockerfile, exposes and connects it to internal network.
* **Kairoskup (Blog API) service:** Builds image from Dockerfile, exposes and connects it to database and internal network. It depends on database and swearing API. **The parameterized implementation** allows redirect Blog API to Swearing API endpoint inside the container and internal network.
* **Internat network:** It allows and manages communication between containers.

## Continuous Integration and Delivery - Tests

### Tests

**It has 50 tests** in total.

**H2 embedded database** has been used it in the tests.

It has **16 unit tests** that ensure the correct functioning of the **persistence layer**. 

![Alt text](https://github.com/alexibro/KairosKUP/blob/master/resources/PersistanceTests.png)

It has **17 unit tests** that ensure the correct functioning of the **controllers**. It has been necessary to **mock service layer**.

![Alt text](https://github.com/alexibro/KairosKUP/blob/master/resources/ControllerTests.png)

It has **17 integration tests** that ensure the correct functioning of the **API** itself.

![Alt text](https://github.com/alexibro/KairosKUP/blob/master/resources/ApiIntegrationTests.png)

It has a Postman collection which contains **15 integration tests** that ensure the correct functioning of the **API** once it has been deployed.

*You must disable SSL certificate verification (Setting/General) due to our Sef-signed certificate.

![Alt text](https://github.com/alexibro/KairosKUP/blob/master/resources/PostmanTests.PNG)

### Continuous Integration [![Build Status](https://travis-ci.com/alexibro/KairosKUP.svg?branch=master)](https://travis-ci.com/alexibro/KairosKUP)

**Travis CI** tool has been used it. Every commit runs next steps:

* **Builds kairoskup (Blog API) image.**
* **Execute all the kairoskup tests**
* **Builds swearingAPI image.**
* **Push kairoskup and swearingAPI images to Docker Hub**


[Docker Hub - alexibro/kairoskup](https://hub.docker.com/repository/docker/alexibro/kairoskup)

[Docker Hub - alexibro/swearingAPI](https://hub.docker.com/repository/docker/alexibro/swearingapi)
      
### Sonar [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=alexibro_KairosKUP&metric=alert_status)](https://sonarcloud.io/dashboard?id=alexibro_KairosKUP)

**SonarCloud** tool has been used it to clean the code. It helps identifying bugs, vulnerabilities, security hotspots, code smells and technical debt.
