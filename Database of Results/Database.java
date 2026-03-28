import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class Database {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("We need the database file as an argument.");
            return;
        }

        String dbUrl = "jbdc:sqlite:" + args[0];

        try (Connection connection = DriverManager.getConnection(dbUrl);) {
            this.createTable();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

        private static void createTable(Connection connection) throws SQLException {
            try (Statement statement = connection.createStatement();) {
                statement.executeUpdate(
                """
                CREATE TABLE IF NOT EXISTS gameData (
                id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, 
                );      
                """);
            }
        }
    }