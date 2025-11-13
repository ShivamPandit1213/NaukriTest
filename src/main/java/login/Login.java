package login;

import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import publicMethod.PublicMethod;
import utils.ConfigReader;

public class Login {
	private WebDriver driver;

	public Login(WebDriver driver) {
		this.driver = driver;
	}

	public void nokriLogin() throws InterruptedException, IOException {
		driver.get(ConfigReader.get("nokri_base.url"));
		
		driver.findElement(By.xpath("//a[text()='Login']")).click();

		String emailLocator = "//label[text()='Email ID / Username']/following-sibling::input";
		WebElement email = PublicMethod.waitUntilElementClickable(driver, emailLocator);
		String user = ConfigReader.get("naukri.user");
		email.sendKeys(user);
		System.out.println("Entered Email Id: " + user);

		String passLocator = "//label[text()='Password']/following-sibling::input";
		WebElement passElement = driver.findElement(By.xpath(passLocator));
		String pass = ConfigReader.get("naukri.password");
		passElement.sendKeys(pass);
		System.out.println("Entered Password: " + pass);

		driver.findElement(By.xpath("//button[text()='Login']")).click();
		Thread.sleep(3000);
		PublicMethod.getScreenshot(driver);
		System.out.println("Login successful");
	}
}
