package naukriJob;

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

import helper.ChatBot;
import login.Login;
import publicMethod.PublicMethod;
import testng_frame.TestNG_Annotations;

public class NaukriTest extends TestNG_Annotations {

	Actions action;

	// XPath for Next button
	static String nextPagi = "//span[text()='Next']/parent::a";

	// Scroll full page
	public void scrollToBottom(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}

	// Scroll element to center (Highly recommended before clicking jobs)
	public void scrollToElement(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
	}

	public void closeAllChildWindows(String parentWindow) {
		for (String windowHandle : driver.getWindowHandles()) {
			if (!windowHandle.equals(parentWindow)) {
				driver.switchTo().window(windowHandle);
				driver.close();
			}
		}
		// Switch back to parent
		driver.switchTo().window(parentWindow);
	}

	@Test
	public void nokri() throws Throwable {
		PublicMethod callCustomMethod = new PublicMethod(driver);
		ChatBot callChatBot = new ChatBot(driver);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// ================= STEP 1: OPEN WEBSITE =================
		driver.get("https://www.naukri.com/");
		System.out.println("Naukri launched: " + driver.getCurrentUrl());

		// ================= STEP 2: LOGIN =================
		Login callLoging = new Login(driver);
		callLoging.nokriLogin();
		
		// Wait for the dashboard to load after login
		Thread.sleep(4000);

		// ================= STEP 3: SEARCH =================
		Thread.sleep(8000);
		callCustomMethod.click("//span[text()='Search jobs here']/following-sibling::button");

		WebElement keyword = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//input[@placeholder='Enter keyword / designation / companies']")));

		keyword.sendKeys("Automation Testing, Selenium, QA Automation");

		driver.findElement(By.xpath("//span[text()='Search']//ancestor::button")).click();

		// ================= STEP 4: EXPERIENCE FILTER =================
		String slider = "//div[@class='slider-Container']//div[contains(@class, 'handle')]";
		callCustomMethod.waitForElementVisible(slider);
		callCustomMethod.handleSlider(slider, -182);

		// ================= STEP 5: FRESHNESS =================
		Thread.sleep(2000);
		WebElement latest = wait.until(ExpectedConditions.elementToBeClickable(
				By.xpath("//span[text()='Freshness']/parent::div/following-sibling::div//button")));
		latest.click();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Last 7 days']/parent::a"))).click();

		System.out.println("Filter applied: Last 7 days");

		// ================= STEP 6: SORT =================
		Thread.sleep(3000);
		wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//button[@id='filter-sort' or @title='Recommended']"))).click();

		callCustomMethod.getScreenshot();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[@title='Date']/a"))).click();

		System.out.println("Sorted by Date");

		// ================= STEP 7: STORE PARENT WINDOW =================
		String parentWindow = driver.getWindowHandle();

		// ================= PAGINATION CONTROL =================
		int currentPage = 1;
		int maxPages = 50; 

		String jobXpath = "//div[@class='srp-jobtuple-wrapper']//div[contains(@class,'row1')]//a";

		// ================= MAIN LOOP =================
		while (true) {

			System.out.println("\n=========== Page: " + currentPage + " ==========");

			List<WebElement> jobs = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(jobXpath)));
			System.out.println("Total jobs: " + jobs.size());

			// ================= JOB LOOP =================
			for (int j = 0; j < jobs.size(); j++) {

				List<WebElement> jobsFresh = driver.findElements(By.xpath(jobXpath));
				WebElement job = jobsFresh.get(j);

				System.out.println("Job " + (j + 1) + ": " + job.getText());

				scrollToElement(job);
				Thread.sleep(2000);
				
				job.click();

				// ================= SWITCH WINDOW =================
				String childWindow = callCustomMethod.switchToChildWindow(parentWindow);

				// ✅ WAIT for the new child page to load its background scripts!
				Thread.sleep(3500);

				boolean isChildClosed = false;
				boolean applied = false;

				try {
					// Turn OFF implicit wait temporarily for blazing-fast pre-checks
					driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

					// Locators for different states
					String alreadyAppliedXpath = "//button[text()='Applied'] | //span[text()='Applied'] | //span[contains(@class,'Applied')] | //span[contains(@class,'naukicon-check')]";
					String applySiteJobXpath = "(//button[text()='Apply on company site'])[1]";
					String femaleJobXpath = "//span[contains(text(),'women')]";
					String actionBtnXpath = "(//button[text()='Apply'])[1] | (//button[text()='I am interested'])[1]";

					// 1. Check Skip Conditions First
					if (!driver.findElements(By.xpath(alreadyAppliedXpath)).isEmpty()) {
						System.out.println("✅ Already Applied. Skipping.");
					} else if (!driver.findElements(By.xpath(applySiteJobXpath)).isEmpty()) {
						System.out.println("-> External job. Skipping.");
						isChildClosed = true;
					} else if (!driver.findElements(By.xpath(femaleJobXpath)).isEmpty()) {
						System.out.println("* Female-only job. Skipping.");
						isChildClosed = true;
					} 
					// 2. If it passed skip conditions, click Apply!
					else {
						// Turn implicit wait back ON for the actual button find
						driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
						
						List<WebElement> actionBtns = driver.findElements(By.xpath(actionBtnXpath));
						
						if (!actionBtns.isEmpty()) {
							WebElement applyBtn = actionBtns.get(0);
							
							// ROBUST CLICK LOGIC
							scrollToElement(applyBtn);
							Thread.sleep(1000);
							
							try {
								applyBtn.click();
								System.out.println("✅ Apply button clicked (Standard).");
							} catch (Exception clickEx) {
								System.out.println("⚠️ Standard click intercepted. Attempting JS Force Click...");
								((JavascriptExecutor) driver).executeScript("arguments[0].click();", applyBtn);
								System.out.println("✅ Apply button clicked (JavaScript).");
							}
							
							applied = true;
						} else {
							System.out.println("❌ No Apply button found on page.");
						}
					}

				} catch (Exception e) {
					System.out.println("⚠️ Error during Apply phase: " + e.getMessage());
				} finally {
					// Always ensure implicit wait is restored
					driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
				}

				// ================= CHATBOT =================
				if (applied) {
					Thread.sleep(1500); // Give Chatbot time to slide up
					try {
						callChatBot.processChat();
					} catch (Exception e) {
						System.out.println("⚠️ Chatbot skipped or failed.");
					}
				}

				// ================= CLOSE CHILD WINDOW =================
				driver.switchTo().window(childWindow);
				driver.close();
				driver.switchTo().window(parentWindow);
				Thread.sleep(1500);
			}

			// ================= STOP CONDITIONS =================
			if (currentPage >= maxPages) {
				System.out.println(" Reached max page limit");
				break;
			}

			// ================= NEXT BUTTON =================
			List<WebElement> nextBtn = driver.findElements(By.xpath(nextPagi));

			if (!nextBtn.isEmpty() && nextBtn.get(0).isDisplayed()) {
				String classAttr = nextBtn.get(0).getAttribute("class");
				if (!classAttr.contains("disabled")) {
					nextBtn.get(0).click();
					System.out.println("➡️ Moving to next page...");
					wait.until(ExpectedConditions.stalenessOf(jobs.get(0)));
					currentPage++;
				} else {
					System.out.println("🚫 Last page reached");
					break;
				}
			} else {
				System.out.println("🚫 Next button not found");
				break;
			}
		}
		System.out.println("✅ Test Completed");
	}
}