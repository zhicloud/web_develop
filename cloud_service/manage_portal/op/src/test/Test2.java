package test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import net.sf.json.JSONObject;

import com.zhicloud.op.app.helper.RegionHelper;
import com.zhicloud.op.app.helper.RegionHelper.RegionData;
import com.zhicloud.op.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.op.httpGateway.HttpGatewayManager;

public class Test2 {
  //Driver name and database URL    
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/test?zeroDateTimeBehavior=convertToNull";

    static final String username = "root";
    static final String password = "root";

    public static void main(String[] args) {

    Connection conn = null;
    Statement stmnt = null;

        try {

            System.out.println("Connecting...");
            // Open connection
            conn = DriverManager.getConnection(DB_URL, username, password);

            // Execute query
            System.out.println("Creating statement...");
            stmnt = conn.createStatement();
            String sql = "UPDATE customer_test_accounts " + "SET balance = balance + 1000 " + "WHERE accountnumber = 2";
            stmnt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }        
    }
    public static int specislChar(String s){
        int counter = 0;
        char ch;
        for (int i =0 ; i<=s.length(); i++){
            ch = s.charAt(i);
            if (!Character.isLetterOrDigit(ch) || ch != ' ') {
                System.out.print(" " + ch);
                counter++;
            }           
        }

        return counter;
    } 
}
