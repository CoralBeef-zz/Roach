package SeleniumTesters;

import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author donne
 */

public class ChromeHeadlessTest {
    
    public static void main(String[] args) {
        try {
            new ChromeHeadlessTest().testExecution();
        } catch(IOException ioe) {
        }
    }
    
    /*
        Each WebDriver instance is a single browser.
        Run it headless by putting "--headless" in the arguments,
        Without the headless argument it just runs the regular chromium instance
    */
    
    public void testExecution() throws IOException {

        //The headless browser instance
        WebDriver driver = null;
        try {
            System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            
            /*
                I'm running it constantly in incognito mode to make sure each instance is fresh without sessions and cookies
                After multiple test cases, I have proven the screen size affects clicks even in headless mode.
                The most consistent results are achieved by starting maximized.
            */
            options.addArguments("--incognito");
            options.addArguments("--start-maximized");

            driver = new ChromeDriver(options);
            
            /*
                Series of page navigation through button clicks.
                I prefer cssSelector but you can use xpath and id's just as easy
            */
            driver.get("https://www.kumon.ne.jp/");
            driver.findElement(By.cssSelector("#sec_gakunen > div > div.sec_graded > "
                    + "div > div.sec_graded_list > ul:nth-child(2) > li:nth-child(2) > a")).click();
            driver.findElement(By.cssSelector("#wrapper > div.h1_style_area_01.ptn_youji > "
                    + "div.inside_area > div > div > p.btn_style_01 > a > span")).click();
            driver.findElement(By.cssSelector("#category-search > map > area:nth-child(48)")).click();
            driver.findElement(By.cssSelector("#lineA > tbody > tr > td:nth-child(2) > p > a")).click();
            driver.findElement(By.cssSelector("#lineA > tbody > tr > td:nth-child(2) > p > a")).click();

            //Initializing displayed browser
            ChromeOptions opt = new ChromeOptions();
            opt.addArguments("--incognito");
            opt.addArguments("--start-maximized");
            //WebDriver displayedBrowser = new ChromeDriver(opt);
            //displayedBrowser.get(driver.getCurrentUrl());
            
            String mappath = "//*[@id=\"icn_no1\"]/div[1]/a"; //The cssSelector of that javascript-generated thing
            /*This next code is an instance of explicit wait, basically checking every 500ms 
               up to the defined timeout. If the conditions are met, only then will it assign 
               myDynamicElement a value, then it proceeds */
            WebElement myDynamicElement = (new WebDriverWait(driver, 10)) //<- Timeout integer
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(mappath)));

            System.out.println("It is " + (myDynamicElement.isDisplayed() ? "DISPLAYED" : "NOT displayed")
                + "!! Element details: " +myDynamicElement);

            if(myDynamicElement.isDisplayed()) {
                System.out.println("The javascript-generated thing is displayed! Clicking it!");
                myDynamicElement.click();
            }
            System.out.println("The last link is:  "+driver.getCurrentUrl());

            System.out.println("Done! Quitting..");
            driver.quit();
        } catch(Exception e) {
            System.out.println(e.toString());
            driver.quit();
        }
    }
}
