package nokrijob;

// ======================================================
// IMPORT REQUIRED LIBRARIES
// ======================================================

// Java utility libraries
import java.time.Duration;
import java.util.List;

// Selenium libraries
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
// TestNG annotation
import org.testng.annotations.Test;
import login.Login;
// Custom reusable methods (click, wait, screenshot, slider handling etc.)
import publicMethod.PublicMethod;

// ======================================================
// MAIN TEST CLASS
// ======================================================

// This class extends BaseTest so it can use WebDriver initialized there
public class NokriDotCom extends Login {
	public void scrollToTop(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// Scroll to top
		js.executeScript("window.scrollTo(0, 0);");
	}
//
//	public void scrollToBottom(WebDriver driver) {
//		JavascriptExecutor js = (JavascriptExecutor) driver;
//		// Scroll to bottom
//		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
//	}
//
//	public void scrollToElement(String element) {
//		WebElement scroll = driver.findElement(By.xpath(element));
//		JavascriptExecutor js = (JavascriptExecutor) driver;
//		// Scroll element into center
//		js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", scroll);
//	}

	Actions action;
	// Locator for Pagination "Next" button
	static String nextPagi = "//span[text()='Next']/parent::a";

	@Test
	public void nokri(WebDriver driver) throws Throwable {

		// ======================================================
		// STEP 1: LAUNCH NAUKRI WEBSITE
		// ======================================================

		// Open Naukri website in browser
		driver.get("https://www.naukri.com/");

		// Print current URL to console for verification
		System.out.println("🔗 Naukri.com launched: " + driver.getCurrentUrl());

		// ======================================================
		// STEP 2: LOGIN TO NAUKRI ACCOUNT
		// ======================================================

		// Locator for Login button on homepage
		String loginBtn = "//a[text()='Login']";

		// Click login button using reusable method
		PublicMethod.click(driver, loginBtn);

		// Take screenshot after clicking login
		PublicMethod.getScreenshot(driver);

		// ------------------------------------------------------

		// Enter Email ID
		String emailLocator = "//label[text()='Email ID / Username']/following-sibling::input";

		WebElement email = driver.findElement(By.xpath(emailLocator));
		email.sendKeys("shivamparashar1213@gmail.com");

		// ------------------------------------------------------

		// Enter Password
		String passLocator = "//label[text()='Password']/following-sibling::input";

		WebElement pass = driver.findElement(By.xpath(passLocator));
		pass.sendKeys("shivam1213");

		// ------------------------------------------------------

		// Click Login Button
		driver.findElement(By.xpath("//button[text()='Login']")).click();

		// Wait for login process to complete
		Thread.sleep(3000);

		// Take screenshot after successful login
		PublicMethod.getScreenshot(driver);

		System.out.println("Login successful");

		// ======================================================
		// STEP 3: SEARCH FOR AUTOMATION TESTING JOBS
		// ======================================================

		// Click search job icon to open job search panel
		String searchBox = "//span[text()='Search jobs here']/following-sibling::button";

		PublicMethod.click(driver, searchBox);
		Thread.sleep(3000);

		// ------------------------------------------------------

		// Locate search keyword field
		WebElement keyword = driver
				.findElement(By.xpath("//input[@placeholder='Enter keyword / designation / companies']"));

		// Enter multiple keywords related to automation testing
		keyword.sendKeys("Automation Testing, Automation Tester, QA Automation, Selenium");

		// ------------------------------------------------------

		// Click Search button
		driver.findElement(By.xpath("//span[text()='Search']//ancestor::button")).click();

		// Take screenshot after results appear
		PublicMethod.getScreenshot(driver);

		// ======================================================
		// STEP 4: HANDLE EXPERIENCE SLIDER
		// ======================================================

		// Locator for experience range slider
		String slider = "//div[@class='slider-Container']//div[contains(@class, 'handle')]";

		// Wait until slider becomes visible
		PublicMethod.waitForElementVisible(driver, slider);

		// Move slider left/right to set experience value
		// (-182 means moving slider towards fresher side)
		PublicMethod.handleSlider(driver, slider, -182);

		// ======================================================
		// STEP 5: APPLY FRESHNESS FILTER (LAST 7 DAYS)
		// ======================================================

		// Locator for freshness dropdown
		String jobTime = "//span[text()='Freshness']/parent::div/following-sibling::div//button";

		// Wait until dropdown is clickable
		PublicMethod.waitUntilElementClickable(driver, jobTime);

		WebElement latest = driver.findElement(By.xpath(jobTime));

		// If dropdown visible then click it
		if (latest.isDisplayed()) {
			latest.click();
		}

		Thread.sleep(2000);
		// Screenshot before selecting filter
		PublicMethod.getScreenshot(driver);

		// Select "Last 7 days"
		driver.findElement(By.xpath("//span[text()='Last 7 days']/parent::a")).click();
		System.out.println("✅ Filter applied: Last 7 days");

		// ======================================================
		// STEP 6: SORT JOBS BY DATE (NEWEST FIRST)
		// ======================================================

		String date = "//button[@id='filter-sort' or @title='Recommended']";

		// Wait until sort dropdown is clickable
		PublicMethod.waitUntilElementClickable(driver, date);
		Thread.sleep(3000);
		try {

			// Open sort dropdown
			driver.findElement(By.xpath(date)).click();
			Thread.sleep(2000);

			// Screenshot for reference
			PublicMethod.getScreenshot(driver);

			// Select "Date" sorting option
			driver.findElement(By.xpath("//li[@title='Date']/a")).click();

			System.out.println("Filter || Sort, Job post increasing order done.");

		} catch (ElementNotInteractableException e) {
			System.err.println("Element exists but cannot interact " + e.getMessage());
		}

		// ======================================================
		// STEP 7: STORE PARENT WINDOW HANDLE
		// ======================================================

		// Store main window handle so we can switch back later
		String parentWindow = driver.getWindowHandle();
		System.out.println("Parent Window Name: " + parentWindow);

		// ======================================================
		// STEP 8: PAGINATION LOOP THROUGH JOB PAGES
		// ======================================================

		Thread.sleep(5000);

		// Get last page number in pagination
		String lastPage = "(//div[@align='center']//a)[last()]";
		String countPage = driver.findElement(By.xpath(lastPage)).getText();
		int countPageN = Integer.valueOf(countPage);

		// Loop through each page
		for (int i = 0; i <= countPageN; i++) {

			System.out.println(
					"\n==========================  Currently on Page: " + (i + 1) + "==========================");

			// ======================================================
			// STEP 9: GET ALL JOB LISTINGS FROM CURRENT PAGE
			// ======================================================

			String count = "//div[@class='srp-jobtuple-wrapper']//div[contains(@class,'row1')]//a";
			// WebElement element = PublicMethod.waitForElementVisible(driver, count);
			List<WebElement> jobs = driver.findElements(By.xpath(count));
			int JobNum = jobs.size();
			System.out.println("Total job count on current page: " + JobNum);

			// ======================================================
			// STEP 10: PROCESS EACH JOB LISTING
			// ======================================================

			for (int j = 1; j <= JobNum; j++) {

				// Refresh job list to avoid stale element exception
				jobs = driver.findElements(By.xpath(count));
				WebElement job = jobs.get(j - 1);

				// Scroll job element into view
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", job);
				// Get job title text
				String jobTitle = job.getText();
				System.out.println("\nProcessing Job " + (j + 1) + ": " + jobTitle);

				// Click job link
				String jobName = "//a[text()='" + jobTitle + "']";
				System.out.println(jobName);
				Thread.sleep(3000);
				WebElement jobApply = driver.findElement(By.xpath(jobName));

				action = new Actions(driver);
				scrollToTop(driver);
				action.moveToElement(jobApply).perform();
				System.out.println("Action to move to Job apply page.");
				jobApply.click();
				Thread.sleep(30000);

				// ======================================================
				// STEP 11: SWITCH TO JOB DETAILS WINDOW
				// ======================================================

				for (String handle : driver.getWindowHandles()) {
					System.out.println("Window: " + handle);
					if (!handle.equals(parentWindow)) {
						WebDriver childWindow = driver.switchTo().window(handle);
						System.out.println("Child window: " + childWindow);
						Thread.sleep(3000);
						// break;
					}
				}

				// ======================================================
				// STEP 12: SKIP JOBS THAT PREFER WOMEN
				// ======================================================

				List<WebElement> preferWomen = driver
						.findElements(By.xpath("//span[contains(text(),'Prefers women')]"));

				if (!preferWomen.isEmpty()) {

					System.out.println("Skipped (Prefers women)");

					driver.close();

					driver.switchTo().window(parentWindow);

					continue;
				}

				// ======================================================
				// STEP 13: APPLY TO JOB
				// ======================================================

				String applyBtn = "//button[contains(text(),'Apply')]";

				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

				WebElement apply = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(applyBtn)));

				// Scroll to Apply button
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", apply);

				// Click using JavaScript to avoid overlay issues
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", apply);

				System.out.println("✅ Applied to job: " + jobTitle);

				// ======================================================
				// STEP 14: CLOSE CHATBOT IF IT APPEARS
				// ======================================================

				List<WebElement> chatClose = driver
						.findElements(By.xpath("//div[@data-placeholder='Type message here...']"));

				if (!chatClose.isEmpty()) {
					
					chatClose.get(0).click();

					System.out.println("Closed chatbot overlay.");
				}

				Thread.sleep(3000);

				// Close job details window
				driver.close();

				// Switch back to parent window
				driver.switchTo().window(parentWindow);

				// ======================================================
				// STEP 15: GO TO NEXT PAGE (PAGINATION)
				// ======================================================

				List<WebElement> next = driver.findElements(By.xpath(nextPagi));

				// Check if next button exists and is clickable
				if (!next.isEmpty() && next.get(0).isDisplayed() && next.get(0).isEnabled()) {

					next.get(0).click();

					Thread.sleep(4000);

				} else {

					System.out.println("No more pages left in pagination.");

					break;
				}
			}
		}
	}
}