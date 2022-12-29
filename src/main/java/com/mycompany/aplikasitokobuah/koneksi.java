/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.mycompany.aplikasitokobuah;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ThinkPad L450 i5
 */
public class koneksi {
    private static Connection koneksi;
    
    public static Connection getBarang(){
        if (koneksi == null) {
            try {
                String url = "jdbc:mysql://localhost:3306/penjualan";
                String user = "root";
                String password = "";
                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                koneksi = DriverManager.getConnection(url, user, password);
                System.out.println("Berhasil");
            } catch (SQLException e) {
                System.out.println("Error");
            }
        }
        return koneksi;
    }
    public static void main(String args[]){
        getBarang();
    }
}
