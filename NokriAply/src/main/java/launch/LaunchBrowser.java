package publicmethods;

import java.io.IOException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class LaunchBrowser {
	static WebDriver driver;

	public static WebDriver chromelaunch() throws IOException {
		WebDriverManager.chromedriver().setup();
//		ChromeOptions ch = new ChromeOptions();
//		driver = new ChromeDriver(ch);
		driver = new ChromeDriver();
	    driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		//PublicMethod call = new PublicMethod(driver);
		//call.getScreenshot(driver);
		System.out.println("\nBrowser launched");
		return driver;
	}

	public static void close(WebDriver driver) {
		driver.close();
	}

}
