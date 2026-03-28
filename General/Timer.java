import java.time.Duration;
import java.time.Instant;

public class Timer {
    private Instant start;

    public void setStartToNow() {
        start = Instant.now();
    }

    public String timeSinceStart() {
        Duration duration =  Duration.between(start, Instant.now());
        return String.format("%d:%d:%d", duration.toMinutes(), duration.toSecondsPart(), duration.toMillisPart());
    }
}