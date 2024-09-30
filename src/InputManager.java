import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InputManager {

    // Registration input system
    public static boolean register(String name, String description, String sum, String date) {

        String sql = "INSERT INTO expenses (name, description, sum, date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {

            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setString(3, sum);
            pstmt.setString(4, date);

            int effectedRows = pstmt.executeUpdate();

            System.out.println("Reģistrācijas ietekmētās rindas: " + effectedRows);

            return effectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Datubāzes kļūda reģistrācijas laikā: " + e.getMessage());
            e.printStackTrace();

            return false;

        }

    }

}