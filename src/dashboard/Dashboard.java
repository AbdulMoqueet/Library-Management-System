package dashboard;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import my_utils.MyUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {

    @FXML
    private JFXButton btn_add_book;

    @FXML
    private JFXButton btn_view_book;

    @FXML
    private JFXButton btn_issue_book;

    @FXML
    private JFXButton btn_view_issued_list;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btn_add_book.setOnAction(e-> MyUtils.openStageAsModal(getClass(), "/add_book/add_book.fxml"));
        btn_view_book.setOnAction(e-> MyUtils.openStageAsModal(getClass(), "/view_books/view_books.fxml"));
        btn_issue_book.setOnAction(e-> MyUtils.openStageAsModal(getClass(), "/issue_book/issue_book.fxml"));
        btn_view_issued_list.setOnAction(e-> MyUtils.openStageAsModal(getClass(), "/view_issued_list/view_issued_list.fxml"));

    }

}
