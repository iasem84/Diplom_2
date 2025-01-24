import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.UserData;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class ChangeUserDataTest extends BaseApiTest {

    @DisplayName("Authorised user can change name test")
    @Description("Check authorised user can change name")
    @Test
    public void authorisedUserCanChangeNameTest() {
        createUser(email, password, name);
        loginUser(email, password);

        String newName = "A" + name;
        userData = UserData.builder()
                .name(newName)
                .build();
        response = userApi.changeUserDataWithAuth(userData, token);

        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", is(true))
                .body("user.email", is(email))
                .body("user.name", is(newName));
    }

    @DisplayName("Authorised user can change email test")
    @Description("Check authorised user can change email")
    @Test
    public void authorisedUserCanChangeEmailTest() {
        createUser(email, password, name);
        loginUser(email, password);

        String newEmail = "s" + email;
        userData = UserData.builder()
                .email(newEmail)
                .build();
        response = userApi.changeUserDataWithAuth(userData, token);

        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", is(true))
                .body("user.email", is(newEmail))
                .body("user.name", is(name));
    }

    @DisplayName("Unauthorised user can't change name test")
    @Description("Check unauthorised user can't change name")
    @Test
    public void unauthorisedUserCantChangeNameTest() {
        createUser(email, password, name);

        String newName = "A" + name;
        userData = UserData.builder()
                .name(newName)
                .build();
        response = userApi.changeUserDataWithoutAuth(userData);

        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("message", is("You should be authorised"));
    }

    @DisplayName("Unauthorised user can't change email test")
    @Description("Check unauthorised user can't change email")
    @Test
    public void unauthorisedUserCantChangeEmailTest() {
        createUser(email, password, name);

        userData = UserData.builder()
                .password("s" + email)
                .build();
        response = userApi.changeUserDataWithoutAuth(userData);

        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("message", is("You should be authorised"));
    }
}
