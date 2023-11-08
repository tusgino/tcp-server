import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class Connect {

  String url = "jdbc:mysql://localhost:3306/test";
  String username = "root";
  String password = "";
  Connection con;

  public Connect() {
    try {
      // Updated driver class name
      Class.forName("com.mysql.cj.jdbc.Driver");
      con = DriverManager.getConnection(url, username, password);
      System.out.println("Connected");
    } catch (ClassNotFoundException e) {
      System.out.println("MySQL JDBC Driver not found. Add it to your build path.");
      e.printStackTrace();
      throw new IllegalStateException("MySQL JDBC Driver not found.", e);
    } catch (SQLException e) {
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
      throw new IllegalStateException("Cannot connect the database!", e);
    }
  }


  // Execute a query and return a ResultSet
  public ResultSet executeQuery(String sql) {
    try {
      Statement stmt = con.createStatement();
      return stmt.executeQuery(sql);
    } catch (SQLException e) {
      throw new RuntimeException("Error executing query: " + sql, e);
    }
  }

  // Execute an update and return the number of affected rows
  public int executeUpdate(String sql) {
    try {
      Statement stmt = con.createStatement();
      return stmt.executeUpdate(sql);
    } catch (SQLException e) {
      throw new RuntimeException("Error executing update: " + sql, e);
    }
  }

  // Close the connection
  public void close() {
    try {
      if (con != null && !con.isClosed()) {
        con.close();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error closing connection", e);
    }
  }
}
