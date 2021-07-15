/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.Customer;
import domain.Subscription;
import helpers.ScryptHelper;
import java.math.BigDecimal;
import java.util.Collection;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import net.sf.oval.constraint.AssertTrueCheck;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

/**
 *
 * @author Luke Tang
 */
public class CustomerDAOTest {

    private CustomerDAO CustDAO;

    private Customer cust1;
    private Customer cust2;

    private String url = "jdbc:h2:mem:tests;INIT=runscript from 'src/main/java/dao/schema.sql'";
    private String resetCustomer = "alter table Customer alter column Customer_ID restart with 1";

    @BeforeEach
    public void setUp() {
//        CustDAO = new CustomerCollectionsDAO();

        // Currently, only the saveCustomer() test works with the JDBC DAO 
        // though this requires you to comment out everything in tearDown() 
        // and the entirety of the other test methods
        CustDAO = new CustomerJdbcDAO(url);

        this.cust1 = new Customer();
        //This customer setup becomes redundant due to my set up
        cust1.setFirstName("Taine");
        cust1.setLastName("Bayly");
        cust1.setUsername("bayta267");
        cust1.setPassword("INFO310");
        cust1.setPhoneNumber("0273842");
        cust1.setEmailAddress("bayta@student.com");
        cust1.setCustomerId(1);

        this.cust2 = new Customer();
        cust2.setFirstName("ne");
        cust2.setLastName("ly");
        cust2.setUsername("a267");
        cust2.setPassword("1234");
        cust2.setPhoneNumber("042");
        cust2.setEmailAddress("tudent.com");
        cust2.setCustomerId(2);

        CustDAO.saveCustomer(cust1);
        //puposely didnt save cust2
    }

    @AfterEach
    public void tearDown() {
        CustDAO.deleteCustomer(cust1);
        CustDAO.deleteCustomer(cust2);

        // reset auto increment
        try (
                Connection dbCon = DbConnection.getConnection(url);
                PreparedStatement stmt1 = dbCon.prepareStatement(resetCustomer);) {
            stmt1.execute();
        } catch (SQLException ex) {
            throw new DAOException(ex.getMessage(), ex);
        }
    }

    @Test
    public void testSaveCustomer() {

        Customer retrieved = CustDAO.getCustomer(cust1.getUsername());
        assertThat("check if cust1 save was succesful in setup", cust1, samePropertyValuesAs(retrieved, "customerId", "password"));

        CustDAO.saveCustomer(cust2);
        Customer retrievedNext = CustDAO.getCustomer(cust2.getUsername());
        assertThat("check if cust2 was successfully saved to dao", cust2, samePropertyValuesAs(retrievedNext, "customerId", "password"));
        
        // check that hashed passwords are the same
        String hash = retrieved.getPassword();
        assertTrue(ScryptHelper.check(hash, cust1.getPassword()));
        
        String hashNext = retrievedNext.getPassword();
        assertTrue(ScryptHelper.check(hashNext, cust2.getPassword()));
    }

    @Test
    public void testGetCustomer() {

        //assertTrue("cust1 should exist", CustDAO.contains(cust1));
        CustDAO.saveCustomer(cust2);
        Customer secondRetreieved = CustDAO.getCustomer("a267");

        //Manual checks of each function
        assertEquals(cust2.getFirstName(), secondRetreieved.getFirstName());
        assertEquals(cust2.getLastName(), secondRetreieved.getLastName());
        assertEquals(cust2.getUsername(), secondRetreieved.getUsername());
//        assertEquals(cust2.getPassword(), secondRetreieved.getPassword());
        assertEquals(cust2.getPhoneNumber(), secondRetreieved.getPhoneNumber());
        assertEquals(cust2.getEmailAddress(), secondRetreieved.getEmailAddress());
//        assertEquals(cust2.getCustomerId(), secondRetreieved.getCustomerId());

        // Check that hashed password is the same
        String hash = secondRetreieved.getPassword();
        assertTrue(ScryptHelper.check(hash, cust2.getPassword()));
    }

    @Test
    public void testValidateCredentials() {
        CustDAO.saveCustomer(cust2);
        assertTrue(CustDAO.validateCredentials("a267", "1234"));
        assertFalse(CustDAO.validateCredentials("Taine", "Bayly"));
    }

    @Test
    public void testDeleteCustomer() {
        CustDAO.deleteCustomer(cust1);
        assertNull(CustDAO.getCustomer("bayta267"));
    }
}
