package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.OrderData;

import static api.Endpoints.INGREDIENT_URI;
import static api.Endpoints.ORDER_URI;
import static io.restassured.RestAssured.given;

public class OrderApi extends RestApi {

    @Step("Create order with authorisation")
    public ValidatableResponse createOrderWithAuth(OrderData orderData, String token) {
        return given()
                .spec(requestSpecification())
                .header("Authorization", token)
                .and()
                .body(orderData)
                .when()
                .post(ORDER_URI)
                .then();
    }

    @Step("Create order without authorisation")
    public ValidatableResponse createOrderWithoutAuth(OrderData orderData) {
        return given()
                .spec(requestSpecification())
                .and()
                .body(orderData)
                .when()
                .post(ORDER_URI)
                .then();
    }

    @Step("Get ingredients")
    public ValidatableResponse getIngredient() {
        return given()
                .spec(requestSpecification())
                .when()
                .get(INGREDIENT_URI)
                .then();
    }

    @Step("Get orders without authorisation")
    public ValidatableResponse getOrderWithoutAuth() {
        return given()
                .spec(requestSpecification())
                .when()
                .get(ORDER_URI)
                .then();
    }

    @Step("Get orders with authorisation")
    public ValidatableResponse getOrderWithAuth(String token) {
        return given()
                .spec(requestSpecification())
                .header("Authorization", token)
                .when()
                .get(ORDER_URI)
                .then();
    }
}
