package GoRest;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUsersTests {
    Faker faker = new Faker(); // random üretici
    int userID;

    RequestSpecification requestSpecification;
    @BeforeClass
    public void setup() {
        baseURI = "https://gorest.co.in/public/v2/users/";
        requestSpecification = new RequestSpecBuilder()
                .addHeader("Authorization" , "Bearer 4e9aabf3a4a0d952a1a3c335fbe748e315d09716db6964b9347e94f41a94be63")
                .setContentType(ContentType.JSON)
                .setBaseUri(baseURI)
                .build();


    }

    @Test(enabled = false)
    public void createUserClass() {
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

        userID =
                given()
                        .spec(requestSpecification)
                        .body(newUser)
                        // .log().uri()
                        // .log().body()

                        .when()
                        .post("")

                        .then()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
        ;
        System.out.println("userID = " + userID);
    }

    @Test
    public void createUserMap() {

        String rndFullname = faker.name().fullName();
        String rndEmail = faker.internet().emailAddress();

        Map<String, String> newUser = new HashMap<>();
        newUser.put("name", rndFullname);
        newUser.put("gender", "male");
        newUser.put("email", rndEmail);
        newUser.put("status", "active");
        userID =
                given()
                        .spec(requestSpecification)
                        .body(newUser)
                        // .log().uri()
                        // .log().body()

                        .when()
                        .post("")

                        .then()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
        ;
        System.out.println(userID);
    }

    @Test(dependsOnMethods = "createUserMap")
    public void getUserByID() {

        given()
                .spec(requestSpecification)
                .when()
                .get("" + userID)

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

        Map<String,String > updateUser = new HashMap<>();
        updateUser.put("name", "kubilay culha");

        given()
                .spec(requestSpecification)
                .body(updateUser)
                .log().uri()
                .when()
                .put(""+userID)

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(userID))
                .body("name", equalTo("kubilay culha"))
                ;
    }

    @Test(dependsOnMethods = "updateUser")
    public void deleteUser() {
        given()
                .spec(requestSpecification)
                .body(userID)

                .when()
                .delete(""+userID)

                .then()
                .log().all()
                .statusCode(204)
                ;
    }

    @Test(dependsOnMethods = "deleteUser")
    public void deleteUserNegative() {
        given()
                .spec(requestSpecification)
                .body(userID)

                .when()
                .delete(""+userID)

                .then()
                .log().all()
                .statusCode(404)
        ;
    }
    // TODO :HaftaSonu TODO:  GoRest de daha önce yaptığınız posts ve comments resourcelarını
    // TODO  API Automation yapınız(Create,get,update,delete,deletenegatife)
}
