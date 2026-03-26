package nokrijob;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import launch.BaseTest;
import publicMethod.PublicMethod;

public class NokriJob extends BaseTest {

	// Action class for advanced mouse/keyboard operations
	Actions action;

	// Explicit wait to handle dynamic web elements
	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

	// XPath locator for pagination "Next" button
	static String nextPagi = "//span[text()='Next']/parent::a";

	// Method: Scroll entire page to bottom
	public void scrollToBottom(WebDriver driver) {

		JavascriptExecutor js = (JavascriptExecutor) driver;

		// Scroll page to bottom using JavaScript
		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}

	// Method: Scroll specific element to center of the screen
	public void scrollToElement(WebElement element) {

		JavascriptExecutor js = (JavascriptExecutor) driver;

		// Bring element to center of screen
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
	}

	@Test
	public void nokri() throws Throwable {

		// Explicit wait used inside test method
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// =========================
		// STEP 1: OPEN NAUKRI WEBSITE
		// =========================
		driver.get("https://www.naukri.com/");

		System.out.println("Naukri launched: " + driver.getCurrentUrl());

		// =========================
		// STEP 2: LOGIN TO ACCOUNT
		// =========================

		String loginBtn = "//a[text()='Login']";

		// Click login button using reusable method
		PublicMethod.click(driver, loginBtn);

		// Locate email field and enter email
		WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//label[text()='Email ID / Username']/following-sibling::input")));

		email.sendKeys("shivamparashar1213@gmail.com");

		// Locate password field and enter password
		WebElement pass = driver.findElement(By.xpath("//label[text()='Password']/following-sibling::input"));

		pass.sendKeys("shivam1213");

		// Click login button
		driver.findElement(By.xpath("//button[text()='Login']")).click();

		Thread.sleep(3000);

		System.out.println("Login successful");

		// =========================
		// STEP 3: SEARCH FOR JOB
		// =========================

		String searchBox = "//span[text()='Search jobs here']/following-sibling::button";

		// Open job search bar
		PublicMethod.click(driver, searchBox);

		// Enter job keywords
		WebElement keyword = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//input[@placeholder='Enter keyword / designation / companies']")));

		keyword.sendKeys("Automation Testing, Automation Tester, QA Automation, Selenium");

		// Click search button
		driver.findElement(By.xpath("//span[text()='Search']//ancestor::button")).click();

		// =========================
		// STEP 4: SET EXPERIENCE FILTER
		// =========================

		String slider = "//div[@class='slider-Container']//div[contains(@class, 'handle')]";

		// Wait until slider visible
		PublicMethod.waitForElementVisible(driver, slider);

		// Move slider to required experience value
		PublicMethod.handleSlider(driver, slider, -182);

		Thread.sleep(2000);

		// =========================
		// STEP 5: APPLY FRESHNESS FILTER
		// =========================

		String jobTime = "//span[text()='Freshness']/parent::div/following-sibling::div//button";

		// Open freshness dropdown
		WebElement latest = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(jobTime)));

		latest.click();

		// Select "Last 7 days"
		WebElement last7days = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Last 7 days']/parent::a")));

		last7days.click();

		System.out.println("Filter applied: Last 7 days");

		// =========================
		// STEP 6: SORT JOBS BY DATE
		// =========================

		Thread.sleep(3000);

		String date = "//button[@id='filter-sort' or @title='Recommended']";

		// Click sort dropdown
		WebElement sortBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(date)));

		sortBtn.click();

		Thread.sleep(2000);

		// Capture screenshot for report
		PublicMethod.getScreenshot(driver);

		// Select "Date" sorting option
		WebElement sortDate = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[@title='Date']/a")));

		sortDate.click();

		System.out.println("Sorted by Date");

		// =========================
		// STEP 7: STORE PARENT WINDOW
		// =========================

		String parentWindow = driver.getWindowHandle();

		System.out.println("Parent Window: " + parentWindow);

		Thread.sleep(4000);

		// =========================
		// STEP 8: GET TOTAL PAGES
		// =========================

		String lastPage = "(//div[@align='center']//a)[last()]";

		int countPageN = Integer.valueOf(driver.findElement(By.xpath(lastPage)).getText());

		// =========================
		// STEP 9: PAGINATION LOOP
		// =========================

		for (int i = 0; i < countPageN; i++) {

			System.out.println("=========== Page: " + (i + 1) + " ==========");

			// Get all job titles on page
			String jobXpath = "//div[@class='srp-jobtuple-wrapper']//div[contains(@class,'row1')]//a";

			List<WebElement> jobs = driver.findElements(By.xpath(jobXpath));

			int jobCount = jobs.size();

			System.out.println("Total jobs: " + jobCount);

			// =========================
			// STEP 10: LOOP THROUGH JOB LIST
			// =========================

			for (int j = 1; j <= jobCount; j++) {

				jobs = driver.findElements(By.xpath(jobXpath));

				WebElement job = jobs.get(j - 1);

				String jobTitle = job.getText();

				System.out.println("\nProcessing Job " + j + ": " + jobTitle);

				// Scroll job into view
				scrollToElement(job);

				// Click job using JavaScript
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", job);

				Thread.sleep(3000);

				// =========================
				// STEP 11: SWITCH TO JOB WINDOW
				// =========================

				String childWindow = null;

				for (String handle : driver.getWindowHandles()) {

					if (!handle.equals(parentWindow)) {

						childWindow = handle;

						driver.switchTo().window(childWindow);

						System.out.println("Child window id: " + childWindow);

						PublicMethod.getScreenshot(driver);

						break;
					}
				}

				// =========================
				// STEP 12: CLICK APPLY / INTEREST BUTTON
				// =========================

				try {

					List<WebElement> applyButton = driver.findElements(By.xpath("(//button[contains(.,'Apply')])[1]"));

					List<WebElement> interestButton = driver
							.findElements(By.xpath("(//button[contains(text(),'interested')])[1]"));

					WebElement btn = null;

					if (!applyButton.isEmpty()) {

						btn = applyButton.get(0);

						((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});",
								btn);

						wait.until(ExpectedConditions.elementToBeClickable(btn));

						((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);

						System.out.println("Clicked Apply button for: " + jobTitle);
					}

					else if (!interestButton.isEmpty()) {

						btn = interestButton.get(0);

						((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});",
								btn);

						wait.until(ExpectedConditions.elementToBeClickable(btn));

						((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);

						System.out.println("Clicked Interested button for: " + jobTitle);
					}

					else {
						System.out.println("No Apply/Interested button available for: " + jobTitle);
					}

				}

				catch (Exception e) {

					System.out.println("Apply / Interested button not found for job: " + jobTitle);

					PublicMethod.getScreenshot(driver);
				}

				// =========================
				// STEP 13: CLOSE CHILD WINDOW
				// =========================

				if (childWindow != null) {

					driver.close();

					driver.switchTo().window(parentWindow);
				}

				else {

					driver.navigate().back();
				}
			}

			// =========================
			// STEP 14: GO TO NEXT PAGE
			// =========================

			List<WebElement> next = driver.findElements(By.xpath(nextPagi));

			if (!next.isEmpty() && next.get(0).isDisplayed()) {

				scrollToBottom(driver);

				next.get(0).click();

				System.out.println("Moving to next page");

				Thread.sleep(4000);
			}

			else {

				System.out.println("No more pages");

				break;
			}
		}
	}
}