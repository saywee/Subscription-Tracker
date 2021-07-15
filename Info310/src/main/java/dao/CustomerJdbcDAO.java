/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.Customer;
import helpers.ScryptHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
///**
// *
// * @author trbay
// */
public class CustomerJdbcDAO implements CustomerDAO {
     private String url = "jdbc:h2:tcp://localhost/info310proj";

    public CustomerJdbcDAO() {
    }

    public CustomerJdbcDAO(String url) {

        this.url = url;
    }

    @Override
    public void saveCustomer(Customer customer) {
        String sql = "insert into Customer (Username, "
                + "Firstname, Lastname, Password, Phone_Number, Email_Address) "
                + "values (?,?,?,?,?,?)";

        try (
                Connection dbCon = DbConnection.getConnection(url);
                PreparedStatement stmt = dbCon.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            //ResultSet rs = stmt.getGeneratedKeys();

            stmt.setString(1, customer.getUsername());
            stmt.setString(2, customer.getFirstName());
            stmt.setString(3, customer.getLastName());
            stmt.setString(4, ScryptHelper.hash(customer.getPassword()).toString());
            stmt.setString(5, customer.getPhoneNumber());
            stmt.setString(6, customer.getEmailAddress());
            

            stmt.executeUpdate();
            System.out.println("Saving customer: " + customer);

        } catch (SQLException ex) {
            throw new DAOException(ex.getMessage(), ex);
        }
    }

    @Override
    public Customer getCustomer(String username) {
         String sql = "select * from Customer where Username = ?";
        try (
                Connection dbCon = DbConnection.getConnection(url);
                PreparedStatement stmt = dbCon.prepareStatement(sql);) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt("Customer_ID");
                String user_name = rs.getString("Username");
                String password = rs.getString("Password");
                String firstname = rs.getString("Firstname");
                String lastname = rs.getString("Lastname");
                String phoneNumber = rs.getString("Phone_Number");
                String emailAddress = rs.getString("Email_Address");
           
                Customer cust1 = new Customer(id, username, firstname, lastname, password, phoneNumber, emailAddress);
                return cust1;
            }
            return null;

        } catch (SQLException ex) {
            throw new DAOException(ex.getMessage(), ex);
        }
    }

    @Override
    public Boolean validateCredentials(String username, String password) {
         String sql = "select Password from Customer where Username = ?";
        try (
                Connection dbCon = DbConnection.getConnection(url);
                PreparedStatement stmt = dbCon.prepareStatement(sql);) {
            stmt.setString(1, username);
            
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hash = rs.getString("password");

                // check that the password matches the hash
                return ScryptHelper.check(hash, password);
//                return true;
            } else {
                // no matching username
                return false;
            }

        } catch (SQLException ex) {
            throw new DAOException(ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteCustomer(Customer customer) {
         String sql = "delete from Customer where Username = ?";
        try(
            // get a connection to the database
            Connection dbCon = DbConnection.getConnection(url);

            // create the statement
            PreparedStatement stmt = dbCon.prepareStatement(sql);
        ) {
            stmt.setString(1, customer.getUsername());
            stmt.executeUpdate();  // execute the statement
            
        }catch(SQLException ex){
            throw new DAOException(ex.getMessage(), ex);
        }
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateCustomer(Customer customer) {
        String sql = "update Customer "
                + "set Firstname = ?, Lastname = ?, Password = ?, Phone_Number = ?,"
                + "Email_Address = ?"
                + "where Customer_ID = ?";
        try (
                // get a connection to the database
                 Connection dbCon = DbConnection.getConnection(url); // create the statement
                  PreparedStatement stmt = dbCon.prepareStatement(sql);) {
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, ScryptHelper.hash(customer.getPassword()).toString());
            stmt.setString(4, customer.getPhoneNumber());
            stmt.setString(5, customer.getEmailAddress());
            stmt.setInt(6, customer.getCustomerId());
            stmt.executeUpdate();  // execute the statement

        } catch (SQLException ex) {
            throw new DAOException(ex.getMessage(), ex);
        }
    }
}
