// Validate if Add place API is working as expected.
// Given - All the input details
// When - Submit the API
// Then - Validate the Response

import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class Basics {
    public static void main(String[] args) throws IOException {
        baseURI = "https://rahulshettyacademy.com";

        //Content of the file to String -> Content of the file can convert to Byte -> Byte data to String
        String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body(new String (Files.readAllBytes(Paths.get("resources/addPlace.json"))))
                .when().post("/maps/api/place/add/json")
                .then().assertThat().statusCode(200).body("scope", equalTo("APP"))
                .header("server", "Apache/2.4.18 (Ubuntu)").extract().response().asString();

        System.out.println(response);

        JsonPath jsonPath = new JsonPath(response);
        String placeID = jsonPath.getString("place_id");
        System.out.println(placeID);

        //Update Place
        String updatedPlace = "Gaikwad Nagar, Sai Sadan CHS";

        given()
                .log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "\"place_id\":\"" + placeID + "\",\n" +
                        "\"address\":\"" + updatedPlace + "\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}")
                .when()
                .put("maps/api/place/update/json")
                .then().assertThat().log().all()
                .statusCode(200).body("msg", equalTo("Address successfully updated"));

        //Get Place
        String anotherResponse =
                given()
                        .log().all()
                        .queryParam("place_id", placeID)
                        .queryParam("key", "qaclick123")
                        .when().get("/maps/api/place/get/json")
                        .then()
                        .assertThat().log().all()
                        .statusCode(200).extract().response().asString();
        //.body("address",equalTo(updatedPlace));
//                .body("location.latitude", equalTo("-38.383494"))
//                .body("location.longitude", equalTo( "33.427362"));
//
        JsonPath js = ReusableMethods.rawToJSON(anotherResponse);
        String actualPlace = js.getString("address");
        Assert.assertEquals(actualPlace, updatedPlace);

    }
}
