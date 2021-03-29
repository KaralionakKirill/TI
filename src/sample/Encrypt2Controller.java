package sample;

import javafx.scene.control.*;
import javafx.stage.FileChooser;
import reader.TextReader;
import service.Register;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Encrypt2Controller {
    public MenuItem OpenFileMenuItem;
    public Button BackToMenuButton;
    public TextField KeyInput;
    public TextArea Key;
    public TextArea Result;
    public Button EncryptButton;
    public Button DecryptButton;
    public TextArea Input;
    private Register register;
    private byte[] fileBytes;
    private byte[] keyBytes;

    public void openFileAction() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(Main.getAppStage());
        if (file != null) {
            fileBytes = TextReader.readFile(file);
            if (fileBytes.length == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error file");
                alert.setHeaderText(null);
                alert.setContentText("Selected file could not be read. Please, try again.");
                alert.showAndWait();
            } else {
                outputInputBytes();
            }
        }
    }

    public void backToMenu() throws IOException {
        Main.setRoot("menu");
    }

    public void onEncryptButtonClick() {
        String number = KeyInput.getText().replaceAll("[^[0-1]]", "");
        if (!number.isEmpty() && fileBytes != null) {
            initRegister(number);
            fillKeyBytes();
            outputKeyBytes();
            encrypt();
            outputResultBytes();
        } else {
            alert();
        }
    }

    public void initRegister(String number) {
        if (number.length() > 32) {
            number = number.substring(0, 31);
        }
        int num = Integer.parseInt(number, 2);
        register = new Register(num);
    }

    public void fillKeyBytes() {
        int amount = fileBytes.length;
        keyBytes = new byte[amount];
        for (int i = 0; i < amount; i++) {
            byte keyByte = 0;
            for (int j = 0; j < 7; j++) {
                keyByte |= register.move(28, 1);
                keyByte <<= 1;
            }
            keyByte += register.move(28, 1);
            keyBytes[i] = keyByte;
        }
    }

    public void encrypt() {
        for (int i = 0; i < fileBytes.length; i++) {
            fileBytes[i] = (byte) (fileBytes[i] ^ keyBytes[i]);
        }
    }

    public void decrypt() {
        for (int i = 0; i < fileBytes.length; i++) {
            fileBytes[i] = (byte) (fileBytes[i] ^ keyBytes[i]);
        }
    }

    public String byteToString(byte[] numbers) {
        StringBuilder str = new StringBuilder();
        int amount = Math.min(numbers.length, 1000);
        for (int i = 0; i < amount; i++) {
            str.append(String.format("%8s", Integer.toBinaryString(numbers[i] & 0xFF)).replace(' ', '0'));
            str.append(" | ");
            if (i != 0 && i % 10 == 0) {
                str.append("\n");
            }
        }
        return str.toString();
    }

    public void outputKeyBytes() {
        Key.setText(byteToString(keyBytes));
    }

    public void outputResultBytes() {
        Result.setText(byteToString(fileBytes));
    }

    public void outputInputBytes() {
        Input.setText(byteToString(fileBytes));
    }

    public void onDecryptButtonClick() {
        String number = KeyInput.getText().replaceAll("[^[0-1]]", "");
        if (!number.isEmpty() && fileBytes != null) {
            initRegister(number);
            fillKeyBytes();
            outputKeyBytes();
            decrypt();
            outputResultBytes();
        } else {
            alert();
        }
    }

    private void alert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input error");
        alert.setHeaderText(null);
        alert.setContentText("Check your input");
        alert.showAndWait();
    }

    public void saveToFile() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(Main.getAppStage());
        if (file != null) {
            try (OutputStream fileWriter = new FileOutputStream(file)) {
                fileWriter.write(fileBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error file");
            alert.setHeaderText(null);
            alert.setContentText("Choose file to save.");
            alert.showAndWait();
        }
    }
}
