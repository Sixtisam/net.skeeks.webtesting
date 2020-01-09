# net.skeeks.webtesting

This is an experiment to implement a junit extension which:

- executes a test method multiple times (for each defined browser)
- close the browser after each test method (without writing driver.quit in the test method)

This is achieved by using JUnit 5 Test template (https://junit.org/junit5/docs/current/user-guide/#writing-tests-test-templates). Each "Invocation" of the template represents a different browser.

Additionally, the driver is passed as argument to the test (WebDriver driver) and after the test execution, the driver is closed (driver.quit)
This reduces duplicate code in the test method itself.