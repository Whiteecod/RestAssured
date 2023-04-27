package GoRest;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUsersTests {
    Faker faker = new Faker(); // random üretici
    int userID;

    @Test
    public void createUser() {
        // POST https://gorest.co.in/public/v2/users
        // {"name":"{{$randomFullName}}", "gender":"male", "email":"{{$randomEmail}}", "status":"active"}
        // "Authorization: Bearer 523891d26e103bab0089022d20f1820be2999a7ad693304f560132559a2a152d"


        String rndFullname = faker.name().fullName();
        String rndEmail = faker.internet().emailAddress();

        User newUser = new User();
        newUser.name = rndFullname;
        newUser.gender = "male";
        newUser.email = rndEmail;
        newUser.status = "active";
/*
        Map<String, String> newUser = new HashMap<>();
        newUser.put("name", rndFullname);
        newUser.put("gender", "male");
        newUser.put("email", rndEmail);
        newUser.put("status", "active");

 */

        userID =
                given()
                        .header("Authorization", "Bearer 4e9aabf3a4a0d952a1a3c335fbe748e315d09716db6964b9347e94f41a94be63")
                        .contentType(ContentType.JSON) // gönderilecek data JSON demiş oluyoruz burada, format olarak
                        .body("{\"name\":\"" + rndFullname + "\", \"gender\":\"male\", \"email\":\"" + rndEmail + "\", \"status\":\"active\"}")
                        // .log().uri()
                        // .log().body()

                        .when()
                        .post("https://gorest.co.in/public/v2/users")

                        .then()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
        ;
        System.out.println("userID = " + userID);
    }

    @Test(dependsOnMethods = "createUser")
    public void getUserByID() {

        given()
                .header("Authorization", "Bearer 4e9aabf3a4a0d952a1a3c335fbe748e315d09716db6964b9347e94f41a94be63")
                .when()
                .get("https://gorest.co.in/public/v2/users/" + userID)

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(userID))
        ;
        System.out.println("userID = " + userID);
    }

    @Test(dependsOnMethods = "getUserByID")
    public void updateUser() {

    }

    @Test(dependsOnMethods = "updateUser")
    public void deleteUser() {

    }

    @Test(dependsOnMethods = "deleteUser")
    public void deleteUserNegative() {

    }
}
