import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class APIRateLimiter {
    public class RateLimiter {
        private static final int MAX_CALLS = 15;
        private static final int WINDOW_DURATION_IN_SECONDS = 60;
        private static final int PENALTY_DURATION_IN_SECONDS = 60;

        private final Queue<Long> callTimeStampInSeconds= new ConcurrentLinkedQueue<>();
        private long lastPenaltyTime = 0;

        public synchronized String callAPI(String input) throws RateLimitException {
            long currentTime = System.currentTimeMillis() / 1000;
            if(isUnderPenalty(currentTime)) {
                throw new RateLimitException("You can't call API, you are under penalty");
            }

            pruneOldTimestamps(currentTime);

            if(callTimeStampInSeconds.size() >= MAX_CALLS) {
                lastPenaltyTime = currentTime;
                throw new RateLimitException("Rate limit exceeds. You can only call max 15 times");
            }

            callTimeStampInSeconds.add(currentTime);
            return "API Called " + input;
        }

        private void pruneOldTimestamps(long currentTime) {
            callTimeStampInSeconds.removeIf(timestamp ->
                    currentTime - timestamp > WINDOW_DURATION_IN_SECONDS);
        }


        private boolean isUnderPenalty(long currentTime) {
            if (lastPenaltyTime == 0)
                return false;

            long penaltyEndTime = lastPenaltyTime + PENALTY_DURATION_IN_SECONDS;
            return currentTime < penaltyEndTime;
        }
    }

    public class RateLimitException extends RuntimeException {
        public RateLimitException(String message) {
            super(message);
        }
    }
}
