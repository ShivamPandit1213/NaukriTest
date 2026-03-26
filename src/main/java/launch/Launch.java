package launch;

import java.time.Duration;
import org.openqa.selenium.By;
// Selenium imports
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import helper.ChatBot;
// WebDriverManager import (auto-manages driver binaries)
import io.github.bonigarcia.wdm.WebDriverManager;
import login.Login;
import utils.ConfigReader;

public class Launch {

	// WebDriver declared as protected so child classes can use it
	protected static WebDriver driver;

	// This method runs before every test method
	@BeforeMethod
	@Parameters("browser") // Reads browser name from testng.xml
	public void launchByBrowserName(@Optional("chrome") String browser) {

		// Switch case to launch browser based on parameter
		switch (browser.toLowerCase()) {

		case "chrome":
			// Setup ChromeDriver automatically
			WebDriverManager.chromedriver().setup();

			// Create ChromeDriver instance
			driver = new ChromeDriver();
			break;

		case "firefox":
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			break;

		case "ie":
			WebDriverManager.iedriver().setup();
			driver = new InternetExplorerDriver();
			break;

		case "edge":
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			break;

		default:
			// Throw error if browser name is invalid
			throw new IllegalArgumentException("Browser not supported: " + browser);
		}

		// Maximize browser window
		driver.manage().window().maximize();
		driver.get(ConfigReader.getConfig("naukri.login.url"));
		// Log which browser was launched
		System.out.println("🌐 Browser launched: " + browser);
	}

	@Test
	public void test1() throws Throwable {
		Login.nokriLogin(driver);
		Thread.sleep(1000);
		String test = ConfigReader.getConfig("naukri.test");
		driver.navigate().to(test);

		String applyBtn2 = "(//button[text()='Apply'])[1]";
		// !next.isEmpty() && next.get(0).isDisplayed() && next.get(0).isEnabled()
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement applyBtn3 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(applyBtn2)));
		applyBtn3.click();
		System.out.println("Apply button is clicked.");
		
		ChatBot.chatBot(driver);
	}

	// This method runs after every test method
	@AfterMethod
	public void exitBrowser() {
//		 Check if driver is not null before quitting
		if (driver != null) {
			driver.quit(); // Close all browser windows
			System.out.println("✅ Browser closed successfully.");
		}
	}
}