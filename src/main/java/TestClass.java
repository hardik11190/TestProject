
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestClass {

    static String url = "https://in.bookmyshow.com/buytickets/the-marksman-pune/movie-pune-ET00307138-MT/20210226";
    static String XPATH_TIME_BUTTONS = "//div[@class='showtime-pill-container']/a";
    static String XPATH_THEATRE = "//div[@class='showtime-pill-container']/a[1]//parent::div//parent::div//parent::div//parent::li";

    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        Actions actions = new Actions(driver);
        Wait<WebDriver> wait = new WebDriverWait(driver, 10);

        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        driver.get(url);

        if (driver.findElements(By.xpath("//button[@id=\"wzrk-cancel\"]")).size() != 0) {
            driver.findElement(By.xpath("//button[@id=\"wzrk-cancel\"]")).click();
        }


        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(XPATH_TIME_BUTTONS)));
        List<WebElement> elementList = driver.findElements(By.xpath(XPATH_TIME_BUTTONS));

        JSONArray jsonArray;
        JSONObject jsonObject;
        float lowestPrice = 0;
        HashMap<String, String> hashMap = new HashMap<>();
        SimpleDateFormat f = new SimpleDateFormat("HH:mm");

        for (WebElement element : elementList) {
            actions.moveToElement(element).build().perform();
            jsonArray = new JSONArray(element.getAttribute("data-cat-popup"));

            for (int i = 0; i < jsonArray.length(); i++) {

                if (lowestPrice == 0)
                    lowestPrice = Float.parseFloat(jsonArray.getJSONObject(i).get("price").toString());

                else if (Float.parseFloat(jsonArray.getJSONObject(i).get("price").toString()) == lowestPrice) {

                    hashMap.put(driver.findElement(By.xpath(XPATH_THEATRE)).getAttribute("data-name"), driver.findElement(By.xpath(XPATH_TIME_BUTTONS)).getAttribute("data-date-time"));

                } else if (Float.parseFloat(jsonArray.getJSONObject(i).get("price").toString()) < lowestPrice) {

                    hashMap.clear();
                    lowestPrice = Float.parseFloat(jsonArray.getJSONObject(i).get("price").toString());
                    hashMap.put(driver.findElement(By.xpath(XPATH_THEATRE)).getAttribute("data-name"), driver.findElement(By.xpath(XPATH_TIME_BUTTONS)).getAttribute("data-date-time"));

                }
            }

        }


        System.out.println("Cheapese earliest show is at: " + Arrays.asList(hashMap));
        driver.close();
    }
}
