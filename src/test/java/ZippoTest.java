import Model.Location;
import Model.Place;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;

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
                .get("http://api.zippopotam.us/us/90210")

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

    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    @BeforeClass
    public void Setup() {
        baseURI = "https://gorest.co.in/public/v1";

        requestSpec = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setContentType(ContentType.JSON)
                .build();
        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .log(LogDetail.BODY)
                .build();
    }
    @Test
    public void test1(){
        given()
                .param("page",1)
                .spec(requestSpec)

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .spec(responseSpec)
                ;
    }
    @Test
    public void extractingJsonPath() {
        String countryName =
                given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .extract().path("country")
                ;
        System.out.println("countryName = " + countryName);
        Assert.assertEquals(countryName , "United States");
    }
    @Test
    public void extractingJsonPath2() {
        String placeName =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")
                        .then()
                        .log().body()
                        .extract().path("places[0].'place name'")
                ;
        System.out.println("placeName = " + placeName);
        Assert.assertEquals(placeName, "Beverly Hills");
    }
    @Test
    public void extractingJsonPath3() {
        int limit =
        given()
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                // .log().body()
                .statusCode(200)
                .extract().path("meta.pagination.limit")
                ;
        System.out.println("limit = " + limit);
    }
    @Test
    public void extractingJsonPath4() {
        // https://gorest.co.in/public/v1/users dönen değerdeki bütün idleri yazdırınız
        List<Integer> idList =
        given()
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                //.log().body()
                .statusCode(200)
                .extract().path("data.id")
                ;
        System.out.println("idList = " + idList);
    }
    @Test
    public void extractingJsonPath5() {
        // https://gorest.co.in/public/v1/users dönen değerdeki bütün idleri yazdırınız
        List<String> names =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("data.name")
                ;
        System.out.println("names = " + names);
    }
    @Test
    public void extractingJsonPathResponsAll() {
        // https://gorest.co.in/public/v1/users dönen değerdeki bütün idleri yazdırınız
        Response donenData =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().response()
                ;
        List<Integer> idler = donenData.path("data.id");
        List<String> names = donenData.path("data.name");
        int limit = donenData.path("meta.pagination.limit");

        System.out.println("idler = " + idler);
        System.out.println("names = " + names);
        System.out.println("limit = " + limit);

        // Assert.assertTrue(names.contains("Ms. Kamalesh Dubashi"));
        Assert.assertTrue(idler.contains(1203756));
        Assert.assertEquals(limit,10, " test sonucu hatalı");
    }
    @Test
    public void extractJsonAll_POJO() {
        // POJO : JSON nesnesi demek
        Location locationNesnesi =
                given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                // .log().body()
                .extract().body().as(Location.class)
                ;
        System.out.println("locationNesnesi = " + locationNesnesi.getCountry());

        for (Place p: locationNesnesi.getPlaces())
            System.out.println("p = " + p);

        System.out.println(locationNesnesi.getPlaces().get(0).getPlacename());

    }
    @Test
    public void extractJsonPOJO_Soru() {
        // aşağıdaki endpointte dörtağaç köyüne ait diğer bilgileri yazdırınız
       Location adana =
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                // .log().body()
                .statusCode(200)
                .extract().body().as(Location.class)
                ;
         for (Place p: adana.getPlaces())
             if (p.getPlacename().equalsIgnoreCase("Dörtağaç Köyü")) {
                 System.out.println("p = " + p);
             }
    }
}

 //   PM                            RestAssured
 //   body.country                  body("country")
 //body.'post code'              body("post code")
 //       body.places[0].'place name'   body("places[0].'place name'")
  //      body.places.'place name'      body("places.'place name'")
  //      bütün place nameleri bir arraylist olarak verir
