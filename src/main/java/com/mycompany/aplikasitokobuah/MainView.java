/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.aplikasitokobuah;
import javax.swing.JFrame;

import java.awt.HeadlessException;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.table.DefaultTableModel;
import javax.swing.Timer;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author ThinkPad L450 i5
 */
public class MainView extends javax.swing.JFrame {
    // Baru
    koneksi koneksi = new koneksi();
    
    private DefaultTableModel model;
    
    // Tampil_Waktu
    public void Tampil_Waktu(){
        ActionListener taskPerformer = new ActionListener() {
 
        @Override
            public void actionPerformed(ActionEvent evt) {
            String nol_jam = "", nol_menit = "",nol_detik = "";
 
            java.util.Date dateTime = new java.util.Date();
            int nilai_jam = dateTime.getHours();
            int nilai_menit = dateTime.getMinutes();
            int nilai_detik = dateTime.getSeconds();
 
            if(nilai_jam <= 9) nol_jam= "0";
            if(nilai_menit <= 9) nol_menit= "0";
            if(nilai_detik <= 9) nol_detik= "0";
 
            String jam = nol_jam + Integer.toString(nilai_jam);
            String menit = nol_menit + Integer.toString(nilai_menit);
            String detik = nol_detik + Integer.toString(nilai_detik);
            
            waktu = jam+":"+menit+":"+detik+"";
            jLabel34.setText(jam+":"+menit+":"+detik+"");
            }
        };
        
    new Timer(1000, taskPerformer).start();
    }
    
    // Tampil Tanggal
    public void Tampil_Tanggal() {
        java.util.Date tglsekarang = new java.util.Date();
        SimpleDateFormat smpdtfmt = new SimpleDateFormat("dd MMMMMMMMM yyyy", Locale.getDefault());
        String tanggal = smpdtfmt.format(tglsekarang);
        this.tanggal = tanggal;
        jLabel48.setText(tanggal);
    }

    private void autonumber(){
        try {
            Connection c = koneksi.getBarang();
            try (Statement s = c.createStatement()) {
                String sql = "SELECT * FROM barang ORDER BY ID_Buah DESC";
                try (ResultSet r = s.executeQuery(sql)) {
                    if (r.next()) {
                        String No = r.getString("id_buah").substring(2);
                        String BR = "" +(Integer.parseInt(No)+1);
                        String Nol = "";
                        
                        switch (BR.length()) {
                            case 1 -> Nol = "00";
                            case 2 -> Nol = "0";
                            case 3 -> Nol = "";
                            default -> {
                            }
                        }
                        
                        txIDBuah.setText("BR" + Nol + BR);
                    }else{
                        txIDBuah.setText("BR001");
                    }
                }
            }
        } catch (NumberFormatException | SQLException e) {
            System.out.println("autonumber error");
        }}
    
    public void clear(){
        txNamaBuah.setText("");
        txHargaBeli.setText("");
        txHargaJual.setText("");
        txStock.setText("");
    }
    
