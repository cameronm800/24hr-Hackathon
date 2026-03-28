package GameUI;
import GameUI.Display;
import java.io.*;
import java.util.*;
import java.nio.file.Paths;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        try {
            // 1. Get the absolute path to your 'Data' folder
            String projectRoot = System.getProperty("user.dir");
            String dataFolderPath = Paths.get(projectRoot, "Data").toString();

            // 2. Build the command: python -m package.module
            // Note: We use the dot notation for packages
            ProcessBuilder pb = new ProcessBuilder("python3", "-m", "Data.InterpretData");

            // 3. Set the PYTHONPATH so Python can see the 'Data' folder
            Map<String, String> env = pb.environment();
            env.put("PYTHONPATH", dataFolderPath);

            // 4. Standard process execution
            pb.redirectErrorStream(true);
            Process p = pb.start();

            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = r.readLine()) != null) {
                System.out.println("Python: " + line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Display display = new Display();
        display.showWindow();
    }
}
