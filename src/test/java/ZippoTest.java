import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

public class ZippoTest {
    @Test
    public void test() {
        given()
                // Hazırlık işlemlerinin yapılacağı kısım : (token,send body, parametreler)
                .when()
                   // endpoint(URL), methodu
                        .then()
                       // assertion, test, data işlemleri
        ;
    }
    @Test
    public void statusCodeTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() // dönen body json datası, log.all()
                .statusCode(200) // dönüş kodu 200 mü demek oluyor.

                ;
    }
    @Test
    public void contentTypeTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() // dönen body json datası, log.all()
                .statusCode(200) // dönüş kodu 200 mü demek oluyor.
                .contentType(ContentType.JSON) // dönen sonuç JSON mı 

        ;
    }
}
