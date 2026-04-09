package job;

import java.util.List;
import java.util.Set;
import java.util.NoSuchElementException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import publicmethods.LaunchBrowser;
import publicmethods.PublicMethod;

public class NokriDotComMain {
	// static String text = null;
	static WebDriver driver;

	static String nextPagi = "//span[text()='Next']/parent::a";

	public static void handlePagination() throws Throwable {
		WebElement nextButton = null;
		try {
		} catch (NoSuchElementException e) {
			System.out.println("Next button not found. Exiting pagination.");
		}

		// Check if "Next" button is enabled
		String classAttr = nextButton.getAttribute("class");

		if (classAttr == null || !classAttr.contains("page-next")) {
			System.out.println("Next button is not enabled. Stopping.");
			// break;
		}

		// Scroll and click next
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextButton);
		nextButton.click();

		// Wait for next page to load (adjust as needed)
		Thread.sleep(2000);
	}
	
@Test
	public static void main(String[] args) throws Throwable {
//		LaunchBrowser browser = new LaunchBrowser();
//		WebDriver driver = browser.chromelaunch();
//		PublicMethod call = new PublicMethod(driver);
		driver = LaunchBrowser.chromelaunch();
		driver.get("https://www.naukri.com/");
		System.out.println("Current URL launched: " + driver.getCurrentUrl());

		String locator = "//a[text()='Login']";
		String text = PublicMethod.getText(driver, locator);
		WebElement btnText = driver.findElement(By.xpath(locator));
		PublicMethod.moveToElement(driver, btnText);
		PublicMethod.click(driver, locator);
		PublicMethod.getTitle(driver);
		PublicMethod.getScreenshot(driver);

		// Nokri.com login with credential
		String emailLocatior = "//label[text()='Email ID / Username']/following-sibling::input";
		String lText = "//label[text()='Email ID / Username']";
		text = PublicMethod.getText(driver, lText);
		PublicMethod.waitTillElementClickable(driver, emailLocatior);
		WebElement email = driver.findElement(By.xpath(emailLocatior));
		email.sendKeys("shivamparashar1213@gmail.com");
		String passLocatior = "//label[text()='Password']/following-sibling::input";
		WebElement pass = driver.findElement(By.xpath(passLocatior));
		pass.sendKeys("shivam1213");
		driver.findElement(By.xpath("//button[text()='Login']")).click();
		Thread.sleep(3000);
		PublicMethod.getScreenshot(driver);
		System.out.println("Login successful");

		String searchBox = "//span[text()='Search jobs here']/following-sibling::button";
		PublicMethod.waitTillElementClickable(driver, searchBox);
		PublicMethod.getScreenshot(driver);
		driver.findElement(By.xpath(searchBox)).click();
		Thread.sleep(3000);
		PublicMethod.getScreenshot(driver);

		WebElement we = driver.findElement(By.xpath("//input[@placeholder='Enter keyword / designation / companies']"));
		we.sendKeys("Automation Testing, Automation Tester, Qa Automation, Qa Testing, Selenium Automation Jobs");
		driver.findElement(By.xpath("//span[text()='Search']//ancestor::button")).click();
		Thread.sleep(5000);
		PublicMethod.getScreenshot(driver);
		String parentWindow = PublicMethod.getWindowHandle(driver);
		System.out.println("Parent session Id: " + parentWindow);

		String slider = "//div[@class='slider-Container']//div[contains(@class, 'handle')]";
		PublicMethod.waitForElementVisible(driver, slider);
		PublicMethod.handleSlider(driver, slider, -182);

		// Jobs latest - Only before 7 days
		String jobTime = "//span[text()='Freshness']/parent::div/following-sibling::div//button";
		WebElement latest = driver.findElement(By.xpath(jobTime));
		PublicMethod.waitForElementVisible(driver, jobTime);
		latest.click();
		driver.findElement(By.xpath("//span[text()='Last 7 days']/parent::a")).click();
		// Select select = new Select();
		Thread.sleep(5000);

		int totalPageCount = 0;
		// List<WebElement> pageCount =
		// driver.findElements(By.xpath("//div[@align='center']//a"));
		int totalPages = driver.findElements(By.xpath("//div[@align='center']//a")).size();

		for (int j = 0; j < totalPages; j++) {
			List<WebElement> pageCount = driver.findElements(By.xpath("//div[@align='center']//a"));
			WebElement page = pageCount.get(j);

			String pageNumber = page.getText();
			System.out.println("Navigating to page: " + pageNumber);
			PublicMethod.moveToElement(driver, page);
			page.click();

			Thread.sleep(3000); // or use proper wait until job listings load
			if (page.equals(10)) {
				break;
			}

			List<WebElement> jobLinks = driver.findElements(By.xpath("//a[contains(@class,'title')]"));
			System.out.println("\nTotal jobs count: " + jobLinks.size());
			for (int i = 0; i < jobLinks.size(); i++) {
				try {
					// Re-fetch the list each time to avoid stale element exception
					jobLinks = driver.findElements(By.xpath("//a[contains(@class,'title')]"));
					WebElement job = jobLinks.get(i);
					String jobTitle = job.getText();

					System.out.println("\nJob Title " + (i + 1) + ": " + jobTitle);
					if (!jobTitle.contains("Female")) {
						PublicMethod.moveToElement(driver, job);
						job.click();// Selecting job on Job Pagination count

						String childWindow = PublicMethod.getChildWindowName(driver, parentWindow);
						PublicMethod.extractMiddleDigits(childWindow, 2, 3);
						System.out.println("Child session Id: " + childWindow);

						try {
							String applyBtnXpath = "(//button[contains(text(),'Apply')])[1]";
							PublicMethod.waitForElementVisible(driver, applyBtnXpath);

							WebElement applyBtn = driver.findElement(By.xpath(applyBtnXpath));
							if (applyBtn != null && applyBtn.isDisplayed() && applyBtn.isEnabled()) {
								PublicMethod.moveToElement(driver, applyBtn);
								PublicMethod.waitTillElementClickable(driver, applyBtnXpath);
								Thread.sleep(1000);
								PublicMethod.getScreenshot(driver);
								String clickable = applyBtn.getText();
								String text2 = "Apply";
								String text3 = "Apply on company site";
								if (clickable.equalsIgnoreCase(text2)) {
									Thread.sleep(5000);
									applyBtn.click(); // Apply job button on Job Page
									PublicMethod.getScreenshot(driver);
									System.out.println("Button 'Apply' is visible on job" + (i + 1));
									Set<String> childWindow2 = driver.getWindowHandles();
									for (String childWindowNew : childWindow2) {
										if (!childWindowNew.equals(parentWindow)) {
											driver.switchTo().window(childWindowNew);
											System.out.println("\nVisible button Name: " + text2);

											Thread.sleep(6000);
											System.out.println("✅ Applied to job: " + jobTitle);

											// Optional: wait for confirmation
											String appliedMsg = "//span[contains(text(),'successfully applied')]";
											PublicMethod.waitForElementVisible(driver, appliedMsg);
											WebElement confirmation = driver.findElement(By.xpath(appliedMsg));
											// driver.close();
											if (confirmation.isDisplayed()) {
												System.out.println("\n✅ Application confirmed.");
											}
											driver.close();
										}
									}
								} else if (clickable.equalsIgnoreCase(text3)) {
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
							driver.close(); // close child window
							driver.switchTo().window(parentWindow); // return to parent
						}
					} else {
						System.err.println("⚠️ Skipping female-only job: " + jobTitle);
					}

				} catch (Exception e) {
					System.err.println("⚠️ Error while processing job " + (i + 1) + ": " + e.getMessage());
					driver.switchTo().window(parentWindow); // Always ensure switch back
				}
			}
		}
	}
}