    public void loadData(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        
        try {
            Connection c = koneksi.getBarang();
            try (Statement s = c.createStatement()) {
                String sql = "SELECT * FROM barang";
                try (ResultSet r = s.executeQuery(sql)) {
                    while (r.next()) {
                        Object[] o = new Object[5];
                        o [0] = r.getString("id_buah");
                        o [1] = r.getString("nama_buah");
                        o [2] = r.getString("harga_beli");
                        o [3] = r.getString("harga_jual");
                        o [4] = r.getString("stok_buah");
                        
                        model.addRow(o);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("terjadi kesalahan");
        }
    }
    
    public void cariData(){
        DefaultTableModel tabel = new DefaultTableModel();
        
        tabel.addColumn("ID Buah");
        tabel.addColumn("Nama Buah");
        tabel.addColumn("Harga Beli");
        tabel.addColumn("Harga Jual");
        tabel.addColumn("Stock");
        
        try {
            Connection c = koneksi.getBarang();
            String sql = "SELECT * from barang WHERE id_buah LIKE '%" + txSearch.getText() + "%'" +
                    "or nama_buah LIKE '%" + txSearch.getText() + "%'";
            Statement stat = c.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {                
                tabel.addRow(new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                });
            }
            jTable1.setModel(tabel);
            loadData();
        } catch (SQLException e) {
            System.out.println("Cari Data Error");
        }finally{
        }
    }
    
    String pilihan = "";
    int harga_buah;
    int jml_harga;
    int bayar;
    int jml_buah = 0;
    int kembalian;
    int attemps;
    int total_jumlah_buah = 0;
    
    // Properti Ambil Data yang dibeli
    String kumpulan_nama_buah = "";
    String kumpulan_jumlah_buah = "";
    String kumpulan_id_buah = "";
    String kumpulan_harga_buah = "";
    
    // Waktu dan Tanggal
    String waktu;
    String tanggal;
    
    // Id transaksi
    int id_transaksi = 0;
    
    /**
     * Creates new form MainView
     */
    public MainView() {
        initComponents();
        Tampil_Waktu();
        Tampil_Tanggal();
        
        this.setExtendedState(JFrame.MAXIMIZED_HORIZ);
        this.setExtendedState(JFrame.MAXIMIZED_VERT);
        setVisible(true);
        setResizable(true);
        
        this.setLocationRelativeTo(null);
        
        model = new DefaultTableModel();
        
        jTable1.setModel(model);
        
        model.addColumn("ID Buah");
        model.addColumn("Nama Buah");
        model.addColumn("Harga Beli (kg)");
        model.addColumn("Harga Jual (kg)");
        model.addColumn("Stock (kg)");
        
        loadData();
        autonumber();
        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);
        btnBatal.setEnabled(false);
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bodyPanel = new javax.swing.JPanel();
        menuPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        berandaPanel = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        transaksiPanel = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        loginPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPasswordField2 = new javax.swing.JPasswordField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel17 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        totalanPanel = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        stokBuahPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txIDBuah = new javax.swing.JTextField();
        txNamaBuah = new javax.swing.JTextField();
        txStock = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txHargaJual = new javax.swing.JTextField();
        txHargaBeli = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        btnSimpan = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        txSearch = new javax.swing.JTextField();
        cetakStruk = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextArea5 = new javax.swing.JTextArea();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextArea6 = new javax.swing.JTextArea();
        jButton11 = new javax.swing.JButton();
        jTextField7 = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        listBuahPanel = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        daftarNamaBuah = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        list = new javax.swing.JList<>();
        jLabel49 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Rumah Buah");
        setResizable(false);

        bodyPanel.setBackground(new java.awt.Color(102, 0, 51));
        bodyPanel.setNextFocusableComponent(bodyPanel);

        menuPanel.setBackground(new java.awt.Color(255, 204, 255));

        jButton1.setBackground(new java.awt.Color(102, 0, 51));
        jButton1.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon("E:\\Folder Kuliah\\SEMESTER 3\\Pemrograman 2\\Tugas Besar\\Icons\\homecek3.png")); // NOI18N
        jButton1.setText("Beranda");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton1.setIconTextGap(13);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(102, 0, 51));
        jButton2.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon("E:\\Folder Kuliah\\SEMESTER 3\\Pemrograman 2\\Tugas Besar\\Icons\\billcek.png")); // NOI18N
        jButton2.setText("Transaksi");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton2.setIconTextGap(13);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(102, 0, 51));
        jButton3.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon("E:\\Folder Kuliah\\SEMESTER 3\\Pemrograman 2\\Tugas Besar\\Icons\\database.png")); // NOI18N
        jButton3.setText("Stok Buah");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton3.setIconTextGap(13);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(102, 0, 51));
        jButton4.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon("E:\\Folder Kuliah\\SEMESTER 3\\Pemrograman 2\\Tugas Besar\\Icons\\infocek.png")); // NOI18N
        jButton4.setText("Tentang");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton4.setIconTextGap(13);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(102, 0, 51));
        jButton5.setFont(new java.awt.Font("Poppins Medium", 0, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setIcon(new javax.swing.ImageIcon("E:\\Folder Kuliah\\SEMESTER 3\\Pemrograman 2\\Tugas Besar\\Icons\\logoutcek.png")); // NOI18N
        jButton5.setText("Keluar");
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton5.setIconTextGap(13);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon("E:\\Folder Kuliah\\SEMESTER 3\\Praktikum Pemrograman 2\\AplikasiTokoBuah\\Icons\\LogoCek3.png")); // NOI18N

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Roboto Bk", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 0, 51));
        jLabel2.setText("RUMAHNYA");

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Roboto Bk", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 0, 51));
        jLabel3.setText("BUAH SEGAR");

        javax.swing.GroupLayout menuPanelLayout = new javax.swing.GroupLayout(menuPanel);
        menuPanel.setLayout(menuPanelLayout);
        menuPanelLayout.setHorizontalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addGroup(menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menuPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(menuPanelLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addGroup(menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(jLabel1)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        menuPanelLayout.setVerticalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        mainPanel.setBackground(new java.awt.Color(102, 0, 51));
        mainPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white));
        mainPanel.setLayout(new java.awt.CardLayout());

        berandaPanel.setBackground(new java.awt.Color(102, 0, 51));
        berandaPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBackground(new java.awt.Color(255, 204, 255));
        jPanel7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 0, 51), 6, true));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Poppins SemiBold", 0, 36)); // NOI18N
        jLabel4.setText("SELAMAT DATANG");
        jPanel7.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, -1, -1));

        jLabel5.setFont(new java.awt.Font("Poppins SemiBold", 0, 36)); // NOI18N
        jLabel5.setText("RUMAH BUAH");
        jPanel7.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 120, -1, -1));

        jLabel6.setFont(new java.awt.Font("Poppins SemiBold", 0, 36)); // NOI18N
        jLabel6.setText("DI");
        jPanel7.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 70, -1, -1));

        berandaPanel.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 410, 200));

        jLabel33.setIcon(new javax.swing.ImageIcon("E:\\Folder Kuliah\\SEMESTER 3\\Praktikum Pemrograman 2\\AplikasiTokoBuah\\Icons\\bg faktur.png")); // NOI18N
        berandaPanel.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 420));

        mainPanel.add(berandaPanel, "card2");

        transaksiPanel.setBackground(new java.awt.Color(102, 0, 51));
        transaksiPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton7.setBackground(new java.awt.Color(255, 204, 255));
        jButton7.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jButton7.setText("Tambah");
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        transaksiPanel.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 340, 149, 47));

        jButton8.setBackground(new java.awt.Color(255, 204, 255));
        jButton8.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jButton8.setText("Selesai");
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        transaksiPanel.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 340, 149, 47));

        jPanel3.setBackground(new java.awt.Color(255, 228, 241));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 102), 6));

        jLabel7.setFont(new java.awt.Font("Minnie", 0, 24)); // NOI18N
        jLabel7.setText("Rumah Buah");

        jLabel51.setFont(new java.awt.Font("Minnie", 0, 36)); // NOI18N
        jLabel51.setText("TRANSAKSI");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel51))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(jLabel7)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(jLabel51)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(12, 12, 12))
        );

        transaksiPanel.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 300, -1));

        jPanel5.setBackground(new java.awt.Color(204, 255, 204));

        jTextField1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTextField2.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jComboBox1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Buah", "Mangga", "Durian", "Apel", "Kelengkeng", "Manggis", "Semangka", "Pepaya", "Jambu", "Anggur", "Jeruk", "Nanas", "Pisang", "Naga", "Alpukat", "Melon", "Lemon", "Duku", "Nangka" }));
        jComboBox1.setPreferredSize(new java.awt.Dimension(64, 22));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel9.setText("Nama Buah");

        jLabel10.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel10.setText("Jumlah Buah (Kg)");

        jLabel11.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel11.setText("Total Harga (Rp)");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addComponent(jLabel11))
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        transaksiPanel.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 410, 160));

        jLabel50.setIcon(new javax.swing.ImageIcon("E:\\Folder Kuliah\\SEMESTER 3\\Praktikum Pemrograman 2\\AplikasiTokoBuah\\Icons\\BG.png")); // NOI18N
        transaksiPanel.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 440));

        mainPanel.add(transaksiPanel, "card3");

        loginPanel.setBackground(new java.awt.Color(255, 255, 255));
        loginPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Montserrat", 1, 36)); // NOI18N
        jLabel8.setText("Welcome!");
        loginPanel.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(251, 118, -1, -1));

        jPanel1.setBackground(new java.awt.Color(193, 35, 117));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel32.setIcon(new javax.swing.ImageIcon("E:\\Folder Kuliah\\SEMESTER 3\\Praktikum Pemrograman 2\\AplikasiTokoBuah\\Icons\\tb.png")); // NOI18N
        jPanel1.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 170, 420));

        loginPanel.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 421));

        jLabel16.setText("Insert PIN Number");
        loginPanel.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(296, 168, -1, -1));

        jPasswordField2.setBackground(new java.awt.Color(236, 228, 236));
        jPasswordField2.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jPasswordField2.setBorder(null);
        jPasswordField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField2ActionPerformed(evt);
            }
        });
        loginPanel.add(jPasswordField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(241, 219, 174, 33));

        jSeparator1.setForeground(new java.awt.Color(255, 99, 172));
        jSeparator1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 99, 172)));
        loginPanel.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(241, 258, 174, -1));

        jLabel17.setIcon(new javax.swing.ImageIcon("E:\\Folder Kuliah\\SEMESTER 3\\Praktikum Pemrograman 2\\AplikasiTokoBuah\\Icons\\gembok.png")); // NOI18N
        loginPanel.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 225, -1, -1));

        jButton6.setIcon(new javax.swing.ImageIcon("E:\\Folder Kuliah\\SEMESTER 3\\Praktikum Pemrograman 2\\AplikasiTokoBuah\\Icons\\buttonLogin.png")); // NOI18N
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        loginPanel.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(435, 217, 68, 35));
        loginPanel.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 273, -1, -1));

        mainPanel.add(loginPanel, "card4");

        totalanPanel.setBackground(new java.awt.Color(102, 0, 51));
        totalanPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton9.setBackground(new java.awt.Color(204, 255, 204));
        jButton9.setFont(new java.awt.Font("Poppins Medium", 1, 14)); // NOI18N
        jButton9.setText("Hitung");
        jButton9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        totalanPanel.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 260, 149, 47));

        jTextArea2.setBackground(new java.awt.Color(255, 204, 255));
        jTextArea2.setColumns(1);
        jTextArea2.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jTextArea2.setRows(5);
        jTextArea2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 102), 3, true));
        jTextArea2.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane4.setViewportView(jTextArea2);
        jTextArea2.getAccessibleContext().setAccessibleName("");

        totalanPanel.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 130, 150));

        jPanel6.setBackground(new java.awt.Color(204, 255, 204));

        jLabel52.setFont(new java.awt.Font("Minnie", 0, 24)); // NOI18N
        jLabel52.setText("Rumah Buah");

        jLabel53.setFont(new java.awt.Font("Minnie", 0, 24)); // NOI18N
        jLabel53.setText("TOTAL TRANSAKSI");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel53))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(jLabel52)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(jLabel53)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel52)
                .addContainerGap())
        );

        totalanPanel.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 300, -1));

        jButton10.setBackground(new java.awt.Color(204, 255, 204));
        jButton10.setFont(new java.awt.Font("Poppins Medium", 1, 14)); // NOI18N
        jButton10.setText("Cetak Struk");
        jButton10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        totalanPanel.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 330, 149, 47));

        jTextArea3.setBackground(new java.awt.Color(255, 204, 255));
        jTextArea3.setColumns(1);
        jTextArea3.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jTextArea3.setRows(5);
        jTextArea3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 102), 3, true));
        jScrollPane5.setViewportView(jTextArea3);

        totalanPanel.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 260, 130, 150));

        jPanel4.setBackground(new java.awt.Color(204, 255, 204));

        jTextField3.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jTextField4.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jTextField5.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel13.setText("Total Harga (Rp)");

        jLabel14.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel14.setText("Jumlah Bayar (Rp)");

        jLabel15.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel15.setText("Kembalian (Rp)");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 199, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(21, 21, 21)
                        .addComponent(jLabel14)
                        .addGap(21, 21, 21)
                        .addComponent(jLabel15))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        totalanPanel.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 460, 120));

        jTextField6.setBackground(new java.awt.Color(255, 204, 255));
        jTextField6.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jTextField6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField6.setText("Jumlah Buah (Kg)");
        jTextField6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 102), 3, true));
        totalanPanel.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 230, 130, -1));

        jTextField8.setBackground(new java.awt.Color(255, 204, 255));
        jTextField8.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jTextField8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField8.setText("List Buah");
        jTextField8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 102), 3, true));
        totalanPanel.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 130, -1));

        jLabel12.setIcon(new javax.swing.ImageIcon("E:\\Folder Kuliah\\SEMESTER 3\\Praktikum Pemrograman 2\\AplikasiTokoBuah\\Icons\\tb.png")); // NOI18N
        totalanPanel.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 440));

        mainPanel.add(totalanPanel, "card5");

        stokBuahPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(102, 0, 102));

        jLabel19.setIcon(new javax.swing.ImageIcon("E:\\Folder Kuliah\\SEMESTER 3\\Praktikum Pemrograman 2\\AplikasiTokoBuah\\Icons\\LogoCek3.png")); // NOI18N

        jLabel20.setFont(new java.awt.Font("Roboto", 1, 36)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Database Barang");

        jLabel21.setFont(new java.awt.Font("Roboto", 3, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Rumah Buah");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jLabel20))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(133, 133, 133)
                        .addComponent(jLabel21)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel21))
                    .addComponent(jLabel19))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        stokBuahPanel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 514, -1));

        jLabel22.setIcon(new javax.swing.ImageIcon("E:\\Folder Kuliah\\SEMESTER 3\\Praktikum Pemrograman 2\\AplikasiTokoBuah\\Icons\\search.png")); // NOI18N
        stokBuahPanel.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 139, -1, -1));

        jLabel23.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jLabel23.setText("ID Buah :");
        stokBuahPanel.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 179, -1, -1));
        stokBuahPanel.add(txIDBuah, new org.netbeans.lib.awtextra.AbsoluteConstraints(91, 175, 135, -1));
        stokBuahPanel.add(txNamaBuah, new org.netbeans.lib.awtextra.AbsoluteConstraints(91, 203, 135, -1));
        stokBuahPanel.add(txStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(91, 231, 135, -1));

        jLabel24.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jLabel24.setText("Nama Buah :");
        stokBuahPanel.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 207, -1, -1));

        jLabel25.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jLabel25.setText("Stock :");
        stokBuahPanel.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(49, 235, -1, -1));

        jLabel26.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jLabel26.setText("/Kg");
        stokBuahPanel.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(232, 235, -1, -1));

        jLabel27.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jLabel27.setText("Harga Jual :");
        stokBuahPanel.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 188, -1, -1));

        jLabel28.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jLabel28.setText("Harga Beli :");
        stokBuahPanel.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(265, 222, -1, -1));

        txHargaJual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txHargaJualActionPerformed(evt);
            }
        });
        stokBuahPanel.add(txHargaJual, new org.netbeans.lib.awtextra.AbsoluteConstraints(333, 184, 135, -1));
        stokBuahPanel.add(txHargaBeli, new org.netbeans.lib.awtextra.AbsoluteConstraints(333, 218, 135, -1));

        jLabel29.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jLabel29.setText("/Kg");
        stokBuahPanel.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(474, 188, -1, -1));

        jLabel30.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jLabel30.setText("/Kg");
        stokBuahPanel.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(474, 222, -1, -1));

        btnSimpan.setBackground(new java.awt.Color(51, 0, 51));
        btnSimpan.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        btnSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan.setText("Simpan");
        btnSimpan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });
        stokBuahPanel.add(btnSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(91, 265, -1, -1));

        btnEdit.setBackground(new java.awt.Color(51, 0, 51));
        btnEdit.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setText("Edit");
        btnEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        stokBuahPanel.add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(181, 265, -1, -1));

        btnHapus.setBackground(new java.awt.Color(51, 0, 51));
        btnHapus.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        btnHapus.setForeground(new java.awt.Color(255, 255, 255));
        btnHapus.setText("Hapus");
        btnHapus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });
        stokBuahPanel.add(btnHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(271, 265, -1, -1));

        btnBatal.setBackground(new java.awt.Color(51, 0, 51));
        btnBatal.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        btnBatal.setForeground(new java.awt.Color(255, 255, 255));
        btnBatal.setText("Batal");
        btnBatal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });
        stokBuahPanel.add(btnBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(361, 265, -1, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        stokBuahPanel.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 298, 479, 100));

        txSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txSearchActionPerformed(evt);
            }
        });
        txSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txSearchKeyTyped(evt);
            }
        });
        stokBuahPanel.add(txSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(327, 139, 141, -1));

        mainPanel.add(stokBuahPanel, "card6");

        cetakStruk.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel31.setIcon(new javax.swing.ImageIcon("E:\\Folder Kuliah\\SEMESTER 3\\Praktikum Pemrograman 2\\AplikasiTokoBuah\\Icons\\LogoCek3.png")); // NOI18N
        cetakStruk.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));

        jLabel35.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("ID Transaksi");
        jLabel35.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cetakStruk.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 90, -1));

        jLabel36.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel36.setText("Waktu Transaksi");
        cetakStruk.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 20, -1, -1));

        jLabel37.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("ID Buah");
        jLabel37.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cetakStruk.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, 90, -1));

        jLabel38.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("Nama Buah");
        jLabel38.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cetakStruk.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 90, -1));

        jLabel39.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("Jumlah Buah/Kg");
        jLabel39.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cetakStruk.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 140, 110, -1));

        jLabel40.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText("Harga Buah");
        jLabel40.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cetakStruk.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 140, 90, -1));

        jTextArea1.setColumns(1);
        jTextArea1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jTextArea1.setRows(8);
        jScrollPane3.setViewportView(jTextArea1);

        cetakStruk.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, 93, -1));

        jTextArea4.setColumns(1);
        jTextArea4.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jTextArea4.setRows(8);
        jScrollPane6.setViewportView(jTextArea4);

        cetakStruk.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, 93, -1));

        jTextArea5.setColumns(1);
        jTextArea5.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jTextArea5.setRows(8);
        jScrollPane7.setViewportView(jTextArea5);

        cetakStruk.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 170, 110, -1));

        jTextArea6.setColumns(1);
        jTextArea6.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jTextArea6.setRows(8);
        jScrollPane8.setViewportView(jTextArea6);

        cetakStruk.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 170, 93, -1));

        jButton11.setBackground(new java.awt.Color(102, 0, 51));
        jButton11.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("Selesai");
        jButton11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        cetakStruk.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 341, 100, 45));
        cetakStruk.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 110, -1));

        jLabel41.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel41.setText("Tanggal Transaksi");
        cetakStruk.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 50, -1, -1));

        jLabel42.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel42.setText("Total Bayar :");
        cetakStruk.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(177, 369, -1, -1));

        jLabel43.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel43.setText("Total Beli :");
        cetakStruk.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 341, -1, -1));

        jLabel44.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel44.setText("Kembalian :");
        cetakStruk.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(179, 398, -1, -1));

        jLabel45.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel45.setText("Total Beli :");
        cetakStruk.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(264, 341, -1, -1));

        jLabel46.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel46.setText("Total Beli :");
        cetakStruk.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(264, 369, -1, -1));

        jLabel47.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel47.setText("Total Beli :");
        cetakStruk.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(264, 398, -1, -1));

        mainPanel.add(cetakStruk, "card7");

        listBuahPanel.setBackground(new java.awt.Color(255, 204, 255));

        jLabel34.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jLabel34.setText("Jam");

        jLabel48.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jLabel48.setText("Tanggal");

        daftarNamaBuah.setBackground(new java.awt.Color(255, 255, 255));
        daftarNamaBuah.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        daftarNamaBuah.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        daftarNamaBuah.setText("Daftar Nama Buah");
        daftarNamaBuah.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.black, java.awt.Color.black, java.awt.Color.black, java.awt.Color.black));
        daftarNamaBuah.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jScrollPane1.setBackground(new java.awt.Color(102, 0, 51));

        list.setBackground(new java.awt.Color(255, 204, 255));
        list.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.black, java.awt.Color.black, java.awt.Color.black, java.awt.Color.black));
        list.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        list.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Mangga", "Durian", "Apel", "Kelengkeng", "Manggis", "Semangka", "Pepaya", "Jambu", "Anggur", "Jeruk", "Nanas", "Pisang", "Naga", "Alpukat", "Melon", "Lemon", "Duku", "Nangka" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        list.setSelectionBackground(new java.awt.Color(102, 0, 51));
        jScrollPane1.setViewportView(list);

        jLabel49.setFont(new java.awt.Font("Poppins Medium", 0, 12)); // NOI18N
        jLabel49.setText("Waktu");

        javax.swing.GroupLayout listBuahPanelLayout = new javax.swing.GroupLayout(listBuahPanel);
        listBuahPanel.setLayout(listBuahPanelLayout);
        listBuahPanelLayout.setHorizontalGroup(
            listBuahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(listBuahPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(listBuahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(daftarNamaBuah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, listBuahPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel49)
                        .addGap(40, 40, 40))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, listBuahPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(listBuahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel48)
                    .addComponent(jLabel34))
                .addGap(23, 23, 23))
        );
        listBuahPanelLayout.setVerticalGroup(
            listBuahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, listBuahPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(daftarNamaBuah)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout bodyPanelLayout = new javax.swing.GroupLayout(bodyPanel);
        bodyPanel.setLayout(bodyPanelLayout);
        bodyPanelLayout.setHorizontalGroup(
            bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(menuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listBuahPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        bodyPanelLayout.setVerticalGroup(
            bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bodyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(listBuahPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 425, Short.MAX_VALUE)
                    .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(menuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bodyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bodyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
        // Mouse Hover
        
        
        // Remove Panel
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
        
        // Add Panel
        mainPanel.add(berandaPanel);
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        
        // Remove Panel
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
        
        // Add Panel
        mainPanel.add(transaksiPanel);
        mainPanel.repaint();
        mainPanel.revalidate();
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        
        // Remove Panel
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
        
        // Add Panel
        mainPanel.add(loginPanel);
        mainPanel.repaint();
        mainPanel.revalidate();
        
        jPasswordField2.setText("");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        
        TentangView tv = new TentangView();
        tv.setVisible(true);
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
       Keluar kl = new Keluar();
       kl.setVisible(true);
        
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        if( total_jumlah_buah > 0 ) {
            // Remove Panel
            mainPanel.removeAll();
            mainPanel.repaint();
            mainPanel.revalidate();

            // Add Panel
            mainPanel.add(totalanPanel);
            mainPanel.repaint();
            mainPanel.revalidate();
        }
        
        // Menentukan diskon
        if ( total_jumlah_buah >= 5 && total_jumlah_buah < 10 ) {
            JOptionPane.showMessageDialog(null, "Anda Mendapatkan Diskon Sebesar 10%");
            this.jml_harga = jml_harga * 90 / 100;
        } else if ( total_jumlah_buah >= 10 && total_jumlah_buah < 15 ) {
            this.jml_harga = jml_harga * 80 / 100;
            JOptionPane.showMessageDialog(null, "Anda Mendapatkan Diskon Sebesar 20%");
        } else if ( total_jumlah_buah >= 15 ) {
            this.jml_harga = jml_harga * 70 / 100;
            JOptionPane.showMessageDialog(null, "Anda Mendapatkan Diskon Sebesar 30%");
        }
        
        // Membuah Total Harga yang harus dibayar
        jTextField3.setText("" + Integer.toString(this.jml_harga));
        
        
        // Membuat List Buah
        jTextArea2.setText("" + this.kumpulan_nama_buah);
        jTextArea3.setText("" + this.kumpulan_jumlah_buah);
        
        // List Buah dalam Struk
        jTextArea1.setText("" + this.kumpulan_nama_buah);
        jTextArea5.setText("" + this.kumpulan_jumlah_buah);
        jTextArea4.setText("" + this.kumpulan_id_buah);
        jTextArea6.setText("" + this.kumpulan_harga_buah);
        

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        pilihan = (String) jComboBox1.getSelectedItem();
        
        // Ambil Text
        this.jml_buah = Integer.parseInt(jTextField1.getText());
        
        for ( int i = 0; i < 18; i++ ) {
            String namaBuah = (String) model.getValueAt(i, 1);
            String id = (String) model.getValueAt(i, 0);
            String stokBuah = (String) model.getValueAt(i, 4);
            String hargaBuah = (String) model.getValueAt(i, 3);
            if ( pilihan.equals(namaBuah) ) {
                if ( this.jml_buah > Integer.parseInt(stokBuah) ) {
                    JOptionPane.showMessageDialog(null, "Stok Tidak Mencukupi");
                    this.jml_buah = 0;
                } else {
                    // Jika jumlah buah itu sama dengan 0 
                    if ( this.jml_buah <= 0 ) {
                        JOptionPane.showMessageDialog(null, "Jumlah Buah Tidak Boleh 0");
                    } else {

                        // Menambahkan Diskon
                        this.total_jumlah_buah = total_jumlah_buah + Integer.parseInt(jTextField1.getText());

                        // Mengurangi Stok di database
                        int stokBuahBaru = Integer.parseInt(stokBuah) - this.jml_buah;
                        String stokBuahBaruString = Integer.toString(stokBuahBaru);
                        try {
                            Connection c = koneksi.getBarang();
                            String sql = "UPDATE barang SET stok_buah = ? WHERE id_buah = ?";
                            try (PreparedStatement p = c.prepareStatement(sql)) {
                                p.setString(1, stokBuahBaruString);
                                p.setString(2, id);

                                p.executeUpdate();
                            }
                        } catch (HeadlessException | SQLException e) {
                            System.out.println("Edit Error");
                        }finally{
                            loadData();
                            autonumber();
                        }


                        String hargaJualIni = (String) model.getValueAt(i, 3);
                        // Cetak buah yang ingin dibeli
                        this.kumpulan_nama_buah = this.kumpulan_nama_buah + namaBuah + "\n";
                        this.kumpulan_jumlah_buah = this.kumpulan_jumlah_buah + Integer.toString(this.jml_buah) + "\n";
                        this.kumpulan_id_buah = this.kumpulan_id_buah + id + "\n";
                        this.kumpulan_harga_buah = this.kumpulan_harga_buah + hargaBuah + "\n";
                        this.jml_harga = this.jml_harga + ( Integer.parseInt(hargaJualIni) * this.jml_buah );
                        jTextField2.setText("" + Integer.toString(this.jml_harga));

                        // Popup pembelian terhitung
                        JOptionPane.showMessageDialog(null, "Buah berhasil dibeli!");
                    }
                }
            }
        }

        // Hapus Isian JTextField       
        jTextField1.setText("");        
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
        
        
        
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
        
        
        
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        
        this.bayar = Integer.parseInt(jTextField4.getText());
        
        if( this.bayar < this.jml_harga ) {
            JOptionPane.showMessageDialog(null, "Uang Pemabayaran Tidak Mencukupi");
        } else {
            this.kembalian = this.bayar - this.jml_harga;
            jTextField5.setText("" + Integer.toString(this.kembalian));
        }   
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jPasswordField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordField2ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        txIDBuah.setEnabled(false);
        
        String passwordText = String.valueOf(jPasswordField2.getText());
        this.attemps++;
        if( this.attemps > 3 ) {
            jLabel16.setText("Insert Manager's Number!");
        }
        
        if( passwordText.equals("123") && attemps > 3) {
            // Remove Panel
            mainPanel.removeAll();
            mainPanel.repaint();
            mainPanel.revalidate();

            // Add Panel
            mainPanel.add(stokBuahPanel);
            mainPanel.repaint();
            mainPanel.revalidate();
        } else if( passwordText.equals("789") && attemps < 3 ) {
            // Remove Panel
            mainPanel.removeAll();
            mainPanel.repaint();
            mainPanel.revalidate();

            // Add Panel
            mainPanel.add(stokBuahPanel);
            mainPanel.repaint();
            mainPanel.revalidate();
        } else {
            jLabel18.setText("PIN Salah!");
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        
        int i = jTable1.getSelectedRow();
        if (i == -1) {
            return;
        } 
        String id = (String) model.getValueAt(i, 0);
        String nama = txNamaBuah.getText();
        String hargaBeli = txHargaBeli.getText();
        String hargaJual = txHargaJual.getText();
        String stock = txStock.getText();

        try {
            Connection c = koneksi.getBarang();
            String sql = "UPDATE barang SET nama_buah = ?, harga_beli = ?, harga_jual = ?, stok_buah = ? WHERE id_buah = ?";
            try (PreparedStatement p = c.prepareStatement(sql)) {
                p.setString(1, nama);
                p.setString(2, hargaBeli);
                p.setString(3, hargaJual);
                p.setString(4, stock);
                p.setString(5, id);

                p.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Data Terubah");
            btnSimpan.setEnabled(true);
            btnEdit.setEnabled(false);
            btnHapus.setEnabled(false);
            btnBatal.setEnabled(false);
            clear();
        } catch (HeadlessException | SQLException e) {
            System.out.println("Edit Error");
        }finally{
            loadData();
            autonumber();
        }
        
        
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        // TODO add your handling code here:
        clear();
        loadData();
        btnSimpan.setEnabled(true);
        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);
        btnBatal.setEnabled(false);
        autonumber();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        int i = jTable1.getSelectedRow();
        if (i == -1) {
            return;
        }
        
        String id = (String) model.getValueAt(i, 0);
        
        int pernyataan = JOptionPane.showConfirmDialog(null, "Yakin Data Akan Dihapus","Konfirmasi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (pernyataan== JOptionPane.OK_OPTION) {
            try {
                Connection c = koneksi.getBarang();
                String sql = "DELETE FROM barang WHERE id_buah= ?";
                try (PreparedStatement p = c.prepareStatement(sql)) {
                    p.setString(1, id);
                    p.executeUpdate();
                }
                JOptionPane.showMessageDialog(null, "Data Terhapus");
            } catch (HeadlessException | SQLException e) {
                System.out.println("Hapus Error");
            }finally{
                btnSimpan.setEnabled(true);
                btnEdit.setEnabled(false);
                btnHapus.setEnabled(false);
                btnBatal.setEnabled(false);
                loadData();
                autonumber();
                clear();
            }
        }
        if (pernyataan== JOptionPane.CANCEL_OPTION) {
            
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        String id = txIDBuah.getText();
        String nama = txNamaBuah.getText();
        String hargaBeli = txHargaBeli.getText();
        String hargaJual = txHargaJual.getText();
        String stock = txStock.getText();
        
        try {
            Connection c = koneksi.getBarang();
            String sql = "INSERT INTO barang VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement p = c.prepareStatement(sql)) {
                p.setString(1, id);
                p.setString(2, nama);
                p.setString(3, hargaBeli);
                p.setString(4, hargaJual);
                p.setString(5, stock);
                p.executeUpdate();
            }
            JOptionPane.showMessageDialog(null, "Data Tersimpan");
            loadData();
        } catch (HeadlessException | SQLException e) {
            System.out.println("Simpan Error");
        }finally{
            autonumber();
            clear();
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        btnSimpan.setEnabled(false);
        btnEdit.setEnabled(true);
        btnHapus.setEnabled(true);
        btnBatal.setEnabled(true);
        
        int i = jTable1.getSelectedRow();
        if (i == -1) {
            return;
        }
        
        String id = (String) model.getValueAt(i, 0);
        String nama = (String) model.getValueAt(i, 1);
        String hargaBeli = (String) model.getValueAt(i, 2);
        String hargaJual = (String) model.getValueAt(i, 3);
        String stock = (String) model.getValueAt(i, 4);
        
        txIDBuah.setText(id);
        txNamaBuah.setText(nama);
        txHargaBeli.setText(hargaBeli);
        txHargaJual.setText(hargaJual);
        txStock.setText(stock);
    }//GEN-LAST:event_jTable1MouseClicked

    private void txHargaJualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txHargaJualActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txHargaJualActionPerformed

    private void txSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txSearchActionPerformed
        cariData();
    }//GEN-LAST:event_txSearchActionPerformed

    private void txSearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txSearchKeyTyped
        cariData();
    }//GEN-LAST:event_txSearchKeyTyped

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        
        if( this.kembalian < 0 ) {
            JOptionPane.showMessageDialog(null, "Belum Dibayar Customer");
        } else {
            // Remove Panel
                mainPanel.removeAll();
                mainPanel.repaint();
                mainPanel.revalidate();

                // Add Panel
                mainPanel.add(cetakStruk);
                mainPanel.repaint();
                mainPanel.revalidate();

                jLabel36.setText("" + this.waktu);
                jLabel41.setText("" + this.tanggal);

                jLabel45.setText("" + Integer.toString(this.jml_harga));
                jLabel46.setText("" + jTextField4.getText());
                jLabel47.setText("" + jTextField5.getText());

                this.id_transaksi = this.id_transaksi + 1;
                jTextField7.setText("" + this.id_transaksi);

                // Print PDF
                Document document = new Document();
            try {
                PdfWriter.getInstance(document,new FileOutputStream("StrukTransaksi.pdf"));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DocumentException ex) {
                Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            }
                document.open();  
            try {
                document.add(new Paragraph("============ Struk Rumah Buah ===========\n"));
                document.add(new Paragraph("\t\t" + this.waktu + "\n"));
                document.add(new Paragraph("\t\t" + this.tanggal + "\n"));
                document.add(new Paragraph("=======================================\n"));
                document.add(new Paragraph("ID Transaksi : " + this.id_transaksi + "\n"));
                document.add(new Paragraph("=======================================\n"));
                document.add(new Paragraph("Nama Buah : \n" + this.kumpulan_nama_buah));
                document.add(new Paragraph("=======================================\n"));
                document.add(new Paragraph("Jumlah Buah : \n" + this.kumpulan_jumlah_buah));
                document.add(new Paragraph("=======================================\n"));
                document.add(new Paragraph("Total Beli  \t: " + this.jml_harga + "\n"));
                document.add(new Paragraph("Total Bayar \t: " + jTextField4.getText() + "\n"));
                document.add(new Paragraph("Kembalian   \t: " + jTextField5.getText() + "\n"));
                document.add(new Paragraph("=======================================\n"));
                document.add(new Paragraph("=           Terima Kasih Atas Kunjungan Anda         =\n"));
                document.add(new Paragraph("=======================================\n"));
            } catch (DocumentException ex) {
                Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            }
                document.close(); 
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        // Remove Panel
            mainPanel.removeAll();
            mainPanel.repaint();
            mainPanel.revalidate();

            // Add Panel
            mainPanel.add(transaksiPanel);
            mainPanel.repaint();
            mainPanel.revalidate();
            
            jTextField2.setText("");
            jTextField3.setText("");
            jTextField4.setText("");
            jTextField5.setText("");
            jTextArea2.setText("");
            jTextArea3.setText("");
            
            this.kumpulan_nama_buah = "";
            this.kumpulan_jumlah_buah = "";
            this.kumpulan_id_buah = "";
            this.kumpulan_harga_buah = "";
            
            this.jml_harga = 0;
            this.bayar = 0;
            this.jml_buah = 0;
            this.kembalian = 0;
            this.total_jumlah_buah = 0;
    }//GEN-LAST:event_jButton11ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainView().setVisible(true);
            }
        });
    }    
        

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel berandaPanel;
    private javax.swing.JPanel bodyPanel;
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JPanel cetakStruk;
    private javax.swing.JLabel daftarNamaBuah;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JTextArea jTextArea5;
    private javax.swing.JTextArea jTextArea6;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JList<String> list;
    private javax.swing.JPanel listBuahPanel;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JPanel stokBuahPanel;
    private javax.swing.JPanel totalanPanel;
    private javax.swing.JPanel transaksiPanel;
    private javax.swing.JTextField txHargaBeli;
    private javax.swing.JTextField txHargaJual;
    private javax.swing.JTextField txIDBuah;
    private javax.swing.JTextField txNamaBuah;
    private javax.swing.JTextField txSearch;
    private javax.swing.JTextField txStock;
    // End of variables declaration//GEN-END:variables
}
