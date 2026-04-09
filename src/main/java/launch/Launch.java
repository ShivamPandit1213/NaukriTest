package launch;

import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ConfigReader;

public class Launch {

	protected WebDriver driver;

	// ✅ Launch method
	public WebDriver launchByBrowserName(String browser) {

		switch (browser.toLowerCase()) {

		case "chrome":
			WebDriverManager.chromedriver().setup();

			ChromeOptions options = new ChromeOptions();
			// Directs all those "data_1" and "lockfile" items to a local temp folder
//			options.addArguments("--user-data-dir=C:/temp/selenium_profile");

			options.addArguments("--start-maximized");
			options.addArguments("--disable-notifications");
			options.addArguments("--disable-infobars");
			options.addArguments("--disable-extensions");
			options.addArguments("--remote-allow-origins=*");

			// ⭐ ADD THESE (fix timeout)
			options.addArguments("--disable-blink-features=AutomationControlled");
			options.addArguments("--disable-gpu");
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");

			// ⭐ Optional but powerful
			options.addArguments("--dns-prefetch-disable");
			options.addArguments("--disable-features=NetworkService");

			// stability
			options.addArguments("--dns-prefetch-disable");
			options.addArguments("--disable-features=NetworkService");

			// VERY IMPORTANT
			options.setPageLoadStrategy(PageLoadStrategy.EAGER);

			driver = new ChromeDriver(options);
			break;

		case "firefox":
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			break;

		case "edge":
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			break;

		case "ie":
			WebDriverManager.iedriver().setup();
			driver = new InternetExplorerDriver();
			break;

		default:
			throw new IllegalArgumentException("Browser not supported: " + browser);
		}

		// driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));

		driver.get(ConfigReader.getConfig("naukri.login.url"));

		System.out.println("=== Browser launched: " + browser);

		return driver;
	}

	/**
	 * ✅ Cookie Management Section
	 */

	private void manageInitialCookies() {
		// Clear existing cookies to start fresh
		driver.manage().deleteAllCookies();
		System.out.println("All initial cookies cleared.");
	}

	// Method to add a specific cookie (useful for bypassing login if you have a
	// valid token)
	public void addCustomCookie(String name, String value) {
		Cookie customCookie = new Cookie(name, value);
		driver.manage().addCookie(customCookie);
		System.out.println("Added cookie: " + name);
	}

	// Method to print all current cookies to console (for debugging)
	public void printAllCookies() {
		Set<Cookie> cookies = driver.manage().getCookies();
		System.out.println("Current Cookies Count: " + cookies.size());
		for (Cookie ck : cookies) {
			System.out.println(
					String.format("Name: %s | Domain: %s | Expiry: %s", ck.getName(), ck.getDomain(), ck.getExpiry()));
		}
	}

	// Method to delete a specific cookie by name
	public void deleteCookieNamed(String name) {
		driver.manage().deleteCookieNamed(name);
	}

	// ✅ Exit method
	public void exitBrowser() {
		if (driver != null) {
			driver.quit();
			System.out.println("✅ Browser closed successfully.");
		}
	}
}