package PublicMethods;

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
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import nokriPages.NokriLogin;
import utils.ConfigReader;

public class PublicMethod {
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static Actions action;

    public PublicMethod(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.action = new Actions(driver);
    }

    public void loginToNaukri() {
        NokriLogin loginPage = new NokriLogin(driver);
        //waitForElementVisible(locator);
        loginPage.enterUsername(ConfigReader.get("naukri.user"));
        loginPage.enterPassword(ConfigReader.get("naukri.password"));
        loginPage.clickLogin();
    }

    public static File getScreenshot() throws IOException {
        File screenshotDir = new File("./Screenshots");
        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File dest = new File("./Screenshots/Screenshot_" + timeStamp + ".png");
        FileUtils.copyFile(src, dest);
        return dest;
    }

    public static String getText(String locator) {
        WebElement element = driver.findElement(By.xpath(locator));
        String text = element.getText();
        System.out.println("Element Text: " + text);
        return text;
    }

    public static WebElement waitForElementVisible(String locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
        System.out.println("Element visible: " + element.getText());
        return element;
    }

    public static WebElement waitTillElementClickable(String locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
        System.out.println("Element clickable: " + element.getText());
        return element;
    }

    public static void moveToElement(WebElement element) {
        action.moveToElement(element).perform();
    }

    public static void click(String locator) {
        WebElement element = waitForElementVisible(locator);
        element.click();
        System.out.println("Clicked on: " + locator);
    }

    public static String getTitle() {
        String title = driver.getTitle();
        System.out.println("Page Title: " + title);
        return title;
    }

    public int elementCount(String locator) {
        List<WebElement> elements = driver.findElements(By.xpath(locator));
        int count = elements.size();
        System.out.println("Element count: " + count);
        return count;
    }

    public String getTextFromListElement(String locator, int index) {
        List<WebElement> elements = driver.findElements(By.xpath(locator));
        wait.until(ExpectedConditions.visibilityOfAllElements(elements));
        return elements.get(index).getText();
    }

    public void handleSlider(String locator, int offset) throws InterruptedException {
        WebElement slider = driver.findElement(By.xpath(locator));
        action.clickAndHold(slider).moveByOffset(offset, 0).release().perform();
        Thread.sleep(2000);
    }

    public void switchToChildWindow() {
        String parentWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();
        for (String window : allWindows) {
            if (!window.equals(parentWindow)) {
                driver.switchTo().window(window);
                System.out.println("Switched to child window with title: " + driver.getTitle());
                break;
            }
        }
    }
}

