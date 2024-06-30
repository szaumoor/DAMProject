import com.szaumoor.rumple.model.entities.User;
import com.szaumoor.rumple.model.entities.types.UserEmail;
import com.szaumoor.rumple.model.entities.types.UserPass;
import com.szaumoor.rumple.model.entities.types.Username;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTests {

    private static UserPass pass;
    private static UserEmail email;
    private static Username username;

    @BeforeAll
    public static void setUp() {
        pass = UserPass.of(new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd', '1'}).orElseThrow();
        email = UserEmail.of("szaumoor@gmail.com").orElseThrow();
        username = Username.of("szaumoor").orElseThrow();
    }

    @Test
    public void canCreateUserSuccessfully() {
        var user = User.of(username, email, pass);
        assertTrue(user.isPresent(), "User could not me created");
    }
}
