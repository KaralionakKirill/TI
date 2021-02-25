package reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;


public class TextReader {
    private TextReader() {
    }

    public static String readFromFile(File file) {
        String text = "";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            text = bufferedReader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }
}
