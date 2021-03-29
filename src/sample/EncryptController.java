package sample;

import javafx.scene.control.*;
import javafx.stage.FileChooser;
import reader.TextReader;
import service.EncryptionService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class EncryptController {


    public Button BackToMenuButton;
    public MenuItem OpenFileMenuItem;
    public javafx.scene.control.TextArea TextArea;
    public Button EncryptButton;
    public Button DecryptButton;
    public ToggleGroup EncryptGroup;
    public TextField KeyInput;


    public void backToMenu() throws IOException {
        Main.setRoot("menu");
    }

    public void openFileAction() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(Main.getAppStage());
        String text = "";
        if (file != null) {
            if (file.getAbsolutePath().matches(".+.txt")) {
                text = TextReader.readFromFile(file);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error file");
                alert.setHeaderText(null);
                alert.setContentText("Selected file is not a .txt . Please, try again.");
                alert.showAndWait();
            }
        }
        TextArea.setText(text);
    }

    public void encryptText() {
        String text = TextArea.getText();
        String key = KeyInput.getText();
        if (text.isEmpty() && key.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty text area");
            alert.setHeaderText(null);
            alert.setContentText("Text area or key is empty . Please, try again.");
            alert.showAndWait();
        } else {
            String toggleId = ((RadioMenuItem) EncryptGroup.getSelectedToggle()).getId();
            try {
                String encryptText = switch (toggleId) {
                    case "vigener" -> EncryptionService.encryptText(text, key, 1);
                    case "pleupher" -> EncryptionService.encryptText(text, key, 2);
                    case "rail" -> EncryptionService.encryptText(text, key, 3);
                    default -> "";
                };
                TextArea.setText(encryptText);
            } catch (Exception e) {
                alert();
            }
        }
    }

    private void alert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input error");
        alert.setHeaderText(null);
        alert.setContentText("Check your input");
        alert.showAndWait();
    }

    public void decryptText() {
        String text = TextArea.getText();
        String key = KeyInput.getText();
        if (text.isEmpty() && key.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty text area");
            alert.setHeaderText(null);
            alert.setContentText("Text area or key is empty . Please, try again.");
            alert.showAndWait();
        } else {
            String toggleId = ((RadioMenuItem) EncryptGroup.getSelectedToggle()).getId();
            try {
                String encryptText = switch (toggleId) {
                    case "vigener" -> EncryptionService.decryptText(text, key, 1);
                    case "pleupher" -> EncryptionService.decryptText(text, key, 2);
                    case "rail" -> EncryptionService.decryptText(text, key, 3);
                    default -> "";
                };
                TextArea.setText(encryptText);
            }catch (Exception e) {
                alert();
            }
        }
    }

    public void saveFileAction() {
        String fileName = UUID.randomUUID().toString();
        fileName = fileName +".txt";
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(TextArea.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}