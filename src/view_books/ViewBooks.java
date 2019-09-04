package view_books;

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
import javafx.util.StringConverter;
import modals.BookModal;
import my_database.MyDatabase;
import my_utils.MyAlert;
import my_utils.MyUtils;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ViewBooks implements Initializable {

    @FXML
    private TableView<BookModal> table_view;

    @FXML
    private TableColumn<BookModal, String> book_id;

    @FXML
    private TableColumn<BookModal, String> book_name;

    @FXML
    private TableColumn<BookModal, String> author_name;

    @FXML
    private TableColumn<BookModal, String> category;

    @FXML
    private TableColumn<BookModal, Integer> quantity;

    @FXML
    private TableColumn<BookModal, String> date_of_adding;

    @FXML
    private JFXTextField tf_book_id;

    @FXML
    private JFXTextField tf_author_name;

    @FXML
    private JFXTextField tf_quantity;

    @FXML
    private JFXTextField tf_book_name;

    @FXML
    private JFXTextField tf_category;

    @FXML
    private JFXButton btn_save_changes;

    @FXML
    private JFXButton btn_delete;

    @FXML
    private JFXDatePicker dp_date_of_adding;

    @FXML
    private JFXTextField tf_search;

    @FXML
    private JFXComboBox<String> cb_by;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        book_id.setCellValueFactory(new PropertyValueFactory<>("book_id"));
        book_name.setCellValueFactory(new PropertyValueFactory<>("book_name"));
        author_name.setCellValueFactory(new PropertyValueFactory<>("author_name"));
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        date_of_adding.setCellValueFactory(new PropertyValueFactory<>("date_of_adding"));

        MyDatabase.loadMainBookRecordsObList();
        FilteredList<BookModal> filteredData = new FilteredList<>(MyDatabase.mainBookRecordsObList, p -> true);

        SortedList<BookModal> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table_view.comparatorProperty());

        table_view.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table_view.setItems(sortedData);

        cb_by.getItems().addAll("Book Name", "Book Id", "Author Name", "Category");
        cb_by.setValue("Book Name");
        cb_by.setOnAction(e->search(filteredData, tf_search.getText()));

        MyUtils.setNumberOnly(tf_quantity);
        dateFormatter(dp_date_of_adding);

        table_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            try {

                tf_book_id.setText(newValue.getBook_id());
                tf_book_name.setText(newValue.getBook_name());
                tf_author_name.setText(newValue.getAuthor_name());
                tf_category.setText(newValue.getCategory());
                tf_quantity.setText(String.valueOf(newValue.getQuantity()));
                dp_date_of_adding.setValue(LocalDate.parse(newValue.getDate_of_adding(), DateTimeFormatter.ofPattern("dd-MM-yyyy")));

            }catch (Exception ignored){ }
        });

        btn_save_changes.setOnAction(e->save());
        btn_delete.setOnAction(e->delete());
        tf_search.textProperty().addListener((observable, oldValue, newValue) -> search(filteredData, newValue));

    }

    private void search(FilteredList<BookModal> filteredData, String newValue) {
        filteredData.setPredicate(s -> {

            if ((newValue == null || newValue.trim().isEmpty())) {
                return true;
            }

            String targetText = newValue.toLowerCase();
            String targetColumn;

            switch (cb_by.getSelectionModel().getSelectedIndex()) {
                case 1:
                    targetColumn = s.getBook_id().toLowerCase();
                    break;
                case 2:
                    targetColumn = s.getAuthor_name().toLowerCase();
                    break;
                case 3:
                    targetColumn = s.getCategory().toLowerCase();
                    break;
                default:
                    targetColumn = s.getBook_name().toLowerCase();
            }


            if (targetColumn.contains(targetText))
                return true;

            return false;

        });
    }

    private void delete() {

        if(table_view.getSelectionModel().getSelectedIndex() == -1)
            return;

        ObservableList<BookModal> selectedItems = table_view.getSelectionModel().getSelectedItems();

        MyAlert.confirmAlert(selectedItems.size()==1?"Are u sure you wanna delete " +
                table_view.getSelectionModel().getSelectedItem().getBook_name() + " Book record?" :
                "Are you sure you want to delete all selected record", () -> {

            for(int i=0; i<selectedItems.size(); i++)
                MyDatabase.delete("book", "book_id", selectedItems.get(i).getBook_id());

            MyDatabase.mainBookRecordsObList.removeAll(selectedItems);

        });

    }

    private void save() {

        if (table_view.getSelectionModel().getSelectedIndex() == -1)
            return;

        if(MyUtils.checkEmpty(tf_book_name, "Book Name") &&
        MyUtils.checkEmpty(tf_author_name, "Author Name") &&
        MyUtils.checkEmpty(tf_category, "Category") &&
        MyUtils.checkEmpty(tf_quantity, "Quantity")){

            String date = dp_date_of_adding.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            BookModal bookModal = new BookModal(
                    tf_book_id.getText(),
                    tf_book_name.getText(),
                    tf_author_name.getText(),
                    tf_category.getText(),
                    Integer.parseInt(tf_quantity.getText()),
                    date);

            if(MyDatabase.updateBookData(bookModal)){

                refreshTable(bookModal);
                MyAlert.successAlert("Book Record Modified Successfully");

            }else{
                MyAlert.errorAlert("Failed To Modify Book Record. (Internal Error occurred)");
            }

        }

    }

    private void refreshTable(BookModal newModal) {

        BookModal bookModal = table_view.getSelectionModel().getSelectedItem();
        bookModal.setBook_name(newModal.getBook_name());
        bookModal.setAuthor_name(newModal.getAuthor_name());
        bookModal.setCategory(newModal.getCategory());
        bookModal.setQuantity(newModal.getQuantity());
        bookModal.setDate_of_adding(newModal.getDate_of_adding());
        table_view.refresh();

    }

    private void dateFormatter(JFXDatePicker dp) {
        dp.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }
}
