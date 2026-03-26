package linkedIn;

import java.io.FileOutputStream;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import loginPage.LogInPage;
import utills.ConfigReader;
//import loginPage.XPath;
import loginPage.*;

public class LinkedInJob2 {
//1.	Launch browser and navigate to LinkedIn.
//2.	Login using your login helper class.
//3.	Close any popups that might block the view.
//4.	Loop over a fixed number of posts:
//			Scroll to each post,
//			Expand truncated posts by clicking "more",
//			Extract emails with regex,
//			Add unique emails to a set,
//			Scroll down to load more posts.
//5. 	Write collected emails into an Excel file, one email per row, first column.

//	public void writeEmailsToExcel(Set<String> emails, String filePath) throws Exception {
//		Workbook workbook = new XSSFWorkbook();
//		Sheet sheet = workbook.createSheet("Emails");
//
//		int rowNum = 0;
//		for (String email : emails) {
//			Row row = sheet.createRow(rowNum++);
//			Cell cell = row.createCell(0);
//			cell.setCellValue(email);
//		}
//
//		FileOutputStream fileOut = new FileOutputStream(filePath);
//		workbook.write(fileOut);
//		fileOut.close();
//		workbook.close();
//
//		System.out.println("Emails written to Excel successfully.");
//	}

	private static void writeEmailsToExcel(Set<String> emails, String filePath) throws Exception {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Emails");

		int rowNum = 0;
		for (String email : emails) {
			Row row = sheet.createRow(rowNum++);
			Cell cell = row.createCell(0); // first column = 0
			cell.setCellValue(email);
		}

		try (FileOutputStream out = new FileOutputStream(filePath)) {
			workbook.write(out);
		}
		workbook.close();

		System.out.println("Emails saved to " + filePath);
	}

	WebDriver driver;
	WebElement element;
	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10000));

	@BeforeMethod
	public void launchBrowser() {
		WebDriverManager.chromedriver().setup(); // Setup ChromeDriver automatically
		driver = new ChromeDriver(); // Instantiate Chrome browser

		driver.manage().window().maximize(); // Maximize browser window
		driver.get(ConfigReader.getProperty("linkedIn_URL")); // Open LinkedIn URL from config

		wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // Setup explicit wait with 18 seconds timeout
	}

	@Test
	public void linkedIn() throws Throwable {
		LogInPage login = new LogInPage(driver);
		login.linkedInLoginMethod(); // Log in to LinkedIn using your own login page class

		// close subscription modal if present
		try {
			WebElement subModalCloseBtn = driver.findElement(By.xpath("//button[@aria-label='Dismiss']"));
			if (subModalCloseBtn.isDisplayed()) {
				subModalCloseBtn.click();
				System.out.println("Subscription modal dismissed.");
			}
		} catch (NoSuchElementException e) {
			// Modal not present, continue
		}

		Set<String> emailSet = new HashSet<>();
		Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,6}");

		for (int i = 1; i <= 15; i++) {
			try {
				WebElement post = driver.findElement(By.xpath("(//div[@class='fie-impression-container'])[" + i + "]"));

				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", post);
				Thread.sleep(2000);

				wait.until(ExpectedConditions.visibilityOf(post));

				// Try to expand post if "...more" button is visible
				try {
					WebElement moreBtn = post.findElement(By.xpath(Locator.moreButton));
					if (moreBtn.isDisplayed() && moreBtn.isEnabled()) {
						wait.until(ExpectedConditions.elementToBeClickable(moreBtn));
						moreBtn.click();
						System.out.println("'...more' clicked on post #" + i);
						Thread.sleep(1000); // Wait for content to load
					}
				} catch (NoSuchElementException e) {
					System.out.println("No '...more' button in post #" + i);
				}

				// Now get full post text
				String postText = post.getText();
				Matcher matcher = emailPattern.matcher(postText);

				boolean foundEmail = false;

				while (matcher.find()) {
					String email = matcher.group();
					emailSet.add(email);
					foundEmail = true;
					System.out.println("Email found in post #" + i + ": " + email);
				}

				if (!foundEmail) {
					System.out.println("No email found in post #" + i);
				}

			} catch (StaleElementReferenceException sere) {
				System.out.println("Post #" + i + " became stale, skipping.");
			} catch (NoSuchElementException e) {
				System.out.println("Post #" + i + " not found, skipping.");
			}

			// Scroll down to load more
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 1000);");
			Thread.sleep(2000);
		}

		// Write all unique emails to Excel after processing all posts
		writeEmailsToExcel(emailSet, "./emails.xlsx");
	}
}

// String jobTab = "//span[text()='Jobs']/ancestor::a";
//		driver.findElement(By.xpath("//span[text()='Jobs']/ancestor::a")).click();// Selecting Job Tab
//		String showJobs = "(//span[text()='Show all']/ancestor::a)[1]";
//		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(showJobs)));
//		driver.findElement(By.xpath(showJobs)).click();// show all Jobs

//	@AfterMethod
//	public void closeBrowser() {
//		if (driver != null) {
//			driver.quit();
//		}
//	}
//}