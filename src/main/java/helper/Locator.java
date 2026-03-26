package helper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Locator {
	//static WebDriver driver;
	public static WebElement callXpathLocator(WebDriver driver, String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		return element;
	}
	public static WebElement callIDLocator(WebDriver driver, String locator) {
		WebElement element = driver.findElement(By.id(locator));
		return element;
	}
	public static WebElement callNameLocator(WebDriver driver, String locator) {
		WebElement element = driver.findElement(By.name(locator));
		return element;
	}
	public static WebElement callCSSselectorLocator(WebDriver driver, String locator) {
		WebElement element = driver.findElement(By.cssSelector(locator));
		return element;
	}
	public static WebElement calllinkTextLocator(WebDriver driver, String locator) {
		WebElement element = driver.findElement(By.linkText(locator));
		return element;
	}
	public static WebElement callPartialLinkTextLocator(WebDriver driver, String locator) {
		WebElement element = driver.findElement(By.partialLinkText(locator));
		return element;
	}
	public static WebElement callTagNameLocator(WebDriver driver, String locator) {
		WebElement element = driver.findElement(By.tagName(locator));
		return element;
	}
	public static WebElement callClassNameLocator(WebDriver driver, String locator) {
		WebElement element = driver.findElement(By.className(locator));
		return element;
	}
}