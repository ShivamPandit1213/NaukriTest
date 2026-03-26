package nokrijob;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import helper.ChatBot;
import launch.BaseTest;
import publicMethod.PublicMethod;

public class Nokri extends BaseTest {

	Actions action;

	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

	// XPath for pagination "Next" button
	static String nextPagi = "//span[text()='Next']/parent::a";

	// Scroll entire page to bottom
	public void scrollToBottom(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}

	// Scroll specific element to center of screen
	public void scrollToElement(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
	}

	@Test
	public void nokri() throws Throwable {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// STEP 1: OPEN NAUKRI WEBSITE
		driver.get("https://www.naukri.com/");
		System.out.println("Naukri launched: " + driver.getCurrentUrl());

		// STEP 2: LOGIN
		String loginBtn = "//a[text()='Login']";
		PublicMethod.click(driver, loginBtn);

		WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//label[text()='Email ID / Username']/following-sibling::input")));
		email.sendKeys("shivamparashar1213@gmail.com");

		WebElement pass = driver.findElement(By.xpath("//label[text()='Password']/following-sibling::input"));
		pass.sendKeys("shivam1213");

		driver.findElement(By.xpath("//button[text()='Login']")).click();
		Thread.sleep(3000);
		System.out.println("Login successful");

		// STEP 3: SEARCH JOB
		String searchBox = "//span[text()='Search jobs here']/following-sibling::button";
		PublicMethod.click(driver, searchBox);

		WebElement keyword = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//input[@placeholder='Enter keyword / designation / companies']")));
		keyword.sendKeys("Automation Testing, Automation Tester, QA Automation, Selenium");

		driver.findElement(By.xpath("//span[text()='Search']//ancestor::button")).click();

		// STEP 4: EXPERIENCE SLIDER
		String slider = "//div[@class='slider-Container']//div[contains(@class, 'handle')]";
		PublicMethod.waitForElementVisible(driver, slider);
		PublicMethod.handleSlider(driver, slider, -182);
		Thread.sleep(2000);

		// STEP 5: FRESHNESS FILTER
		String jobTime = "//span[text()='Freshness']/parent::div/following-sibling::div//button";
		WebElement latest = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(jobTime)));
		latest.click();
		WebElement last7days = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Last 7 days']/parent::a")));
		last7days.click();
		System.out.println("Filter applied: Last 7 days");

		// STEP 6: SORT BY DATE
		Thread.sleep(3000);
		String date = "//button[@id='filter-sort' or @title='Recommended']";
		WebElement sortBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(date)));
		sortBtn.click();
		Thread.sleep(2000);
		PublicMethod.getScreenshot(driver);
		WebElement sortDate = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[@title='Date']/a")));
		sortDate.click();
		System.out.println("Sorted by Date");

		// STEP 7: STORE PARENT WINDOW
		String parentWindow = driver.getWindowHandle();
		System.out.println("Parent Window: " + parentWindow);
		Thread.sleep(4000);

		// STEP 8: TOTAL PAGES
		String lastPage = "(//div[@align='center']//a)[last()]";
		int countPage = Integer.valueOf(driver.findElement(By.xpath(lastPage)).getText());

		// STEP 9: PAGINATION LOOP
		for (int i = 0; i < countPage; i++) {
			System.out.println("\n=========== Page: " + (i + 1) + " ==========");

			String jobXpath = "//div[@class='srp-jobtuple-wrapper']//div[contains(@class,'row1')]//a";
			List<WebElement> jobs = driver.findElements(By.xpath(jobXpath));
			int jobCount = jobs.size();
			System.out.println("Total jobs: " + jobCount);

			// STEP 10: LOOP THROUGH JOBS
			for (int j = 0; j < jobCount; j++) {
				// jobs = driver.findElements(By.xpath(jobXpath));
				WebElement job = jobs.get(j);
				String jobTitle = job.getText();
				// STEP 12: CLICK APPLY / INTERESTED BUTTON
				// Re-fetch list to avoid stale element exception
				System.out.println("\nJob Title " + (j + 1) + ": " + jobTitle);

				// List<WebElement> jobLinks =
				// driver.findElements(By.xpath("//a[contains(@class,'title')]"));
//				System.out.println("\nTotal jobs count: " + jobLinks.size());

				// Skip female-only jobs
				if (!jobTitle.contains("Female")) {
					if (!jobTitle.contains("Female")) {
						PublicMethod.moveToElement(driver, job);
						// Open job details
						job.click();

						// Switch to child window
						String childWindow = PublicMethod.switchToChildWindow(driver, parentWindow);
						System.out.println("Child session Id: " + childWindow);

						try {
							// Locate Apply button
							String applyBtnXpath = "(//button[contains(text(),'Apply') or contains(text(),'interested')])[1]";
							PublicMethod.waitForElementVisible(driver, applyBtnXpath);
							WebElement applyBtn = driver.findElement(By.xpath(applyBtnXpath));

							// Check if apply button visible
							if (applyBtn != null && applyBtn.isDisplayed() && applyBtn.isEnabled()) {
								PublicMethod.moveToElement(driver, applyBtn);
								PublicMethod.waitUntilElementClickable(driver, applyBtnXpath);
								Thread.sleep(1000);
								PublicMethod.getScreenshot(driver);
								String clickable = applyBtn.getText();

								String text2 = "Apply";
								String text3 = "Apply on company site";

								// =============================
								// STEP 11: Apply directly
								// =============================

								if (clickable.equalsIgnoreCase(text2)) {
									Thread.sleep(5000);
									PublicMethod.waitUntilElementClickable(driver, applyBtnXpath);
									applyBtn.click();
									PublicMethod.getScreenshot(driver);
									System.out.println("Button 'Apply' is visible on job: " + (j + 1));
									Thread.sleep(5000);

									Set<String> childWindow2 = driver.getWindowHandles();
									for (String childWindowNew : childWindow2) {
										if (!childWindowNew.equals(parentWindow)) {
											driver.switchTo().window(childWindowNew);
											System.out.println("\nVisible button Name: " + text2);
											PublicMethod.moveToElement(driver, applyBtn);

											WebElement chatBot = driver
													.findElement(By.xpath("//div[@class='chatbot_Nav']"));
											if (chatBot.isDisplayed()) {
												ChatBot.chatBot(driver);
											}
											System.out.println("✅ Applied to job: " + jobTitle);
											// Wait for confirmation message
											String appliedMsg = "//span[contains(text(),'successfully applied')]";

											PublicMethod.waitForElementVisible(driver, appliedMsg);
											WebElement confirmation = driver.findElement(By.xpath(appliedMsg));
											if (confirmation.isDisplayed()) {
												System.out.println("\n✅ Application confirmed.");
											}
											driver.close();
										}
									}
								}

								// =============================
								// STEP 12: Apply on company site
								// =============================

								else if (clickable.equalsIgnoreCase(text3)) {
									System.out.println("Button 'Apply on company site' is visible on job " + (i + 1));
									Set<String> childWindow3 = driver.getWindowHandles();
									for (String childWindowNew : childWindow3) {
										if (!childWindowNew.equals(parentWindow)) {
											driver.switchTo().window(childWindowNew);
											System.out.println("\nVisible button Name: " + text3);
											driver.close();
										}
									}
								}
							} else {
								System.err.println("\n⚠️ Apply button not visible or not clickable.");
							}
						} catch (org.openqa.selenium.NoSuchElementException e) {
							System.err.println("\n❌ Apply button not found for job: " + jobTitle);
						} finally {
							// Close job window
							driver.close();
							// Switch back to parent window
							driver.switchTo().window(parentWindow);
						}
					} else {
						System.err.println("⚠️ Skipping female-only job: " + jobTitle);
					}
				}
			}

			// STEP 14: NEXT PAGE
			List<WebElement> next = driver.findElements(By.xpath(nextPagi));
			if (!next.isEmpty() && next.get(0).isDisplayed()) {
				scrollToBottom(driver);
				next.get(0).click();
				System.out.println("Moving to next page");
				Thread.sleep(4000);
			} else {
				System.out.println("No more pages");
				break;
			}
		}
	}
}