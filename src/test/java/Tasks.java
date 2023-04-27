import Model.Location;
import Model.Place;
import Model.ToDo;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Locale;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Tasks {
    @Test
    public void test1() {
        /*
        Task 2
            create a request to https://httpstat.us/203
            expect status 203
            expect content type TEXT

         */
        given()
                .when()
                .get("https://httpstat.us/203")

                .then()
                .log().all()
                .statusCode(203)
                .contentType(ContentType.TEXT)

                ;
    }
    @Test
    public void Task2() {
        /** Task 1

         create a request to https://jsonplaceholder.typicode.com/todos/2
         expect status 200
         Converting Into POJO
         */
         ToDo todo =
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .log().body()
                .statusCode(200)
                .extract().body().as(ToDo.class)
                ;
        System.out.println("todo = " + todo);
        System.out.println("todo.getTitle() = " + todo.getTitle());
    }
    @Test
    public void Task3(){
        /**

         Task 3
         create a request to https://jsonplaceholder.typicode.com/todos/2
         expect status 200
         expect content type JSON
         expect title in response body to be "quis ut nam facilis et officia qui"
         */
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title", equalTo("quis ut nam facilis et officia qui"))
                ;
    }
    @Test
    public void Task4() {
        /**

         Task 4
         create a request to https://jsonplaceholder.typicode.com/todos/2
         expect status 200
         expect content type JSON
         expect response completed status to be false
         extract completed field and testNG assertion
         */

        // 1. YÖNTEM
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("completed", equalTo(false))
                ;

        // 2. YÖTNEM
         Boolean completed =
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().path("completed")
        ;
        Assert.assertFalse(completed);

    }
}
