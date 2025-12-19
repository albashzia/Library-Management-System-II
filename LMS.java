import java.util.*;
import java.io.*;
public class LMS
{
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
  static int[] roomNumbers = {101, 102, 103, 104, 105};
  static String[] roomStatus = {"Available", "Available", "Available", "Available", "Available"};
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
                    //bookMenu();
                    break;
                case 2:
                    //memberMenu();
                    break;
                case 3:
                    //roomMenu();
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
  public static void main(String args[])
  {
    loadDataFromFiles();
    displayMainMenu();
    int choice = 0;
    
  }
}
