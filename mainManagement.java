import java.sql.*;
import java.util.Scanner;

public class mainManagement {
    private String activeClass;
    private Connection connection;

    public mainManagement() {
        activeClass = null;
        connection = getConnection();
    }

    private Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/CS410Final?user=root&password=root");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    // Functions for managing class

    public static void main(String[] args) {
        mainManagement shell = new mainManagement();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to class management");

        while (true) {
            System.out.print(">");
            String input = scanner.nextLine();
            String[] tokens = input.trim().split("\\s+");

            switch (tokens[0]) {
                case "new-class":
                    if (tokens.length == 5) {
                        shell.newClass(tokens[1], tokens[2], Integer.parseInt(tokens[3]), tokens[4]);
                    } else {
                        System.out.println("Usage: new-class <courseNumber> <term> <sectionNumber> <description>");
                    }
                    break;
                case "list-classes":
                    shell.listClasses();
                    break;
                case "select-class":
                    if (tokens.length == 2) {
                        shell.selectClass(tokens[1]);
                    } else {
                        System.out.println("Usage: select-class <courseNumber>");
                    }
                    break;
                case "show-class":
                    shell.showClass();
                    break;
                case "show-categories":
                    shell.showCategories();
                    break;
                case "add-category":
                    if (tokens.length == 3) {
                        shell.addCategory(tokens[1], Float.parseFloat(tokens[2]));
                    } else {
                        System.out.println("Usage: add-category <name> <weight>");
                    }
                    break;
                case "show-assignments":
                    shell.showAssignments();
                    break;
                case "add-assignment":
                    if (tokens.length == 5) {
                        shell.addAssignment(tokens[1], tokens[2], tokens[3], Integer.parseInt(tokens[4]));
                    } else {
                        System.out.println("Usage: add-assignment <name> <category> <description> <pointValue>");
                    }
                    break;
                case "add-student":
                    if (tokens.length == 5) {
                        shell.addStudent(tokens[1], tokens[2], tokens[3], tokens[4]);
                    } else if (tokens.length == 2) {
                        shell.addStudent(tokens[1]);
                    } else {
                        System.out.println("Usage: add-student <username> <studentId> <lastName> <firstName>");
                        System.out.println("       add-student <username>");
                    }
                    break;
                case "show-students":
                    if (tokens.length == 1) {
                        shell.showStudents();
                    } else if (tokens.length == 2) {
                        shell.showStudents(tokens[1]);
                    } else {
                        System.out.println("Usage: show-students [<filterString>]");
                    }
                    break;
                case "grade":
                    if (tokens.length == 4) {
                        shell.gradeAssignment(tokens[1], tokens[2], Float.parseFloat(tokens[3]));
                    } else {
                        System.out.println("Usage: grade <assignmentName> <username> <grade>");
                    }
                    break;
                case "student-grades":
                    if (tokens.length == 2) {
                        shell.studentGrades(tokens[1]);
                    } else {
                        System.out.println("Usage: student-grades <username>");
                    }
                    break;
                case "gradebook":
                    shell.gradebook();
                    break;
                case "help":
                    System.out.println("See the available commands");
                    break;
                case "exit":
                    System.out.println("Exiting");
                    return;
                default:
                    System.out.println("Unknown command");
            }
        }
    }
}
