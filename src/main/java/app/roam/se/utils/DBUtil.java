package app.roam.se.utils;

import app.roam.se.App;
import app.roam.se.models.test.TestData;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBUtil {
    public final static String DATABASENAME ="Data.db";
    public static Connection getConnection() {

        try {

            boolean create_table = false;
            if (!new File(App.testProject.getLocation() + "\\" + DATABASENAME).exists()) {
                create_table = true;
            }

            Connection conn = null;
            conn = DriverManager.getConnection("jdbc:sqlite:" + App.testProject.getLocation() + "\\" + DATABASENAME);
            // System.out.println("Connected to database");
            if (create_table) {
                createTables();
                System.out.println("Tables created!");
            }
            return conn;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }

    }
    private static void createTables(){
        try {
            Statement stmt = getConnection().createStatement();

            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS DataTable ("+
                   "         id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                   "         category TEXT NOT NULL,"+
                   "         subcategory TEXT NOT NULL,"+
                   "         datagroup TEXT NOT NULL,"+
                   "         label TEXT NOT NULL,"+
                   "         value TEXT NOT NULL"+
                   " );"

            );

        } catch (SQLException e) {
            // TODO Auto-generated catch block

        }
    }

    public static String insertData(TestData td){
        try {

            PreparedStatement st = getConnection().prepareStatement(
                    "INSERT INTO DataTable (category,subcategory,datagroup,label,value) VALUES(?,?,?,?,?)"

            );
            st.setString(1, td.getCategory());
            st.setString(2, td.getSubCategory());
            st.setString(3, td.getDataGroup());
            st.setString(4, td.getLabel());
            st.setString(5, td.getValue());

            st.executeUpdate();
            st.close();
            return "Added Object " + td.getLabel() + " to Data table successfully!";

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "Encountered an error in adding data  to the Data table: " + e.getMessage();
        }
    }

    public static String updateData(TestData td){
        try {

            PreparedStatement st = getConnection().prepareStatement(
                    "UPDATE DataTable SET category = ?, subcategory =?, datagroup =?, label =?, value =? WHERE id=?"

            );
            st.setString(1, td.getCategory());
            st.setString(2, td.getSubCategory());
            st.setString(3, td.getDataGroup());
            st.setString(4, td.getLabel());
            st.setString(5, td.getValue());
            st.setInt(6, td.getId());
            st.executeUpdate();
            st.close();
            return "Updated data " + td.getLabel() + " successfully!";

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "Encountered an error in updating Data table: " + e.getMessage();
        }
    }

    public static List<TestData> getAllData() {
        try {
            Statement stmt = getConnection().createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM DataTable");
            List<TestData> tds = new ArrayList<TestData>();
            while (rs.next()) {
                TestData td = new TestData();
                td.setId(rs.getInt(TestData.ID));
                td.setCategory(rs.getString(TestData.CATEGORY));
                td.setSubCategory(rs.getString(TestData.SUBCATEGORY));
                td.setDataGroup(rs.getString(TestData.DATAGROUP));
                td.setLabel(rs.getString(TestData.LABEL));
                td.setValue(rs.getString(TestData.VALUE));
                tds.add(td);
            }
            stmt.close();

            return tds;
        } catch (SQLException e) {
            System.out.println(e);
            return new ArrayList<TestData>();
        }
    }

    public static TestData getData(int id) {
        try {

            PreparedStatement stmt = getConnection().prepareStatement(
                    "SELECT * FROM DataTable WHERE id=?"
            );
            stmt.setInt(1, id);


            ResultSet rs = stmt.executeQuery();
            TestData td = new TestData();
            if (rs.next()) {

                td.setId(rs.getInt(TestData.ID));
                td.setCategory(rs.getString(TestData.CATEGORY));
                td.setSubCategory(rs.getString(TestData.SUBCATEGORY));
                td.setDataGroup(rs.getString(TestData.DATAGROUP));
                td.setLabel(rs.getString(TestData.LABEL));
                td.setValue(rs.getString(TestData.VALUE));

            } else {
                td = null;
            }
            stmt.close();
            return td;
        } catch (SQLException e) {

            return null;
        }
    }

    public static String deleteData(int id) {
        try {

            PreparedStatement st = getConnection().prepareStatement("DELETE FROM DataTable  WHERE id =?");
            st.setInt(1, id);

            st.executeUpdate();
            st.close();
            return "Deleted data from datatable " + id + " successfully!";

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "Encountered an error in deliting data from Data Table : " + e.getMessage();
        }
    }

    public static  List<String> getDistinctVariants() {
        try {

            PreparedStatement stmt = getConnection().prepareStatement(
                    "SELECT DISTINCT category,subcategory FROM DataTable"
            );
            ResultSet rs = stmt.executeQuery();
            List<String> variants = new ArrayList<String>();

            while (rs.next()) {
                String v = rs.getString(1) + "." + rs.getString(2);
                variants.add(v);
            }
            stmt.close();
            return variants;
        } catch (SQLException e) {
            System.out.println(e);
            return new ArrayList<String>();
        }
    }
}
