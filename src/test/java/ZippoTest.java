import com.sun.xml.internal.ws.wsdl.writer.document.soap.Body;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


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
    @Test
    public void checkCountryInResponseBody() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() // dönen body json datası, log.all()
                .statusCode(200) // dönüş kodu 200 mü demek oluyor.
                .body("country", equalTo("United States")) // body nin country degiskeni "United States" esit Mİ

        // pm.response.json().id -> body.id gibi çalışıyor

        ;
    }
    @Test
    public void checkCountryInResponseState() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)
                .body("places[0].state", equalTo("California"));
    }

    @Test
    public void checkHasItem() {
        given()

                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                .log().body()
                .statusCode(200)
                .body("places.'place name'" , hasItem("Dörtağaç Köyü"))
                // bütün place namlerin herhangi birinde Dörtağaç köyü var mı
                ;
    }
    @Test
    public void bodyArrayHasSizeTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                // .log().body()
                .statusCode(200)
                .body("places" , hasSize(1))
          // places de 1 dizi bulunuyor eğer burada 2 verir isek hata verecektir ve hatada sadece 1 dizi var
          //  ama sen bana 2 verdin bu sebepten dolayı hata alıyor.
        ;
    }
    @Test
    public void combiningTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                 // .log().body()
                .statusCode(200)
                .body("places", hasSize(1))
                .body("places.state", hasItem("California"))
                .body("places[0].'place name'", equalTo("Beverly Hills"))
                ;
    }
    @Test
    public void pathParamTest() {
        given()
                .pathParam("ulke","us")
                .pathParam("postaKod", 90210)
                .log().uri() // request Link
                .when()
                .get("http://api.zippopotam.us/{ulke}/{postaKod}")

                .then()
                .statusCode(200)
                .log().body()


        ;
    }
    @Test
    public void queryParamTest() {
        given()
                // https://gorest.co.in/public/v1/users?page=1

                .param("page",1)
                .log().uri() // request Link

                .when()
                .get("https://gorest.co.in/public/v1/users") // ?page=1

                .then()
                .statusCode(200)
                .log().body()


        ;
    }
    @Test
    public void queryParamTest2() {
        // https://gorest.co.in/public/v1/users?page=3
        // bu linkteki 1 den 10 kadar sayfaları çağırdığınızda response daki donen page degerlerinin
        // çağrılan page nosu ile aynı olup olmadığını kontrol ediniz.
        for (int i = 1; i <10; i++) {
            given()
                    .param("page", i)
                    .log().uri()

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .statusCode(200)
                    //.log().body()
                    .body("meta.pagination.page", equalTo(i))

            ;
        }
    }
}

 //   PM                            RestAssured
 //   body.country                  body("country")
 //body.'post code'              body("post code")
 //       body.places[0].'place name'   body("places[0].'place name'")
  //      body.places.'place name'      body("places.'place name'")
  //      bütün place nameleri bir arraylist olarak verir
