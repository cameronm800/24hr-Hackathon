package General;

import java.time.Duration;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class UIStopWatch {
    private Instant start;
    private Timer timer;

    public UIStopWatch() {
        setStartToNow();
    }

    public UIStopWatch(Consumer<String> onTick) {
        this();
        this.timer = new Timer(true); // 'true' makes it a daemon thread

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // This calls your lambda e -> { ... }
                onTick.accept(timeSinceStart());
            }
        }, 0, 1000);
    }

    public void stop() {
        if (timer != null) timer.cancel();
    }

    public void setStartToNow() {
        start = Instant.now();
    }

    public long getSeconds() {
        return Duration.between(start, Instant.now()).toSeconds();
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