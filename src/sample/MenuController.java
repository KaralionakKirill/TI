package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import java.io.IOException;

public class MenuController {
    public Button FirstButton;
    public Button SecondButton;

    public void firstTask() throws IOException {
        Main.setRoot("encrypt");
    }

    public void secondTask(ActionEvent actionEvent) throws IOException {
        Main.setRoot("encrypt2");
    }
}
