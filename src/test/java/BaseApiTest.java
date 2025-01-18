import api.OrderApi;
import api.UserApi;
import com.github.javafaker.Faker;
import io.restassured.response.ValidatableResponse;
import model.LoginData;
import model.UserData;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;

public class BaseApiTest {

    protected UserData userData;
    protected LoginData loginData;
    protected UserApi userApi;
    protected Faker faker;
    protected String email;
    protected String password;
    protected String name;
    protected String token;
    protected String authToken;
    protected ValidatableResponse response;
    protected OrderApi orderApi;

    @Before
    public void initUser() {
        userApi = new UserApi();
        faker = new Faker();
        email = faker.internet().emailAddress();
        password = faker.internet().password();
        name = faker.name().firstName();
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

    protected void createUser(String email, String password, String name) {
        userData = new UserData(email, password, name);
        response = userApi.createUser(userData);
        if (response.extract().statusCode() == HttpStatus.SC_OK) {
            token = response.extract().body().jsonPath().getString("accessToken");
        }
    }

    protected void loginUser(String email, String password) {
        loginData = new LoginData(email, password);
        response = userApi.loginUser(loginData);
    }

    protected void getIngr() {
        response = orderApi.getIngredient();
        String id = response.extract().body().jsonPath().getString("data");
    }

//    protected void changeUserData(UserData userData) {
//        userData = new UserData();
//    }
}
