package add_book;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import modals.BookModal;
import my_database.MyDatabase;
import my_utils.MyAlert;
import my_utils.MyUtils;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AddBook implements Initializable {

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
    private JFXButton btn_add_book;

    @FXML
    private JFXTextField tf_date_of_addition;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        MyUtils.setNumberOnly(tf_quantity);

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        tf_date_of_addition.setText(date);

        btn_add_book.setOnAction(e->saveBook());

    }

    private void saveBook() {

        if(MyUtils.checkEmpty(tf_book_id, "Book Id") &&
                MyUtils.checkEmpty(tf_book_name, "Book Name") &&
                MyUtils.checkEmpty(tf_author_name, "Author Name") &&
                MyUtils.checkEmpty(tf_category, "Category") &&
                MyUtils.checkEmpty(tf_quantity, "Quantity")){

            if(MyDatabase.isBookIdUnique(tf_book_id.getText())){

                BookModal bookModal = new BookModal(
                        tf_book_id.getText(),
                        tf_book_name.getText(),
                        tf_author_name.getText(),
                        tf_category.getText(),
                        Integer.parseInt(tf_quantity.getText()),
                        tf_date_of_addition.getText()
                );

                if(MyDatabase.saveBookDate(bookModal)){
                    MyAlert.successAlert("Book Added Successfully");
                    clearAll();
                }else {
                    MyAlert.errorAlert("Failed To Add book. (Internal Error occurred)");
                }

            }else{
                MyAlert.errorAlert("Book Id Already Exist! please enter a unique Book Id");
            }

        }

    }

    private void clearAll() {
        tf_book_id.clear();
        tf_book_name.clear();
        tf_author_name.clear();
        tf_category.clear();
        tf_quantity.clear();
    }

}
