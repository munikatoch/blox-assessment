import java.util.Map;

public class Main {

    public static void main(String[] args) {
        testJsonParser(); // Problem 3
        try {
            testAPIRateLimiter(1); //problem 4
            // 4.1 Done
            // 4.2 If we are supposed to call the API 20 times then we have to increase the limit from 15-20 or we have to wait for cooldown
            // 4.3 Use concurrent Queue to maintain the Time stamps and then check on the number of requests as per the last request
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void testAPIRateLimiter(int retry) throws InterruptedException {
        if(retry >= 0) {
            APIRateLimiter.RateLimiter basicLimiter = new APIRateLimiter().new RateLimiter();

            try {
                for (int i = 0; i < 16; i++) {
                    System.out.println(basicLimiter.callAPI("Request " + i));
                }
            } catch (APIRateLimiter.RateLimitException e) {
                System.out.println("Rate limit caught: " + e.getMessage());
                Thread.sleep(60 * 1000 - 1);
                testAPIRateLimiter(retry - 1);
            }
        }
    }

    private static void testJsonParser() {
        String json = "{\"name\": \"Test\", \"balance\": 100.1021, \"age\": \"26\", \"tags\": [\"Hello\", \"Word\"]}";
        try {
            Object obj = new JsonParser().parseJson(json);
            if(obj instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) obj;
                for(Map.Entry<String, Object> entry : map.entrySet()) {
                    System.out.println(entry.getKey() + " " + entry.getValue().toString());
                }
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
