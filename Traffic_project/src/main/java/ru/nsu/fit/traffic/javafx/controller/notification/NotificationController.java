package ru.nsu.fit.traffic.javafx.controller.notification;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import ru.nsu.fit.traffic.controller.notification.NotificationType;

public class NotificationController {
    @FXML
    Text text;

    @FXML
    Text title;

    @FXML
    ImageView image;

    @FXML
    Pane pane;

    private Image warning;
    private Image information;
    private Text currText;
    private Text currTitle;

    public void showNotification(String title, String text, NotificationType type){
        switch (type){
            case ERROR:
                image.setImage(warning);
                break;
            case CONFIRM:
                image.setImage(information);
        }
        currTitle.setText(title);
        currText.setText(text);
        Platform.runLater(
                new Runnable() {
                    @Override
                    public void run() {
                        pane.setVisible(true);
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            pane.setVisible(false);
                        }
                        pane.setVisible(false);
                    }
                }
        );
    }

    @FXML
    public void initialize() {
        warning = new Image(getClass().getResource("./../../../view/Images/warning.png").toExternalForm());
        information = new Image(getClass().getResource("./../../../view/Images/information.png").toExternalForm());
        currText = new Text();
        currText.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        currText.setFill(Paint.valueOf("white"));
        currText.setX(text.getX());
        currText.setY(text.getY());
        currTitle = new Text();
        currTitle.setFont(Font.font("Arial", 13));
        currTitle.setFill(Paint.valueOf("white"));
        currTitle.setX(title.getX());
        currTitle.setY(title.getY());
    }
}
