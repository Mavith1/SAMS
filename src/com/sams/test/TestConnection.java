package com.sams.test;

import com.sams.util.DBConnection;
import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {

        Connection con = DBConnection.getConnection();

        if (con != null) {
            System.out.println("Database Connected!");
        } else {
            System.out.println("Connection Failed!");
        }
    }
}