package Testing;

import General.Timer;

public class Main {
    public static void main(String[] args) {
        Timer timer = new Timer();
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
