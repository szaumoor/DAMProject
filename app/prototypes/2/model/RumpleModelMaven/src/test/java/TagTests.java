import com.szaumoor.rumple.model.entities.Tag;
import com.szaumoor.rumple.model.entities.User;
import com.szaumoor.rumple.utils.Bools;
import com.szaumoor.rumple.model.entities.types.UserEmail;
import com.szaumoor.rumple.model.entities.types.UserPass;
import com.szaumoor.rumple.model.entities.types.Username;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TagTests {

    private static Tag defTag;

    @BeforeAll
    public static void setUp(){
        defTag = Tag.of("Default").orElseThrow();
        var username = Username.of("szaumoor");
        var email = UserEmail.of("szaumoor@gmail.com");
        var pass = UserPass.of(new char[8]);

        var user = User.of(username.get(), email.get(), pass.get());
        defTag.setUser(user.get());
    }

    @Test
    public void canCreatePaymentMethodNormally() {
        final var pm = Tag.of("Food");
        assertTrue(pm.isPresent(), "Tag could not be created normally");
    }

    @Test
    public void cannotCreatePaymentMethodWithNullName() {
        final var pm = Tag.of(null);
        assertTrue(pm.isEmpty(), "Tag could be created abnormally with null string");
    }

    @Test
    public void cannotCreatePaymentMethodWithBlankName() {
        final var pm = Tag.of("            ");
        assertTrue(pm.isEmpty(), "Tag could be created abnormally with blank string");
    }

    @Test
    public void cannotSetNullName() {
        final boolean setting = defTag.setName(null);
        final boolean nulled = Objects.isNull(defTag.getName());
        assertTrue(Bools.allFalse(setting, nulled), "Setting to null succeeded unexpectedly");
    }

    @Test
    public void cannotSetBlankName() {
        final boolean setting = defTag.setName("    ");
        final boolean isBlank = defTag.getName().isBlank();
        assertTrue(Bools.allFalse(isBlank, setting), "Setting to blank string succeeded unexpectedly");
    }

    @Test
    public void cannotSetNullUser() {
        final boolean setting = defTag.setUser(null);
        final boolean nulled = Objects.isNull(defTag.getUser());
        assertTrue(Bools.allFalse(setting, nulled), "Setting to null succeeded unexpectedly");
    }

}
