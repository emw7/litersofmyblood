# Spring (REST) answers 404 (not found) for a endpoint I was sure it was mapped

> This was written because after adding a `@RestController` to a project generated with [spring initializer](https://start.spring.io/) applicaton responded 404 trying to invoke the endpoint.

The problem was I tried to map by using the `value` field of the `@RestController` annotation, but it is meaningless and the right mapping is done with `@RequestMapping`, so  
Incorrect:
```java
@RestController("/server/vx/acme")
public class AcmeControllerRest {

  @GetMapping("/foo")
  public AcmeFooGetResponse fooGet (...) {...}
```

Correct:
```java
@RestController
@RequestMapping("/server/vx/acme")
public class AcmeControllerRest {

  @GetMapping("/foo")
  public AcmeFooGetResponse fooGet (...) 
```

---
