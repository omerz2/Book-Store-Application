package bookstore;

/*
*
Author: Omer
*
*/

// JavaFX libraries for UI components
import javafx.scene.control.CheckBox;

// The Book class represents a book with a name, price and a selection checkbox.
public class Book {

    // The name of the book
    private final String bookName;
    
    // The price of the book
    private final double price;
    
    // A checkbox that indicates whether the book is selected or not
    private CheckBox selection;

    // Constructor method for the Book class.
    // Initializes a new Book with the given title, price, and an unselected checkbox.
    public Book (String bookTitle, double bookPrice) {
        this.bookName = bookTitle;
        this.price = bookPrice;
        selection = new CheckBox();
    }

    // Getter method for the book's name
    public String getBookName() {
        return this.bookName;
    }

    // Getter method for the book's price
    public double getPrice() {
        return this.price;
    }

    // Getter method for the book's selection status
    public CheckBox getSelection() {
        return selection;
    }

    // Setter method for the book's selection status
    // Allows to change the selection status of the book
    public void setSelection(CheckBox select) {
        this.selection = select;
    }
    
}
