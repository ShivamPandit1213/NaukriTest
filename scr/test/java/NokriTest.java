package Nokri;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import utils.ConfigReader;
import PublicMethods.PublicMethod;
import io.github.bonigarcia.wdm.WebDriverManager;

public class NokriTest {
	WebDriver driver;
	PublicMethod methods;

	@BeforeMethod
	public void launchBrowser() throws Throwable {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		methods = new PublicMethod(driver);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		driver.get(ConfigReader.get("base.url"));
		System.out.println("\nURL launched: " + driver.getCurrentUrl());

//        PublicMethod.getScreenshot();
	}

	@Test(priority = 1)
	public void naukriLoginTest() throws Throwable {

		// Navigate to Login
		String locator = "//a[text()='Login']";

		String text = PublicMethod.getText(locator);
		System.out.println("Found text: " + text);
		WebElement btnText = driver.findElement(By.xpath(locator));
		// PublicMethod.moveToElement(btnText);
		PublicMethod.waitTillElementClickable(locator);
		PublicMethod.getScreenshot();
		PublicMethod.click(locator);
		PublicMethod.getTitle();

		methods.loginToNaukri();
		System.out.println("✅ Login action completed.");

		// Add a basic validation (placeholder):
		String expectedTitle = "Jobseeker Login | Naukri.com"; // adjust based on real post-login title
		String actualTitle = driver.getTitle();
		System.out.println("✅ Title after login: " + actualTitle);
		assert actualTitle.contains("Naukri") : "❌ Login may have failed or page title mismatch.";
	}

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			driver.quit();
			System.out.println("✅ Browser closed after test.");
		}
	}
}
