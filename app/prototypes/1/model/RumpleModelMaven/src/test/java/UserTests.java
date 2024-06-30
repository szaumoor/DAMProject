import com.szaumoor.rumple.model.entities.User;
import com.szaumoor.rumple.model.utils.types.UserEmail;
import com.szaumoor.rumple.model.utils.types.UserPass;
import com.szaumoor.rumple.model.utils.types.Username;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTests {

    private static UserPass pass;
    private static UserEmail email;
    private static Username username;

    @BeforeAll
    public static void setUp() {
        pass = UserPass.of(new char[]{'1', 'm', 'a', 'r', 't', 's', 'o', 'n', '1'}).orElseThrow();
        email = UserEmail.of("szaumoor@gmail.com").orElseThrow();
        username = Username.of("szaumoor").orElseThrow();
    }

    @Test
    public void canCreateUserSuccessfully() {
        var user = User.of(username, email, pass);
        assertTrue(user.isPresent(), "User could not me created");
    }
}
