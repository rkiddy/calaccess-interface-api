# calaccess-interface-api
Accessing the updated Cal-Access implementation.

The new Cal-Access system is due to become live in February of 2021. This project contains
tests to confirm the accessibility and correctness of the API.

== Running ==

```
% ant     
Buildfile: /Users/ray/Projects/CalAccessImpls/calaccess-interface-api/build.xml

init:

compile:
    [javac] Compiling 3 source files to /Users/ray/Projects/CalAccessImpls/calaccess-interface-api/build

execute:
    [junit] Running org.ganymede.cars.api_test.Form410Test
    [junit] Testsuite: org.ganymede.cars.api_test.Form410Test
    [junit] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.506 sec
    [junit] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.506 sec
    [junit] 
    [junit] Testcase: testMockedFirstForm410 took 1.493 sec
    [junit] Running org.ganymede.cars.api_test.SpecificationBasicsTest
    [junit] Testsuite: org.ganymede.cars.api_test.SpecificationBasicsTest
    [junit] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.158 sec
    [junit] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.158 sec
    [junit] 
    [junit] Testcase: testSchemaDocumentLoadable took 0.001 sec
    [junit] Testcase: testFormsAreSpecified took 0.007 sec

BUILD SUCCESSFUL
Total time: 3 seconds
```
