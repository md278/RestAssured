package com.specbuilder;

import com.serialize.AddPlace;
import com.serialize.Location;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class SpecBuilder {
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

        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addQueryParam("key", "qaclik123")
                .setContentType(ContentType.JSON)
                .build();

        RequestSpecification requestSpecification1 = given()
                .spec(requestSpecification)
                .body(addPlace);

      ResponseSpecification responseSpecification =  new ResponseSpecBuilder()
              .expectStatusCode(200)
              .expectContentType(ContentType.JSON)
              .build();


        Response response = requestSpecification1
                .when()
                .post("/maps/api/place/add/json")
                .then()
                .spec(responseSpecification)
                .extract().response();
        System.out.println(response.asString());

        JsonPath jsonPath = new JsonPath(response.asString());
        String placeID = jsonPath.getString("place_id");
        System.out.println(placeID);

        Response getPlace = requestSpecification1
                .when()
                .get("/maps/api/place/get/json")
                .then()
                .spec(responseSpecification)
                .extract().response();

        System.out.println(getPlace.asString());
    }
}
