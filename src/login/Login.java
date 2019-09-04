package login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;
import my_database.MyDatabase;
import my_utils.MyAlert;
import my_utils.MyUtils;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Login implements Initializable {

    @FXML
    private JFXTextField tf_username;

    @FXML
    private JFXPasswordField tf_password;

    @FXML
    private JFXButton btn_login;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btn_login.setOnAction(e->login(e));
        tf_username.setOnAction(e->tf_password.requestFocus());
        tf_password.setOnAction(e->login(e));

    }

    private void login(Event event) {

        Stage stage = MyUtils.openStage(getClass(), "/dashboard/dashboard.fxml");
        stage.setMaximized(true);

        Node node = (Node) event.getSource();
        Stage currentStage = (Stage) node.getScene().getWindow();
        currentStage.close();

//        if(MyUtils.checkEmpty(tf_username, "Username") && MyUtils.checkEmpty(tf_password, "Password")) {
//
//            String user = tf_username.getText().trim();
//            String pass = tf_password.getText().trim();
//
//            String dbUser = "", dbPass = "";
//
//            ResultSet rs = MyDatabase.getLoginDetails();
//
//            try {
//                if (rs.next()) {
//                    dbUser = rs.getString(1);
//                    dbPass = rs.getString(2);
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//            if(user.equalsIgnoreCase(dbUser)){
//
//                if(pass.equals(dbPass)){
//
//                    Stage stage = MyUtils.openStage(getClass(), "/dashboard/dashboard.fxml");
//                    stage.setMaximized(true);
//
//                    Node node = (Node) event.getSource();
//                    Stage currentStage = (Stage) node.getScene().getWindow();
//                    currentStage.close();
//
//                }else{
//                    MyAlert.errorAlert("Invalid Password");
//                }
//
//            }else{
//                MyAlert.errorAlert("Invalid Username!");
//            }
//
//        }

    }

}
