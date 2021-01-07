
calaccess-interface-api
Accessing the updated Cal-Access implementation.

The new Cal-Access system is due to become live in February of 2021. This project contains
tests to confirm the accessibility and correctness of the API.

## Running ##

You need a property file:

```
% cat ~/.calaccess_api.txt

vendorEmail: TBD
client_id: TBD
client_secret: TBD
vendorCode: TBD
% 
```
These will be added as headers to the requests.


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
