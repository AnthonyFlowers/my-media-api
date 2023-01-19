# My Media
An app for tracking a user's media they have watched (and read/listened to eventually). 

This repository contains a Spring Boot REST API and uses a MySql database for data persistence. The movie and tv show data were downloaded from kaggle and loaded into the database using a Python script. Also included are some tests using jUnit and postman.

## Database setup [my-media-sql](/my-media-sql)
This app requires a MySQL database. Using the `production.sql` script file, run it on a database and use it in the Spring Boot API section when setting up the datasource.

## Spring Boot API [my-media](/my-media-api)
The Spring Boot REST API code is located in the my-media-api folder. JWT tokens are used for tracking user sessions.

The API has a few basic endpoints:
- `/auth`, `/auth/create_account`
  - Used for authorizing users and creating accounts
- `/movie`, `/movie/search` 
  - Used to find/search for movies
- `/tv-show`, `/tv-show/search`
  - Used to find/search for tv shows
- `/user/movie`, `/user/tv-show`
  - Used to find a user's media

Both `/movie` and `/tv-show` can be combined with query parameters to limit the quantity and select different pages of media
- `/movie` 
  - By default gets the first 50 movies.
- `/movie?page=5`
  - Gets the 5th page of movies in groups of 50 by default.
- `/tv-show?pageSize=100`
  - Gets the first 100 tv shows.
- `/tv-show?pageSize=80&page=10`
  - Gets the 10th page of 80 tv-shows.

For deployment, a WAR file is built using Maven and loaded onto a Tomcat server. After loading the WAR file an `applications.properties` file needs to be made available to the Tomcat server with values filled in like below replacing the <DB_URL> with the url of the database to use and replacing the <DB_PASSWORD> with the database password to use.
```
spring.datasource.url=<DB_URL>
spring.datasource.password=<DB_PASSWORD>

spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=false
```

Development testing before deployment was done using a local Ubuntu 22 server to ensure proper configuration before deploying to AWS.

## Postman API Testing [my-media-postman](/my-media-postman)
Using Postman, some tests were created to test the API:
- Creating an account
- Finding movies/tv shows
- Adding tv shows and movies to user media collections
- Updating user media

## Future Improvements
- [ ] Example images in the README
- [ ] Improve overall UI/UX
- [ ] Adding more media types like books, music, comic books, and manga
- [ ] Better media filtering and sorting

Community Features
- [ ] Friends list
- [ ] Groups
- [ ] Social media sharing
