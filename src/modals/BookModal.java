package modals;

public class BookModal {

    private String book_id;
    private String book_name;
    private String author_name;
    private String category;
    private int quantity;
    private String date_of_adding;


    public BookModal(String book_id, String book_name, String author_name, String category, int quantity, String date_of_adding) {
        this.book_id = book_id;
        this.book_name = book_name;
        this.author_name = author_name;
        this.category = category;
        this.quantity = quantity;
        this.date_of_adding = date_of_adding;
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
}
