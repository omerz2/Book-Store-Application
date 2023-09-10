package bookstore;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
*
Author: Omer
*
*/

public class bookStoreMain extends Application {

    private final Owner owner = new Owner();
    private Customer currentCustomer;
    private static final fileHandle files = new fileHandle();

    // UI components initialization
    private Button loginButton = new Button("Login");
    private Button booksButton = new Button("Books");
    private Button customersButton = new Button("Customers");
    private Button logoutButton = new Button("Logout");
    private Button backButton = new Button("\uD83E\uDC60 Back");
    private Button buyButton = new Button("Buy");
    private Button pointsBuyButton = new Button("Redeem points & Buy");
    private TextField userTextField = new TextField();
    private PasswordField passTextField = new PasswordField();
    private HBox hb = new HBox();
    
    // Table views and their data initialization
    private TableView<Book> booksTable = new TableView<>();
    private final TableView.TableViewFocusModel<Book> defaultFocusModel = booksTable.getFocusModel();
    private ObservableList<Book> books = FXCollections.observableArrayList();

    private TableView<Customer> customersTable = new TableView<>();
    private ObservableList<Customer> customers = FXCollections.observableArrayList();

    public ObservableList<Book> addBooks(){
        ObservableList<Book> books = FXCollections.observableArrayList();
        for (Book book : Owner.books) {
            books.add(book);
        }
        return books;
    }

    public ObservableList<Customer> addCustomers(){
        customers.addAll(owner.getCustomers());
        return customers;
    }

    @Override
    public void start(Stage primaryStage) {
        setupStage(primaryStage);
        setupButtonsActions(primaryStage);

        // Handle close request
        primaryStage.setOnCloseRequest(e -> handleCloseRequest());
    }

    private void setupStage(Stage primaryStage) {
        // Stage setup
        primaryStage.setTitle("BookStore Application");

        // GridPane setup
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Buttons for grid pane
        Button button1 = new Button("Button 1");
        Button button2 = new Button("Button 2");
        Button button3 = new Button("Button 3");

        // Setting the grid layout constraints
        GridPane.setConstraints(button1, 0, 0);
        GridPane.setConstraints(button2, 1, 0);
        GridPane.setConstraints(button3, 2, 0);

        // Adding buttons to grid pane
        gridPane.getChildren().addAll(button1, button2, button3);

        // Set responsive behavior using percentage widths for columns
        gridPane.getColumnConstraints().addAll(
            new ColumnConstraints(33.33), // 33.33% of the width
            new ColumnConstraints(33.33),
            new ColumnConstraints(33.33)
        );

        // Setting up the scene and adding to stage
        Scene scene = new Scene(gridPane, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Further stage setup
        primaryStage.setTitle("BookStore App");
        primaryStage.getIcons().add(new Image("file:src/bookPic.png"));
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(loginScreen(false), 605, 550));
        primaryStage.show();

        // Message to the console
        System.out.println("Opened bookstore application");

        // Attempting to restock arrays from files
        try {
            owner.restockArrays();
            System.out.println("Arrays restocked from files");
        } catch (IOException e){
            System.out.println("File Importing Error");
        }
    }

    private void setupButtonsActions(Stage primaryStage) {
        // Setting up actions for each button
        loginButton.setOnAction(e -> handleLoginAction(primaryStage));
        logoutButton.setOnAction(e -> handleLogoutAction(primaryStage));
        booksButton.setOnAction(e -> primaryStage.setScene(new Scene(booksTableScreen(), 605, 550)));
        customersButton.setOnAction(e -> primaryStage.setScene(new Scene(customerTableScreen(), 605, 600)));
        backButton.setOnAction(e -> primaryStage.setScene(new Scene(ownerStartScreen(), 605, 550)));
        pointsBuyButton.setOnAction(e -> handlePointsBuyAction(primaryStage));
        buyButton.setOnAction(e -> handleBuyAction(primaryStage));
    }

    private void handleLoginAction(Stage primaryStage) {
        boolean loggedIn = false;

        if(userTextField.getText().equals(owner.getUsername()) && passTextField.getText().equals(owner.getUsername())) {
            primaryStage.setScene(new Scene(ownerStartScreen(), 605, 550));
            loggedIn = true;
        }
        for(Customer c: owner.getCustomers()) {
            if (userTextField.getText().equals(c.getUsername()) && passTextField.getText().equals(c.getPassword())) {
                currentCustomer = c;
                primaryStage.setScene(new Scene(customerHomeScreen(0), 605, 550));
                loggedIn = true;
            }
        }
        if(!loggedIn) {
            primaryStage.setScene(new Scene(loginScreen(true), 605, 550));
        }
    }

