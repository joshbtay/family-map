package service;

import Request.ClearRequest;
import Result.ClearResult;
import dao.DataAccessException;
import dao.Database;

public class ClearService {
    private Database db;



    /**
     * Makes a request to clear all data
     * @param clearRequest
     * @return clearResult
     */
    public ClearResult clear(ClearRequest clearRequest) {
        try{
            db = new Database();
            db.openConnection();
            db.clearTables();
            db.closeConnection(true);
            return new ClearResult(true, "Clear succeeded.");
        } catch (DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (DataAccessException exception) {
                exception.printStackTrace();
            }
            return new ClearResult(false, "Error clearing database");
        }
    }
}
