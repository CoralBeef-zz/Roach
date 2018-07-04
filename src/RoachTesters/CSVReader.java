/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RoachTesters;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author donne
 */
public class CSVReader {
    public static void main(String[] args) {

        String csvFile = "lechengshrinp.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://192.168.10.20:3306/bit_shopping?useUnicode=true&characterEncoding=utf-8"; 
            Connection conn = DriverManager.getConnection(url,"root",""); 
            Statement stat = conn.createStatement();
            
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] c = line.split(cvsSplitBy);
                String que = "";
                que+= "UPDATE products SET name_en = '"+c[1]+"', detail_en = '"+c[2]+"' WHERE id = "+c[0]+";";
                System.out.println(que);
                //stat.executeUpdate(que);
            }
            
        } catch (ClassNotFoundException | SQLException e) { 
            System.err.println(e.getMessage()); 
        } catch (FileNotFoundException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
            
    

}