    private void handleLogoutAction(Stage primaryStage) {
        primaryStage.setScene(new Scene(loginScreen(false), 605, 550));
        for(Book book: Owner.books){
            book.setSelection(new CheckBox());
        }
        userTextField.clear();
        passTextField.clear();
    }

    private void handlePointsBuyAction(Stage primaryStage) {
        boolean bookSelected = false;
        for(Book b: Owner.books) {
            if (b.getSelection().isSelected()) {
                bookSelected = true;
            }
        }

        if(!bookSelected) {
            primaryStage.setScene(new Scene(customerHomeScreen(1), 605, 550));
        } else if(currentCustomer.getPoints() == 0) {
            primaryStage.setScene(new Scene(customerHomeScreen(2), 605, 550));
        } else if(currentCustomer.getPoints() != 0) {
            primaryStage.setScene(new Scene(checkoutScreen(true), 605, 550));
        }
    }

    private void handleBuyAction(Stage primaryStage) {
        boolean bookSelected = false;
        for (Book b : Owner.books) {
            if (b.getSelection().isSelected()) {
                bookSelected = true;
            }
        }

        if(bookSelected) {
            primaryStage.setScene(new Scene(checkoutScreen(false), 605, 550));
        } else {
            primaryStage.setScene(new Scene(customerHomeScreen(1), 605, 550));
        }
    }

