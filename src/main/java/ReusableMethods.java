import io.restassured.path.json.JsonPath;

public class ReusableMethods {

    public static JsonPath rawToJSON(String jsonResponse){
        JsonPath jsonPath = new JsonPath(jsonResponse);
        return jsonPath;
    }
}
