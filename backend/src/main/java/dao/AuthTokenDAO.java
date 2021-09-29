package dao;
import model.AuthToken;
import model.Person;
import model.User;

import java.sql.*;

public class AuthTokenDAO {
    private final Connection conn;



    public AuthTokenDAO(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * inserts a new person into the database
     * @param authToken
     * @throws DataAccessException
     */
    public void insert(AuthToken authToken) throws DataAccessException{
        String sql = "INSERT INTO authtokens (auth_token, username) VALUES(?,?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken.getAuth_token());
            stmt.setString(2, authToken.getUsername());
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException("Error while inserting new AuthToken for " + authToken.getUsername());
        }
    }

    /**
     * Given a personID, returns the associated person object
     * @param tokenID
     * @return authToken
     * @throws DataAccessException
     */
    public AuthToken find(String tokenID) throws DataAccessException{
        AuthToken authToken;
        ResultSet rs = null;
        String sql = "SELECT * FROM authtokens WHERE auth_token = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tokenID);
            rs = stmt.executeQuery();
            if(rs.next()){
                authToken = new AuthToken(rs.getString("auth_token"), rs.getString("username"));
                return authToken;
            }
        }
        catch (SQLException e){
            throw new DataAccessException("Error encountered while finding authtoken");
        }
        finally {
            if(rs != null){
                try{
                    rs.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Clears all AuthTokens from database
     * @throws DataAccessException
     */
    public void clear() throws DataAccessException{
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM authtokens";
            stmt.executeUpdate(sql);
        }
        catch (SQLException e){
            throw new DataAccessException("Error while clearing authtoken table");
        }
    }

}