    private void handleCloseRequest() {
        System.out.println("Exited the book store");
        try {
            files.bookFileReset(); //when program closes, reset file and add all current stock
        } catch (IOException ex) {
            Logger.getLogger(bookStoreMain.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            // program opens arrays are restocked with current available stock
            files.customerFileReset();
        } catch (IOException ex) {
            Logger.getLogger(bookStoreMain.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Files reset");

        try {
            files.bookFileWrite(Owner.books);
        } catch (IOException ex) {
            Logger.getLogger(bookStoreMain.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            files.customerFileWrite(owner.getCustomers());
        } catch (IOException ex) {
            Logger.getLogger(bookStoreMain.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Files updated with current array data");
   

        //css
        {
            buyButton.setStyle("-fx-background-color: #62fc7a;" + "-fx-font-size:15;"); //changed
            pointsBuyButton.setStyle("-fx-background-color: #62fc7a;" + "-fx-font-size:15;" ); //changed
            customersButton.setStyle("-fx-background-color: #e1e2e3;" + "-fx-font-size:25;" + "-fx-background-radius: 10;"); //changed
            customersButton.setFont(new Font("Verdana", 25));
            booksButton.setStyle("-fx-background-color: #e1e2e3;" + "-fx-font-size:25;" + "-fx-background-radius: 10;"); //changed
            booksButton.setFont(new Font("Verdana", 25));
            logoutButton.setStyle("-fx-background-color: #e1e2e3;" + "-fx-font-size:25;" ); //changed
            backButton.setStyle("-fx-background-color: #356de6;" + "-fx-font-size:15;"); //changed
            loginButton.setStyle("-fx-background-color: #24c998;"); //changed
           


            customersTable.setStyle("-fx-control-inner-background: #d9d9d9;" +
                    "-fx-selection-bar: #4ca7d9; -fx-selection-bar-non-focused: #4ca7d9;" + "-fx-border-color: #d9d9d9;" +
                    "-fx-table-cell-border-color: #d9d9d9;" + "-fx-background-color: #d9d9d9;"); //changed selection color to blue

            booksTable.setStyle("-fx-control-inner-background: #d9d9d9;" + "-fx-border-color: #d9d9d9;" +
                    "-fx-selection-bar: #4ca7d9; -fx-selection-bar-non-focused: #4ca7d9;" +
                    "-fx-table-cell-border-color: #d9d9d9;" + "-fx-background-color: #d9d9d9;" + "-fx-column-header-background: #d9d9d9;"); //changed selection color to blue
        }
    }
    
   
    public Group loginScreen(boolean loginError){
    Group lis = new Group();

    // header setup
    HBox header = new HBox();
    Image rawLogo = new Image("file:src/bookPic.png");
    ImageView logo = new ImageView(rawLogo);
    logo.setFitHeight(50);
    logo.setFitWidth(50);

    header.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 10 10 10 10;");
    Label brand = new Label("BOOKSTORE");
    brand.setFont(new Font("Verdana", 35));
    header.getChildren().addAll(brand, logo);
    header.setSpacing(5);
    header.setAlignment(Pos.CENTER);

    // loginBox setup
    VBox loginBox = new VBox();
    loginBox.setOpacity(0.7);
    loginBox.setPadding(new Insets(20,65,60,65));
    loginBox.setStyle("-fx-background-color: ##62fc7a; -fx-background-radius: 10 10 10 10;"); //changed
    loginBox.setSpacing(10);

    // User fields setup
    Text user = new Text("Username");
    userTextField.setStyle("-fx-background-color: #e1e2e3;"); //changed
    userTextField.setPromptText("Username here");

    // Password fields setup
    Text pass = new Text("Password");
    passTextField.setStyle("-fx-background-color: #e1e2e3;"); //changed
    passTextField.setPromptText("Password here");

    // Login button setup
    loginButton.setMinWidth(175);
    loginButton.setAlignment(Pos.CENTER);
    
    // Add elements to loginBox
    loginBox.getChildren().addAll(user, userTextField, pass, passTextField, loginButton);

    // Add error message if loginError is true
    if(loginError){
        Text errorMsg = new Text("Incorrect username and/or password.");
        errorMsg.setFill(Color.RED);
        loginBox.getChildren().add(errorMsg);
    }

    // Background setup
    VBox bg = new VBox();
    bg.getChildren().addAll(header, loginBox);
    bg.setStyle("-fx-background-color: #d2d3d4;");
    bg.setPadding(new Insets(80, 280, 200, 150));
    bg.setSpacing(80);

    lis.getChildren().addAll(bg);
    
    return lis;
} //complete 

    public Group customerHomeScreen(int type){
        Group bookstore = new Group();
        booksTable.getItems().clear();
        booksTable.getColumns().clear();
        booksTable.setFocusModel(null);

        Font font = new Font(14);
        Text welcomeMsg = new Text("Welcome, " + currentCustomer.getUsername() + ". You have " + currentCustomer.getPoints() + " points. Your status is " + currentCustomer.getStatus() + ".");
        welcomeMsg.setFont(font);
       

        //Book title column
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setMinWidth(200);
        titleColumn.setStyle("-fx-alignment: CENTER;");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));



        //Book price column
        TableColumn<Book, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(100);
        priceColumn.setStyle("-fx-alignment: CENTER;");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Checkbox column
        TableColumn<Book, CheckBox> selectColumn = new TableColumn<>("Select");
selectColumn.setMinWidth(100);
selectColumn.setStyle("-fx-alignment: CENTER;");
selectColumn.setCellValueFactory(new PropertyValueFactory<>("selection"));


        booksTable.setItems(addBooks());
booksTable.getColumns().addAll(titleColumn, priceColumn, selectColumn);

     
        BorderPane header = new BorderPane();
        header.setLeft(welcomeMsg);
      

        HBox bottom = new HBox();
        bottom.setAlignment(Pos.BOTTOM_CENTER);
        bottom.setSpacing(5);
        bottom.getChildren().addAll(buyButton, pointsBuyButton, logoutButton);

        
        VBox vbox = new VBox();
        String errMsg = "";
        if(type == 1){
            errMsg = "Please select a book";
        }
        else if(type == 2){
            errMsg =  "No redeemable points";
        }
        Text warning = new Text(errMsg);
        warning.setFill(Color.RED);
        vbox.setStyle("-fx-background-color: #d2d3d4;");
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(40, 200, 30, 100));
        vbox.getChildren().addAll(header, booksTable, bottom, warning);

        bookstore.getChildren().addAll(vbox);

        return bookstore;
    }//complete

    public Group checkoutScreen(boolean usedPoints){
        Group cos = new Group();
        double total, subtotal = 0, discount;
        int pointsEarned, i = 0, bookCount = 0;
        String[][] booksBought = new String[25][2];

        for (Book book : Owner.books) {
    if (book.getSelection().isSelected()) {
        subtotal += book.getPrice();
        booksBought[i][0] = book.getBookName();
        booksBought[i][1] = String.valueOf(book.getPrice());
        i++;
    }
}


        if(usedPoints){
            if((double)currentCustomer.getPoints()/100 >= subtotal){
                discount = subtotal;
                currentCustomer.setPoints(-(int)subtotal*100);
            }
            else{
                discount = ((double)currentCustomer.getPoints()/100);
                currentCustomer.setPoints(-currentCustomer.getPoints());
            }
        }else discount = 0;

        total = subtotal - discount;
        pointsEarned = (int)total*10;
        currentCustomer.setPoints(pointsEarned);

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.setSpacing(15);
        header.setPadding(new Insets(0,0,25,0));
        Label brandName = new Label("BOOKSTORE");
        brandName.setFont(new Font("Verdana", 35));
        Image rawLogo = new Image("file:src/bookPic.png");
        ImageView logo = new ImageView(rawLogo);
        logo.setFitHeight(50);
        logo.setFitWidth(50);
        header.getChildren().addAll(brandName, logo);

        VBox receipt = new VBox();
        receipt.setSpacing(7);
        Text receiptTxt = new Text("Receipt");
        receiptTxt.setFont(Font.font(null, FontWeight.BOLD, 20));
        receipt.getChildren().addAll(receiptTxt);

        VBox receiptItems = new VBox();
        receiptItems.setPadding(new Insets(10,10,10,10));
        receiptItems.setStyle("-fx-background-color: #e8e8e8;"); //changed
        receiptItems.setAlignment(Pos.CENTER);
        receiptItems.setSpacing(7);
        for (i = 0; i<25; i++) {
            if(booksBought[i][0] != null){
                Text bookTitle = new Text(booksBought[i][0]);
                Text bookPrice = new Text(booksBought[i][1]);
                BorderPane item = new BorderPane();
                item.setLeft(bookTitle);
                item.setRight(bookPrice);
                Line thinLine = new Line(0, 150, 380, 150);
                receiptItems.getChildren().addAll(item, thinLine);
                bookCount++;
            }
        }

        ScrollPane scrollReceipt = new ScrollPane(receiptItems); // lets us scroll through books in receipt if more than 4 bought
        scrollReceipt.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollReceipt.setStyle("-fx-background-color:transparent;");
        scrollReceipt.setFitToWidth(true);
        if(bookCount<=4){
            scrollReceipt.setFitToHeight(true);
        } else scrollReceipt.setPrefHeight(130);


        Text subtotalText = new Text("Subtotal: $" + (Math.round(subtotal*100.0))/100.0);
        Text pointsDisc = new Text("Discount (from redeemed points): $" + (Math.round(discount*100.0))/100.0);
        Text totalText = new Text("Total: $" + (Math.round(total*100.0))/100.0);
        totalText.setFont(new Font("Arial", 15));
        totalText.setFont(Font.font(null, FontWeight.BOLD,15));
        receipt.getChildren().addAll(scrollReceipt, subtotalText, pointsDisc, totalText);
        
        // receiptTxt.setFont(Font.font(null, FontWeight.BOLD, 20));

        VBox bottom = new VBox();
        bottom.setSpacing(40);
        bottom.setAlignment(Pos.CENTER);
        Text info = new Text("You earned " + pointsEarned + " points " +
                "and your current status is " + currentCustomer.getStatus() + "\n\t\t\tThanks for shopping with us!");
        bottom.getChildren().addAll(info, logoutButton);

        VBox screen = new VBox();
        screen.setStyle("-fx-background-color: #d2d3d4;");
        screen.setPadding(new Insets(60,105,500,100));
        screen.setAlignment(Pos.CENTER);
        screen.setSpacing(10);
        screen.getChildren().addAll(header, receipt, bottom);

        cos.getChildren().addAll(screen);
Owner.books.removeIf(b -> b.getSelection().isSelected());
        return cos;
    }//complete

    public VBox ownerStartScreen() {
    // Create a new VBox that will contain all UI elements for the owner start screen
    VBox osc = new VBox();
    
    // Set the style, alignment, spacing, and padding of the owner start screen
    osc.setStyle("-fx-background-color: #d2d3d4;");  // Set the background color of the screen
    osc.setAlignment(Pos.CENTER);  // Center align the elements on the screen
    osc.setSpacing(100);  // Set the spacing between elements on the screen
    osc.setPadding(new Insets(80,0,30,0));  // Set the padding around the edges of the screen

    // Create a new VBox for the buttons
    VBox buttons = new VBox();
    
    // Set the alignment and spacing of the buttons
    buttons.setAlignment(Pos.CENTER);  // Center align the buttons
    buttons.setSpacing(40);  // Set the spacing between the buttons
    
    // Add the booksButton and customersButton to the buttons VBox
    buttons.getChildren().addAll(booksButton, customersButton);
    
    // Set the size of the booksButton and customersButton
    booksButton.setPrefSize(275,175);
    customersButton.setPrefSize(275,175);

    // Add the buttons VBox and the logoutButton to the owner start screen
    osc.getChildren().addAll(buttons, logoutButton);
    
    return osc;  
} 


    public Group booksTableScreen() {
    // Create a new Group for the books table screen
    Group bt = new Group();

    // Clear the contents of hb (an HBox), booksTable (a TableView), and booksTable columns
    hb.getChildren().clear();
    booksTable.getItems().clear();
    booksTable.getColumns().clear();

    // Reset the focus model of booksTable to its default
    booksTable.setFocusModel(defaultFocusModel);

    // Create a label for the book list and set its font and padding
    Label label = new Label("Book List");
    label.setFont(new Font("Verdana", 20));
    label.setPadding(new Insets(10,0,0,0));

    // Set up the title column of the books table
    TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
    titleColumn.setMinWidth(200);
    titleColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
    titleColumn.setStyle("-fx-alignment: CENTER");

    // Set up the price column of the books table
    TableColumn<Book, Double> priceColumn = new TableColumn<>("Price");
    priceColumn.setMinWidth(100);
    priceColumn.setStyle("-fx-alignment: CENTER;");
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

    // Add books to the books table and add the columns to the table
    booksTable.setItems(addBooks());
    booksTable.getColumns().addAll(titleColumn, priceColumn);

    // Create text fields for adding a book's title and price
    final TextField addBookTitle = new TextField();
    final TextField addBookPrice = new TextField();
    addBookTitle.setPromptText("Title");
    addBookPrice.setPromptText("Price");
    addBookTitle.setMaxWidth(titleColumn.getPrefWidth());
    addBookPrice.setMaxWidth(priceColumn.getPrefWidth());
    addBookTitle.setStyle("-fx-background-color: #ffffff;"); 
    addBookPrice.setStyle("-fx-background-color: #ffffff;");

    // Create a VBox for the core contents of the screen
    VBox core = new VBox();
    final Button addButton = new Button("Add");
    addButton.setStyle("-fx-background-color: #62fc7a;"); 
    Label bookAddErr = new Label("Invalid Input");
    bookAddErr.setTextFill(Color.color(1,0,0));

    // Set the functionality of the addButton
    addButton.setOnAction(e -> {
        try {
            // Get the price and round it
            double price = Math.round((Double.parseDouble(addBookPrice.getText()))*100);
            
            // Add the new book to the Owner's books
            Owner.books.add(new Book(addBookTitle.getText(), price/100));

            // Clear the books table and refresh the items
            booksTable.getItems().clear();
            booksTable.setItems(addBooks());

            // Clear the text fields
            addBookTitle.clear();
            addBookPrice.clear();

            // Remove any previous Invalid Input errors
            core.getChildren().remove(bookAddErr);
        }
        catch (Exception exception){
            // If an error occurred, display the Invalid Input error
            if(!core.getChildren().contains(bookAddErr)){
                core.getChildren().add(bookAddErr);
            }
        }
    });

    // Create the delete button and set its functionality
    final Button deleteButton = new Button("Delete");
    deleteButton.setStyle("-fx-background-color: #f54c4c;" + "-fx-font-size:15;");
    deleteButton.setOnAction(e -> {
        // Get the selected item and remove it from the books table and the Owner's books
        Book selectedItem = booksTable.getSelectionModel().getSelectedItem();
        booksTable.getItems().remove(selectedItem);
        Owner.books.remove(selectedItem);
    });

    // Add elements to the hb (an HBox) and set its properties
    hb.getChildren().addAll(addBookTitle, addBookPrice, addButton);
    hb.setSpacing(3);
    hb.setAlignment(Pos.CENTER);

    // Create an HBox for the back and delete buttons and set its properties
    HBox back = new HBox();
    back.setPadding(new Insets(20,0,0,150));
    back.setSpacing(10);
    back.getChildren().addAll(backButton, deleteButton);
    back.setAlignment(Pos.BOTTOM_CENTER);

    // Set properties of the core VBox and add its elements
    core.setAlignment(Pos.CENTER);
    core.setSpacing(5);
    core.setPadding(new Insets(0, 0, 0, 150));
    core.getChildren().addAll(label, booksTable, hb);

    // Create a VBox for the whole screen and set its properties
    VBox vbox = new VBox();
    vbox.setStyle("-fx-background-color: #d2d3d4;");
    vbox.setPadding(new Insets(0, 200, 60, 0));
    vbox.setAlignment(Pos.CENTER);
    vbox.getChildren().addAll(core, back);

    // Add the vbox to the books table screen
    bt.getChildren().addAll(vbox);

    // Return the completed books table screen
    return bt;
}//complete


    public Group customerTableScreen() {
        Group ct = new Group();
        hb.getChildren().clear();
        customersTable.getItems().clear();
        customersTable.getColumns().clear();

        Label label = new Label("Customer List");
        label.setFont(new Font("Verdana", 20));

        //Customer username column
        TableColumn<Customer, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setMinWidth(140);
        usernameCol.setStyle("-fx-alignment: CENTER;");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        //Customer password column
        TableColumn<Customer, String> passwordCol = new TableColumn<>("Password");
        passwordCol.setMinWidth(140);
        passwordCol.setStyle("-fx-alignment: CENTER;");
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

        //Customer points column
        TableColumn<Customer, Integer> pointsCol = new TableColumn<>("Points");
        pointsCol.setMinWidth(100);
        pointsCol.setStyle("-fx-alignment: CENTER;");
        pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));

        customersTable.setItems(addCustomers());
        customersTable.getColumns().addAll(usernameCol, passwordCol, pointsCol);

        final TextField addUsername = new TextField();
        addUsername.setPromptText("Username");
        addUsername.setMaxWidth(usernameCol.getPrefWidth());
        final TextField addPassword = new TextField();
        addPassword.setMaxWidth(passwordCol.getPrefWidth());
        addPassword.setPromptText("Password");
        addPassword.setStyle("-fx-background-color: #ffffff;");
        addUsername.setStyle("-fx-background-color: #ffffff;");

        VBox core = new VBox();
        Text customerAddErr = new Text("Customer already exists!");
        customerAddErr.setFill(Color.color(1,0,0));
        final Button addButton = new Button("Add");
        addButton.setStyle("-fx-background-color: #62fc7a;"); //green add button
        addButton.setOnAction(e -> {
            boolean duplicate = false;

            for(Customer c: owner.getCustomers()){
                if((c.getUsername().equals(addUsername.getText()) && c.getPassword().equals(addPassword.getText())) ||
                        (addUsername.getText().equals(owner.getUsername()) && addPassword.getText().equals(owner.getPassword()))){
                    duplicate = true;
                    if(!core.getChildren().contains(customerAddErr)){
                        core.getChildren().add(customerAddErr);
                    }
                }
            }

            if(!(addUsername.getText().equals("") || addPassword.getText().equals("")) && !duplicate) {
                owner.addCustomer(new Customer(addUsername.getText(), addPassword.getText())); //for the actual arraylist
                customersTable.getItems().clear(); //this is to refresh the table with actual values instead of visual ones
                customersTable.setItems(addCustomers());
                core.getChildren().remove(customerAddErr); //remove any previous error text messages
                addPassword.clear(); //clear text fields
                addUsername.clear();
            }
        });

        final Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #f54c4c;" + "-fx-font-size:15;"); //red for delete
        deleteButton.setOnAction(e -> {
            Customer selectedItem = customersTable.getSelectionModel().getSelectedItem();
            customersTable.getItems().remove(selectedItem); //remove from tableview
            //customers.remove(selectedItem); //and this removes from the observable one
            owner.deleteCustomer(selectedItem); //removes from the actual arraylist
        });

        hb.getChildren().addAll(addUsername, addPassword, addButton);
        hb.setAlignment(Pos.CENTER);
        hb.setSpacing(3);

        HBox bottom = new HBox();
        bottom.setPadding(new Insets(5));
        bottom.setSpacing(10);
        bottom.setPadding(new Insets(20,0,0,110));
        bottom.setAlignment(Pos.CENTER);
        bottom.getChildren().addAll(backButton, deleteButton);
        

        core.setAlignment(Pos.CENTER);
        core.setSpacing(5);
        core.setPadding(new Insets(10,0,0,110));
        core.getChildren().addAll(label, customersTable, hb);

        VBox vbox = new VBox();
        vbox.setStyle("-fx-background-color: #d2d3d4;");
        vbox.setPadding(new Insets(0, 150, 60, 0));
        vbox.getChildren().addAll(core, bottom);
        vbox.setAlignment(Pos.CENTER);

        ct.getChildren().addAll(vbox);
        return ct;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
