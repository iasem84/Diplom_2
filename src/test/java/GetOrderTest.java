import api.OrderApi;
import api.UserApi;
import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.IngredientData;
import model.LoginData;
import model.OrderData;
import model.UserData;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

public class GetOrderTest {

    private ValidatableResponse response;
    private OrderApi orderApi;
    private UserApi userApi;
    private String token;

    @After
    public void cleanUp() {
        if (token != null) {
            response = userApi.deleteUser(token);
            response.log().all()
                    .assertThat()
                    .statusCode(HttpStatus.SC_ACCEPTED);
        }
    }

    @DisplayName("Order cant be got without auth test")
    @Description("Check order can't be got without authorisation")
    @Test
    public void orderCantBeGotWithoutAuthTest() {

        orderApi = new OrderApi();
        response = orderApi.getOrderWithoutAuth();
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("message", is("You should be authorised"));
    }

    @DisplayName("Order can be got with auth test")
    @Description("Check order can be got with authorisation")
    @Test
    public void orderCanBeGotWithAuthTest() {

        Faker faker = new Faker();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().firstName();

        UserData userData = new UserData(email, password, name);
        userApi = new UserApi();
        userApi.createUser(userData);

        LoginData loginData = new LoginData(email, password);
        response = userApi.loginUser(loginData);
        token = response.extract().body().jsonPath().getString("accessToken");

        orderApi = new OrderApi();
        IngredientData ingredientData = orderApi.getIngredient().extract().as(IngredientData.class);
        List<String> ingredients = new ArrayList<>();
        ingredients.add(ingredientData.getData().get(0).get_id());
        ingredients.add(ingredientData.getData().get(4).get_id());
        OrderData orderData = new OrderData(ingredients);
        orderApi.createOrderWithAuth(orderData, token);

        response = orderApi.getOrderWithAuth(token);
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", is(true));
    }
}
