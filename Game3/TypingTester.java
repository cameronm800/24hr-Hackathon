package Game3;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.List;

public class TypingTester {
    private List<Character> characters = new ArrayList<Character>();
    private int currentCharIndex = 0;
    private int score = 0;
    private String paragraph;

    public enum TypeResult {
        CORRECT,
        INCORRECT,
        COMPLETE
    }

    public int getScore() {
        return score;
    }

    public String getParagraph() {
        return paragraph;
    }

    public String getTypedString() {
        StringBuilder typed = new StringBuilder();
        for (int i = 0; i < currentCharIndex; i++) {
            typed.append(characters.get(i));
        }
        return typed.toString();
    }

    public String getUntypedString() {
        StringBuilder unTyped = new StringBuilder();
        for (int i = currentCharIndex+1; i < characters.size(); i++) {
            unTyped.append(characters.get(i));
        }
        return unTyped.toString();
    }

    public String getCharAsString() {
        return characters.get(currentCharIndex).toString();
    }

    public TypingTester(int numWords) throws IOException {
        List<String> words = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader("words.txt"));) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                words.add(line);
            }
        }
        Random rand = new Random();
        String paragraph = words.get(rand.nextInt(words.size() - 1));
        for (int i = 1; i < numWords; i++) {
            paragraph += " " + words.get(rand.nextInt(words.size() - 1));
        }

        setParagraph(paragraph);
    }

    public TypingTester(String paragraph) {
        setParagraph(paragraph);
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
        for (char c : paragraph.toCharArray()) {
            characters.add(c);
        }
    }

    public TypeResult typeChar(char c) {
            if (currentCharIndex >= characters.size()) {
                return TypeResult.COMPLETE;
            }
            if (c == characters.get(currentCharIndex)) {
                currentCharIndex++;
                score++;
                if (currentCharIndex == characters.size()) {
                    return TypeResult.COMPLETE;
                }
                return TypeResult.CORRECT;
            } else {
                score--;
                return TypeResult.INCORRECT;
            }
    }
}

