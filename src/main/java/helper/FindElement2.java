package helper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FindElement2 {
	public WebElement getElement(WebDriver driver, String locator) {
//EVEN BETTER (PRO FRAMEWORK TIP)
//Instead of : you can use = twice or ||:
//		String[] parts = locator.split("=", 2); 
		//		✔ Safe if your XPath doesn’t contain =
		//		⚠ But XPath often contains = (like @id='username') → can break logic if misused
//		String[] parts = locator.split("||", 2);
		//		|| is a special regex operator (OR operator), so Java treats it incorrectly.
		String[] parts = locator.split(":", 2);

		String type = parts[0];
		String value = parts[1];

		switch (type.toLowerCase()) {

		case "id":
			return driver.findElement(By.id(value));

		case "name":
			return driver.findElement(By.name(value));

		case "xpath":
			return driver.findElement(By.xpath(value));

		case "css":
			return driver.findElement(By.cssSelector(value));

		case "linktext":
			return driver.findElement(By.linkText(value));

		case "partiallinktext":
			return driver.findElement(By.partialLinkText(value));

		case "tagname":
			return driver.findElement(By.tagName(value));

		case "classname":
			return driver.findElement(By.className(value));

		default:
			throw new RuntimeException("❌ Invalid locator type: " + type);
		}
	}
}