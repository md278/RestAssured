// Validate if Add place API is working as expected.
// Given - All the input details
// When - Submit the API
// Then - Validate the Response

import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class Basics {
    public static void main(String[] args) {
        baseURI = "https://rahulshettyacademy.com";

        String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body("{\n" +
                        "  \"location\": {\n" +
                        "    \"lat\": -38.383494,\n" +
                        "    \"lng\": 33.427362\n" +
                        "  },\n" +
                        "  \"accuracy\": 50,\n" +
                        "  \"name\": \"Shammi Villa\",\n" +
                        "  \"phone_number\": \"(+91) 983 893 3937\",\n" +
                        "  \"address\": \"29, side layout, cohen 09\",\n" +
                        "  \"types\": [\n" +
                        "    \"shoe park\",\n" +
                        "    \"shop\"\n" +
                        "  ],\n" +
                        "  \"website\": \"http://google.com\",\n" +
                        "  \"language\": \"French-IN\"\n" +
                        "}\n")
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
