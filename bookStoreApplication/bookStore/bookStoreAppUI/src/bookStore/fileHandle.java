package bookstore;

/*
*
Author: Omer
*
*/
// Import necessary classes for file handling and data manipulation
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

// The fileHandle class handles file-related operations for the Bookstore.
public class fileHandle {

    // Method to write book information to a file
    public void bookFileWrite(ArrayList<Book> books) throws IOException {
        // Create a FileWriter object for the "book.txt" file in append mode
        FileWriter bookFile = new FileWriter("book.txt", true);
        
        // Loop through each book in the ArrayList and write its information to the file
        for (Book b : books) {
            String bookInfo = b.getBookName() + ", " + b.getPrice() + "\n";
            bookFile.write(bookInfo);
        }
        
        // Close the FileWriter
        bookFile.close();
    }

    // Method to write customer information to a file
    public void customerFileWrite(ArrayList<Customer> customers) throws IOException {
        // Create a FileWriter object for the "customer.txt" file in append mode
        FileWriter customerFile = new FileWriter("customer.txt", true);
        
        // Loop through each customer in the ArrayList and write its information to the file
        for (Customer c : customers) {
            String outputText = c.getUsername() + ", " + c.getPassword() + ", " + c.getPoints() + "\n";
            customerFile.write(outputText);
        }
        
        // Close the FileWriter
        customerFile.close();
    }

    // Method to reset the book file (delete all data)
    public void bookFileReset() throws IOException {
        // Create a FileWriter object for the "book.txt" file in overwrite mode
        FileWriter bookFile = new FileWriter("book.txt", false);
        bookFile.close();
    }

    // Method to reset the customer file (delete all data)
    public void customerFileReset() throws IOException {
        // Create a FileWriter object for the "customer.txt" file in overwrite mode
        FileWriter customerFile = new FileWriter("customer.txt", false);
        customerFile.close();
    }

    // Method to read book information from a file
    public ArrayList<Book> readBookFile() throws IOException {
        // Create a Scanner object for the "book.txt" file
        Scanner scan = new Scanner(new FileReader("book.txt"));
        ArrayList<Book> tempBookHolder = new ArrayList<>();

        // Loop through each line in the file, parse the book information and add it to the ArrayList
        while (scan.hasNext()) {
            String[] bookInfo = scan.nextLine().split(",");
            String title = bookInfo[0].trim();
            double price = Double.parseDouble(bookInfo[1].trim());
            tempBookHolder.add(new Book(title, price));
        }
        scan.close();
        return tempBookHolder;
    }

    // Method to read customer information from a file
    public ArrayList<Customer> readCustomerFile() throws IOException {
        // Create a Scanner object for the "customer.txt" file
        Scanner scan = new Scanner(new FileReader("customer.txt"));
        ArrayList<Customer> tempCustomerHolder = new ArrayList<>();

        // Loop through each line in the file, parse the customer information and add it to the ArrayList
        while (scan.hasNext()) {
            String[] customerInfo = scan.nextLine().split(", ");
            String username = customerInfo[0];
            String password = customerInfo[1];
            int points = Integer.parseInt(customerInfo[2]);
            tempCustomerHolder.add(new Customer(username, password));
            tempCustomerHolder.get(tempCustomerHolder.size() - 1).setPoints(points);
        }
        scan.close();
        return tempCustomerHolder;
    }
}
