package my_utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import splash.Splash;

public class MyUtils {

    public static void delay(double sec, CallBack callBack){

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(sec)));
        timeline.play();
        timeline.setOnFinished(e->callBack.execute());

    }

    public static boolean checkEmpty(TextField tf, String msg){

        if(tf.getText().trim().isEmpty()){
            MyAlert.errorAlert(msg+" can't be empty");
            tf.requestFocus();
            return false;
        }
        return true;

    }

    public static Stage openStage(Class getClass, String fxml){

        Stage stage = new Stage();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass.getResource(fxml));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            MyAlert.errorAlert(ex);
        }
        return stage;
    }

    public static void openStageAsModal(Class getClass, String fxml){

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass.getResource(fxml));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (Exception ex) {
            MyAlert.errorAlert(ex);
        }
    }

    public static void setNumberOnly(TextField textField){

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) textField.setText(newValue.replaceAll("[^\\d]", ""));
        });

    }

}
