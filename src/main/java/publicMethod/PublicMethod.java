package publicMethod;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PublicMethod {

	// ===== SCREENSHOT =====
	public static File getScreenshot(WebDriver driver) throws IOException {
		File screenshotDir = new File("src/test/resources/screenshots");
		if (!screenshotDir.exists()) {
			screenshotDir.mkdirs();
			System.out.println("📂 Screenshot folder created.");
		}

		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		File destination = new File(screenshotDir, "Screenshot_" + timestamp + ".png");
		FileUtils.copyFile(source, destination);

		System.out.println("📸 Screenshot captured: " + destination.getAbsolutePath());
		return destination;
	}

	// ===== WAIT METHODS =====
	public static WebElement waitForElementVisible(WebDriver driver, String locator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
		System.out.println("👁️ Element visible: " + locator);
		return element;
	}

	public static WebElement waitUntilElementClickable(WebDriver driver, String locator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
		System.out.println("✅ Element clickable: " + locator);
		return element;
	}

	// ===== BASIC ACTIONS =====
	public static void click(WebDriver driver, String locator) throws IOException {
		WebElement element = waitUntilElementClickable(driver, locator);
		element.click();
		getScreenshot(driver);
		System.out.println("🖱️ Clicked element: " + locator);
	}

	public static void moveToElement(WebDriver driver, WebElement element) {
		Actions actions = new Actions(driver);
		actions.moveToElement(element).perform();
		System.out.println("➡️ Moved to element: " + element);
	}

	public static String getText(WebDriver driver, String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		String text = element.getText();
		System.out.println("📄 Text from element: " + text);
		return text;
	}

	// ===== SLIDER HANDLER =====
	public static void handleSlider(WebDriver driver, String locator, int slidePixel) throws Exception {
		WebElement slider = waitForElementVisible(driver, locator);
		Actions action = new Actions(driver);
		action.clickAndHold(slider).moveByOffset(slidePixel, 0).release().perform();
		getScreenshot(driver);
		System.out.println("🎚️ Slider moved by " + slidePixel + " pixels.");
	}

	// ===== WINDOW HANDLING =====
	public static String getParentWindow(WebDriver driver) {
		String parent = driver.getWindowHandle();
		System.out.println("🪟 Parent window handle: " + parent);
		return parent;
	}

	public static void switchToChildWindow(WebDriver driver, String parentWindow) {
		Set<String> allWindows = driver.getWindowHandles();
		for (String window : allWindows) {
			if (!window.equals(parentWindow)) {
				driver.switchTo().window(window);
				System.out.println("🔄 Switched to child window: " + driver.getTitle());
				return;
			}
		}
		System.err.println("⚠️ No child window found.");
	}

	public static void switchBackToParent(WebDriver driver, String parentWindow) {
		driver.switchTo().window(parentWindow);
		System.out.println("↩️ Switched back to parent window.");
	}

	// ===== ELEMENT LIST UTILS =====
	public static int elementCount(WebDriver driver, String locator) {
		List<WebElement> elements = driver.findElements(By.xpath(locator));
		System.out.println("🔢 Found " + elements.size() + " elements for: " + locator);
		return elements.size();
	}

	public static String getTextFromListElement(WebDriver driver, String locator, int index) {
		List<WebElement> elements = driver.findElements(By.xpath(locator));
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfAllElements(elements));

		try {
			return elements.get(index).getText();
		} catch (StaleElementReferenceException e) {
			elements = driver.findElements(By.xpath(locator));
			return elements.get(index).getText();
		}
	}

	// ===== UTILITY CONVERSIONS =====
	public static int textToInteger(String text) {
		int number = Integer.parseInt(text.replaceAll("[^0-9]", ""));
		System.out.println("🔢 Converted text to number: " + number);
		return number;
	}
}
