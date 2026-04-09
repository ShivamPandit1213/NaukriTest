package helper;

import java.time.Duration;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.LocatorReader;

/**
 * ChatBot Handler Class 
 * Uses a Strategy Pattern to handle dynamic recruiter questions.
 * Optimized for same-page overlays, speed scanning, memory tracking, and instant-apply detection.
 */
public class ChatBot {

	private WebDriver driver;
	private WebDriverWait wait;

	// Registry to store question keys, XPaths, and interaction types
	private Map<String, FieldConfig> fieldRegistry = new LinkedHashMap<>();
	
	// The Bot's Memory: Keeps track of questions already answered to prevent infinite loops
	private Set<String> answeredQuestions = new HashSet<>();

	// Internal POJO for field configuration
	private static class FieldConfig {
		String xpath;
		String value;
		FieldType type;

		FieldConfig(String xpath, String value, FieldType type) {
			this.xpath = xpath;
			this.value = value;
			this.type = type;
		}
	}

	public ChatBot(WebDriver driver) {
		this.driver = driver;
		// 15 seconds is usually enough for the overlay to slide up
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		registerFields(); 
	}

	/**
	 * 🔹 REGISTRY SETUP 
	 * Pairs logical question names with values and interaction strategies.
	 */
	private void registerFields() {
		// General Questions
		register("manual", "manual", "3.9 Years", FieldType.TEXT);
		register("notice", "notice", "Immediate Joiner", FieldType.TEXT);
		register("relocate", "relocateCheckBox1", null, FieldType.CHOICE);
		register("expected", "expected", "8.5 LPA", FieldType.TEXT);
		register("automation", "automation", "3.4 Years", FieldType.TEXT);
		register("selenium", "selenium", "3.4 Years", FieldType.TEXT);
		register("current", "current", "5.2 LPA", FieldType.TEXT);
		register("experience", "experience", "3.9 Years", FieldType.TEXT);
		
		// Adaptive handling for "Immediately" question (Text box vs. Radio Chips)
		register("immediately_text", "immediately", "Yes", FieldType.TEXT);
		register("immediately_choice", "immediately.choice", null, FieldType.CHOICE);
	}

	private void register(String key, String propKey, String value, FieldType type) {
		String xpath = LocatorReader.getLocator(propKey);
		if (xpath != null) {
			fieldRegistry.put(key, new FieldConfig(xpath, value, type));
		}
	}

