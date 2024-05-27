# litersofmyblood

Things discovered by shedding liters of my blood.

# Things

**Spring (REST) answers 404 (not found) for a endpoint I was sure it was mapped**

This was written because after adding a `@RestControl` to a project generated with [spring initializer](https://start.spring.io/) applicaton responded 404 trying to invoke the endpoint.

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

**Spring (REST) @RequestoBody obligatoriness and nullability**

Here it is analyzed what application has to face with `@RequestBody` parameter.

Endpoint request body can be `null` if the `@RequestBody` `required` field is set to `false` (default is `true`). In such a case then it is possible doing a request without specifying the request body and the application gets a `null` request body parameter. If `required` is `true` then the request body must be specified in the request otherwise a _Bad Request_ is answered so, in the case `required` is `true` the application never gets a `null` request body. However, in the latter casea the body could be empty (**empty != null**).

```java
// note: acmeFooPostBody is annotated with @NonNull because it cannot be null as @RequestBody required is true.
@PostMapping("/fooa")
public AcmeFooPostResponse fooPostA(@NonNull final AcmeFooPostParamters acmeFooPostParamters,
      @NonNull @RequestBody(required = true) final AcmeFooPostBody acmeFooPostBody) { ... }
/*
cURL examples

Example A.1
curl -v -XPOST -H "Content-Type: application/json" --data '{}' http://localhost/foo
# ^^^ works (NOTE: body is empty)

Example A.2
curl -v -XPOST http://localhost/foo
# ^^^ does NOT works and answer is {"timestamp":"...","status":400,"error":"Bad Request","path":"/foo"}
*/

// note: acmeFooPostBody is annotated with @NNullable because it can be null as @RequestBody required is false.
@PostMapping("/foob")
public AcmeFooPostResponse fooPostB(@NonNull final AcmeFooPostParamters acmeFooPostParamters,
      @Nullable @RequestBody(required = false) final AcmeFooPostBody acmeFooPostBody) { ... }
/*
cURL examples

Example B.1
curl -v -XPOST -H "Content-Type: application/json" --data '{}' http://localhost/foo
# ^^^ works (NOTE: body is empty)

Example B.2
curl -v -XPOST http://localhost/foo
# works and acmeFooPostBody will be null
*/
```

In the following table are recapped the description and the cases above.

| cURL example | @RequestBody / required | Body of the request | Application gets                                             |
|--------------|-------------------------|---------------------|--------------------------------------------------------------|
| A.1          | true                    | {...}               | NON null body filled with either default or specified values |
| A.2          | true                    |                     | ERROR: Bad Request                                           |
| B.1          | false                   | {...}               | NON null body filled with either default or specified values |
| B.2          | false                   |                     | NULL body                                                    |

---


