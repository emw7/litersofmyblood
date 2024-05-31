# Maven test surfire JUnit5

Here it shown how to enable JUnit5 test with maven and the surfire plugin.

The problem was running `mvn test` tests were not run:

```shell
mvn test

[INFO] --- surefire:2.22.1:test (default-test) @ litersofmyblood-bloodyx ---
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.github.emw7.litersofmyblood.bloodyx.comparator.SortComparatorFactoryTests
[INFO] Tests run: 0, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.004 s - in com.github.emw7.litersofmyblood.bloodyx.comparator.SortComparatorFactoryTests
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 0, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
```

The solution is to add the following dependency in the `pom.xml` (as indicated in the [Apache Maven Surfire plugin page](https://maven.apache.org/surefire/maven-surefire-plugin/examples/junit-platform.html)):

```xml
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-engine</artifactId>
      <version>5.10.2</version>
      <scope>test</scope>
    </dependency>
```

And at this point tests are run:

```shell
[INFO] --- surefire:2.22.1:test (default-test) @ litersofmyblood-bloodyx ---
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.github.emw7.litersofmyblood.bloodyx.comparator.SortComparatorFactoryTests
[INFO] Tests run: 31, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.226 s - in com.github.emw7.litersofmyblood.bloodyx.comparator.SortComparatorFactoryTests
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 31, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
```

**Note**: the page [Why Maven Doesn’t Find JUnit Tests to Run](https://www.baeldung.com/maven-cant-find-junit-tests#incorrect-dependencies) gives the same suggestion but instead of adding in the `<dependencies>` sections, suggests to add in the `<plugins>` section:

```xml
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.0.0-M7</version>
            <dependencies>
                <dependency>
                    <groupId>org.junit.jupiter</groupId>
                    <artifactId>junit-jupiter-engine</artifactId>
                    <version>5.10.2</version>
                </dependency>
            </dependencies>
        </plugin>
    </plugins>
```

but this solution did not work for me.

---
