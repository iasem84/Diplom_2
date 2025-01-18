import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.UserData;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class ChangeUserDataTest extends BaseApiTest {

    @DisplayName("Authorised user can change data test")
    @Description("Check authorised user can change data")
    @Test
    public void authorisedUserCanChangeDataTest() {
        createUser(email, password, name);
        loginUser(email, password);

        userData = new UserData("s" + email, password + "!", name + "f");
        response = userApi.changeUserDataWithAuth(userData, token);

        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", is(true));
    }

    @DisplayName("Unauthorised user cant change data test")
    @Description("Check unauthorised user cant change data")
    @Test
    public void unauthorisedUserCantChangeDataTest() {
        createUser(email, password, name);

        userData = new UserData("s" + email, password + "!", name + "f");
        response = userApi.changeUserDataWithoutAuth(userData);

        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("message", is("You should be authorised"));
    }
}
