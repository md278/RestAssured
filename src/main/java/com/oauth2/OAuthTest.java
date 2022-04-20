package com.oauth2;

import com.pojo.Api;
import com.pojo.Courses;
import com.pojo.GetCourse;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static io.restassured.RestAssured.authentication;
import static io.restassured.RestAssured.given;

public class OAuthTest {
//  private static WebDriver driver;
    private static String url = "https://rahulshettyacademy.com/getCourse.php?state=shammi&code=4%2F0AX4XfWiqzi2yU9TbKj0eKUtTyw2pLtRdqt0e-CiHXp8n3Rm6tcnV0RqmEDnc5X9r_k9i-w&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none";

    private static String getAuthorizationCode(){
//        WebDriverManager.chromedriver().browserVersion("100.0.4896.88").setup();
//        driver = new ChromeDriver();
//        driver.get(url);
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
//        driver.findElement(By.id("identifierId")).sendKeys("tradernoob626@gmail.com");
//        driver.findElement(By.xpath("//span[text()='Next']")).click();
//        driver.findElement(By.xpath("//input[@aria-label=\"Enter your password\"]")).sendKeys("Trading@123");
//        driver.findElement(By.xpath("//span[text()='Next']")).click();
        String partialUrl = url.split("code=")[1];
        String authorizationCode = partialUrl.split("&scope")[0];
        return authorizationCode;
    }
    public static void main(String[] args) {
        String accessTokenResponse = given()
                .urlEncodingEnabled(false)
                .queryParams("code",getAuthorizationCode())
                .queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
                .queryParams("grant_type", "authorization_code")
                .log().all()
                .when().post("https://www.googleapis.com/oauth2/v4/token").asString();

        JsonPath jsonPath = new JsonPath(accessTokenResponse);
        String accessToken = jsonPath.getString("access_token");

        GetCourse response = given()
                .queryParam("access_token", accessToken)
                .expect()
                .defaultParser(Parser.JSON)
                .when()
                .get("https://rahulshettyacademy.com/getCourse.php")
                .as(GetCourse.class);

        List<Api> apiList = response.getCourses().getApi();

        for(Api api : apiList) {
            System.out.println(api.getCourseTitle());
            System.out.println(api.getPrice());
        }

        //System.out.println(response.getCourses().getApi().get(1));

//        System.out.println(response.getServices());
//        System.out.println(response.getLinkedIn());
//        System.out.println(response.getInstructor());
    }
}
