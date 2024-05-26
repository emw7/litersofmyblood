# litersofmyblood

Things discovered by shedding liters of my blood.

# Things

**Spring answers 404 (not found) for a endpoint I was sure it was mapped**

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
