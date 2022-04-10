package com.jira;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import java.io.File;

import static io.restassured.RestAssured.given;

public class JIRATest {
    public static void main(String[] args) {
        RestAssured.baseURI = "http://localhost:8080";

        //Login to JIRA
        SessionFilter sessionFilter = new SessionFilter();
        given()
                .header("Content-Type", "application/json")
                .body("{\"username\": \"shammi93\", \"password\": \"Rozeena@123\"}")
                .log().all()
                .filter(sessionFilter)
                .when()
                .post("/rest/auth/1/session")
                .then()
                .log().all()
                .extract().response().asString();

        String expectedMessage = "Hello";
//
//        //Add a Comment
        String commentsArray = given()
                .pathParam("key", "10000").log().all()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"body\": \"" + expectedMessage + "\",\n" +
                        "    \"visibility\": {\n" +
                        "        \"type\": \"role\",\n" +
                        "        \"value\": \"Administrators\"\n" +
                        "    }\n" +
                        "}")
                .filter(sessionFilter)
                .when()
                .post("/rest/api/2/issue/{key}/comment")
                .then()
                .log().all()
                .assertThat().statusCode(201)
                .extract().response().asString();

        JsonPath jsonPath = new JsonPath(commentsArray);
        String commentID = jsonPath.getString("id");
//
//        //Add an Attachment
//        given()
//                .header("X-Atlassian-Token", "nocheck")
//                .filter(sessionFilter)
//                .pathParam("key", "10000")
//                .header("Content-Type", "multipart/form-data")
//                .multiPart("file", new File("resources/addPlace.json"))
//                .when()
//                .post("/rest/api/2/issue/{key}/attachments")
//                .then()
//                .log().all()
//                .assertThat().statusCode(200);

        //Get an Issue
        String issueDetail = given().filter(sessionFilter)
                .pathParam("key", "10000")
                .queryParam("fields", "comment")
                .when()
                .get("/rest/api/2/issue/{key}")
                .then()
                .log().all()
                .extract().response().asString();
        System.out.println("Issue: " + issueDetail);

        JsonPath jsonPath_1 = new JsonPath(issueDetail);
        int commentsCount = jsonPath_1.get("fields.comment.comments.size()");

        for (int i = 0; i < commentsCount; i++) {
            if (commentID.contentEquals(jsonPath_1.get("fields.comment.comments[" + i + "].id").toString())) {
                String message = jsonPath_1.get("fields.comment.comments[" + i + "].body");
                System.out.println(message);
                Assert.assertEquals(message, expectedMessage);
            }

        }
    }
}
