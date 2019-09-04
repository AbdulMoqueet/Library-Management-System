package my_database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modals.BookModal;
import modals.IssueModal;
import my_utils.MyAlert;

import java.sql.*;


public class MyDatabase {

    private static Connection connection;
    public static ObservableList<BookModal> mainBookRecordsObList;
    public static ObservableList<IssueModal> mainIssueRecordsObList;

    public static void connectToOracle() {

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "Danish", "danish777");
        } catch (Exception e) {
            MyAlert.errorAlert(e);
        }

    }

    public static ResultSet getLoginDetails() {

        ResultSet rs = null;

        try {
            Statement statement = connection.createStatement();
            rs = statement.executeQuery("select * from login");
        } catch (Exception e) {
            MyAlert.errorAlert(e);
        }
        return rs;
    }

    public static boolean isBookIdUnique(String targetBookId) {

        try {

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select book_id from book");

            while (rs.next()) {

                if (rs.getString(1).equalsIgnoreCase(targetBookId)) {
                    return false;
                }

            }


        } catch (Exception e) {
            MyAlert.errorAlert(e);
        }

        return true;

    }

    public static boolean isIssueIdUnique(String targetIssueId) {

        try {

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select issue_id from issue");

            while (rs.next()) {

                if (rs.getString(1).equalsIgnoreCase(targetIssueId)) {
                    return false;
                }

            }


        } catch (Exception e) {
            MyAlert.errorAlert(e);
        }

        return true;

    }

    public static boolean saveBookDate(BookModal bookModal) {

        try {

            PreparedStatement preparedStatement = connection.prepareStatement("insert into book values (?,?,?,?,?,?)");
            preparedStatement.setString(1, bookModal.getBook_id());
            preparedStatement.setString(2, bookModal.getBook_name());
            preparedStatement.setString(3, bookModal.getAuthor_name());
            preparedStatement.setString(4, bookModal.getCategory());
            preparedStatement.setInt(5, bookModal.getQuantity());
            preparedStatement.setString(6, bookModal.getDate_of_adding());
            preparedStatement.execute();
            return true;

        } catch (Exception e) {
            MyAlert.errorAlert(e);
        }

        return false;

    }

    public static void loadMainBookRecordsObList() {

        mainBookRecordsObList = FXCollections.observableArrayList();

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("select * from book");

            while (rs.next()) {
                mainBookRecordsObList.add(new BookModal(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getString(6)
                ));
            }

        } catch (Exception e) {
            MyAlert.errorAlert(e);
        }
    }

    public static void loadMainIssueRecordsObList() {

        mainIssueRecordsObList = FXCollections.observableArrayList();

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("select * from issue");

            while (rs.next()) {
                mainIssueRecordsObList.add(new IssueModal(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getString(10),
                        rs.getString(11)
                ));
            }

        } catch (Exception e) {
            MyAlert.errorAlert(e);
        }
    }

    public static boolean updateBookData(BookModal bookModal) {

        try {

            PreparedStatement preparedStatement =
                    connection.prepareStatement("update book set book_name = ?, author_name = ?, category = ?, quantity  = ? where book_id = ?");
            preparedStatement.setString(1, bookModal.getBook_name());
            preparedStatement.setString(2, bookModal.getAuthor_name());
            preparedStatement.setString(3, bookModal.getCategory());
            preparedStatement.setInt(4, bookModal.getQuantity());
            preparedStatement.setString(5, bookModal.getBook_id());
            preparedStatement.executeUpdate();
            return true;

        } catch (Exception e) {
            MyAlert.errorAlert(e);
        }
        return false;
    }

    public static boolean updateIssueData(IssueModal issueModal) {

        try {

            PreparedStatement preparedStatement =
                    connection.prepareStatement("update issue set student_name = ?, student_class = ?, student_roll = ?, date_of_issue  = ? where issue_id = ?");
            preparedStatement.setString(1, issueModal.getStudent_name());
            preparedStatement.setString(2, issueModal.getStudent_class());
            preparedStatement.setString(3, issueModal.getStudent_roll());
            preparedStatement.setString(4, issueModal.getDate_of_issue());
            preparedStatement.setString(5, issueModal.getIssue_id());
            preparedStatement.executeUpdate();
            return true;

        } catch (Exception e) {
            MyAlert.errorAlert(e);
        }
        return false;
    }

    public static void delete(String tableName, String colName, String id) {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from " + tableName + " where " + colName + " = ? ");
            preparedStatement.setString(1, id);
            preparedStatement.execute();
        } catch (Exception e) {
            MyAlert.errorAlert(e);
        }
    }

    public static void returnBook(IssueModal issueModal){

        delete("issue", "issue_id", issueModal.getIssue_id());

        int currentQuantity = getBooksQuantity(issueModal.getBook_id());

        if(currentQuantity==0) {
            dynamicallyCreateBook(issueModal);
        }else{
            currentQuantity++;
            updateBookQuantity(issueModal.getBook_id(), currentQuantity);
        }

    }

    private static void dynamicallyCreateBook(IssueModal issueModal) {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into book values (?,?,?,?,?,?)");
            preparedStatement.setString(1, issueModal.getBook_id());
            preparedStatement.setString(2, issueModal.getBook_name());
            preparedStatement.setString(3, issueModal.getAuthor_name());
            preparedStatement.setString(4, issueModal.getCategory());
            preparedStatement.setInt(5, 1);
            preparedStatement.setString(6, issueModal.getDate_of_adding());
            preparedStatement.execute();

        } catch (Exception e) {
            MyAlert.errorAlert(e);
        }

    }

    private static int getBooksQuantity(String bookId){

        int quantity=0;

        try {

            PreparedStatement preparedStatement = connection.prepareStatement("select quantity from book where book_id = ?");
            preparedStatement.setString(1, bookId);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next())
                quantity = rs.getInt(1);

        }catch (Exception e){
            MyAlert.errorAlert(e);
        }

        return quantity;
    }

    public static boolean saveIssueBookData(IssueModal issueModal) {

        try {

            int currentQuantity = getBooksQuantity(issueModal.getBook_id());

            if (currentQuantity <= 1) {
                delete("book", "book_id", issueModal.getBook_id());
            } else {
                currentQuantity--;
                issueModal.setQuantity(currentQuantity);
                updateBookQuantity(issueModal.getBook_id(), issueModal.getQuantity());
            }

            PreparedStatement preparedStatement = connection.prepareStatement("insert into issue values (?,?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, issueModal.getBook_id());
            preparedStatement.setString(2, issueModal.getBook_name());
            preparedStatement.setString(3, issueModal.getAuthor_name());
            preparedStatement.setString(4, issueModal.getCategory());
            preparedStatement.setInt(5, currentQuantity);
            preparedStatement.setString(6, issueModal.getDate_of_adding());
            preparedStatement.setString(7, issueModal.getIssue_id());
            preparedStatement.setString(8, issueModal.getStudent_name());
            preparedStatement.setString(9, issueModal.getStudent_class());
            preparedStatement.setString(10, issueModal.getStudent_roll());
            preparedStatement.setString(11, issueModal.getDate_of_issue());
            preparedStatement.execute();
            return true;

        } catch (Exception e) {
            MyAlert.errorAlert(e);
        }

        return false;
    }

    private static void updateBookQuantity(String bookId, int quantity) {

        try {
            PreparedStatement pr = connection.prepareStatement("update book set quantity = ? where book_id = ?");
            pr.setInt(1, quantity);
            pr.setString(2, bookId);
            pr.executeUpdate();
        } catch (Exception e) {
            MyAlert.errorAlert(e);
        }

    }

}
