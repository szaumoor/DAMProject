import com.szaumoor.rumple.model.entities.types.UserPass;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserPassTests {

    @Test
    public void canCreateAPassSuccessfully() {
        char[] password = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd', '1'};
        var pass = UserPass.of(password);
        assertTrue(pass.isPresent(), "Pass couldn't be created with right values: non-null password of length " + password.length);
    }

    @Test
    public void hashedPassVerifiesCorrectlyAgainstSamePlainPassword() {
        var pass = UserPass.of(new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd', '1'});
        boolean valid = pass.isPresent();
        if (valid) valid = pass.get().verify("password1");
        assertTrue(valid, "Plain password failed to be matched against hash.");
    }

    @Test
    public void cannotCreatePassWithNullArray() {
        var pass = UserPass.of(null);
        assertTrue(pass.isEmpty(), "Pass could be created unexpectedly");
    }

    @Test
    public void cannotCreatePassWithBelowMinLength() {
        var pass = UserPass.of(new char[]{'1', 'm', 'a'});
        assertTrue(pass.isEmpty(), "Pass could be created unexpectedly");
    }

    @Test
    public void cannotCreatePassWithAboveMaxLength() {
        var pass = UserPass.of(new char[UserPass.MAX_SIZE + 10]);
        assertTrue(pass.isEmpty(), "Pass could be created unexpectedly");
    }
}
