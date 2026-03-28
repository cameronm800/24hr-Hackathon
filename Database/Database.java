package Database;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
public class Database {
    public static void main() {
        String dbUrl = "jdbc:sqlite:database.db";
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
                score1 INTEGER NOT NULL,
                score2 INTEGER NOT NULL,
                score3 INTEGER NOT NULL);
            """
            );
        }
        catch (Exception e) {

        }
    }

    


    public static void insertTable(Connection connection, String username, Date dateOfTest, int score1, int score2, int score3) {
        try {PreparedStatement prep = connection.prepareStatement(
            """
            INSERT INTO gameData(username, testDuration, score1, score2, score3)
            VALUES (?, ?, ?, ?, ?)
            """
            );
            prep.setString(1, username);
            prep.setDate(2, dateOfTest);
            prep.setInt(3, score1);
             prep.setInt(4, score2);
            prep.setInt(5, score3);
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