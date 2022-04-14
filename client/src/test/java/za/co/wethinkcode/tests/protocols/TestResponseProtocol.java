package za.co.wethinkcode.tests.protocols;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robotworlds.clientprotocols.ResponseProtocol;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestResponseProtocol {

    @Test
    public void testSimpleResponseProtocol() {
        Gson gson = new Gson();
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Done");
        ResponseProtocol response = new ResponseProtocol("OK", data);
        assertEquals( "{\"result\":\"OK\",\"data\":{\"message\":\"Done\"}}", gson.toJson(response));
    }
}
