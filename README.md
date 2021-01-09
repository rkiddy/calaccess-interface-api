
Accessing the updated Cal-Access implementation.

The new Cal-Access system is due to become live in February of 2021. This project contains
tests to confirm the accessibility and correctness of the API.

TBD: Still trying to figure out the best way to package the submission data so that
different people can use these tests for the Cal-Access-API. Entities cannot be
created via the API. The API calls that are, at this point, allowed will let you update
an already-created entity. This means that tests are dependant on data created outside
of the testing system. So there needs to be a way for a user to supply the data for the
entities that are authorized for their use.

This problem does not apply to the Cal-Access-Data-API, since everyone has the same access
to that data and it is read-only. But the environment is also less complete, so less is
testable.

[2021-01-08 -rrk]

## Running ##

You need a property file:

```
% cat ~/.calaccess_api.txt

vendorEmail: TBD
client_id: TBD
client_secret: TBD
vendorCode: TBD
verbose: true
caa_url_base: TBD
cada_url_base: TBD
% 
```
These will be added as headers to the requests.

The tests run with Java14. On a Mac, I have this as my default VM.

On Ubuntu 18.04, I run with:
```
$ JAVA_HOME=/opt/jdk-14.0.2 ant
```

Result:
```
% ant     
Buildfile: /Users/ray/Projects/CalAccessImpls/calaccess-interface-api/build.xml

init:

compile:
    [javac] Compiling 2 source files to /Users/ray/Projects/CalAccessImpls/calaccess-interface-api/build

execute:
    [junit] Running org.ganymede.cars.Form410Test
    [junit] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.012 sec
    [junit] Running org.ganymede.cars.SpecificationBasicsTest
    [junit] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.125 sec
    [junit] Running org.ganymede.cars.cal_access_api.FilersTest
    [junit] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.633 sec

BUILD SUCCESSFUL
Total time: 4 seconds
```
