package General;

import java.time.Duration;
import java.time.Instant;

public class Timer {
    private Instant start;

    public Timer() {
        setStartToNow();
    }

    public void setStartToNow() {
        start = Instant.now();
    }

    public String timeSinceStart() {
        Duration duration =  Duration.between(start, Instant.now());
        String minutesStr = padNumberAsString(duration.toMinutes(), 1);
        String secondsStr = padNumberAsString(duration.toSecondsPart(), 2);
        String millisStr = padNumberAsString(duration.toMillisPart(), 3);
        return String.format("%s:%s:%s", minutesStr, secondsStr, millisStr);
    }

    public static String padNumberAsString(long num, int minLen) {
        String numStr = Long.toString(num);
        while (numStr.length() < minLen) {
            numStr = "0" + numStr;
        }
        return numStr;
    }
}