package nl.hu.v1wac.herkansing.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO extends BaseDAO {
    public String findRoleForUsernameAndPassword(String username, String password) {
        String role = null;
        String query = "SELECT role FROM users WHERE username = ? AND password = ?";

        try (Connection con = super.getConnection()) {

            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next())
                role = rs.getString("role");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        System.out.println("dit is de role: "+ role);
        return role;
    }

    public boolean registerUser(String username, String password, String role) {
        String query = "INSERT INTO users (username, password, role) VALUES(?,?,?)";

        try (Connection con = super.getConnection()) {
            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) return true;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return false;
    }
}
