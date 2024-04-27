import java.sql.*;


/**
 * The Database class contains helper functions to connect and test your
 * connection with the database.
 */
public class Database {

    /**
     * Creates a connection to the database using environment variables. The following environment
     * variables must be set prior to running your program:
     *
     * <ul>
     *    <li>CS410_PORT - The database management system port
     *    <li>CS410_HOST - The database management system host
     *    <li>CS410_USERNAME - The database management system username
     *    <li>CS410_PASSWORD - The database management system user's password
     *    <li>CS410_DATABASE - The name of the database in the database management system
     * </ul>
     *
     * For more information on environment variables see:
     * <a href="https://docs.oracle.com/javase/tutorial/essential/environment/env.html">
     *  https://docs.oracle.com/javase/tutorial/essential/environment/env.html
     * </a>
     * @return java.sql.Connection
     * @throws SQLException
     */
    public static Connection getDatabaseConnection() throws SQLException {
        int databasePort = 56341;
        String databasePassword = "FinalDatabase";
        String databaseName = "CS410FinalDatabase";
        /*
         * STEP 1 and 2
         * LOAD the Database DRIVER and obtain a CONNECTION
         *
         * */
        //System.out.println("jdbc:mysql://localhost:"+databasePort+"/test?verifyServerCertificate=false&useSSL=true");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:"+databasePort+"/CS410Final?verifyServerCertificate=false&useSSL=true&serverTimezone=UTC", "msandbox",
                databasePassword);
        // Do something with the Connection
        System.out.println("Database [test db] connection succeeded!");
        System.out.println();
        return con;
    }

    /**
     * Tests the connection to your database. If your connection fails, please check:
     * <ul>
     *    <li>Database is running
     *    <li>Environment variables are set and being read in properly
     *    <li>Database Driver is in the CLASSPATH.
     *    <li>SSH port forwarding is properly setup
     * </ul>
     */
    public static void testConnection() {
        System.out.println("Attempting to connect to MySQL database using:");
        System.out.printf("CS410_HOST: %s%n", System.getenv("CS410_HOST"));
        System.out.printf("CS410_PORT: %s%n", System.getenv("CS410_PORT"));
        System.out.printf("CS410_USERNAME: %s%n", System.getenv("CS410_USERNAME"));
        System.out.printf("CS410_PASSWORD: %s%n", System.getenv("CS410_PASSWORD"));
        System.out.printf("CS410_DATABASE: %s%n", System.getenv("CS410_DATABASE"));

        Connection connection = null;
        ResultSet resultSet = null;

        try{
            connection = getDatabaseConnection();
            Statement sqlStatement = connection.createStatement();
            String sql = "SELECT VERSION();";
            resultSet = sqlStatement.executeQuery(sql);
            resultSet.next();
            System.out.printf("Connected SUCCESS! Database Version: %s%n", resultSet.getString(1));
        } catch (SQLException sql){
            System.out.println("Failed to connect to database! Please make sure your Environment variables are set!");
        } finally {
            try { resultSet.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public static void main(String args[]) {

    }
}

