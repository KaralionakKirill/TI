package service;

import java.util.Locale;

public class EncryptionService {
    public static final String alphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    public static String rusExcept = "[^А-Яа-яЁё]";
    public static String engExcept = "[^A-Za-z]";

    public static String encryptText(String plaintext, String key, int encryptionMethod) throws Exception {
        return switch (encryptionMethod) {
            case 1 -> vigenereMethod(plaintext, key);
            case 2 -> pleupherMethod(plaintext);
            case 3 -> railFenceMethod(plaintext, key);
            default -> null;
        };
    }

    private static boolean isRus(char c) {
        return c >= 'а' && c <= 'я' || c >= 'А' && c <= 'Я';
    }

    private static boolean isEng(char c) {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }


    public static String decryptText(String plaintext, String key, int encryptionMethod) throws Exception {
        return switch (encryptionMethod) {
            case 1 -> deVigenereMethod(plaintext, key);
            case 2 -> dePleupherMethod(plaintext);
            case 3 -> deRailFenceMethod(plaintext, key);
            default -> null;
        };
    }


    private static String railFenceMethod(String plaintext, String inputKey) throws Exception {
        plaintext = plaintext.replaceAll(engExcept, "").toUpperCase(Locale.ROOT);
        int key;
        try {
            key = Integer.parseInt(inputKey);
            if (key > plaintext.length() || key < 2) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new Exception(e);
        }

        StringBuilder answer = new StringBuilder();
        int index = 0;
        while (index < plaintext.length()) {
            char textChar = plaintext.charAt(index);
            if (isEng(textChar)) {
                answer.append(textChar);
            }
            index = index + key * 2 - 2;
        }
        index = 1;
        int level = 1;
        while (level < key - 1) {
            char textChar = plaintext.charAt(index);
            if (isEng(textChar)) {
                answer.append(textChar);
            }
            index = index + (key - level) * 2 - 2;
            if (index >= plaintext.length()) {
                index = ++level;
            } else {
                textChar = plaintext.charAt(index);
                if (isEng(textChar)) {
                    answer.append(textChar);
                }
                index = index + level * 2;
                if (index >= plaintext.length()) {
                    index = ++level;
                }
            }
        }

        while (index < plaintext.length()) {
            char textChar = plaintext.charAt(index);
            if (isEng(textChar)) {
                answer.append(textChar);
            }
            index = index + key * 2 - 2;
        }

        if (answer.length() == 0) return plaintext;
        return answer.toString();
    }

    private static String deRailFenceMethod(String cypherText, String inputKey) throws Exception {
        cypherText = cypherText.toUpperCase(Locale.ROOT).trim();
        int key;
        try {
            key = Integer.parseInt(inputKey);
            if (key > cypherText.length() || key < 2) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new Exception(e);
        }

        char[] answer = new char[cypherText.length()];

        int index = 0;
        int i = 0;
        while (index < cypherText.length()) {
            answer[index] = cypherText.charAt(i++);
            index = index + key * 2 - 2;
        }
        int level = 1;
        index = 1;
        while (level < key - 1) {
            char textChar = cypherText.charAt(i++);
            if (isEng(textChar)) {
                answer[index] = textChar;
            }
            index = index + (key - level) * 2 - 2;
            if (index >= cypherText.length()) {
                index = ++level;
            } else {
                textChar = cypherText.charAt(i++);
                if (isEng(textChar)) {
                    answer[index] = textChar;
                }
                index = index + level * 2;
                if (index >= cypherText.length()) {
                    index = ++level;
                }
            }
        }

        while (index < cypherText.length()) {
            answer[index] = cypherText.charAt(i++);
            index = index + key * 2 - 2;
        }

        StringBuilder str = new StringBuilder();
        for (char c : answer) {
            str.append(c);
        }

        if (str.length() == 0) return cypherText;
        return str.toString();
    }

    private static String pleupherMethod(String plaintext) {
        plaintext = plaintext.toUpperCase(Locale.ROOT).replaceAll(engExcept, "");
        String[][] table = {
                {"C", "R", "Y", "P", "T"},
                {"O", "G", "A", "H", "B"},
                {"D", "E", "F", "IJ", "K"},
                {"L", "M", "N", "Q", "S"},
                {"U", "V", "W", "X", "Z"}
        };
        int colCount = table[0].length;
        int rowCount = table.length;
        StringBuilder answer = new StringBuilder();

        for (int i = 0; i < plaintext.length(); i += 2) {
            char first;
            char second;

            first = plaintext.charAt(i);
            if (i + 1 == plaintext.length()) {
                second = 'X';
            } else {
                second = plaintext.charAt(i + 1);
            }

            if (first == second) {
                second = 'X';
                i--;
            }
            Coords cFirst = findCoords(table, first);
            Coords cSecond = findCoords(table, second);

            if (cFirst.row == cSecond.row) {
                answer.append(table[cFirst.row][(cFirst.col + 1) % colCount].charAt(0))
                        .append(table[cSecond.row][(cSecond.col + 1) % colCount].charAt(0));
                continue;
            }
            if (cFirst.col == cSecond.col) {
                answer.append(table[(cFirst.row + 1) % rowCount][cFirst.col].charAt(0))
                        .append(table[(cSecond.row + 1) % rowCount][cSecond.col].charAt(0));
                continue;
            }

            answer.append(table[cFirst.row][cSecond.col].charAt(0)).append(table[cSecond.row][cFirst.col].charAt(0));
        }

        if (answer.length() == 0) return plaintext;
        return answer.toString();
    }

