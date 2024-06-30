import com.szaumoor.rumple.model.utils.types.UserEmail;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserEmailTests {

    @Test
    public void canCreateEmailNormally() {
        var user = UserEmail.of("szaumoor@gmail.com");
        assertTrue(user.isPresent(), "Couldn't create email normally");
    }

    @Test
    public void cannotCreateNullEmail() {
        var user = UserEmail.of(null);
        assertTrue(user.isEmpty(), "Could create email with null");
    }

    @Test
    public void cannotCreateBlankEmail() {
        var user = UserEmail.of("              ");
        assertTrue(user.isEmpty(), "Could create email with blank string");
    }

    @Test
    public void emailEqualsAnotherObjectWithSameUsername() {
        var email1 = UserEmail.of("szaumoor@gmail.com");
        var email2 = UserEmail.of("szaumoor@gmail.com");
        assertEquals(email1, email2, "The two objects weren't equal");
    }
}
