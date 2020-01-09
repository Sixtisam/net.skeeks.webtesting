package net.skeeks.webtesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GoogleSearchTest {

	@BeforeAll
	static void setupOnce() {
		System.setProperty("webdriver.chrome.driver", "C:\\Coding\\chromedriver_win32\\chromedriver.exe");
		System.setProperty("webdriver.gecko.driver", "C:\\Coding\\geckodriver-v0.26.0-win64\\geckodriver.exe");
//		driver = new ChromeDriver();
	}

	@TestTemplate
	@ExtendWith(BrowserTestTemplate.class)
	void testElementsOnSearchPage(WebDriver driver) {
		System.out.println("Start test");
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.navigate().to("http://www.google.com/");

		// find search button
		// Note 1: since findElement throws an exception if not found,
		// this is caught here with try-catch, and handled as an
		// unit test error
		// Note 2: we could also use asssertDoesNotThrow method for the test,
		// but this does not allow to check for specific exception types
		try {
			WebElement messageElement = driver.findElement(By.name("btnK"));

			assertEquals("Google-Suche", messageElement.getAttribute("value"));
		} catch (NoSuchElementException e) {
			fail("Search button not found", e);
		}

		// check for input field
		try {
			driver.findElement(By.name("q"));
		} catch (NoSuchElementException e) {
			fail("search input field not found", e);
		}
	}
}
