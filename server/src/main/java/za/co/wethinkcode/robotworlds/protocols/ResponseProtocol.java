package za.co.wethinkcode.robotworlds.protocols;

import java.util.Map;

public class ResponseProtocol {
    private String result;
    private Map<String, Object> data;
    private Map<String, Object> state;
    private Map<String, Object> look;

    public ResponseProtocol(String result, Map<String, Object> data) {
        this.result = result;
        this.data = data;
    }

    public ResponseProtocol(String result, Map<String, Object> data, Map<String, Object> state) {
        this.result = result;
        this.data = data;
        this.state = state;
        this.look = look;
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

    public Map<String, Object> getLook() {
        return this.look;
    }

    public String getPosition(){return state.get("position").toString();}

}
