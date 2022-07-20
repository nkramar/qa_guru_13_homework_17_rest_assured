import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.equalTo;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class ReqresPageApiTests {

  @Test
  @DisplayName("Check successful user registration")
  public void successfulUserRegistrationTest() {
    Specifications.installSpecification(Specifications.requestSpecBaseSettings(), Specifications.responseSpecStatus200());
    Map<String, String> user = new HashMap<>();
    user.put("email", "eve.holt@reqres.in");
    user.put("password", "pistol");
    Response response =
            given()
                    .body(user)
                    .post("api/register")
                    .then()
                    .extract().response();
    JsonPath jsonPath = response.jsonPath();
    int id = jsonPath.get("id");
    String token = jsonPath.get("token");
    Assertions.assertEquals(4, id);
    Assertions.assertEquals("QpwL5tke4Pnpja7X4", token);
  }

  @Test
  @DisplayName("Check unsuccessful user registration")
  public void unsuccessfulUserRegistrationTest() {

    Specifications.installSpecification(Specifications.requestSpecBaseSettings(), Specifications.responseSpecError400());
    Map<String, String> user = new HashMap<>();
    user.put("email", "sydney@fife");
    Response response = given()
            .body(user)
            .when()
            .post("api/register")
            .then()
            .extract().response();
    JsonPath jsonPath = response.jsonPath();
    String error = jsonPath.get("error");
    Assertions.assertEquals("Missing password", error);
  }

  @Test
  @DisplayName("Check successful user login")
  public void successfulUserLoginTest() {
    Specifications.installSpecification(Specifications.requestSpecBaseSettings(), Specifications.responseSpecStatus200());
    Map<String, String> user = new HashMap<>();
    user.put("email", "eve.holt@reqres.in");
    user.put("password", "cityslicka");
    Response response =
            given()
                    .body(user)
                    .post("api/login")
                    .then()
                    .extract().response();
    JsonPath jsonPath = response.jsonPath();
    String token = jsonPath.get("token");
    Assertions.assertEquals("QpwL5tke4Pnpja7X4", token);
  }

  @Test
  @DisplayName("Check unsuccessful user login")
  public void unsuccessfulUserLoginTest() {

    Specifications.installSpecification(Specifications.requestSpecBaseSettings(), Specifications.responseSpecError400());
    Map<String, String> user = new HashMap<>();
    user.put("email", "peter@klaven");
    Response response = given()
            .body(user)
            .when()
            .post("api/login")
            .then()
            .extract().response();
    JsonPath jsonPath = response.jsonPath();
    String error = jsonPath.get("error");
    Assertions.assertEquals("Missing password", error);
  }

  @Test
  @DisplayName("Check that each avatar contains the right user id and that each user email ends with '@reqres.in'")
  public void checkAvatarAndIdTest() {

    Specifications.installSpecification(Specifications.requestSpecBaseSettings(), Specifications.responseSpecStatus200());
    Response response = given()
            .when()
            .get("api/users?page=2")
            .then()
            .body("page", equalTo(2))
            .body("data.id", notNullValue())
            .body("data.email", notNullValue())
            .body("data.first_name", notNullValue())
            .body("data.last_name", notNullValue())
            .body("data.avatar", notNullValue())
            .extract().response();
    JsonPath jsonPath = response.jsonPath();
    List<String> emails = jsonPath.get("data.email");
    List<Integer> ids = jsonPath.get("data.id");
    List<String> avatars = jsonPath.get("data.avatar");
    for (int i = 0; i < avatars.size(); i++) {
      Assertions.assertTrue(avatars.get(i).contains(ids.get(i).toString()));
    }
    Assertions.assertTrue(emails.stream().allMatch(x -> x.endsWith("@reqres.in")));
  }


  @Test
  @DisplayName("Check that the json data is returned sorted by year")
  public void checkSortedYearsTest() {
    Specifications.installSpecification(Specifications.requestSpecBaseSettings(), Specifications.responseSpecStatus200());
    Response response = given()
            .when()
            .get("api/unknown")
            .then()
            .extract().response();
    JsonPath jsonPath = response.jsonPath();
    List<Integer> years = jsonPath.get("data.year");
    List<Integer> sortedYears = years.stream().sorted().collect(Collectors.toList());
    Assertions.assertEquals(sortedYears, years);
    System.out.println(years);
    System.out.println(sortedYears);

  }

  @Test
  @DisplayName("Check deletion of user")
  public void deleteUserTest() {
    Specifications.installSpecification(Specifications.requestSpecBaseSettings(), Specifications.responseSpecUniqueStatus(204));
    given()
            .when()
            .delete("api/users/2")
            .then();
  }


}

