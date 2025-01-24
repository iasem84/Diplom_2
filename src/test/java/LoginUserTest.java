import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class LoginUserTest extends BaseApiTest {

    @DisplayName("User can login test")
    @Description("Check user can login with right arguments")
    @Test
    public void userCanLoginTest() {
        createUser(email, password, name);
        loginUser(email, password);
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", is(true));
    }

    @DisplayName("User cant login with wrong email test")
    @Description("Check user cant login with wrong email")
    @Test
    public void userCantLoginWithWrongEmailTest() {
        createUser(email, password, name);
        loginUser(email + "h", password);
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("message", is("email or password are incorrect"));
    }

    @DisplayName("User cant login with wrong password test")
    @Description("Check user cant login with wrong password")
    @Test
    public void userCantLoginWithWrongPasswordTest() {
        createUser(email, password, name);
        loginUser(email, password + "!");
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("message", is("email or password are incorrect"));
    }

}
