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

    public void newClass(String courseNumber, String term, int sectionNumber, String description) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();
            String sql = "insert into Classes(course_number, term, section_number, description) values (" + courseNumber
                    + ", " + term + ", " + sectionNumber + ", " + description + ")";
            sqlStatement.execute(sql);
        } catch (SQLException sqlException) {
            System.out.println("Failed to create class");
            System.out.println(sqlException.getMessage());
        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public void listClasses() {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();
            String sql = "select c.course_number, c.term, c.section_number, c.description, count(e.student_id) from Classes as c left join Enrollments as e on c.class_id = e.class_id group by e.class_id";
            System.out.println("Course Number | Term | Section Number | Description | # of Students");
            System.out.println("-".repeat(80));
            ResultSet results = sqlStatement.executeQuery(sql);
            results.next();
            while (!results.isAfterLast()) {
                System.out.println(results.getString(1) + " | " + results.getString(2) + " | " + results.getInt(3)
                        + " | " + results.getString(4) + " | " + results.getInt(5));
                results.next();
            }
        } catch (SQLException sqlException) {
            System.out.println("Failed to list classes");
            System.out.println(sqlException.getMessage());
        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public void selectClass(String courseNumber) {
        selectClass(courseNumber, "Sp24");
    }

    public void selectClass(String courseNumber, String term) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();
            String sql = "select * from Classes where course_number = " + courseNumber + " and term = " + term + ";";
            ResultSet results = sqlStatement.executeQuery(sql);

            int size = 0;
            if (results != null) {
                results.last(); // moves cursor to the last row
                size = results.getRow(); // get row id
            } else {
                throw new SQLException("Class does not exist");
            }
            if (size > 1) {
                throw new SQLException("Multiple classes exist. Please change term or specify section.");
            }
            results.first();
            activeClass = results.getString(1);
        } catch (SQLException sqlException) {
            System.out.println("Failed to select class");
            System.out.println(sqlException.getMessage());
        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public void selectClass(String courseNumber, String term, int sectionNumber) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();
            String sql = "select * from Classes where course_number = " + courseNumber + " and term = " + term
                    + " and section_number = " + sectionNumber + ";";
            ResultSet results = sqlStatement.executeQuery(sql);

            if (results == null) {
                throw new SQLException("Class does not exist");
            }
            results.next();
            activeClass = results.getString(1);
        } catch (SQLException sqlException) {
            System.out.println("Failed to select class");
            System.out.println(sqlException.getMessage());
        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public void showClass() {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            if (activeClass == null) {
                throw new SQLException("Please select a class.");
            }
            connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();
            String sql = "select * from Classes where class_id = " + activeClass;
            ResultSet results = sqlStatement.executeQuery(sql);

            if (results == null) {
                throw new SQLException("Class doesn't exist.");
            }

            System.out.println(results.getString(1) + " | " + results.getString(2) + " | " + results.getInt(3) + " | "
                    + results.getString(4));
        } catch (SQLException sqlException) {
            System.out.println("Failed to show class");
            System.out.println(sqlException.getMessage());
        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public void showCategories() {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            if (activeClass == null) {
                throw new SQLException("Please select a class.");
            }
            connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();
            String sql = "select name, weight from Categories where class_id = " + activeClass;
            ResultSet results = sqlStatement.executeQuery(sql);

            if (results == null) {
                throw new SQLException("Class doesn't exist.");
            }

            System.out.println(results.getString(1) + " | " + results.getFloat(2));
        } catch (SQLException sqlException) {
            System.out.println("Failed to show categories");
            System.out.println(sqlException.getMessage());
        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public void addCategory(String name, float weight) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            if (activeClass == null) {
                throw new SQLException("Please select a class.");
            }
            connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();
            String sql = "insert into Categories(class_id, name, weight) values (" + activeClass + ", " + name + ", " + weight + ")";
            sqlStatement.execute(sql);
        } catch (SQLException sqlException) {
            System.out.println("Failed to create category");
            System.out.println(sqlException.getMessage());
        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public void showAssignments() {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            if (activeClass == null) {
                throw new SQLException("Please select a class.");
            }
            connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();
            String sql = "select name, point_value from Assignments where class_id = " + activeClass + "group by category_id";
            ResultSet results = sqlStatement.executeQuery(sql);

            if (results == null) {
                throw new SQLException("Class doesn't exist.");
            }

            System.out.println(results.getString(1) + " | " + results.getInt(2));
        } catch (SQLException sqlException) {
            System.out.println("Failed to show assignments");
            System.out.println(sqlException.getMessage());
        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public void addAssignment(String name, String category, String description, int points) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            if (activeClass == null) {
                throw new SQLException("Please select a class.");
            }
            connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();
            String sql = "insert into Assignments(class_id, category_id, name, description, point_value) values (" + activeClass + ", " + "(select category_id from Categories where name = " + category + "), " + name + ", " + description + ", " + points + ")";
            sqlStatement.execute(sql);
        } catch (SQLException sqlException) {
            System.out.println("Failed to create assignment");
            System.out.println(sqlException.getMessage());
        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

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
                    } else if (tokens.length == 3) {
                        shell.selectClass(tokens[1], tokens[2]);
                    } else if (tokens.length == 4) {
                        shell.selectClass(tokens[1], tokens[2], Integer.parseInt(tokens[3]));
                    } else {
                        System.out.println("Usage: select-class <courseNumber>");
                        System.out.println("       select-class <courseNumber> <term>");
                        System.out.println("       select-class <courseNumber> <term> <sectionNumber");
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
