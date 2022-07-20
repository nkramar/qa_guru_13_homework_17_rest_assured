import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specifications {
  public static RequestSpecification requestSpecBaseSettings() {
    return new RequestSpecBuilder()
            .setBaseUri("https://reqres.in/")
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
  }

  public static ResponseSpecification responseSpecStatus200() {
    return new ResponseSpecBuilder()
            .expectStatusCode(200)
            .build();
  }

  public static ResponseSpecification responseSpecError400() {
    return new ResponseSpecBuilder()
            .expectStatusCode(400)
            .build();
  }
  public static ResponseSpecification responseSpecUniqueStatus(int status) {
    return new ResponseSpecBuilder()
            .expectStatusCode(status)
            .build();
  }

  public static void installSpecification(RequestSpecification request, ResponseSpecification response) {
    RestAssured.requestSpecification = request;
    RestAssured.responseSpecification = response;
  }
}
