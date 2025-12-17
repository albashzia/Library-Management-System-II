import java.util.*;
import java.io.*;
public class LMS
{
  static ArrayList<Integer> bookIDs = new ArrayList<>();
  static ArrayList<String> titles = new ArrayList<>();
  static ArrayList<String> authors = new ArrayList<>();
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

  public static void main(String args[])
  {
    
  }
}
