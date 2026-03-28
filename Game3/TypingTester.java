package Game3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class TypingTester {
    List<Character> characters = new ArrayList<Character>();
    int currentCharIndex = 0;
    int numberWrong = 0;

    private enum TypeResult {
        CORRECT,
        INCORRECT,
        OTHER
    }

    public TypingTester(File file) throws FileNotFoundException, IOException {
        try (Reader fileReader = new BufferedReader(new FileReader(file));) {
            int c;
            while ((c = fileReader.read()) != -1) {
                characters.add((char) c);
            }
        }
    }

    public TypingTester(String paragraph) {
        for (char c : paragraph.toCharArray()) {
            characters.add(c);
        }
    }

    public TypeResult typeChar(char c) {
        if (c == '\b') {
            currentCharIndex--;
            return TypeResult.OTHER;
        } else {
            currentCharIndex++;
            if (c == characters.get(currentCharIndex)) {
                return TypeResult.CORRECT;
            } else {
                numberWrong++;
                return TypeResult.INCORRECT;
            }
        }
    }
}
