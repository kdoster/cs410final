import javax.xml.transform.Result;
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
            conn = Database.getDatabaseConnection();
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
            //System.out.println("-".repeat(80));
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

    private void addStudent(String username, String studentID, String LastName, String FirstName) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            if (activeClass == null) {
                throw new SQLException("Please select a class");
            }
            connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();

            //Have to check if the student exists already
            String sql = "SELECT * FROM Students WHERE id = '" + studentID + "'";
            ResultSet resultSet = sqlStatement.executeQuery(sql);

            if (resultSet.next()) {
                String storedFirstName = resultSet.getString("firstName");
                String storedLastName = resultSet.getString("lastName");
                if (!storedFirstName.equals(FirstName) || !storedLastName.equals(LastName)) {
                    System.out.println("WARNING: Name is being changed");
                    String sqlUpdateName = "UPDATE students SET last_name = '" + LastName + "', first_name = '" + FirstName + "' WHERE username = '" + username + "'";
                    sqlStatement.executeUpdate(sqlUpdateName);
                }
            } else {
                String sqlInsert = "INSERT INTO students (username, student_id, last_name, first_name) VALUES ('" + username + "', '" + studentID + "', '" + LastName + "', '" + FirstName + "')";
                sqlStatement.executeUpdate(sqlInsert);
                String sqlEnroll = "INSERT INTO Enrolled (student_id, class_id) VALUES (" + studentID + ", " + activeClass + ");";
            }

        } catch(SQLException sqlException) {
            System.out.println("Failed to add student");
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

    private void addStudent(String username) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            if (activeClass == null) {
                throw new SQLException("Please select a class");
            }
            connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();

            String sqlCheck = "SELECT * FROM Students WHERE username = '" + username + "'";
            ResultSet resultSet = sqlStatement.executeQuery(sqlCheck);

            if (resultSet.next()) {
                // Student exists
                int student_id = resultSet.getInt("student_id");
                String sqlEnroll = "INSERT INTO Enrolled (student_id, class_id) VALUES (" + student_id + ", " + activeClass + ");";
                sqlStatement.executeUpdate(sqlEnroll);
            } else {
                System.out.println("ERROR: Student with that username does not exist");
            }

        } catch(SQLException sqlException) {
            System.out.println("Failed to add student");
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


    private void showStudents() {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            if (activeClass == null) {
                throw new SQLException("Please select a class");
            }
            connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();

            String sqlQuery = "SELECT s.username, s.firstName, s.lastName FROM Students s " +
                    "JOIN Enrolled e ON s.student_id = e.student_id " +
                    "WHERE e.class_id = " + activeClass;
            ResultSet resultSet = sqlStatement.executeQuery(sqlQuery);

            System.out.println("Students in the current class:");
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                System.out.println("Username: " + username + ", Name: " + firstName + " " + lastName);
            }

        } catch(SQLException sqlException) {
            System.out.println("Failed to show students");
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

    private void showStudents(String searchString) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            if (activeClass == null) {
                throw new SQLException("Please select a class");
            }
            connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();

            // Retrieve all students matching the search string in their name or username
            String sqlQuery = "SELECT username, firstName, lastName FROM Students " +
                    "WHERE LOWER(username) LIKE '%" + searchString.toLowerCase() + "%' " +
                    "OR LOWER(firstName) LIKE '%" + searchString.toLowerCase() + "%' " +
                    "OR LOWER(lastName) LIKE '%" + searchString.toLowerCase() + "%'";
            ResultSet resultSet = sqlStatement.executeQuery(sqlQuery);

            // Display the students
            System.out.println("Students matching '" + searchString + "':");
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                System.out.println("Username: " + username + ", Name: " + firstName + " " + lastName);
            }

        } catch(SQLException sqlException) {
            System.out.println("Failed to show students");
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

    private void gradeAssignment(String assignmentName, String username, float grade) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            if (activeClass == null) {
                throw new SQLException("Please select a class");
            }
            connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();

            // Grade in sql
            String sqlAssignment = "SELECT a.assignment_id, a.point_value " +
                    "FROM Assignments a " +
                    "JOIN Classes c ON a.class_id = c.class_id " +
                    "WHERE a.name = '" + assignmentName + "' AND c.class_id = " + activeClass;
            ResultSet assignmentResultSet = sqlStatement.executeQuery(sqlAssignment);

            if (assignmentResultSet.next()) {
                int assignmentId = assignmentResultSet.getInt("assignment_id");
                int maxPoints = assignmentResultSet.getInt("point_value");

                if (grade > maxPoints) {
                    System.out.println("The grade exceeds the maximum points for the assignment (" + maxPoints + " points)");
                }

                // Check if the student already has a grade for this assignment
                String sqlCheckGrade = "SELECT * FROM Grades WHERE student_id = (SELECT student_id FROM Students WHERE username = '" + username + "') AND assignment_id = " + assignmentId;
                ResultSet gradeResultSet = sqlStatement.executeQuery(sqlCheckGrade);

                if (gradeResultSet.next()) {
                    // Student already has a grade for this assignment, update it
                    String sqlUpdateGrade = "UPDATE Grades SET grade = " + grade + " WHERE student_id = (SELECT student_id FROM Students WHERE username = '" + username + "') AND assignment_id = " + assignmentId;
                    sqlStatement.executeUpdate(sqlUpdateGrade);
                } else {
                    // Student does not have a grade for this assignment, insert a new record
                    String sqlInsertGrade = "INSERT INTO Grades (student_id, assignment_id, grade) VALUES ((SELECT student_id FROM Students WHERE username = '" + username + "'), " + assignmentId + ", " + grade + ")";
                    sqlStatement.executeUpdate(sqlInsertGrade);
                }
            } else {
              System.out.println("ERROR: Assignment was not found in the class");
            }

        } catch(SQLException sqlException) {
            System.out.println("Failed to show students");
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

    private void studentGrades(String username) {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            if (activeClass == null) {
                throw new SQLException("Please select a class");
            }
            connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();

            // Query to retrieve the student's current grade
            String sqlQuery = "SELECT c.name AS category, a.name AS assignment, a.point_value AS max_points, " +
                    "IFNULL(g.grade, 0) AS grade " +
                    "FROM Categories c " +
                    "LEFT JOIN Assignments a ON c.category_id = a.category_id " +
                    "LEFT JOIN (SELECT assignment_id, grade FROM Grades WHERE student_id = " +
                    "(SELECT student_id FROM Students WHERE username = '" + username + "')) g " +
                    "ON a.assignment_id = g.assignment_id " +
                    "WHERE c.class_id = " + activeClass + " " +
                    "ORDER BY c.category_id, a.assignment_id";

            ResultSet resultSet = sqlStatement.executeQuery(sqlQuery);

            // Display the student's current grade
            System.out.println("Student's Current Grade for " + activeClass + ":");
            float totalPoints = 0;
            float totalMaxPoints = 0;
            float totalAttemptedPoints = 0;
            String currentCategory = null;
            float categoryTotalPoints = 0;
            float categoryMaxPoints = 0;

            while (resultSet.next()) {
                String category = resultSet.getString("category");
                String assignment = resultSet.getString("assignment");
                float grade = resultSet.getFloat("grade");
                float maxPoints = resultSet.getFloat("max_points");

                if (currentCategory == null || !currentCategory.equals(category)) {
                    // Print category name
                    if (currentCategory != null) {
                        //Print subtotal for previous
                        System.out.println("Category Subtotal: " + categoryTotalPoints + "/" + categoryMaxPoints);
                        // Readability
                        System.out.println();
                    }
                    System.out.println("Category: " + category);
                    currentCategory = category;
                    categoryTotalPoints = 0;
                    categoryMaxPoints = 0;
                }

                System.out.println("  " + assignment + ": " + grade + "/" + maxPoints);

                // Update category totals
                categoryTotalPoints += grade;
                categoryMaxPoints += maxPoints;

                // Update overall totals
                totalPoints += grade;
                totalMaxPoints += maxPoints;
                if (grade != 0) {
                    totalAttemptedPoints += maxPoints;
                }
            }

            // Print subtotal for last category
            if (currentCategory != null) {
                System.out.println("Category Subtotal: " + categoryTotalPoints + "/" + categoryMaxPoints);
                System.out.println(); // Add an empty line for readability
            }

            // Print overall grade
            float overallGrade = (totalPoints / totalMaxPoints) * 100;
            float attemptedGrade = (totalPoints / totalAttemptedPoints) * 100;
            System.out.println("Overall Grade (Total): " + overallGrade);
            System.out.println("Overall Grade (Attempted): " + attemptedGrade);

        } catch(SQLException sqlException) {
            System.out.println("Failed to show student grades");
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

    private void gradebook() {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            if (activeClass == null) {
                throw new SQLException("Please select a class");
            }
            connection = Database.getDatabaseConnection();
            sqlStatement = connection.createStatement();

            // Query to retrieve the gradebook for the current class
            String sqlQuery = "SELECT s.username, s.student_id, s.firstName, s.lastName, " +
                    "SUM(COALESCE(g.grade, 0)) AS total_grade, " +
                    "SUM(CASE WHEN g.grade IS NOT NULL THEN a.point_value ELSE 0 END) AS total_attempted_points " +
                    "FROM Students s " +
                    "LEFT JOIN Grades g ON s.student_id = g.student_id " +
                    "LEFT JOIN Assignments a ON g.assignment_id = a.assignment_id " +
                    "WHERE a.class_id = " + activeClass + " " +
                    "GROUP BY s.username, s.student_id, s.firstName, s.lastName";

            ResultSet resultSet = sqlStatement.executeQuery(sqlQuery);

            // Display the gradebook for the current class
            System.out.println("Gradebook for " + activeClass + ":");
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                int studentId = resultSet.getInt("student_id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                float totalGrade = resultSet.getFloat("total_grade");
                float totalAttemptedPoints = resultSet.getFloat("total_attempted_points");

                // Calculate overall grade for the student
                float overallGrade = (totalGrade / totalAttemptedPoints) * 100;

                // Print student details and overall grade
                System.out.println("Username: " + username + ", Student ID: " + studentId + ", Name: " + firstName + " " + lastName);
                System.out.println("Overall Grade: " + overallGrade);
                System.out.println(); // Add an empty line for readability
            }

        } catch(SQLException sqlException) {
            System.out.println("Failed to show gradebook");
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

        /*
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
                case "testDB":
                    Database.testConnection();
                    break;
                default:
                System.out.println("Unknown command");
            }
        }
        */
    }
}
