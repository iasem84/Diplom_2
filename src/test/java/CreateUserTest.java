import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class CreateUserTest extends BaseApiTest {

    @DisplayName("User can be created test")
    @Description("Check user can be created with right arguments")
    @Test
    public void userCanBeCreatedTest() {
        createUser(email, password, name);
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", is(true));
    }

    @DisplayName("Same user can't be created test")
    @Description("User can't be created if it already exist")
    @Test
    public void sameUserCantBeCreatedTest() {

        createUser(email, password, name);
        response = userApi.createUser(userData);
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("message", is("User already exists"));
    }

    @DisplayName("User can't be created without email test")
    @Description("User can't be created without email")
    @Test
    public void userCantBeCreatedWithoutEmailTest() {

        createUser(null, password, name);
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("message", is("Email, password and name are required fields"));
    }

    @DisplayName("User can't be created without password test")
    @Description("User can't be created without password")
    @Test
    public void userCantBeCreatedWithoutPasswordTest() {

        createUser(email, null, name);
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("message", is("Email, password and name are required fields"));
    }
}
