
import com.szaumoor.rumple.model.entities.PaymentMethod;
import com.szaumoor.rumple.model.entities.User;
import com.szaumoor.rumple.model.utils.Bools;
import com.szaumoor.rumple.model.utils.types.UserEmail;
import com.szaumoor.rumple.model.utils.types.UserPass;
import com.szaumoor.rumple.model.utils.types.Username;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public final class PaymentMethodTests {

    private static PaymentMethod defMethod;

    @BeforeAll
    public static void setUp() {
        defMethod = PaymentMethod.of("Default").orElseThrow();
        var username = Username.of("szaumoor");
        var email = UserEmail.of("szaumoor@gmail.com");
        var pass = UserPass.of(new char[]{'1', 'm', 'a', 'r', 't', 's', 'o', 'n', '1'});

        var user = User.of(username.get(), email.get(), pass.get());
        defMethod.setUser(user.get());
    }

    @Test
    public void canCreatePaymentMethodNormally() {
        final var pm = PaymentMethod.of("Cash");
        assertTrue(pm.isPresent(), "Pm could not be created normally");
    }

    @Test
    public void cannotCreatePaymentMethodWithNullName() {
        final var pm = PaymentMethod.of(null);
        assertTrue(pm.isEmpty(), "Pm could be created abnormally with null string");
    }

    @Test
    public void cannotCreatePaymentMethodWithBlankName() {
        final var pm = PaymentMethod.of("            ");
        assertTrue(pm.isEmpty(), "Pm could be created abnormally with blank string");
    }

    @Test
    public void cannotSetNullName() {
        final boolean setting = defMethod.setName(null);
        final boolean nulled = Objects.isNull(defMethod.getName());
        assertTrue(Bools.allFalse(setting, nulled), "Setting to null succeeded unexpectedly");
    }

    @Test
    public void cannotSetBlankName() {
        final boolean setting = defMethod.setName("    ");
        final boolean isBlank = defMethod.getName().isBlank();
        assertTrue(Bools.allFalse(isBlank, setting), "Setting to blank string succeeded unexpectedly");
    }

    @Test
    public void cannotSetNullUser() {
        final boolean setting = defMethod.setUser(null);
        final boolean nulled = Objects.isNull(defMethod.getUser());
        assertTrue(Bools.allFalse(setting, nulled), "Setting to null succeeded unexpectedly");
    }
}
