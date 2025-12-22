import java.util.*;
import java.io.*;

public class LMS {
    static Scanner input = new Scanner(System.in);
    static ArrayList<Integer> bookIDs = new ArrayList<>();
    static ArrayList<String> bookTitles = new ArrayList<>();
    static ArrayList<String> bookAuthors = new ArrayList<>();
    static ArrayList<Integer> totalQuantities = new ArrayList<>();
    static ArrayList<Integer> availableQuantities = new ArrayList<>();
    static ArrayList<Integer> memberIDs = new ArrayList<>();
    static ArrayList<Integer> issueBookIDs = new ArrayList<>();
    static ArrayList<Integer> issueMemberIDs = new ArrayList<>();
    static ArrayList<String> issueDates = new ArrayList<>();
    static ArrayList<String> memberNames = new ArrayList<>();
    static String BOOKSDATAFILE = "books_data.txt";
    static String ISSUEDBOOKSDATAFILE = "issued_data.txt";
    static String MEMBERSDATAFILE = "members.txt";
    static String ROOMSDATAFILE = "rooms.txt";
    static int ROOMCOUNT = 5;
    static int[] roomNumbers = { 101, 102, 103, 104, 105 };
    static String[] roomStatus = { "Available", "Available", "Available", "Available", "Available" };
    static int[] reservedByMemberID = new int[ROOMCOUNT];

