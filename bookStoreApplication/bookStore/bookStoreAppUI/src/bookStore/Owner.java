package bookstore;

/*
*
Author: Omer
*
*/
// Import necessary classes for file handling and data manipulation
import java.io.IOException;
import java.util.ArrayList;

// The Owner class represents the owner of the bookstore. It handles operations related to customers and books.
public class Owner {
   
   // Static instance of the fileHandle class to handle file operations
   private static final fileHandle files = new fileHandle();
   
   // Static list to store books
    public static final ArrayList<Book> books = new ArrayList<>();
    
    // Static list to store customers
    private static final ArrayList<Customer> customers = new ArrayList<>();

    // Method to restock arrays by reading data from files
    public void restockArrays() throws IOException {
        // Read book and customer data from the respective files
        ArrayList<Book> tempBooks = files.readBookFile();
        ArrayList<Customer> tempCustomers = files.readCustomerFile();
        
        // Add all books and customers to the static lists
        books.addAll(tempBooks);
        customers.addAll(tempCustomers);
    }

    // Method to get the username of the owner. Hardcoded as "admin"
    public String getUsername() {
        return "admin";
    }

    // Method to get the password of the owner. Hardcoded as "admin"
    public String getPassword() {
        return "admin";
    }

    // Method to add a new customer to the customers list
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    // Method to delete a customer from the customers list
    public void deleteCustomer(Customer customer) {
        customers.remove(customer);
    }

    // Method to add a new book to the books list
    public void addBook(Book book) {
        books.add(book);
    }

    // Method to delete a book from the books list
    public void deleteBook(Book book) {
        books.remove(book);
    }

    // Method to get a list of all books
    public ArrayList<Book> getBooks() {
        return new ArrayList<>(books);
    }

    // Method to get a list of all customers
    public ArrayList<Customer> getCustomers() {
        return new ArrayList<>(customers);
    }
}
