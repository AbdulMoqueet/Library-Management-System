package view_issued_list;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import modals.BookModal;
import modals.IssueModal;
import my_database.MyDatabase;
import my_utils.MyAlert;
import my_utils.MyUtils;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ViewIssuedList implements Initializable {

    @FXML
    private TableView<IssueModal> table_view;

    @FXML
    private TableColumn<IssueModal, String> issue_id;

    @FXML
    private TableColumn<IssueModal, String> student_name;

    @FXML
    private TableColumn<IssueModal, String> student_class;

    @FXML
    private TableColumn<IssueModal, String> student_roll;

    @FXML
    private TableColumn<IssueModal, String> book_id;

    @FXML
    private TableColumn<IssueModal, String> book_name;

    @FXML
    private TableColumn<IssueModal, String> author_name;

    @FXML
    private TableColumn<IssueModal, String> date_of_issue;

    @FXML
    private JFXTextField tf_book_id;

    @FXML
    private JFXTextField tf_book_name;

    @FXML
    private JFXTextField tf_author_name;

    @FXML
    private JFXTextField tf_category;

    @FXML
    private JFXTextField tf_issue_id;

    @FXML
    private JFXTextField tf_student_name;

    @FXML
    private JFXTextField tf_student_class;

    @FXML
    private JFXTextField tf_student_roll;

    @FXML
    private JFXDatePicker dp_date_of_issue;

    @FXML
    private JFXButton btn_save_changes;

    @FXML
    private JFXButton btn_return_book;

    @FXML
    private JFXButton btn_delete;

    @FXML
    private JFXTextField tf_search;

    @FXML
    private JFXComboBox<String> cb_by;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        MyDatabase.loadMainIssueRecordsObList();

        issue_id.setCellValueFactory(new PropertyValueFactory<>("issue_id"));
        student_name.setCellValueFactory(new PropertyValueFactory<>("student_name"));
        student_class.setCellValueFactory(new PropertyValueFactory<>("student_class"));
        student_roll.setCellValueFactory(new PropertyValueFactory<>("student_roll"));
        book_id.setCellValueFactory(new PropertyValueFactory<>("book_id"));
        book_name.setCellValueFactory(new PropertyValueFactory<>("book_name"));
        author_name.setCellValueFactory(new PropertyValueFactory<>("author_name"));
        date_of_issue.setCellValueFactory(new PropertyValueFactory<>("date_of_issue"));

        FilteredList<IssueModal> filteredData = new FilteredList<>(MyDatabase.mainIssueRecordsObList, p -> true);

        SortedList<IssueModal> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table_view.comparatorProperty());

        table_view.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table_view.setItems(sortedData);

        table_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            try {
                tf_book_id.setText(newValue.getBook_id());
                tf_book_name.setText(newValue.getBook_name());
                tf_author_name.setText(newValue.getAuthor_name());
                tf_category.setText(newValue.getCategory());
                tf_issue_id.setText(newValue.getIssue_id());
                tf_student_name.setText(newValue.getStudent_name());
                tf_student_class.setText(newValue.getStudent_class());
                tf_student_roll.setText(newValue.getStudent_roll());
                dp_date_of_issue.setValue(LocalDate.parse(newValue.getDate_of_issue(), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            }catch (Exception ignored) { }
        });

        cb_by.getItems().addAll("Student Name", "Student Class", "Student Roll", "Issue id", "Book id", "Book Name", "Author Name");
        cb_by.setValue(cb_by.getItems().get(0));
        cb_by.setOnAction(e->search(filteredData, tf_search.getText()));

        btn_save_changes.setOnAction(e -> save());
        btn_delete.setOnAction(e -> delete());
        btn_return_book.setOnAction(e -> returnBook());

        tf_search.textProperty().addListener((observable, oldValue, newValue) -> search(filteredData, newValue));

    }

    private void returnBook() {

        if (table_view.getSelectionModel().getSelectedIndex() == -1)
            return;

        IssueModal issueModal = table_view.getSelectionModel().getSelectedItem();

        MyDatabase.returnBook(issueModal);

        MyDatabase.mainIssueRecordsObList.remove(issueModal);
        MyAlert.successAlert("Book Returned successfully");


    }

    private void search(FilteredList<IssueModal> filteredData, String newValue) {
        filteredData.setPredicate(s -> {

            if ((newValue == null || newValue.trim().isEmpty())) {
                return true;
            }

            String targetText = newValue.toLowerCase();
            String targetColumn;

            switch (cb_by.getSelectionModel().getSelectedIndex()) {
                case 1:
                    targetColumn = s.getStudent_class().toLowerCase();
                    break;
                case 2:
                    targetColumn = s.getStudent_roll().toLowerCase();
                    break;
                case 3:
                    targetColumn = s.getIssue_id().toLowerCase();
                    break;
                case 4:
                    targetColumn = s.getBook_id().toLowerCase();
                    break;
                case 5:
                    targetColumn = s.getBook_name().toLowerCase();
                    break;
                case 6:
                    targetColumn = s.getAuthor_name().toLowerCase();
                    break;
                default:
                    targetColumn = s.getStudent_name().toLowerCase();
            }


            if (targetColumn.contains(targetText))
                return true;

            return false;

        });
    }

    private void delete() {

        if (table_view.getSelectionModel().getSelectedIndex() == -1)
            return;

        ObservableList<IssueModal> selectedItems = table_view.getSelectionModel().getSelectedItems();

        MyAlert.confirmAlert(selectedItems.size() == 1 ? "Are u sure you wanna delete " +
                table_view.getSelectionModel().getSelectedItem().getStudent_name() + " record?" :
                "Are you sure you want to delete all selected record", () -> {

            for (int i = 0; i < selectedItems.size(); i++)
                MyDatabase.delete("issue", "issue_id", selectedItems.get(i).getIssue_id());

            MyDatabase.mainIssueRecordsObList.removeAll(selectedItems);

        });

    }

    private void save() {

        if (table_view.getSelectionModel().getSelectedIndex() == -1)
            return;

        if (MyUtils.checkEmpty(tf_student_name, "Student Name") &&
                MyUtils.checkEmpty(tf_student_class, "Student Class") &&
                MyUtils.checkEmpty(tf_student_roll, "Student Roll")) {

            String date = dp_date_of_issue.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            IssueModal issueModal = table_view.getSelectionModel().getSelectedItem();
            issueModal.setStudent_name(tf_student_name.getText());
            issueModal.setStudent_class(tf_student_class.getText());
            issueModal.setStudent_roll(tf_student_roll.getText());
            issueModal.setDate_of_issue(date);

            if (MyDatabase.updateIssueData(issueModal)) {

                table_view.refresh();
                MyAlert.successAlert("Book Record Modified Successfully");
            } else {
                MyAlert.errorAlert("Failed To Modify Issue Record. (Internal Error occurred)");
            }

        }

    }

}
