/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.aplikasitokobuah;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 *
 * @author ThinkPad L450 i5
 */
public class AplikasiTokoBuah {

    public static void main(String[] args) throws DocumentException, FileNotFoundException {
            // Print PDF
            Document document = new Document();
            PdfWriter.getInstance(document,new FileOutputStream("hello.pdf"));
            document.open();  
            document.add(new Paragraph("Hello world this is my first PDF"));
            document.close(); 
    }
}
