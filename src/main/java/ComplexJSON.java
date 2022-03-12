import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import java.util.ArrayList;

public class ComplexJSON {


    public static void main(String[] args) {
        JsonPath jsonPath = ReusableMethods.rawToJSON(Payloads.coursePrice());

//        //Course Count
        int courseCount = jsonPath.getInt("courses.size()");
//        System.out.println("Course Count: " + courseCount);
//
//        //Purchase Amount
//        float coursePrice = jsonPath.getFloat("dashboard.purchaseAmount");
//        System.out.println("Course Price: " + coursePrice);
//
//        //First Course Title
//        String courseTitle = jsonPath.getString("courses[0].title");
//        System.out.println("Course Title :" + courseTitle);
//
//        //All Courses Title
////        ArrayList<Object>[] allCourses = jsonPath.getList("courses");
////        System.out.println(allCourses);
//
//        for (int i = 0; i < courseCount; i++) {
//            System.out.println(jsonPath.get("courses[" + i + "].title").toString());
//            System.out.println(jsonPath.get("courses[" + i + "].price").toString());
//        }

//        for (int i = 0; i < courseCount; i++) {
//            if ((jsonPath.get("courses[" + i + "].title").toString().equalsIgnoreCase("Cypress"))) {
//                System.out.println("Copies Sold: " + jsonPath.get("courses[" + i + "].copies").toString());
//                break;
//            }
//        }
        //Sum of all Course Prices

        float sumOfPrices = 0;

        for (int i = 0; i < courseCount; i++) {
            float price = jsonPath.getFloat("courses[" + i + "].price");
            int copies = jsonPath.getInt("courses[" + i + "].copies");
            sumOfPrices += (price * copies);
        }


        float purchaseAmount = jsonPath.getFloat("dashboard.purchaseAmount");

        if (sumOfPrices == purchaseAmount)
            System.out.println("Sum of the prices for all the Courses Matches with the Purchase Amount: Rs." + purchaseAmount);
        else
            System.out.println("Sum of the Prices didn't match");

        Assert.assertEquals(sumOfPrices, purchaseAmount);

    }
}

