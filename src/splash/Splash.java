package splash;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import my_database.MyDatabase;
import my_utils.MyAlert;
import my_utils.MyUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class Splash implements Initializable {

    public static Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        MyDatabase.connectToOracle();

        MyUtils.delay(1, ()->{

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login/login.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                Splash.stage.close();

            } catch (Exception ex) {
                MyAlert.errorAlert(ex);
            }

        });

    }
}
