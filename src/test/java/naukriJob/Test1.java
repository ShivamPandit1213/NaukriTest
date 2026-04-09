package naukriJob;

import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import helper.ChatBot;
import login.Login;
import testng_frame.TestNG_Annotations;
import utils.ConfigReader;

public class Test1 extends TestNG_Annotations {
	
	@Test
	public void test1() throws Throwable {
	    Login callLoging = new Login(driver);
	    callLoging.nokriLogin();
	    
	    // 1. Wait for Naukri dashboard to load
	    Thread.sleep(4000); 
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	    
	    // 2. Save Parent Window ID
	    String parentWindow = driver.getWindowHandle();
	    int initialWindowCount = driver.getWindowHandles().size(); 
	    
	    // 3. Fetch URL
	    String test = ConfigReader.getConfig("notice.joblink");
	    
	    // 4. OPEN IN NEW TAB
	    ((JavascriptExecutor) driver).executeScript("window.open(arguments[0], '_blank');", test);
	    
	    // 5. Wait for the 2nd tab to actually open in the browser
	    wait.until(ExpectedConditions.numberOfWindowsToBe(initialWindowCount + 1));
	    
	    // 6. Find the new Child Window and switch to it
	    Set<String> allWindows = driver.getWindowHandles();
	    for (String window : allWindows) {
	        if (!window.equals(parentWindow)) {
	            driver.switchTo().window(window);
	            break; 
	        }
	    }
	    System.out.println("✅ Switched focus to the New Job Tab.");
        
        // 7. Wait for the new page to settle
        Thread.sleep(3000); 
        
        // --- XPaths for different button states ---
        String applyBtnXpath = "(//button[text()='Apply'])[1]";
        String alreadyAppliedXpath = "//button[text()='Applied'] | //span[text()='Applied'] | //span[contains(@class,'Applied')] | //span[contains(@class,'naukicon-check')]";
        String externalApplyXpath = "//button[text()='Apply on Company Site']";

        boolean shouldRunChatbot = false;

        // ========================================================
        // ✅ SMART PRE-CHECK: Did we already apply?
        // ========================================================
        try {
            // Turn off implicit wait to check instantly
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
            
            if (!driver.findElements(By.xpath(alreadyAppliedXpath)).isEmpty()) {
                System.out.println("✅ Already applied to this job. Skipping.");
            } else if (!driver.findElements(By.xpath(externalApplyXpath)).isEmpty()) {
                System.out.println("⏭️ External apply link. Skipping.");
            } else {
                // If neither, turn implicit wait back on and wait for the normal Apply button
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                
                WebElement applyBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(applyBtnXpath)));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", applyBtn);
                Thread.sleep(1000); 
                
                try {
                    applyBtn.click();
                    System.out.println("✅ Apply button is clicked (Standard).");
                } catch (Exception clickEx) {
                    System.out.println("⚠️ Standard click intercepted. Attempting JS Force Click...");
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", applyBtn);
                    System.out.println("✅ Apply button is clicked (JavaScript).");
                }
                
                shouldRunChatbot = true; // We successfully clicked apply, so flag the bot to run
            }
        } catch (Exception e) {
            System.err.println("❌ Apply button never appeared. Page might be slow or layout changed.");
        } finally {
            // Always ensure implicit wait is reset
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }

        // ========================================================
        // 8. Run the Chatbot ONLY if we clicked Apply
        // ========================================================
        if (shouldRunChatbot) {
            Thread.sleep(2000);
            ChatBot chat = new ChatBot(driver);
            chat.processChat();
            Thread.sleep(3000); // Pause to see success message
        }
        
        // ========================================================
        // 9. CLOSE ONLY THE CHILD TAB AND RETURN TO PARENT
        // ========================================================
        driver.close(); 
        System.out.println("✅ Closed the Child Tab.");
        
        // 10. Switch back to the Parent
        driver.switchTo().window(parentWindow);
        System.out.println("✅ Switched back to the Parent Tab. Ready for the next job!");
        
        Thread.sleep(3000);
    }
}