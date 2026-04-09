package nokrijob.apply;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import base.BaseTest;
import login.Login;
import publicMethod.PublicMethod;

public class NokriDotCom extends BaseTest {

    static String nextPagi = "//span[text()='Next']/parent::a";
    static WebDriverWait wait;

    @Test
    public void nokri() throws Throwable {
        // 1️⃣ Login
        Login login = new Login(driver);
        login.nokriLogin();

        // 2️⃣ Search jobs
        String searchBox = "//span[text()='Search jobs here']/following-sibling::button";
        PublicMethod.waitUntilElementClickable(driver, searchBox);
        PublicMethod.getScreenshot(driver);
        driver.findElement(By.xpath(searchBox)).click();
        Thread.sleep(3000);

        WebElement we = driver.findElement(By.xpath("//input[@placeholder='Enter keyword / designation / companies']"));
        we.sendKeys("Automation Testing, Automation Tester, QA Automation, Selenium");
        driver.findElement(By.xpath("//span[text()='Search']//ancestor::button")).click();
        Thread.sleep(5000);
        PublicMethod.getScreenshot(driver);

        // 3️⃣ Slider example
        String slider = "//div[@class='slider-Container']//div[contains(@class, 'handle')]";
        PublicMethod.waitForElementVisible(driver, slider);
        PublicMethod.handleSlider(driver, slider, -182);

        // 4️⃣ Freshness filter: Last 7 days
        try {
            String jobTime = "//span[contains(text(), 'Freshness')]//following::button[1]";
            WebElement latest = driver.findElement(By.xpath(jobTime));
            PublicMethod.waitUntilElementClickable(driver, jobTime);
            latest.click();
            Thread.sleep(1000);
            driver.findElement(By.xpath("//span[text()='Last 7 days']/parent::a")).click();
            System.out.println("✅ Filter applied: Last 7 days");
            Thread.sleep(2000);
        } catch (Exception e) {
            System.err.println("❌ Error applying freshness filter: " + e.getMessage());
        }

        // 5️⃣ Sort by Date
        try {
            wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            By sortDropdown = By.xpath("//button[@title='Recommended']");
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(sortDropdown));
            dropdown.click();
            Thread.sleep(2000);

            // Close chatbot if exists
            List<WebElement> chatbot = driver.findElements(By.xpath("//div[contains(@class,'chatbot')]//button[contains(@class,'close')]"));
            if (!chatbot.isEmpty()) {
                chatbot.get(0).click();
                System.out.println("✅ Closed chatbot overlay.");
            }

            // Click "Date" option
            By sortByDate = By.xpath("//a[.//span[text()='Date']]");
            WebElement dateOption = wait.until(ExpectedConditions.visibilityOfElementLocated(sortByDate));
            wait.until(ExpectedConditions.elementToBeClickable(dateOption)).click();
            System.out.println("✅ Jobs sorted by Date");
            Thread.sleep(2000);

        } catch (Exception e) {
            System.err.println("❌ Error sorting by Date: " + e.getMessage());
        }

        // 6️⃣ Pagination loop for job application
        String page = "//div[@align='center']//a";
        PublicMethod.waitForElementVisible(driver, page);
        int pageIndex = 1;

        while (true) {
            System.out.println("Currently on Page: " + pageIndex);

            String count = "//div[@class='srp-jobtuple-wrapper']//div[contains(@class,'row1')]//a";
            PublicMethod.waitForElementVisible(driver, count);
            List<WebElement> countJob = driver.findElements(By.xpath(count));
            String parentWindow = driver.getWindowHandle();

            for (int j = 0; j < countJob.size(); j++) {
                WebElement title = countJob.get(j);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", title);
                String jobTitle = title.getText();
                System.out.println("\nTitle " + (j + 1) + " : " + jobTitle);

                try {
                    title.click();
                    Thread.sleep(3000);
                    PublicMethod.getScreenshot(driver);

                    for (String handle : driver.getWindowHandles()) {
                        if (!handle.equals(parentWindow)) {
                            driver.switchTo().window(handle);
                            break;
                        }
                    }

                    // Skip jobs meant for women
                    List<WebElement> prefer = driver.findElements(By.xpath("//span[contains(text(),'Prefers women')]"));
                    if (!prefer.isEmpty()) {
                        System.err.println("Skipped (Prefers women)");
                        driver.close();
                        driver.switchTo().window(parentWindow);
                        continue;
                    }

                    // Apply button
                    String buttonApply = "//button[contains(text(),'Apply')]";
                    WebDriverWait waitSort = new WebDriverWait(driver, Duration.ofSeconds(15));
                    waitSort.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(buttonApply)));
                    WebElement checkJobType = driver.findElement(By.xpath(buttonApply));

                    if (checkJobType.getText().equalsIgnoreCase("Apply")) {
                        checkJobType.click();
                        System.out.println("✅ Applied to job: " + jobTitle);
                    } else {
                        System.out.println("Skipped job: " + jobTitle);
                    }

                    driver.close();
                    driver.switchTo().window(parentWindow);
                    Thread.sleep(3000);

                } catch (Exception e) {
                    System.err.println("Error processing job: " + e.getMessage());
                    for (String handle : driver.getWindowHandles()) {
                        if (!handle.equals(parentWindow)) {
                            driver.close();
                            driver.switchTo().window(parentWindow);
                            break;
                        }
                    }
                }
            }

            List<WebElement> nextPageButton = driver.findElements(By.xpath(nextPagi));
            if (!nextPageButton.isEmpty() && nextPageButton.get(0).isDisplayed() && nextPageButton.get(0).isEnabled()) {
                nextPageButton.get(0).click();
                Thread.sleep(4000);
                pageIndex++;
            } else {
                System.out.println("No more pages left in pagination.");
                break;
            }
        }
    }
}
