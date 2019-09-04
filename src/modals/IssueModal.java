package modals;

public class IssueModal {

    private String book_id;
    private String book_name;
    private String author_name;
    private String category;
    private int quantity;
    private String date_of_adding;
    private String issue_id;
    private String student_name;
    private String student_class;
    private String student_roll;
    private String date_of_issue;

    public IssueModal(String book_id, String book_name, String author_name, String category, int quantity, String date_of_adding, String issue_id, String student_name, String student_class, String student_roll, String date_of_issue) {
        this.book_id = book_id;
        this.book_name = book_name;
        this.author_name = author_name;
        this.category = category;
        this.quantity = quantity;
        this.date_of_adding = date_of_adding;
        this.issue_id = issue_id;
        this.student_name = student_name;
        this.student_class = student_class;
        this.student_roll = student_roll;
        this.date_of_issue = date_of_issue;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDate_of_adding() {
        return date_of_adding;
    }

    public void setDate_of_adding(String date_of_adding) {
        this.date_of_adding = date_of_adding;
    }

    public String getIssue_id() {
        return issue_id;
    }

    public void setIssue_id(String issue_id) {
        this.issue_id = issue_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_class() {
        return student_class;
    }

    public void setStudent_class(String student_class) {
        this.student_class = student_class;
    }

    public String getStudent_roll() {
        return student_roll;
    }

    public void setStudent_roll(String student_roll) {
        this.student_roll = student_roll;
    }

    public String getDate_of_issue() {
        return date_of_issue;
    }

    public void setDate_of_issue(String date_of_issue) {
        this.date_of_issue = date_of_issue;
    }
}
