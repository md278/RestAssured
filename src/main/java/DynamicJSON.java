import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class DynamicJSON {
    @Test(dataProvider = "booksData")
    public static void addBook(String aisle, String isbin) {
        RestAssured.baseURI = "http://216.10.245.166";
        String response = given().log().all().header("Content-Type", "application/json")
                .body(Payloads.addBook(aisle, isbin))
                .when()
                .post("Library/Addbook.php")
                .then()
                .assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath jsonPath = ReusableMethods.rawToJSON(response);
        String ID = jsonPath.getString("ID");
        System.out.println(ID);
    }

    @DataProvider(name = "booksData")
    public Object[][] getData(String aisle, String isbin) {
        return new Object[][]{
                {"ABCD", "12345"},
                {"LMNOP", "56789"},
                {"EFGHI", "87432"}};
    }
}
