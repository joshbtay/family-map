package dao;

import model.User;
import org.sqlite.SQLiteConfig;

import java.sql.*;

public class UserDAO {
    private final Connection conn;



    public UserDAO(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * inserts a new user into the database
     * @param user
     * @throws DataAccessException
     */
    public void insert(User user) throws DataAccessException {
        String sql = "INSERT INTO users (username, password, email, first_name," +
                "last_name, gender, person_id) VALUES(?,?,?,?,?,?,?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting user ID " + user.getPersonID() + " into the database");
        }
    }

    /**
     * Given a username, returns the associated user object
     * @param username username
     * @return User
     * @throws DataAccessException
     */
    public User find(String username) throws DataAccessException {
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM users WHERE username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,username);
            rs = stmt.executeQuery();
            if(rs.next()){
                user = new User(rs.getString("username"), rs.getString("password"),
                        rs.getString("email"), rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("gender"), rs.getString("person_id"));
                return user;
            }

        }
        catch (SQLException e) {
            throw new DataAccessException("Error encountered while finding user");
        }
        finally {
            if (rs!=null) {
                try {
                    rs.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


        return null;
    }

    /**
     * clears all users from the database
     * @throws DataAccessException
     */

    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM users";
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            throw new DataAccessException("SQL Error while clearing user table");
        }
    }

}
