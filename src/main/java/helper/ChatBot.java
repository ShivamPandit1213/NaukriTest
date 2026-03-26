package helper;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.LocatorReader;

public class ChatBot {
	static Actions action;

	public static void chatBot(WebDriver driver) throws Throwable {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		Thread.sleep(2000);
		//String inputField = "//div[@class='textArea']";
		WebElement inputField = driver.findElement(By.xpath("//img[@class='platformlogo']"));
		wait.until(ExpectedConditions.elementToBeClickable(inputField));
		
		String logoXpath = "//div[contains(@class,'active')]//img[@class='platformlogo']";
		if (driver.findElements(By.xpath(logoXpath)).size() > 0) {
		    System.out.println("Chatbot screen is visible.");
		} else {
		    System.err.println("Chatbot screen is not visible.");
		}

		// Load locators ONCE
		String manual1 = LocatorReader.getLocator("manual");
		By manual = By.xpath(manual1);
		String notice1 = LocatorReader.getLocator("notice");
		By notice = By.xpath(notice1);
		String noticeCheckBox1 = LocatorReader.getLocator("noticeCheckBox");
		By noticeCheckBox = By.xpath(noticeCheckBox1);
		String automation1 = LocatorReader.getLocator("automation");
		By automation = By.xpath(automation1);
		String selenium1 = LocatorReader.getLocator("selenium");
		By selenium = By.xpath(selenium1);
		String relocate1 = LocatorReader.getLocator("relocate");
		By relocate = By.xpath(relocate1);
		String testing1 = LocatorReader.getLocator("testing");
		By testing = By.xpath(testing1);
		String current1 = LocatorReader.getLocator("current");
		By current = By.xpath(current1);
		String expected1 = LocatorReader.getLocator("expected");
		By expected = By.xpath(expected1);
		String experience1 = LocatorReader.getLocator("experience");
		By experience = By.xpath(experience1);

		for (int i = 0; i < 15; i++) {

			try {

				if (isElementPresent(driver, manual1)) {

					WebElement manual2 = wait.until(ExpectedConditions.elementToBeClickable(manual));
					new Actions(driver).moveToElement(manual2).click().sendKeys("3.9 Years").sendKeys(Keys.ENTER)
							.perform();
//					manual2.click();
//					manual2.sendKeys("3.9 Years", Keys.ENTER);
				}

				else if (isElementPresent(driver, experience1)) {
					WebElement experience2 = wait.until(ExpectedConditions.elementToBeClickable(experience));
					experience2.click();

					action = new Actions(driver);
					action.moveToElement(experience2);
					action.click();
					action.sendKeys("3.9 Years", Keys.ENTER);
					action.perform();
//					experience2.click();
//					experience2.sendKeys("3.9 Years", Keys.ENTER);
				}

				else if (isElementPresent(driver, notice1)) {
					WebElement notice2 = wait.until(ExpectedConditions.elementToBeClickable(notice));
					notice2.click();
					notice2.sendKeys("Immediate Joiner [LWD: 13 July 2025]", Keys.ENTER);
				}

				else if (isElementPresent(driver, noticeCheckBox1)) {
					WebElement noticeCheckBox2 = wait.until(ExpectedConditions.elementToBeClickable(noticeCheckBox));
					noticeCheckBox2.click();
				}

				else if (isElementPresent(driver, automation1)) {
					WebElement automation2 = wait.until(ExpectedConditions.elementToBeClickable(automation));
					automation2.click();
					automation2.sendKeys("3.4 Years", Keys.ENTER);
				}

				else if (isElementPresent(driver, selenium1)) {
					WebElement selenium2 = wait.until(ExpectedConditions.elementToBeClickable(selenium));
					selenium2.click();
					selenium2.sendKeys("3.4 Years", Keys.ENTER);
				}

				else if (isElementPresent(driver, relocate1)) {
					WebElement relocate2 = wait.until(ExpectedConditions.elementToBeClickable(relocate));
					relocate2.click();
					relocate2.sendKeys("Yes", Keys.ENTER);
				}

				else if (isElementPresent(driver, testing1)) {
					WebElement testing2 = wait.until(ExpectedConditions.elementToBeClickable(testing));
					testing2.click();
					testing2.sendKeys("3.9 Years", Keys.ENTER);
				}

				else if (isElementPresent(driver, current1)) {
					WebElement current2 = wait.until(ExpectedConditions.elementToBeClickable(current));
					current2.click();
					current2.sendKeys("5.2 LPA", Keys.ENTER);
				}

				else if (isElementPresent(driver, expected1)) {
					WebElement current2 = wait.until(ExpectedConditions.elementToBeClickable(expected));
					current2.click();
					current2.sendKeys("8.5 LPA", Keys.ENTER);

				} else {
					System.out.println("Chat completed or no matching question.");
					break;
				}

			} catch (Exception e) {
				System.out.println("Chat ended or element not interactable.");
				break;
			}
		}
	}

	// SAFE element check
	public static boolean isElementPresent(WebDriver driver, String xpath) {
		return driver.findElements(By.xpath(xpath)).size() > 0;
	}
}