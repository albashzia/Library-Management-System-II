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
    static String BOOKS_DATA_FILE = "books_data.txt";
    static String ISSUED_BOOKS_DATA_FILE = "issued_data.txt";
    static String MEMBERS_DATA_FILE = "members.txt";
    static String ROOMS_DATA_FILE = "rooms.txt";
    static int ROOM_COUNT = 5;
    static int[] roomNumbers = { 101, 102, 103, 104, 105 };
    static String[] roomStatus = { "Available", "Available", "Available", "Available", "Available" };
    static int[] reservedByMemberID = new int[ROOM_COUNT];

    // utility methods to help with different functions
    public static void loadDataFromFiles() {
        try {
            File f = new File(BOOKS_DATA_FILE);
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
            File f = new File(ISSUED_BOOKS_DATA_FILE);
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

    public static void saveDataToFiles() {

        try (PrintWriter pw = new PrintWriter(new FileWriter(BOOKS_DATA_FILE))) {
            for (int i = 0; i < bookIDs.size(); i++) {
                pw.println(
                        bookIDs.get(i) + ","
                                + bookTitles.get(i) + ","
                                + bookAuthors.get(i) + ","
                                + totalQuantities.get(i) + ","
                                + availableQuantities.get(i));
            }
        } catch (Exception e2) {
            System.out.println("Error saving books data.");
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(ISSUED_BOOKS_DATA_FILE))) {
            for (int i = 0; i < issueBookIDs.size(); i++) {
                pw.println(
                        issueBookIDs.get(i) + ","
                                + issueMemberIDs.get(i) + ","
                                + issueDates.get(i));
            }
        } catch (Exception e3) {
            System.out.println("Error saving issued records.");
        }
    }

    public static void loadMembersFromFile() {
        File file = new File(MEMBERS_DATA_FILE);
        if (!file.exists()) {
            return;
        }

        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] parts = line.split(",");
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    memberIDs.add(id);
                    memberNames.add(name);
                } catch (NumberFormatException e4) {
                    System.out.println("Corrupt data found in file, skipping the line.");
                }
            } // while
            //System.out.println("Member records loaded successfully from members.txt");
        } catch (IOException e5) {
            System.out.println("Critical Error: Could not read member file." + e5.toString());
        }
    }// loadMembersFromTheFile

    public static void saveMembersToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(MEMBERS_DATA_FILE))) {
            for (int i = 0; i < memberIDs.size(); i++) {
                writer.println(memberIDs.get(i) + "," + memberNames.get(i));
            }
        } catch (IOException e6) {
            System.out.println("Critical Error: Could not save members to file." + e6.toString());
        }
    }

    public static void loadRoomData() {
        File file = new File(ROOMS_DATA_FILE);
        try {
            if (!file.exists()) {
                for (int i = 0; i < ROOM_COUNT; i++) {
                    roomStatus[i] = "Available";
                    reservedByMemberID[i] = 0;
                }
                return;
            }
            Scanner fileReader = new Scanner(file);
            int i = 0;

            while (fileReader.hasNextLine() && i < ROOM_COUNT) {
                roomStatus[i] = fileReader.nextLine().trim();
                reservedByMemberID[i] = Integer.parseInt(fileReader.nextLine().trim());
                i++;
            }
            fileReader.close();
        } catch (Exception e7) {
            System.out.println("Error loading file data");
        }
    }

    public static void saveRoomData() {
        try {

            PrintStream ps = new PrintStream(ROOMS_DATA_FILE);
            for (int i = 0; i < ROOM_COUNT; i++) {
                ps.println(roomStatus[i]);
                ps.println(reservedByMemberID[i]);

            }
            ps.close();
        } catch (Exception e8) {
            System.out.println("Error saving the file");
        }
    }

    /**
     * ===========================
     * Utility Methods Section
     * ===========================
     * */

    /**
     * Searches for a book ID inside the bookIDs list.
     * Logic:
     * - Iterates through the bookIDs list
     * - Compares each stored ID with the given ID
     * - Returns the index immediately when a match is found
     * - Returns -1 if the ID does not exist in the list
     */
    public static int findBookIndexByID(int id) {
        for (int i = 0; i < bookIDs.size(); i++) {
            if (bookIDs.get(i) == id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Searches for a member ID inside the memberIDs list.
     * Purpose:
     * - Used to quickly locate a member's position in the data structure
     * - Prevents duplicate code wherever member lookup is required
     */
    public static int findMemberIndexByID(int id) {
        for (int i = 0; i < memberIDs.size(); i++) {
            if (memberIDs.get(i) == id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks whether a given string contains at least one alphabetic character.
     * Use case:
     * - Input validation
     * - Ensuring numeric-only fields do not contain letters
     */
    public static boolean containsLetter(String s) {
        for (char c : s.toCharArray()) {
            if (Character.isLetter(c)) {
                return true;
            }
        }
        return false;
    }

    //book related methods
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
                    addBook();
                    break;
                case 2:
                    removeBook();
                    break;
                case 3:
                    searchBook();
                    break;
                case 4:
                    displayAvailableBooks();
                    break;
                case 5:
                    issueBook();
                    break;
                case 6:
                    returnBook();
                    break;
                case 7:
                    showAllIssuedBooks();
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

    /*
     * searchBook()
     * Allows user to search for books by ID or Author.
     * For ID search, displays details if found.
     * For Author search, displays all books by the given author.
     * Handles invalid input and exceptions.
     */
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

    /*
     * printBookDetails(int index)
     * Prints full details of a book at the given index:
     * ID, Title, Author, Total Quantity, Available Quantity.
     * Handles exceptions if index is invalid.
     */
    public static void printBookDetails(int index) {

        System.out.println("\n---------------------------------------------");
        System.out.println("               BOOK DETAILS");
        System.out.println("---------------------------------------------");

        try {
            System.out.println("Book ID : " + bookIDs.get(index));
            System.out.println("Title : " + bookTitles.get(index));
            System.out.println("Author : " + bookAuthors.get(index));
            System.out.println("Total Quantity : " + totalQuantities.get(index));
            System.out.println("Available Quantity : " + availableQuantities.get(index));
        } catch (IndexOutOfBoundsException e39) {
            System.out.println("Error: Tried to print invalid book entry.");
        }
    }

    /*
     * displayAvailableBooks()
     * Displays all books currently available for issuing.
     * Handles exceptions when reading quantities or book details.
     */

    public static void displayAvailableBooks() {
        System.out.println("\n==============================================================================");
        System.out.println("                               AVAILABLE BOOKS");
        System.out.println("==============================================================================");

        System.out.printf("%-10s | %-30s | %-20s | %-10s%n",
                "ID", "Title", "Author", "Available");
        System.out.println("--------------------------------------------------------------------------------");
        boolean any = false;
        for (int i = 0; i < bookIDs.size(); i++) {
            try {
                if (availableQuantities.get(i) > 0) {
                    System.out.printf("%-10d | %-30s | %-20s | %-10d%n",
                            bookIDs.get(i),
                            bookTitles.get(i),
                            bookAuthors.get(i),
                            availableQuantities.get(i));
                    any = true;
                }
            } catch (Exception e22) {
                System.out.println("Error reading available book quantities.");
            }
        }
        if (!any) {
            System.out.println("No books currently available.");
        }
    }

    /*
     * issueBook()
     * Issues a book to a member.
     * Prompts for Book ID, Member ID, and issue date.
     * Validates input and checks book availability.
     * Updates available quantity and adds to issue records.
     * Handles exceptions at every stage.
     */
    public static void issueBook() {
        System.out.println("\n---------------------------------------------");
        System.out.println("                ISSUE BOOK");
        System.out.println("---------------------------------------------");

        try {
            System.out.print("Enter Book ID to issue: ");
            int bookId = 0;
            while (true) {
                try {
                    bookId = input.nextInt();
                    break;
                } catch (InputMismatchException e23) {
                    System.out.println("Invalid Book ID. Enter numbers only.");
                    input.nextLine();
                    System.out.print("Enter Book ID to issue: ");
                }
            }
            System.out.print("Enter Member ID: ");
            int memberId = 0;
            while (true) {
                try {
                    memberId = input.nextInt();
                    break;
                } catch (InputMismatchException e24) {
                    System.out.println("Invalid Member ID. Enter numbers only.");
                    input.nextLine();
                    System.out.print("Enter Member ID: ");
                }
            }
            input.nextLine();
            int bookIndex = findBookIndexByID(bookId);
            int memberIndex = findMemberIndexByID(memberId);
            if (bookIndex == -1 || memberIndex == -1) {
                System.out.println("Book or Member ID not found.");
                return;
            }
            try {
                if (availableQuantities.get(bookIndex) <= 0) {
                    System.out.println("All copies are issued.");
                    return;
                }
            } catch (IndexOutOfBoundsException e25) {
                System.out.println("Data error: Book index out of range.");
                return;
            }
            try {
                availableQuantities.set(bookIndex, availableQuantities.get(bookIndex) - 1);
            } catch (Exception e26) {
                System.out.println("Error updating book quantity.");
                return;
            }
            issueBookIDs.add(bookId);
            issueMemberIDs.add(memberId);
            String date;

            while (true) {
                System.out.print("Enter today's Date (YYYY-MM-DD): ");

                try {
                    date = input.nextLine().trim();
                } catch (Exception e27) {
                    System.out.println("Error reading input. Try again:");
                    input.nextLine(); // clear buffer
                    continue;
                }

                // Very simple format check: length + hyphens
                if (date.length() == 10
                        && date.charAt(4) == '-'
                        && date.charAt(7) == '-') {

                    break;
                }

                System.out.println("Invalid format. Example: 2025-01-14");
            }

            issueDates.add(date);

            try {
                System.out.println(bookTitles.get(bookIndex) + "' issued to member " + memberId);
            } catch (IndexOutOfBoundsException e28) {
                System.out.println("Error retrieving book title for display.");
            }
        } catch (Exception e29) {
            System.out.println("Unexpected error while issuing book: " + e29.getMessage());
        }
        saveDataToFiles();
    }

    /*
     * returnBook()
     * Returns a previously issued book.
     * Prompts for Book ID and Member ID.
     * Validates if the book was issued to the member.
     * Updates available quantities and removes issue record.
     * Handles exceptions during processing.
     */
    public static void returnBook() {
        System.out.println("\n---------------------------------------------");
        System.out.println("                RETURN BOOK");
        System.out.println("---------------------------------------------");

        try {
            int bookId = 0, memberId = 0;
            while (true) {
                try {
                    System.out.print("Enter Book ID to return: ");
                    bookId = input.nextInt();
                    break;
                } catch (InputMismatchException e30) {
                    System.out.println("Invalid Book ID. Numbers only.");
                    input.nextLine();
                }
            }
            while (true) {
                try {
                    System.out.print("Enter Member ID: ");
                    memberId = input.nextInt();
                    break;
                } catch (InputMismatchException e31) {
                    System.out.println("Invalid Member ID. Numbers only.");
                    input.nextLine();
                }
            }
            input.nextLine();
            int bookIndex = -1;
            try {
                bookIndex = findBookIndexByID(bookId);
            } catch (Exception e32) {
                System.out.println("Error while searching Book ID: " + e32.getMessage());
                return;
            }

            if (bookIndex == -1) {
                System.out.println("Book ID not found.");
                return;
            }
            int issueIndex = -1;
            try {
                for (int i = 0; i < issueBookIDs.size(); i++) {
                    if (issueBookIDs.get(i) == bookId && issueMemberIDs.get(i) == memberId) {
                        issueIndex = i;
                        break;
                    }
                }
            } catch (Exception e33) {
                System.out.println("Unexpected error while checking issued records: " + e33.getMessage());
                return;
            }

            if (issueIndex == -1) {
                System.out.println("This book was not issued to this member.");
                return;
            }
            try {
                issueBookIDs.remove(issueIndex);
                issueMemberIDs.remove(issueIndex);
                issueDates.remove(issueIndex);
            } catch (Exception e34) {
                System.out.println("Error removing issue records: " + e34.getMessage());
                return;
            }
            try {
                availableQuantities.set(bookIndex, availableQuantities.get(bookIndex) + 1);
            } catch (Exception e35) {
                System.out.println("Error updating available quantity: " + e35.getMessage());
                return;
            }
            try {
                System.out.println(bookTitles.get(bookIndex) + "' returned by member " + memberId);
            } catch (Exception e36) {
                System.out.println("Book returned, but error while displaying title.");
            }
        } catch (Exception e37) {
            System.out.println("Unexpected error occurred: " + e37.getMessage());
        }
        saveDataToFiles();
    }

    /*
     * showAllIssuedBooks()
     * Displays all issued books with:
     * Issue number, Member ID, Book Title, and Date of issue.
     * Handles exceptions if book data is missing or corrupt.
     */
    public static void showAllIssuedBooks() {

        System.out.println("\n=============================================");
        System.out.println("             ALL ISSUED BOOKS");
        System.out.println("=============================================");

        if (issueBookIDs.isEmpty()) {
            System.out.println("No books are issued.");
            return;
        }
        System.out.printf("%-10s | %-15s | %-30s | %-15s%n", "Issue #", "Member ID", "Book Title", "Date");
        System.out.println("--------------------------------------------------------------------");
        for (int i = 0; i < issueBookIDs.size(); i++) {
            int bookIndex = findBookIndexByID(issueBookIDs.get(i));
            String title;
            try {
                title = bookTitles.get(bookIndex);
            } catch (Exception e38) {
                title = "Unknown (Data error)";
            }

            System.out.printf("%-10d | %-15d | %-30s | %-15s%n", i + 1, issueMemberIDs.get(i), title,
                    issueDates.get(i));
        }
    }

    //member methods
    public static void memberMenu() {
        int choice;
        while (true) {
            System.out.println("\n=============================================");
            System.out.println("          MEMBERSHIP MANAGEMENT");
            System.out.println("=============================================");
            System.out.println("1. Register a new member");
            System.out.println("2. Display all members");
            System.out.println("3. Search a member by ID or by Name");
            System.out.println("4. View Books issued by a member");
            System.out.println("5. Remove a member");
            System.out.println("6. Back to the Main Menu");
            System.out.print("Enter your choice: ");
            try {
                choice = input.nextInt();
                input.nextLine();
            } catch (InputMismatchException e39) {
                System.out.println("You have entered an invalid choice!!");
                System.out.println("Try again");
                input.nextLine();
                continue;
            } // catch
            try {
                switch (choice) {
                    case 1:
                        registerNewMember();
                        break;
                    case 2:
                        displayAllMembers();
                        break;
                    case 3:
                        searchMember();
                        break;
                    case 4:
                        viewBooksIssuedByMember();
                        break;
                    case 5:
                        removeMember();
                        break;
                    case 6:
                        System.out.println("Returning to main menu..");
                        return;
                    default:
                        System.out.println("Invalid choice");

                }// switch
            } // try
            catch (Exception e40) {
                System.out.println("Unexpected Error: " + e40.getMessage());
            } // catch
        } // while
    }// memberMenu

    /*
     * registerNewMember()
     * Registers a new library member by:
     * - Validating a unique 4-digit numeric member ID
     * - Accepting and validating the member's name
     * - Adding the member data to in-memory lists
     * - Persisting member data to file storage
     * Handles invalid input, duplicate IDs, and allows cancellation.
     */
    public static void registerNewMember() {
        System.out.println("\n---------------------------------------------");
        System.out.println("           REGISTER NEW MEMBER");
        System.out.println("---------------------------------------------\n");

        int ID;
        while (true) {
            try {
                System.out.println("Enter ID of the new member (recommended 4 digit) or 0 to cancel:");
                if (!input.hasNextInt()) {
                    System.out.println("Member ID must be numeric..Try again");
                    input.nextLine();
                    continue;
                }
                ID = input.nextInt();
                input.nextLine();
                if (ID == 0) {
                    System.out.println("Registration cancelled.");
                    return;
                }
                if (ID < 1000 || ID > 9999) {
                    System.out.println("You have entered an invalid ID. Use a 4-digit ID between 1000 and 9999.");
                    continue;
                }
                if (findMemberIndexByID(ID) != -1) {
                    System.out.println("Member ID already exists.");
                    continue;
                }
                break;
            } catch (Exception e41) {
                System.out.println("Unexpected error: " + e41.getMessage());
                input.nextLine();
            }
        }
        String name;
        while (true) {
            System.out.println("Enter the name of the member: ");
            name = input.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty. Please enter a valid name.");
                continue;
            }
            break;
        }
        memberIDs.add(ID);
        memberNames.add(name);
        System.out.println("Member " + name + " has been registered with ID: " + ID);
        saveMembersToFile();
    }// registerNewMember

    /*
     * searchMember()
     * Searches for an existing member by:
     * - Member ID
     * - Member Name (case-insensitive)
     * Provides options to cancel the search.
     * Displays member details if found and handles invalid input gracefully.
     */
    public static void searchMember() {
        System.out.println("\n---------------------------------------------");
        System.out.println("               SEARCH MEMBER");
        System.out.println("---------------------------------------------");

        while (true) {
            try {
                System.out.println("Search a member by \n 1.ID \n 2.Name \n 3.Cancel Search");
                String searchInput = input.nextLine().trim();
                int search;
                try {
                    search = Integer.parseInt(searchInput);
                } catch (NumberFormatException e42) {
                    System.out.println("Invalid input. Please enter a number (1, 2, or 3).");
                    continue;
                }
                if (search == 1) {
                    System.out.print("Enter the ID of the member: ");
                    if (!input.hasNextInt()) {
                        System.out.println("Invalid input. ID must be a number.");
                        input.nextLine();
                        continue;
                    }
                    int ID = input.nextInt();
                    input.nextLine();
                    int index = findMemberIndexByID(ID);
                    if (index != -1) {
                        System.out.println(
                                "\n Found member ID " + memberIDs.get(index) + " Name: " + memberNames.get(index));
                    } else {
                        System.out.println("Member ID not found");
                    }
                    break;
                } else if (search == 2) {
                    System.out.print("Enter the name of the member: ");
                    String name = input.nextLine().trim();
                    System.out.println("Searching member with name : " + name);
                    boolean found = false;
                    for (int i = 0; i < memberIDs.size(); i++) {
                        if (memberNames.get(i).equalsIgnoreCase(name)) {
                            System.out.println("Member Found");
                            System.out.println("Id: " + memberIDs.get(i) + ", Name: " + memberNames.get(i));
                            found = true;
                        }
                    } // for
                    if (!found) {
                        System.out.println("No member found");
                    }
                    break;
                } else if (search == 3) {
                    System.out.println("Search cancelled");
                    break;
                } else {
                    System.out.println("Invalid Option. Please choose 1, 2, or 3.");
                }
            } catch (InputMismatchException e43) {
                System.out.println("Invalid input.. Try Again");
                input.nextLine();
            }
        } // while
    }// searchMember

    /*
     * displayAllMembers()
     * Displays all registered members in a formatted table showing:
     * - Member ID
     * - Member Name
     * Handles empty member list and runtime exceptions during display.
     * Allows retry in case of display failure.
     */
    public static void displayAllMembers() {
        System.out.println("--Displaying All Members--");
        if (memberIDs.isEmpty()) {
            System.out.println("No members registered");
            return;
        }
        boolean success = false;
        do {
            success = true;
            try {
                System.out.println("\n=============================================");
                System.out.println("              ALL REGISTERED MEMBERS");
                System.out.println("=============================================\n");

                System.out.printf("%-15s | %-30s%n", "Member ID", "Name");
                System.out.println("-------------------------------------");
                for (int i = 0; i < memberIDs.size(); i++) {
                    System.out.printf("%-15d | %-30s%n", memberIDs.get(i), memberNames.get(i));
                } // for
            } catch (Exception e44) {
                System.out.println("Error: " + e44.getMessage());
                success = false;
            }
            if (!success) {
                System.out.println("Display failed");
                System.out.print("Do you want to try again? (Type 'y' to retry or 'n' to exit): ");
                String response = input.nextLine();
                if (!response.trim().equalsIgnoreCase("y")) {
                    System.out.println("Display attempt aborted by user.");
                    break;
                }
            }
        } while (!success);
        if (success) {
            System.out.println("\nDisplay process completed successfully.");
        }
    }// displayAll


    /*
     * viewBooksIssuedByMember()
     * Displays all books issued to a specific member by:
     * - Validating the member ID
     * - Retrieving issued book IDs linked to the member
     * - Displaying Book ID, Book Title, and Issue Date
     * Handles cases where:
     * - Member ID does not exist
     * - Member has no issued books
     * - Book data is missing or inconsistent
     * Allows user to cancel the operation.
     */
    public static void viewBooksIssuedByMember() {
        System.out.println("\n---------------------------------------------");
        System.out.println("        BOOKS ISSUED TO A MEMBER");
        System.out.println("---------------------------------------------\n");

        while (true) {
            try {
                System.out.print("Enter ID of the member (or enter 0 to exit): ");
                if (!input.hasNextInt()) {
                    System.out.println("Invalid input.. Enter integer as an ID.");
                    input.nextLine();
                    continue;
                }
                int id = input.nextInt();
                input.nextLine();
                if (id == 0) {
                    System.out.println("Operation cancelled!");
                    break;
                }
                int index = findMemberIndexByID(id);
                if (index == -1) {
                    System.out.println("Member ID not found");
                    continue;
                }
                System.out.println("\nBooks issued to " + memberNames.get(index) + " (ID: " + id + ")");
                System.out.printf("%-15s | %-30s | %-20s%n", "Book ID", "Title", "Issue Date");
                boolean found = false;
                for (int i = 0; i < issueBookIDs.size(); i++) {
                    if (issueMemberIDs.get(i) == id) {
                        int bookIndex = findBookIndexByID(issueBookIDs.get(i));
                        String title;
                        if (bookIndex == -1 || bookIndex >= bookTitles.size() || bookTitles.get(bookIndex) == null) {
                            title = "UNKNOWN BOOK";
                        } else {
                            title = bookTitles.get(bookIndex);
                        }

                        System.out.printf("%-10d | %-30s | %-15s%n", issueBookIDs.get(i), title, issueDates.get(i));
                        found = true;
                    }
                }
                if (!found) {
                    System.out.println("\nThis member having ID (" + id + ") has no issued books.");
                }

                System.out.println("\nView operation completed successfully.");
                return;
            } catch (Exception e45) {
                System.out.println("Unexpected error: " + e45.getMessage());
                System.out.print("Do you want to try again? (Enter 'y' for yes and 'n' for no): ");
                String answer = input.nextLine();
                if (!answer.trim().equalsIgnoreCase("y")) {
                    System.out.println("Operation Cancelled");
                    break;
                }
            } // catch
        } // while
    }// BooksIssuedToMember

    /*
     * removeMember()
     * Removes a registered member from the system by:
     * - Validating the member ID
     * - Ensuring the member has no outstanding issued books
     * - Deleting member details from in-memory records
     * Prevents removal if the member has unreturned books.
     * Handles invalid input and allows cancellation.
     */
    public static void removeMember() {
        System.out.println("\n---------------------------------------------");
        System.out.println("               REMOVE MEMBER");
        System.out.println("---------------------------------------------");

        while (true) {
            try {
                System.out.print("Enter Member ID(4 digit) to remove (or 0 to cancel): ");
                if (!input.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a numeric ID.");
                    input.nextLine();
                    continue;
                }
                int id = input.nextInt();
                input.nextLine();
                if (id == 0) {
                    System.out.println("Operation cancelled.");
                    break;
                }
                int index = findMemberIndexByID(id);
                if (index == -1) {
                    System.out.println("Member ID not found. Try again.");
                    continue;
                }
                boolean hasIssues = false;
                for (int i = 0; i < issueBookIDs.size(); i++) {
                    if (issueMemberIDs.get(i) == id) {
                        hasIssues = true;
                        break;
                    }
                }
                if (hasIssues) {
                    System.out.println("Cannot remove member: outstanding books must be returned first.");
                    continue;
                }
                memberIDs.remove(index);
                memberNames.remove(index);
                // saveMembersToFile();
                System.out.println("Member ID " + id + " removed successfully.");
                break;
            } catch (InputMismatchException e46) {
                System.out.println("\nInput Error: Member ID must be a numeric integer.");
                input.nextLine();
            } catch (Exception e47) {
                System.out.println("Unexpected error: " + e47.getMessage());
            }
        } // while
    }// remove member

    // room management methods
    public static void roomMenu() {
        int choice = 0;
        do {
            while (true) {
                System.out.println("\n=============================================");
                System.out.println("          STUDY ROOM MANAGEMENT");
                System.out.println("=============================================");
                System.out.println("  1. Show Available Study Rooms");
                System.out.println("  2. Reserve A Study Room");
                System.out.println("  3. Cancel Reservation");
                System.out.println("  4. Check Reservation Status");
                System.out.println("  5. Back to main menu");
                System.out.println();
                System.out.print("Enter your choice: ");
                try {
                    choice = input.nextInt();
                    if (choice < 1 || choice > 5) {
                        System.out.println("Invalid choice please enter a number between 1-5");
                        continue;
                    } // end if
                    break;
                } // try
                catch (InputMismatchException e48) {
                    System.out.println("Invalid input! Please enter a number");
                    System.out.println("Try again");
                    input.nextLine();
                    continue;
                } // end catch
            }
            switch (choice) {
                case 1:
                    showRoom();
                    break;
                case 2:
                    reserveRoom();
                    break;
                case 3:
                    cancelReservation();
                    break;
                case 4:
                    checkReservationStatus();
                    break;
                case 5:
                    System.out.println("Exiting Study Room Booking System...");
                    break;
            }
        } while (choice != 5);
    }

    public static void showRoom() {
        System.out.println("\n=============================================");
        System.out.println("            STUDY ROOM AVAILABILITY");
        System.out.println("=============================================");

        System.out.printf("%-12s | %-12s | %-12s\n", "Room Number", "Status", "Reserved By ");
        System.out.println("==========================================");

        for (int i = 0; i < roomNumbers.length; i++) {

            String memberIdDisplay = (reservedByMemberID[i] == 0) ? "N/A" : String.valueOf(reservedByMemberID[i]);
            System.out.printf("%-12d | %-12s | %-12s\n",
                    roomNumbers[i],
                    roomStatus[i],
                    memberIdDisplay);
        }
    }

    public static void reserveRoom() {
        System.out.println("\n---------------------------------------------");
        System.out.println("             RESERVE A STUDY ROOM");
        System.out.println("---------------------------------------------\n");

        int searchRoom = 0;
        int memberId = 0;
        int index = -1;
        while (true) {
            try {
                System.out.println("Enter room you want to reserve");
                searchRoom = input.nextInt();
                // checking the room number
                boolean roomExists = false;
                for (int r : roomNumbers) {
                    if (r == searchRoom) {
                        roomExists = true;
                        break;
                    }

                }

                if (!roomExists) {
                    System.out.println("Invalid room number! Try Again");
                    continue;
                }

                index = -1;
                for (int i = 0; i < roomNumbers.length; i++) {
                    if (roomNumbers[i] == searchRoom) {
                        index = i;
                        break;
                    }
                }
                if (!roomStatus[index].equals("Available")) {

                    System.out.println(
                            "The room " + searchRoom + " is  already reserved  by member " + reservedByMemberID[index]);
                    return;
                }
                break;
            } catch (InputMismatchException e49) {
                System.out.println("Invalid input! Enter a number");
                input.nextLine();
            }
        }
        while (true) {
            try {

                System.out.println("Enter member ID:");
                memberId = input.nextInt();
                int memberIndex = findMemberIndexByID(memberId);
                if (memberIndex == -1) {
                    System.out.println("Member does not exist");
                    continue;
                }
                System.out.println("Member found!");
                break;

            } catch (InputMismatchException e50) {
                System.out.println("Invalid input ! Enter a number for member ID");
                input.nextLine();
            }
        } // end
        roomStatus[index] = "Reserved";
        reservedByMemberID[index] = memberId;

        saveRoomData();
        System.out.println("The room " + searchRoom + " is reserved successfully by member " + memberId);

    }

    public static void cancelReservation() {
        System.out.println("\n---------------------------------------------");
        System.out.println("             CANCEL RESERVATION");
        System.out.println("---------------------------------------------");

        int memberId = 0;
        int cancelRoom = 0;
        int index = -1;
        while (true) {
            try {

                System.out.println("Enter a room to cancel reservation");
                cancelRoom = input.nextInt();

                index = -1;
                for (int i = 0; i < roomNumbers.length; i++) {

                    if (roomNumbers[i] == cancelRoom) {
                        index = i;

                        break;

                    }
                }

                if (index == -1) {
                    System.out.println("Room number not found");
                    continue;

                }
                break;
            } catch (InputMismatchException e51) {
                System.out.println("Invalid Input ! Enter a number.");

                input.nextLine();
            }
        }
        while (true) {
            try {

                System.out.println("Enter Member ID to cancel Reservation");
                memberId = input.nextInt();
                int memberIndex = findMemberIndexByID(memberId);
                if (memberIndex == -1) {
                    System.out.println("Member does not exist");
                    continue;
                }

                break;

            } catch (InputMismatchException e52) {
                System.out.println("Invalid input ! Enter a number for member ID");
                input.nextLine();
            } // end catch
        }
        if (roomStatus[index].equals("Available")) {
            System.out.println("The room" + cancelRoom + "is already available. No need to cancel it.");
        } else if (reservedByMemberID[index] != memberId) {
            System.out.println("The room was reserved by different member");
        } else {
            roomStatus[index] = "Available";

            reservedByMemberID[index] = 0;
            saveRoomData();
            System.out.println("Reservation for room " + cancelRoom + " cancelled!");

        }
    }

    public static void checkReservationStatus() {
        System.out.println("\n---------------------------------------------");
        System.out.println("              ROOM STATUS");
        System.out.println("---------------------------------------------\n");

        int RoomNum = 0;
        while (true) {
            try {

                System.out.println("Enter room number to check status");
                RoomNum = input.nextInt();
                boolean roomExists = false;
                for (int r : roomNumbers) {
                    if (r == RoomNum) {
                        roomExists = true;
                        break;
                    }
                }
                if (!roomExists) {
                    System.out.println("The room number does not exist");
                    continue;
                }
                break;
            } catch (InputMismatchException e53) {
                System.out.println("Invalid input ! Enter a number.");
                input.nextLine();
            }
        }
        int index = -1;
        for (int i = 0; i < roomNumbers.length; i++) {
            if (roomNumbers[i] == RoomNum) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            System.out.println("Invalid Room Number");
            return;
        }
        System.out.println("==== ROOM STATUS ====");
        System.out.println("Room Number: " + RoomNum);
        System.out.println("Room status: " + roomStatus[index]);
        System.out.println("Reserved By: " + (reservedByMemberID[index] == 0 ? "N/A" : reservedByMemberID[index]));

    }

    /*
     * displayMainMenu()
     * Displays the main menu of the system, listing the main modules.
     */
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

    /*
     * processMainMenuChoice(int choice)
     * Processes the user's selection from the main menu.
     */
    public static void processMainMenuChoice(int choice) {
        try {
            switch (choice) {
                case 1:
                    bookMenu();
                    break;
                case 2:
                    memberMenu();
                    break;
                case 3:
                    roomMenu();
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
        loadMembersFromFile();
        loadRoomData();
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
