package za.co.wethinkcode.robotworlds.clientprotocols;

import java.util.Map;

public class ResponseProtocol {
    private String result;
    private Map<String, Object> data;
    private Map<String, Object> state;

    public ResponseProtocol(String result, Map<String, Object> data) {
        this.result = result;
        this.data = data;
    }

    public ResponseProtocol(String result, Map<String, Object> data, Map<String, Object> state) {
        this.result = result;
        this.data = data;
        this.state = state;
    }

    public ResponseProtocol(Map<String, Object> state) {
        this.state = state;
    }

    public String getResult() {
        return this.result;
    }

    public Map<String, Object> getData() {
        return this.data;
    }

    public Map<String, Object> getState() {
        return this.state;
    }
}
