package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.LoginData;
import model.UserData;

import static api.Endpoints.*;
import static io.restassured.RestAssured.given;

public class UserApi extends RestApi{

    @Step("Create user")
    public ValidatableResponse createUser(UserData user) {
        return given()
                .spec(requestSpecification())
                .and()
                .body(user)
                .when()
                .post(CREATE_USER_URI)
                .then();
    }

    @Step("Delete user")
    public ValidatableResponse deleteUser(String token) {
        return given()
                .header("Authorization", token)
                .spec(requestSpecification())
                .when()
                .delete(DELETE_USER_URI)
                .then();
    }

    @Step("Login user")
    public ValidatableResponse loginUser(LoginData login) {
        return given()
                .spec(requestSpecification())
                .and()
                .body(login)
                .when()
                .post(LOGIN_USER_URI)
                .then();
    }

    @Step("Change user's data with authorisation")
    public ValidatableResponse changeUserDataWithAuth(UserData user, String token) {
        return given()
                .header("Authorization", token)
                .spec(requestSpecification())
                .and()
                .body(user)
                .when()
                .patch(CHANGE_USER_DATA_URI)
                .then();
    }

    @Step("Change user's data without authorisation")
    public ValidatableResponse changeUserDataWithoutAuth(UserData user) {
        return given()
                .spec(requestSpecification())
                .and()
                .body(user)
                .when()
                .patch(CHANGE_USER_DATA_URI)
                .then();
    }
}
