package linkedIn;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ConfigReader;

public class LinkedInJob {
	WebDriver driver;
	WebElement element;
	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10000));

	@BeforeMethod
	public void launchBrowser() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();

		driver.manage().window().maximize();
		driver.get(ConfigReader.getProperty("linkedIn_URL")); // ✅

		wait = new WebDriverWait(driver, Duration.ofSeconds(18)); // ✅ Initialize after driver
	}

	@Test
	public void linkedIn() {
		LogInPage login = new LogInPage(driver);
		// Login LinkedIn Page
		login.linkedInLoginMethod();

		String subscriptionModel = "//button[@aria-label='Dismiss']";
		element = driver.findElement(By.xpath(subscriptionModel));
		if (element.isDisplayed()) {
			element.click();
			System.out.println("Subscription X button is selected on Model");
		} else {
			System.err.println("Subscription Model is visible.");
		}

		// String jobTab = "//span[text()='Jobs']/ancestor::a";
		driver.findElement(By.xpath("//span[text()='Jobs']/ancestor::a")).click();// Selecting Job Tab
		String showJobs = "(//span[text()='Show all']/ancestor::a)[1]";
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(showJobs)));
		driver.findElement(By.xpath(showJobs)).click();// show all Jobs

		//
//		if (condition) { for
//			
//		} else {
//
//		}
		By easyApplyButton = By.xpath("//span[text()='Next']/parent::button");

		for (int i = 0; i < 10; i++) {
		    List<WebElement> elements = driver.findElements(easyApplyButton);

		    if (!elements.isEmpty()) {
		        elements.get(0).click(); // ✅ Click the first matched element
		        System.out.println("Clicked Easy Apply button");
		    } else {
		        System.out.println("Easy Apply button not found, stopping loop");
		        break; // ❌ Stop loop if element is not found
		    }
		
		String jobCount = "(//span[text()='Easy Apply']/ancestor::li[contains(@class,'ember-view')]//a)";
		List<WebElement> count = driver.findElements(By.xpath(jobCount));
		int rowCount = count.size(); // Get the total row count
		System.out.println("Total job Count: " + rowCount);
		for (int i = 0; i < rowCount; i++) {
			driver.findElement(By.xpath(jobCount + "[" + i + "]")).click();
			String job = "(//button[@aria-label='Easy Apply to Tester at Unison Group'])[1]";
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(job))); //waiting for easy to Apply Button clickable
			//wait.until(driver -> driver.findElement(By.xpath(job)).isEnabled());
			driver.findElement(By.xpath(job)).click();
			
			
		}
	}

	}

//	@AfterMethod
//	public void closeBrowser() {
//		if (driver != null) {
//			driver.quit();
//		}
//	}
}