	/**
	 * 🔹 CORE ENGINE: processChat 
	 * Continuously monitors the chat for visible questions from the registry.
	 */
	public void processChat() throws InterruptedException {
		// UPDATED: Added your specific success XPaths
		String successMsgXpath = "//span[contains(@class,'naukicon-check')] | //span[text()='Applied'] | //*[contains(text(),'application was successful')]";
		String genericInputXpath = "//div[@contenteditable='true'] | //input | //div[contains(@class,'radio')]";

		int unknownQuestionCounter = 0;

		// 🚀 SPEED FIX: Turn OFF implicit wait to instantly check conditions without stalling
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

		// PRE-CHECK: Did the job apply instantly without the chatbot opening?
		if (!driver.findElements(By.xpath(successMsgXpath)).isEmpty()) {
			System.out.println("✅ Job already applied! Skipping Chatbot.");
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			return;
		}

		try {
			System.out.println("⏳ Waiting for Chatbot overlay to render...");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@id,'ChatbotContainer')]")));
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@contenteditable='true']")));
			System.out.println("✅ Chatbot Overlay detected successfully.");
		} catch (Exception e) {
			System.out.println("⚠️ Chatbot UI did not render. It may have applied directly or failed.");
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			return; // Exit safely instead of crashing
		}

		// Main loop (Max 30 interactions)
		for (int i = 0; i < 30; i++) {

			// Check for Success Message using all 3 XPaths
			if (!driver.findElements(By.xpath(successMsgXpath)).isEmpty()) {
				System.out.println("✅ 'Applied' status detected. Exiting Chatbot.");
				// Turn implicit wait back ON before leaving
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
				return;
			}

			boolean actionTaken = false;

			// Scan registry for visible elements (Happens in milliseconds)
			for (Map.Entry<String, FieldConfig> entry : fieldRegistry.entrySet()) {
				String questionKey = entry.getKey();
				
				// 🧠 MEMORY CHECK: If we already answered this question, skip it instantly
				if (answeredQuestions.contains(questionKey)) {
					continue;
				}

				FieldConfig config = entry.getValue();
				try {
					List<WebElement> elements = driver.findElements(By.xpath(config.xpath));

					if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
						
						// Execute the typing or clicking
						executeInteraction(elements.get(0), config);
						
						// 🧠 ADD TO MEMORY: Record that we answered this
						answeredQuestions.add(questionKey);
						
						// If we answered the 'immediately' question, mark both text and choice versions as done
						if (questionKey.startsWith("immediately")) {
							answeredQuestions.add("immediately_text");
							answeredQuestions.add("immediately_choice");
						}

						actionTaken = true;
						unknownQuestionCounter = 0;
						
						// Wait for the bot's "Typing..." animation to finish before the next scan
						Thread.sleep(2000); 
						break; 
					}
				} catch (Exception e) { /* Ignore stale elements */ }
			}

			// Fail-safe for unrecognized questions
			if (!actionTaken) {
				if (isAnyUnknownInputVisible(genericInputXpath)) {
					unknownQuestionCounter++;
				}

				if (unknownQuestionCounter >= 10) {
					System.err.println("❌ Unrecognized question blocked progress. Closing Chatbot.");
					driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
					return;
				}
				Thread.sleep(500); 
			}
		}
		
		// Turn implicit wait back ON if the loop finishes normally
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}

	private boolean isAnyUnknownInputVisible(String xpath) {
		List<WebElement> inputs = driver.findElements(By.xpath(xpath));
		for (WebElement input : inputs) {
			if (input.isDisplayed()) return true;
		}
		return false;
	}

	/**
	 * 🔹 INTERACTION STRATEGY 
	 * Handles modern contenteditable DIVs, event dispatching, and styled radio labels.
	 */
	private void executeInteraction(WebElement element, FieldConfig config) {
		System.out.println("Applying strategy: " + config.type + " to locator: " + config.xpath);

		switch (config.type) {
		case TEXT:
			try {
				// Wait for the text area to be fully visible
				WebElement textArea = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(config.xpath)));
				
				// 1. Hard Focus and Scroll
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", textArea);
				textArea.click();
				Thread.sleep(500);

				// 2. Inject text and dispatch 'input' and 'keyup' events to enable the "Save" button
				((JavascriptExecutor) driver).executeScript(
					"var el = arguments[0];" +
					"el.focus();" +
					"el.innerText = arguments[1];" +
					"el.dispatchEvent(new Event('input', { bubbles: true }));" +
					"el.dispatchEvent(new Event('change', { bubbles: true }));" +
					"el.dispatchEvent(new KeyboardEvent('keyup', { key: 'Enter', bubbles: true }));", 
					textArea, config.value);
				
				Thread.sleep(1000);

				// 3. Physical click on the "Save" button 
				try {
					// Wait a half-second for the React state to enable the button
					Thread.sleep(500);
					
					// Hyper-specific XPath targeting the LAST 'Save' button on the screen
					String saveBtnXpath = "(//div[contains(@class, 'sendMsgbtn_container')]//div[contains(@class, 'sendMsg') and text()='Save'])[last()]";
					
					WebElement saveBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(saveBtnXpath)));
					
					// Force click via JavaScript to bypass any overlays
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
					
					System.out.println("✅ Entered: '" + config.value + "' and clicked Save.");
				} catch (Exception e) {
					System.out.println("⚠️ Save button click failed. Falling back to ENTER.");
					// Advanced ENTER sequence if button fails
					textArea.sendKeys(Keys.ENTER);
				}
			} catch (Exception e) {
				System.err.println("Text input failed: " + e.getMessage());
			}
			break;

		case CHOICE:
			try {
				// Handle styled Radio Buttons (target the associated Label)
				String id = element.getAttribute("id");
				if (id != null && !id.isEmpty()) {
					By labelLoc = By.xpath("//label[@for='" + id + "']");
					WebElement label = wait.until(ExpectedConditions.elementToBeClickable(labelLoc));
					Thread.sleep(500);
					label.click();
				} else {
					wait.until(ExpectedConditions.elementToBeClickable(element)).click();
				}
				System.out.println("✅ Selected choice element.");
			} catch (Exception e) {
				// Final attempt via JS Click
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			}
			break;
		}
	}
}