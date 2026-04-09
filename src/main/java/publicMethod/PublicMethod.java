package publicMethod;

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
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import launch.Launch;

public class PublicMethod {

	// 🔹 Step 1: Declare WebDriver (browser instance)
	// This will be received from Test class (NOT created here)
	private WebDriver driver;

	// 🔹 Step 2: Constructor Injection
	// When Login object is created, driver is passed from Test class
	public PublicMethod(WebDriver driver) {
		this.driver = driver; // Assign passed driver to class variable
	}

	// ===== SCREENSHOT =====
	public File getScreenshot() throws IOException {
		File screenshotDir = new File("src/test/resources/screenshots");
		if (!screenshotDir.exists()) {
			screenshotDir.mkdirs();
			System.out.println("📂 Screenshot folder created.");
		}

		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		File destination = new File(screenshotDir, "Screenshot_" + timestamp + ".png");
		FileUtils.copyFile(source, destination);

		System.out.println("\n📸 Screenshot captured: " + destination.getAbsolutePath());
		return destination;
	}

	public WebElement verifyAndGetElement(String xpath) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		try {
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));

			wait.until(ExpectedConditions.elementToBeClickable(element));

			if (element.isDisplayed() && element.isEnabled()) {
				System.out.println("✅ Element ready: " + xpath);
				return element;
			} else {
				System.err.println("❌ Element not interactable: " + xpath);
				return null;
			}

		} catch (Exception e) {
			System.err.println("❌ Element not found/visible: " + xpath);
			return null;
		}
	}

	// ===== WAIT METHODS =====
	public WebElement waitForElementVisible(String locator) throws Throwable {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		WebElement element = null;
		try {
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
			Thread.sleep(2000);
			getScreenshot();
		} catch (TimeoutException e) {
			throw new RuntimeException("❌ Element not visible: " + locator);
		}
		return element;
	}

	public WebElement waitUntilElementClickable(String locator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
		// System.out.println("✅ Element clickable: " + locator);
		return element;
	}

	// ===== BASIC ACTIONS =====
	public void click(String locator) throws IOException {
		WebElement element = waitUntilElementClickable(locator);
		element.click();
		getScreenshot();
		System.out.println("🖱️ Clicked element: " + locator);
	}

	public void moveToElement(WebElement element) {
		Actions actions = new Actions(driver);
		actions.moveToElement(element).perform();
		// System.out.println("➡️ Moved to element: " + element);
	}

	public String getText(String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		String text = element.getText();
		System.out.println("📄 Text from element: " + text);
		return text;
	}

	// ===== SLIDER HANDLER =====
	public void handleSlider(String locator, int slidePixel) throws Throwable {
		WebElement slider = waitForElementVisible(locator);
		Actions action = new Actions(driver);
		action.clickAndHold(slider).moveByOffset(slidePixel, 0).release().perform();
		getScreenshot();
		// System.out.println("🎚️ Slider moved by " + slidePixel + " pixels.");
	}

	// ===== WINDOW HANDLING =====
	public String getParentWindow() {
		String parent = driver.getWindowHandle();
		System.out.println("🪟 Parent window handle: " + parent);
		return parent;
	}

	public void switchBackToParent(String parentWindow) {
		driver.switchTo().window(parentWindow);
		System.out.println("↩️ Switched back to parent window.");
	}

	// ===== GET CHILD WINDOW HANDLING AND SWITCH TO IT =====
	public String switchToChildWindow(String parentWindow) {
		Set<String> allWindows = driver.getWindowHandles();
		for (String window : allWindows) {
			// Check if it is not parent window
			if (!window.equals(parentWindow)) {
				driver.switchTo().window(window);
				System.out.println("🔄 Switched to child window: " + driver.getTitle());
				return window; // return immediately when child found
			}
		}
		// If no child window found
		System.err.println("⚠️ No child window found.");
		return null;
	}

	// ====== Close Multiple windows if exist except Parent window ======
	public void closeMultipleWindows(String parentWindow) {
		Set<String> allWindows = driver.getWindowHandles();
		for (String window : allWindows) {
			if (!window.equals(parentWindow)) {
				try {
					driver.switchTo().window(window);
					driver.close();
				} catch (Exception e) {
					System.out.println("Error closing window: " + e.getMessage());
				}
			}
		}
		driver.switchTo().window(parentWindow);
	}

	// ===== ELEMENT LIST UTILS =====
	public int elementCount(String locator) {
		List<WebElement> elements = driver.findElements(By.xpath(locator));
		System.out.println("🔢 Found " + elements.size() + " elements for: " + locator);
		return elements.size();
	}

	public String getTextFromListElement(String locator, int index) {
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
	public int textToInteger(String text) {
		int number = Integer.parseInt(text.replaceAll("[^0-9]", ""));
		System.out.println("🔢 Converted text to number: " + number);
		return number;
	}
}
