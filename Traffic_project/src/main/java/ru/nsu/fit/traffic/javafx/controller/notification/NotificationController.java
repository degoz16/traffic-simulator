package ru.nsu.fit.traffic.javafx.controller.notification;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import ru.nsu.fit.traffic.controller.notification.NotificationType;

public class NotificationController {
    @FXML
    private ImageView image;
    @FXML
    private Pane pane;

    private Image warning;
    private Image information;
    private Text currText;
    private Text currTitle;

    public void showNotification(String title, String text, NotificationType type){
        switch (type) {
            case WARNING -> image.setImage(warning);
            case INFORMATION -> image.setImage(information);
        }
        currTitle.setText(title);
        currText.setText(text);
        pane.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(e -> pane.setVisible(false));
        pause.play();
    }

    @FXML
    public void initialize() {
        pane.setVisible(false);
        warning = new Image(getClass().getResource("./../../../view/Images/warning.png").toExternalForm());
        information = new Image(getClass().getResource("./../../../view/Images/information.png").toExternalForm());
        currText = new Text();
        currText.setFont(Font.font("Arial", 13));
        currText.setFill(Paint.valueOf("white"));
        currText.setX(60);
        currText.setY(50);
        currTitle = new Text();
        currTitle.setFont(Font.font("Arial",FontWeight.BOLD, 13));
        currTitle.setFill(Paint.valueOf("white"));
        currTitle.setX(6);
        currTitle.setY(19);
        pane.getChildren().add(currTitle);
        pane.getChildren().add(currText);
    }
}
