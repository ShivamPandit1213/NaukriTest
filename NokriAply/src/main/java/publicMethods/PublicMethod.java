package publicmethods;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PublicMethod {
	// static String text;
	static WebElement locatorWeb;
	static String attributeName;
	static WebDriver driver;
	static WebElement element;
	static Actions action;
	static String string;
	static WebDriverWait wait;
	
//		In TestNG:
//		Assert.assertNotEquals(actualText, expectedText, "Texts should not match.");
//		In JUnit:
//		Assertions.assertNotEquals(expectedText, actualText);

	// Constructor to accept WebDriver
	public static WebDriver commonMethod() throws IOException {
		driver = LaunchBrowser.chromelaunch();
//		this.browser = driver;
//		this.action = new Actions(driver);
//		this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
		return driver;
	}

	public static File getScreenshot(WebDriver driver) throws IOException {
		File fileLocation = null;
		File flName = new File("./Screenshots");
		if (!flName.exists()) {
			flName.mkdirs(); // Create directory if it doesn't exist
			System.out.println("New Folder Created");
		}
		String timeFormat = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		TakesScreenshot sc = (TakesScreenshot) driver;
		File fileType = sc.getScreenshotAs(OutputType.FILE);
		fileLocation = new File("src/test/resources/screenshots/Screenshot" + timeFormat + ".png");
		FileUtils.copyFile(fileType, fileLocation);
		return fileLocation;
	}

	public static String getText(WebDriver driver, String locator) {
		WebElement locatorWeb = driver.findElement(By.xpath(locator));
		String text = locatorWeb.getText();
		System.out.println("Attribute value_thread: " + text);
//		if (text.equals(null)) {
//			driver.findElement(By.xpath("//label[text()='Email ID / Username']"));
//		}
		return text;
	}

	public static WebElement waitForElementVisible(WebDriver driver, String locator) throws Exception {
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		locatorWeb = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
		if (locatorWeb.isDisplayed()) {
			String text = locatorWeb.getText();
			if (!text.equals(null)) {
				System.out.println(text+" visible on current page");
			} else {
			System.out.println("Element visible: " + locatorWeb);
			getScreenshot(driver);
		} 
//		else if (locatorWeb.isDisplayed()) {
//			driver.navigate().refresh();
//			Thread.sleep(25);
//			System.out.println("Element visible in Thread2: " + locatorWeb);
//			getScreenshot(driver);
		}
		return locatorWeb;
	}

	public static WebElement waitTillElementClickable(WebDriver driver, String locator) {
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		locatorWeb = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
		if (locatorWeb.isDisplayed()) {
			System.out.println("Element is visible.");
			if (locatorWeb.isEnabled()) {
				System.out.println("Element is clickable.");
			} else {
				System.err.println("Element is not clickable.");
			}
		} else {
			System.err.println("Element is not visible.");
		}
		return locatorWeb;
	}

	public static void moveToElement(WebDriver driver, WebElement locator) throws Throwable {
		Actions key = new Actions(driver);
//		element = driver.findElement(By.xpath(locator));
//		key.moveToElement(element).build().perform();

		key.moveToElement(locator).build().perform();
	}

	public static WebElement click(WebDriver driver, String locator) throws Throwable {
		wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		locatorWeb = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
		if (locatorWeb.isDisplayed()) {
			getScreenshot(driver);
			locatorWeb.click();
			System.out.println("Login button selected");
		} else {
			driver.navigate().refresh();
			Thread.sleep(25);
			getScreenshot(driver);
			locatorWeb.click();
			System.out.println("Login button selected");
		}
		return locatorWeb;
	}

	public static String getTitle(WebDriver driver) throws Exception {
//		String windowName = driver.getWindowHandle();
//		System.out.println("Parent window name: "+windowName);
		String parentWindow = driver.getTitle();
		System.out.println("Parent page title is: " + parentWindow);
		return parentWindow;
	}

	public static int elementCount(WebDriver driver, String locator) {
		List<WebElement> list = driver.findElements(By.xpath(locator));
		int count = list.size();
		// System.out.println("Total jobs count: " + count);
		return count;
	}

//	public static String getWebText(WebDriver driver, int i, String locator) {
//		List<WebElement> list = driver.findElements(By.xpath(locator));
//		int count = elementCount(driver, locator);
//		// for (int i = 0; i < count; i++) {
//		// String getWebText = "(" + locator + ")[" + i + "]";
//		// System.out.println("Copied text from web: " + getWebText);
//		text = list.get(i + 1).getText();// in get method i+1 because indexing start from 0
//		System.out.println("WebText " + i + 1 + ": " + text);
//		// }
//		return text;
//	}

	public int textToInteger(String text) {
		int length = Integer.valueOf(text);
		System.out.println("Final Integer: " + length);
		return length;
	}

	public int getTruncatedText(WebDriver driver, int i, int digitLengthToBeRemoved, String locator) throws Throwable {
		String text;
		// WebDriver driver = LaunchBrowser.chromelaunch();
		// int i = digitLengthToBeRemoved;
		List<WebElement> list = driver.findElements(By.xpath(locator));
		// int j = i+1;
		text = list.get(i).getText();// in get method i+1 because indexing start from 0
		System.out.println("Element " + (i + 1) + ": " + text);
		text = text.replaceAll("[^a-zA-Z0-9]", "");// Replacing special character
		System.out.println("After replace special character: " + text);
		text = text.replaceAll("[a-zA-Z]", "");
		System.out.println("After replace alphabatic character: " + text);
		text = text.substring(digitLengthToBeRemoved);
		System.out.println("Final String: " + text);
		int length = Integer.valueOf(text);
		System.out.println("Final Integer: " + length);
		return length;
	}

	public static String extractMiddleDigits(String text, int leftRemove, int rightRemove) {
	    // Safety check
	    if (text.length() < (leftRemove + rightRemove)) {
	        throw new IllegalArgumentException("Not enough digits to truncate.");
	    }
	    // Truncate left and right
	    String result = text.substring(leftRemove, text.length() - rightRemove);
	    System.out.println("Truncated digits: " + result);
	    return result;
	}
	
	public static void handleSlider(WebDriver driver, String element, int slidePixel) throws IOException, Throwable {
		WebElement slider = driver.findElement(By.xpath(element));
		getScreenshot(driver);
		// Create Actions class
		Actions action = new Actions(driver);
		action.clickAndHold(slider).moveByOffset(slidePixel, 0).release().perform(); // move 30px left
		Thread.sleep(2000);
//        // Move slider by offset (pixels)
//        action.clickAndHold(slider).moveByOffset(50, 0).release().perform(); // move 50px right
//        Thread.sleep(2000);
//        action.clickAndHold(slider).moveByOffset(-30, 0).release().perform(); // move 30px left

//        int width = slider.getSize().width;
//        int targetX = (int)(width * 0.7); // 70%
//        action.clickAndHold(slider).moveByOffset(targetX, 0).release().perform();
	}

	static String parentWindowSessionId;
	public static String getWindowHandle(WebDriver driver) throws Throwable {
		// Copy Parent Window Session
		String parentWindowSessionId = driver.getWindowHandle();
		return parentWindowSessionId; // Return Parent Window Session Id
	}

	public static String getChildWindowName(WebDriver driver, String parentWindow) throws Throwable {
//		Set<String> sessionId = driver.getWindowHandles();
		String childName = null;
//		System.out.println("All window sessionId: "+sessionId);
		Set<String> allWindows = driver.getWindowHandles();
		for (String newChild : allWindows) {
			if (!newChild.equals(parentWindow)) {
				driver = driver.switchTo().window(newChild);
				childName = getTitle(driver);
				System.out.println("\nChild Window Name: " + driver);
				break;
			}
		}
		return childName;
	}

	public static void switchToWindow(WebDriver driver, String windowToSwitch) throws Throwable {
		Set<String> allWindows = driver.getWindowHandles();
		if (windowToSwitch.equals(allWindows)) {
			WebDriver newWindow = driver.switchTo().window(windowToSwitch);
			getTitle(driver);
			System.out.println("Window Name: " + newWindow);
		}
	}

	public static String getTextFromListElement(WebDriver driver, String xpath, int index) {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    List<WebElement> elements = driver.findElements(By.xpath(xpath));
	    wait.until(ExpectedConditions.visibilityOfAllElements(elements));

	    try {
	        return elements.get(index).getText();
	    } catch (StaleElementReferenceException e) {
	        // Refetch on stale
	        elements = driver.findElements(By.xpath(xpath));
	        return elements.get(index).getText();
	    }
	}
	
	public WebDriver getWindowHandles(String parentWindowSessionId, String toClickOnLocator) throws Throwable {
		WebElement elementTriggerWindow = driver.findElement(By.xpath(toClickOnLocator));
		action.moveToElement(elementTriggerWindow);
		elementTriggerWindow.click();

		System.out.println("\nSwitch to new window - LogIn (Child Window)");
		// new Window Launched - with Log-In Form
		Set<String> sessionIds = driver.getWindowHandles();
		wait.until(ExpectedConditions.numberOfWindowsToBe(2)); // Ensure new window has opened
		System.out.println("Session Ids : " + sessionIds);

		for (String windowHandle : driver.getWindowHandles()) {
			if (!windowHandle.equals(parentWindowSessionId)) {
				driver.switchTo().window(windowHandle);
				System.out.println("Switched to new window with session id: " + windowHandle);
			}
		}
		return driver;
	}
}
