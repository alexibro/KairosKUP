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

**Login:**

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
