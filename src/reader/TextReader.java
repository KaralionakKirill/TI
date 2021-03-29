package reader;

import java.io.*;
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

    public static byte[] readFile(File file){
        byte[] fileBytes = new byte[0];
        try (InputStream inputStream = new FileInputStream(file)) {
            fileBytes = inputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileBytes;
    }
}
