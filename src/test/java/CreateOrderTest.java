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
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

public class CreateOrderTest {

    private ValidatableResponse response;
    private UserApi userApi;
    private OrderApi orderApi;
    private OrderData orderData;
    private String token;
    private IngredientData ingredientData;
    private List<String> ingredients;

    @Before
    public void initOrder() {
        orderApi = new OrderApi();
        ingredientData = orderApi.getIngredient().extract().as(IngredientData.class);
        ingredients = new ArrayList<>();
        ingredients.add(ingredientData.getData().get(0).get_id());
        ingredients.add(ingredientData.getData().get(4).get_id());
        orderData = new OrderData(ingredients);
    }

    @After
    public void cleanUp() {
        if (token != null) {
            response = userApi.deleteUser(token);
            response.log().all()
                    .assertThat()
                    .statusCode(HttpStatus.SC_ACCEPTED);
        }
    }

    @DisplayName("Order can be created with auth test")
    @Description("Check order can be created with authorisation")
    @Test
    public void orderCanBeCreatedWithAuthTest() {
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

        response = orderApi.createOrderWithAuth(orderData, token);
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", is(true));
    }

    @DisplayName("Order can be created without auth test")
    @Description("Check order can be created without authorisation")
    @Test
    public void orderCanBeCreatedWithoutAuthTest() {

        response = orderApi.createOrderWithoutAuth(orderData);
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", is(true));
    }

    @DisplayName("Order cant be created without ingredient test")
    @Description("Check order can't be created without ingredient")
    @Test
    public void orderCantBeCreatedWithoutIngredientTest() {

        orderData = new OrderData();
        response = orderApi.createOrderWithoutAuth(orderData);
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", is("Ingredient ids must be provided"));
    }

    @DisplayName("Order cant be created with wrong ingredient test")
    @Description("Check order can't be created with wrong ingredient")
    @Test
    public void orderCantBeCreatedWithWrongIngredientTest() {

        ingredients.add(ingredientData.getData().get(1).get_id() + "8");
        orderData = new OrderData(ingredients);
        response = orderApi.createOrderWithoutAuth(orderData);
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}
