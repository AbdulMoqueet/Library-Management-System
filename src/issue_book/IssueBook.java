package issue_book;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import modals.IssueModal;
import my_database.MyDatabase;
import my_utils.MyAlert;
import my_utils.MyUtils;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class IssueBook implements Initializable {

    @FXML
    private JFXComboBox<String> cb_book_name;

    @FXML
    private JFXTextField tf_issue_id;

    @FXML
    private JFXTextField tf_student_name;

    @FXML
    private JFXTextField tf_student_class;

    @FXML
    private JFXTextField tf_student_roll;

    @FXML
    private JFXTextField tf_date_of_issue;

    @FXML
    private JFXButton btn_issue_book;

    @FXML
    private JFXTextField tf_book_id;

    @FXML
    private JFXTextField tf_author_name;

    @FXML
    private JFXTextField tf_category;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        MyDatabase.loadMainBookRecordsObList();
        for (int i=0; i<MyDatabase.mainBookRecordsObList.size(); i++)
            cb_book_name.getItems().add(MyDatabase.mainBookRecordsObList.get(i).getBook_name());

        cb_book_name.setOnAction(e->{

            int index = cb_book_name.getSelectionModel().getSelectedIndex();

            tf_book_id.setText(MyDatabase.mainBookRecordsObList.get(index).getBook_id());
            tf_author_name.setText(MyDatabase.mainBookRecordsObList.get(index).getAuthor_name());
            tf_category.setText(MyDatabase.mainBookRecordsObList.get(index).getCategory());

        });

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        tf_date_of_issue.setText(date);

        btn_issue_book.setOnAction(e->issueBook());

    }

    private void issueBook() {

        if(tf_book_id.getText().isEmpty()){
            MyAlert.errorAlert("Select a book first");
            return;
        }

        if(MyUtils.checkEmpty(tf_issue_id, "Issue Id") &&
        MyUtils.checkEmpty(tf_student_name, "Student Name") &&
        MyUtils.checkEmpty(tf_student_class, "Student Class") &&
        MyUtils.checkEmpty(tf_student_roll, "Student Roll")){

            if(MyDatabase.isIssueIdUnique(tf_issue_id.getText())){

                int index = cb_book_name.getSelectionModel().getSelectedIndex();

                IssueModal issueModal = new IssueModal(
                        tf_book_id.getText(),
                        cb_book_name.getValue(),
                        tf_author_name.getText(),
                        tf_category.getText(),
                        MyDatabase.mainBookRecordsObList.get(index).getQuantity(),
                        MyDatabase.mainBookRecordsObList.get(index).getDate_of_adding(),
                        tf_issue_id.getText(),
                        tf_student_name.getText(),
                        tf_student_class.getText(),
                        tf_student_roll.getText(),
                        tf_date_of_issue.getText()
                );

                if(MyDatabase.saveIssueBookData(issueModal)){
                    MyAlert.successAlert("Book Issued Successfully");
                }


            }else{
                MyAlert.errorAlert("Issue Id already exists, please enter a unique Issue Id.");
            }

        }

    }


}
