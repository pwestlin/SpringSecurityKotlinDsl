# Trying out the Kotlin DSL for Spring Security

Inspired by https://spring.io/blog/2020/03/04/spring-tips-kotlin-and-spring-security.

The application has the following endpoints:

endpoint | description | roles
--- | --- | ---
/ | greets logged in user or anonymous if not logged in| *everyone*
/greetings | greets logged in user | USER, ADMIN
/greetings/{greeting} | changes the default greeting to {greeting} | ADMIN
