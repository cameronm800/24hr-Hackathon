package Database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
public class Database {
    private Connection connection;
    public Database() {
            getConnection();
            createTable();
    }

    public void getConnection() {
        String dbUrl = "jdbc:sqlite:database.db";
        try {
            connection = DriverManager.getConnection(dbUrl);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createTable() {
        try (Statement statement = connection.createStatement();) {
            statement.executeUpdate(
            """
                CREATE TABLE IF NOT EXISTS gameData (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                username VARCHAR(100) NOT NULL,
                testTime TIME NOT NULL,
                score1 INTEGER NOT NULL,
                score2 INTEGER NOT NULL,
                score3 INTEGER NOT NULL);
            """
            );
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    


    public void insertTable(String username, int score1, int score2, int score3) {
        try {PreparedStatement prep = connection.prepareStatement(
            """
            INSERT INTO gameData(username, testTime, score1, score2, score3)
            VALUES (?, TIME('now'), ?, ?, ?)
            """
            );
            prep.setString(1, username);
            prep.setInt(2, score1);
             prep.setInt(3, score2);
            prep.setInt(4, score3);
            prep.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        try (Statement statement = connection.createStatement();) {
            statement.executeUpdate(
            """
                DELETE FROM gameData
            """
            );
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            connection.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}