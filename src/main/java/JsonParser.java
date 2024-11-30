import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonParser {
    public Object parseJson(String json) throws Exception {
        if(json.isBlank())
            return null;
        if(!json.startsWith("{"))
            throw new Exception("Invalid Json");
        return parseJsonObject(new JSONObject(json));
    }

    private Object parseJsonObject(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>();
        for(String key : jsonObject.keySet()) {
            map.put(key, parseValue(jsonObject.get(key)));
        }
        return map;
    }

    private Object parseValue(Object value) {
        if(value instanceof JSONObject) {
            return parseJsonObject((JSONObject) value);
        }
        else if(value instanceof JSONArray) {
            return parseJsonArray((JSONArray) value);
        }
        else if(value instanceof Number) {
            return parseNumber(value);
        }
        return value;
    }

    private Object parseJsonArray(JSONArray jsonArray) {
        List<Object> list = new ArrayList<>();
        for(Object object : jsonArray) {
            list.add(parseValue(object));
        }
        return list;
    }

    private Number parseNumber(Object number) {
        if(number instanceof Integer || number instanceof  Long)
            return new BigInteger(number.toString());
        else if(number instanceof Float || number instanceof Double)
            return new BigDecimal(number.toString());
        return (Number) number;
    }
}