    private static String dePleupherMethod(String plaintext) {
        String[][] table = {
                {"C", "R", "Y", "P", "T"},
                {"O", "G", "A", "H", "B"},
                {"D", "E", "F", "IJ", "K"},
                {"L", "M", "N", "Q", "S"},
                {"U", "V", "W", "X", "Z"}
        };
        int colCount = table[0].length;
        int rowCount = table.length;
        StringBuilder answer = new StringBuilder();

        for (int i = 0; i < plaintext.length(); i += 2) {
            char first;
            char second;

            first = plaintext.charAt(i);
            if (i + 1 == plaintext.length()) {
                second = 'X';
            } else {
                second = plaintext.charAt(i + 1);
            }

            if (first == second) {
                second = 'X';
                i--;
            }
            Coords cFirst = findCoords(table, first);
            Coords cSecond = findCoords(table, second);

            if (cFirst.row == cSecond.row) {
                answer.append(table[cFirst.row][(cFirst.col - 1 + colCount) % colCount].charAt(0))
                        .append(table[cSecond.row][(cSecond.col - 1 + colCount) % colCount].charAt(0));
                continue;
            }
            if (cFirst.col == cSecond.col) {
                answer.append(table[(cFirst.row - 1 + rowCount) % rowCount][cFirst.col].charAt(0))
                        .append(table[(cSecond.row - 1 + rowCount) % rowCount][cSecond.col].charAt(0));
                continue;
            }

            answer.append(table[cFirst.row][cSecond.col].charAt(0)).append(table[cSecond.row][cFirst.col].charAt(0));
        }

        if (answer.length() == 0) return plaintext;
        return answer.toString();
    }

    public static String vigenereMethod(String plaintext, String inputKey) throws Exception {
        StringBuilder key = new StringBuilder(inputKey.replaceAll(rusExcept, "").toUpperCase(Locale.ROOT));
        plaintext = plaintext.toUpperCase(Locale.ROOT).replaceAll(rusExcept, "");
        StringBuilder answer = new StringBuilder();
        if (plaintext.isEmpty() || key.isEmpty()) {
            throw new Exception();
        }

        int keyIndex = 0;
        for (int i = 0; i < plaintext.length(); i++) {
            int letterIndex = alphabet.indexOf(plaintext.charAt(i));
            int letterInKeyIndex = alphabet.indexOf(key.charAt(keyIndex));
            int resultLetterIndex = (letterInKeyIndex + letterIndex) % alphabet.length();
            char resultLetter = alphabet.charAt(resultLetterIndex);
            key.setCharAt(keyIndex, alphabet.charAt((letterInKeyIndex + 1) % alphabet.length()));
            keyIndex = (keyIndex + 1) % key.length();
            answer.append(resultLetter);
        }

        if (answer.length() == 0) return plaintext;
        return answer.toString();
    }

    private static String deVigenereMethod(String plaintext, String inputKey) throws Exception {
        StringBuilder key = new StringBuilder(inputKey.replaceAll(rusExcept, "").toUpperCase(Locale.ROOT));
        plaintext = plaintext.toUpperCase(Locale.ROOT);
        StringBuilder answer = new StringBuilder();

        if (plaintext.isEmpty() || key.isEmpty()) {
            throw new Exception();
        }

        int keyIndex = 0;
        for (int i = 0; i < plaintext.length(); i++) {
            int letterIndex = alphabet.indexOf(plaintext.charAt(i));
            int letterInKeyIndex = alphabet.indexOf(key.charAt(keyIndex));
            int resultLetterIndex = ((letterIndex - letterInKeyIndex + alphabet.length()) % alphabet.length());
            char resultLetter = alphabet.charAt(resultLetterIndex);
            key.setCharAt(keyIndex, alphabet.charAt((letterInKeyIndex + 1) % alphabet.length()));
            keyIndex = (keyIndex + 1) % key.length();
            answer.append(resultLetter);
        }

        if (answer.length() == 0) return plaintext;
        return answer.toString();
    }

    private static Coords findCoords(String[][] table, char first) {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                if (table[i][j].contains(first + "")) {
                    return new Coords(i, j);
                }
            }
        }
        return null;
    }

    static class Coords {
        int row, col;

        Coords(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}
