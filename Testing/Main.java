package Testing;
/// /
import General.UIStopWatch;

public class Main {
    public static void main(String[] args) {
        UIStopWatch timer = new UIStopWatch();
        while (true) {
            System.out.println(timer.timeSinceStart());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
