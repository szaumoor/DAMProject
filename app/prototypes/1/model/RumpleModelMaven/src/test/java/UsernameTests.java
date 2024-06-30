import com.szaumoor.rumple.model.utils.types.Username;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsernameTests {

    @Test
    public void canCreateUsernameNormally() {
        var user = Username.of("szaumoor");
        assertTrue(user.isPresent(), "Couldn't create username normally");
    }

    @Test
    public void cannotCreateNullUsername() {
        var user = Username.of(null);
        assertTrue(user.isEmpty(), "Could create username with null");
    }

    @Test
    public void cannotCreateUsernameOverLimit() {
        var user = Username.of("szaumooraaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        assertTrue(user.isEmpty(), "Could create username with over 30 characters");
    }

    @Test
    public void cannotCreateUsernameUnderLimit() {
        var user = Username.of("sz");
        assertTrue(user.isEmpty(), "Could create username with less than 3 chars");
    }

    @Test
    public void usernameEqualsAnotherObjectWithSameUsername() {
        var user1 = Username.of("szaumoor");
        var user2 = Username.of("szaumoor");
        assertEquals(user1, user2, "The two objects weren't equal");
    }
}
