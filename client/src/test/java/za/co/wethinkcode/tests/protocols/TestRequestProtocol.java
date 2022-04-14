package za.co.wethinkcode.tests.protocols;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robotworlds.clientprotocols.RequestProtocol;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestRequestProtocol {

    @Test
    public void testSimpleRequest() {
        Gson gson = new Gson();
        RequestProtocol request = new RequestProtocol("Anton", "forward", new String[]{"23"});
        assertEquals("{\"name\":\"Anton\",\"command\":\"forward\",\"arguments\":[\"23\"]}", gson.toJson(request));
    }
}
