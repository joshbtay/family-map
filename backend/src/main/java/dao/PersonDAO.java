package dao;

import model.Event;
import model.Person;

import java.sql.*;
import java.util.ArrayList;

public class PersonDAO {

    private final Connection conn;



    public PersonDAO(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * inserts a new person into the database
     * @param person
     * @throws DataAccessException
     */
    public void insert(Person person) throws DataAccessException{
        String sql = "INSERT INTO persons (person_id, username, first_name, last_name, gender," +
                "father_id, mother_id, spouse_id) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();

        }
        catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Given a personID, returns the associated person object
     * @param personID
     * @return person
     * @throws DataAccessException
     */
    public Person find(String personID) throws DataAccessException{
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM persons WHERE person_id = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if(rs.next()){
                person = new Person(rs.getString("person_id"), rs.getString("username"),
                        rs.getString("first_name"), rs.getString("last_name"), rs.getString("gender"),
                        rs.getString("father_id"), rs.getString("mother_id"), rs.getString("spouse_id"));
                return person;

            }
        }
        catch (SQLException e){
            throw new DataAccessException("Error encountered while finding person");
        }
        finally {
            if(rs != null) {
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
     * Builds an array list of all Persons that are associated with a given username
     * @param username
     * @return
     * @throws DataAccessException
     */
    public ArrayList<Person> getAll(String username) throws DataAccessException{
        ArrayList<Person> persons = new ArrayList<>();
        ResultSet rs = null;
        String sql = "SELECT * FROM persons WHERE username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while(rs.next()) {
                Person person = new Person(rs.getString("person_id"), rs.getString("username"),
                        rs.getString("first_name"), rs.getString("last_name"), rs.getString("gender"),
                        rs.getString("father_id"), rs.getString("mother_id"), rs.getString("spouse_id"));
                persons.add(person);
            }
            return persons;
        }
        catch (SQLException e) {
            throw new DataAccessException();
        }
        finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Clears all persons from the database
     * @throws DataAccessException
     */

    public void clear() throws DataAccessException{

        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM persons";
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            throw new DataAccessException("SQL Error while clearing person table");
        }
    }

    /**
     * Clears all person data associated with a single username
     * @param username
     * @throws DataAccessException
     */
    public void clear(String username) throws DataAccessException{
        String sql = "DELETE FROM persons WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        }
        catch(SQLException e) {
            throw new DataAccessException("Error while clearing user " + username + "'s family data");
        }
    }



}
