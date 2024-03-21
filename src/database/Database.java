package database;

import javax.xml.crypto.Data;
import java.awt.dnd.DropTargetAdapter;
import java.sql.*;

public class Database {

    private Connection c = null;
    private Statement stmt = null;
    private static Database instance;
    private static  int score;

    private Database() {
        score = 0;  //set score to 0 when new session starts?
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:gameDB.db");
            stmt = c.createStatement();
            c.setAutoCommit(false);


                String sql = "CREATE TABLE IF NOT EXISTS SCOREDB " +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "SCORE INTEGER)";
                stmt.execute(sql);


            c.commit();
            stmt.close();
            c.close();


        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public Connection getC() {
        return c;
    }

    public Statement getStmt() {
        return stmt;
    }

    public void setC(Connection c) {
        this.c = c;
    }

    public void getConnection() {
        try {
            c = DriverManager.getConnection("jdbc:sqlite:gameDB.db");
            c.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            stmt = c.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setStmt(Statement stmt) {
        this.stmt = stmt;
    }

    public static Database getInstance() {
        if(Database.instance == null) {
            Database.instance = new Database();
        }
        return instance;
    }

    public void setScoreInDatabase(int value) {
        score += value;
        getConnection();
        String sql = "INSERT INTO SCOREDB (SCORE)" + "VALUES (" + score + ")";
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            c.commit();
            c.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getScore() {
        getConnection();
        int score1=0;
        String sql = "SELECT * FROM SCOREDB ORDER BY ID DESC LIMIT 1";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
                score1 = rs.getInt("SCORE");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            c.commit();
            c.close();
            return score1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

