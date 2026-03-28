import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
            createTable(connection);
        } 
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void createTable(Connection connection) {
        try (Statement statement = connection.createStatement();) {
            statement.executeUpdate(
            """
                CREATE TABLE IF NOT EXISTS gameData (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                username VARCHAR(100) NOT NULL,
                dateOfTest DATE NOT NULL,
                score INTEGER NOT NULL);
            """
            );
        }
        catch (Exception e) {

        }
    }

    public static void insertTable(Connection connection, String username, Date dateOfTest, int score) {
        try {PreparedStatement prep = connection.prepareStatement(
            """
            INSERT INTO gameData(username, testDuration, score)
            VALUES (?, ?, ?)
            """
            );
            prep.setString(1, username);
            prep.setDate(2, dateOfTest);
            prep.setInt(3, score);
            prep.executeQuery();
        }
        catch (Exception e) {

        }
    }

    public static void deleteAll(Connection connection) {
        try (Statement statement = connection.createStatement();) {
            statement.executeUpdate(
            """
                DELETE * FROM gameData
            """
            );
        }
        catch (Exception e) {

        }
    }
}