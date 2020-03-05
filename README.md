# Trying out the Kotlin DSL for Spring Security

Inspired by https://spring.io/blog/2020/03/04/spring-tips-kotlin-and-spring-security.

The application has the following endpoints:

endpoint | description | roles
--- | --- | ---
/ | greets | *everyone*
/greetings | greets | USER, ADMIN
/greetings/{greeting} | greets | ADMIN
