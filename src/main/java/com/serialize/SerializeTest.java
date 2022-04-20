package com.serialize;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class SerializeTest {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("shoe park");
        list.add("shop");

        Location location = new Location();
        location.setLat(-38.383494);
        location.setLng(33.427362);

        AddPlace addPlace = new AddPlace();
        addPlace.setAccuracy("50");
        addPlace.setAddress("29 side layout, cohen");
        addPlace.setLanguage("French-IN");
        addPlace.setWebsite("http://google.com");
        addPlace.setName("Frontline house");
        addPlace.setPhone_number("\"(+91) 983 893 3937");
        addPlace.setLocation(location);
        addPlace.setTypes(list);

        RestAssured.baseURI = "https://rahulshettyacademy.com";
        Response response = given()
                .queryParams("key", "qaclik123")
                .body(addPlace)
                .when()
                .log().all()
                .post("/maps/api/place/add/json")
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();

        System.out.println(response);

        JsonPath jsonPath = new JsonPath(response.asString());
        String placeID = jsonPath.getString("place_id");
        System.out.println(placeID);


        String getPlace = given()
                .queryParams("place_id", placeID)
                .queryParams("key", "qaclick123")
                .when()
                .get("/maps/api/place/get/json")
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response().asString();

        System.out.println(getPlace);
    }
}
