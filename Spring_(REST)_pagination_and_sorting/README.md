# Spring (REST) pagination and sorting

Here it is analyzed what application has to face with pagination with `org.springframework.data / spring-data-commons`.

For sure there any many ways to manage pagination and sorting and here is described how to face them using `org.springframework.data / spring-data-commons`.

In order to enable `org.springframework.data / spring-data-commons` this dependency must be added to `pom.xml`:

```xml
<dependency>
  <groupId>org.springframework.data</groupId>
  <artifactId>spring-data-commons</artifactId>
</dependency>
```

<a name="endpoint-signature"></a>After adding such a dependency it is possible to use the `pageable` parameter as follow:

```java
  @GetMapping("/foo")
  @Override
  public @NonNull Page<Foo> fooGet(@Nullable final acmeFooGetParameters acmeFooGetParameters,
      @NonNull @PageableDefault(size=25, page = 0)
      @SortDefault(sort="date", direction = Direction.DESC) @SortDefault(sort="name", direction = Direction.ASC) final Pageable pageable) { ... }

// where 
class Foo {
      private LocalDate date;
      private String name;
      ...
}
```

`pageable` can be passed till repository layer and can be used as is by spring data repository as `JpaRepository` for example.

`pageable` gets global default pagination parameters that can be overriden by using the annotations `PageableDefault` and (repetable) `@SortDefault`.

**Note**: `pagination` is never `null` so applicaton can use it safely.

Global default can be set programmatically or via properties and autoconfiguration. Below an example for setting to 15 the global default for page size, refer to [spring.data.web.* properties](https://docs.spring.io/spring-boot/api/java/org/springframework/boot/autoconfigure/data/web/SpringDataWebProperties.html) for more information:

```
spring.data.web.pageable.default-page-size=15
```

The default query parameter names are:
- `size` for page size.
- `page` for page number.
- `sort` (repeteable) for sorting (example: ...?sort=name,DESC).

That means that `size`, `page` and `sort` are reserved query parameter names that should not be used by application (should not be fields of class `acmeFooGetParameters` [referring to the code excerpt above](#endpoint-signature).  
If class `acmeFooGetParameters` defines (either all or any)`size`, `page` and `sort` then both `pageable` and `acmeFooGetParameters` get the the values.  
Of course it is possible redefine them; for example, by specifing the following properties in the `application.properties`:

```
spring.data.web.pageable.prefix=app-
spring.data.web.sort.sort-parameter=app-sort
```

Can be `acmeFooGetParameters` `null`?
> I think it can not be.

That is because in my attempts, I cannot get it to be `null`. This is reasonable since it collects the query parameters. Even if only one parameter is specified, the receiving object (`acmeFooGetParameters`) cannot be `null`. If no query parameters are specified, the receiving object is still instantiated with default values.

---

