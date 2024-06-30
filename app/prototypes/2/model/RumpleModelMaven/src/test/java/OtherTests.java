import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.szaumoor.rumple.utils.Money;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Currency;

public class OtherTests {

    @Test
    public void test() throws IOException {
        String connectionString = String.format("https://v6.exchangerate-api.com/v6/2edaed81acea162e6e4668ef/latest/USD");
        URL url = new URL(connectionString);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();
        JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonObj = root.getAsJsonObject();
        var r = jsonObj.get("conversion_rates").getAsJsonObject().get("EUR");
        System.out.println(jsonObj);
    }
}