    public static void loadDataFromFiles() {
        try {
            File f = new File(BOOKSDATAFILE);
            if (f.exists()) {
                Scanner sc = new Scanner(f);
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] parts = line.split(",");
                    bookIDs.add(Integer.parseInt(parts[0]));
                    bookTitles.add(parts[1]);
                    bookAuthors.add(parts[2]);
                    totalQuantities.add(Integer.parseInt(parts[3]));
                    availableQuantities.add(Integer.parseInt(parts[4]));
                }
                sc.close();
            } else {
                System.out.println("Books file not found. Starting fresh.");
            }
        } catch (Exception e) {
            System.out.println("Error loading books data.");
        }

        try {
            File f = new File(ISSUEDBOOKSDATAFILE);
            if (f.exists()) {
                Scanner sc = new Scanner(f);
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] parts = line.split(",");
                    issueBookIDs.add(Integer.parseInt(parts[0]));
                    issueMemberIDs.add(Integer.parseInt(parts[1]));
                    issueDates.add(parts[2]);
                }
                sc.close();
            } else {
                System.out.println("Issued file not found.");
            }
        } catch (Exception e1) {
            System.out.println("Error loading issued data.");
        }
    }

    public static void bookMenu() {
        int choice = 0;
        do {
            System.out.println("\n=============================================");
            System.out.println("           BOOKS MANAGEMENT MENU");
            System.out.println("=============================================");
            System.out.println("1. Add a new book");
            System.out.println("2. Remove a book");
            System.out.println("3. Search book by ID or Author");
            System.out.println("4. Display all available books");
            System.out.println("5. Issue a book");
            System.out.println("6. Return a book");
            System.out.println("7. Show all issued books");
            System.out.println("8. Back to Main Menu");
            System.out.print("Enter choice: ");
            while (true) {
                try {
                    choice = input.nextInt();
                    break;
                } catch (InputMismatchException e11) {
                    System.out.println("Invalid input. Please enter numbers only.");
                    input.nextLine();
                }
            }
            switch (choice) {
                case 1:
                    // addBook();
                    break;
                case 2:
                    // removeBook();
                    break;
                case 3:
                    // searchBook();
                    break;
                case 4:
                    // displayAvailableBooks();
                    break;
                case 5:
                    // issueBook();
                    break;
                case 6:
                    // returnBook();
                    break;
                case 7:
                    // showAllIssuedBooks();
                    break;
                default:
                    System.out.println("Invalid Choice, please enter value from 1 - 8 ");
            }
        } while (choice != 8);
    }
    /*
     * addBook()
     * Adds a new book to the library.
     * Prompts for book ID, title, author, and total quantity.
     * Validates numeric inputs and handles exceptions for invalid entries.
     */
    public static void addBook() {
        System.out.println("\n---------------------------------------------");
        System.out.println("              ADD NEW BOOK");
        System.out.println("---------------------------------------------\n");
        try {
            System.out.print("Enter new Book ID : ");
            int id;
            while (true) {
                try {
                    id = input.nextInt();
                    break;
                } catch (InputMismatchException e12) {
                    System.out.println("Invalid Book ID. Enter numbers only.");
                    input.nextLine();
                }
            }
            if (findBookIndexByID(id) != -1) {
                System.out.println("Book ID already exists. Please enter a unique ID.");
                return;
            }
            input.nextLine();
            bookIDs.add(id);
            System.out.print("Enter the title : ");
            String title;
            while (true) {
                try {
                    title = input.nextLine().trim();
                } catch (Exception e13) {
                    System.out.println("Error reading title. Please enter again: ");
                    input.nextLine();
                    title = input.nextLine().trim();
                }
                if (!containsLetter(title)) {
                    System.out.println("Title must contain at least one alphabet. Enter again:");
                    continue;
                }
                break;
            }
            bookTitles.add(title);
            System.out.print("Enter the author : ");
            String author;
            while (true) {
                try {
                    author = input.nextLine().trim();
                } catch (Exception e14) {
                    System.out.println("Error reading author. Please enter again: ");
                    input.nextLine();
                    author = input.nextLine().trim();
                }
                if (!containsLetter(author)) {
                    System.out.println("Author name must contain at least one alphabet. Enter again:");
                    continue;
                }
                break;
            }
            bookAuthors.add(author);
            System.out.print("Enter total quantity : ");
            int total;
            while (true) {
                try {
                    total = input.nextInt();
                    if (total < 0) {
                        System.out.println("Quantity cannot be negative.");
                        continue;
                    }
                    break;
                } catch (InputMismatchException e15) {
                    System.out.println("Invalid quantity. Enter numbers only.");
                    input.nextLine();
                }
            }
            totalQuantities.add(total);
            availableQuantities.add(total);
            System.out.println("Book '" + title + "' added successfully.");
            saveDataToFiles();
        } catch (Exception e16) {
            System.out.println("Unexpected error occurred while adding the book: " + e16.getMessage());
        }
    }
    /*
     * removeBook()
     * Removes a book from the library.
     * Prompts for book ID.
     * Checks if the book exists and all copies are available before removal.
     * Handles exceptions during data removal.
     */
    public static void removeBook() {
        System.out.println("\n---------------------------------------------");
        System.out.println("               REMOVE BOOK");
        System.out.println("---------------------------------------------\n");

        System.out.print("Enter book ID to remove : ");
        int id;
        while (true) {
            try {
                id = input.nextInt();
                break;
            } catch (InputMismatchException e17) {
                System.out.println("Invalid Book ID. Enter numbers only.");
                input.nextLine();
            }
        }
        input.nextLine();
        int index = findBookIndexByID(id);
        if (index == -1) {
            System.out.println("Book ID not found.");
            return;
        }
        if (availableQuantities.get(index) != totalQuantities.get(index)) {
            System.out.println("Cannot remove this book. Some copies are issued.");
            return;
        }
        try {
            bookIDs.remove(index);
            bookTitles.remove(index);
            bookAuthors.remove(index);
            totalQuantities.remove(index);
            availableQuantities.remove(index);
        } catch (IndexOutOfBoundsException e18) {
            System.out.println("Error removing book data. Data corrupted.");
        }
        saveDataToFiles();
    }
    public static void searchBook() {
        System.out.println("\n---------------------------------------------");
        System.out.println("               SEARCH BOOK");
        System.out.println("---------------------------------------------");

        System.out.println("Search by : \n(1) ID \n(2) Author ");
        int choice;
        while (true) {
            try {
                choice = input.nextInt();
                break;
            } catch (InputMismatchException e19) {
                System.out.println("Invalid option. Enter numbers only.");
                input.nextLine();
            }
        }

        if (choice == 1) {
            System.out.print("Enter book ID: ");
            int id;
            while (true) {
                try {
                    id = input.nextInt();
                    break;
                } catch (InputMismatchException e20) {
                    System.out.println("Invalid Book ID. Enter numbers only.");
                    input.nextLine();
                }
            }
            input.nextLine();
            int index = findBookIndexByID(id);
            if (index != -1) {
                printBookDetails(index);
            } else {
                System.out.println("Book not found");
            }
        } else if (choice == 2) {

            input.nextLine();
            String authorName;
            while (true) {
                System.out.print("Enter Author Name: ");
                authorName = input.nextLine().trim().toLowerCase();
                if (!containsLetter(authorName)) {
                    System.out.println("Invalid input! Author name must contain alphabets. Try again.");
                    continue;
                }
                break;
            }
            boolean found = false;
            for (int i = 0; i < bookAuthors.size(); i++) {
                String storedAuthor = bookAuthors.get(i).toLowerCase();
                if (storedAuthor.contains(authorName)) {
                    printBookDetails(i);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No Books found by that author");
            }
        }
    }
    public static void displayMainMenu() {
        System.out.println("\n=============================================");
        System.out.println("             MAIN MENU");
        System.out.println("=============================================");
        System.out.println("1. Books Management");
        System.out.println("2. Membership Management");
        System.out.println("3. Study Room Management");
        System.out.println("4. Exit System");
        System.out.print("Enter your choice: ");
    }

    public static void processMainMenuChoice(int choice) {
        try {
            switch (choice) {
                case 1:
                    bookMenu();
                    break;
                case 2:
                    // memberMenu();
                    break;
                case 3:
                    // roomMenu();
                    break;
                case 4:
                    System.out.println("Exiting System...");
                    break;
                default:
                    System.out.println("Invalid choice.. Try Again");
            }
        } catch (Exception e10) {
            System.out.println(e10.toString());
        }
    }

    /*
     * main() method
     * Entry point of the Library Management System.
     * Displays the main menu repeatedly until user chooses to exit.
     * Handles input validation for menu choices.
     */
    public static void main(String args[]) {
        loadDataFromFiles();
        displayMainMenu();
        int choice = 0;
        do {
            displayMainMenu();
            while (true) {
                try {
                    choice = input.nextInt();
                    break;
                } catch (InputMismatchException e9) {
                    System.out.print("Invalid input. Please enter numbers only.\nTry again. \nEnter your choice: ");
                    input.nextLine();
                }
            }
            processMainMenuChoice(choice);
        } while (choice != 4);
    }
}
