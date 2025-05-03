import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.notNullValue;

public class AuthIntegrationTest {

    @BeforeAll
    static void setUp(){
        RestAssured.baseURI = "http://localhost:4004";
    }

    @Test
    public void shouldReturnOKWithValidToken(){
        // 1.-- Arrange
        // 2.-- Act
        // 3.-- Assert

        // Step 1 ->
        String loginPayload = """
                   {
                     "email":"testuser@test.com",
                     "password": "password123"
                   }
                """;

        // Step 2 ->

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login") // Step3  starts belwo
                .then()
                .statusCode(200)
                .body("token",notNullValue())
                .extract()
                .response();

        System.out.println("Generated Token : "+ response.jsonPath().getString("token"));
    }




    @Test
    public void shouldReturnUnauthorizedOnInvalidLogin(){
        // 1.-- Arrange
        // 2.-- Act
        // 3.-- Assert

        // Step 1 ->
        String loginPayload = """
                   {
                     "email":"invalid_user@test.com",
                     "password": "wrongPassword123"
                   }
                """;

        // Step 2 ->

         RestAssured.given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login") // Step3  starts belwo
                .then()
                .statusCode(401);

    }

    
}
