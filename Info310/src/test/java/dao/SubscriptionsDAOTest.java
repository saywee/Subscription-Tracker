/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.Customer;
import domain.Subscription;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import org.hamcrest.beans.SamePropertyValuesAs;

/**
 *
 * @author trbay
 */
public class SubscriptionsDAOTest {

    private CustomerDAO custDAO;
    private SubscriptionDAO subDAO;

    private Customer cust1;
    private Customer cust2;

    private Subscription sub1;
    private Subscription sub2;
    private Subscription sub3;

    private String url = "jdbc:h2:mem:tests;INIT=runscript from 'src/main/java/dao/schema.sql'";
    private String resetCustomer = "alter table Customer alter column Customer_ID restart with 1";
    private String resetSubscription = "alter table Subscription alter column Subscription_ID restart with 1000";

    @BeforeEach
    public void setUp() {

//        subDAO = new SubscriptionCollectionsDAO();
        
        // Haven't tested the JDBC DAO with any tests yet, but assume that will
        // all fail until shown otherwise
        custDAO = new CustomerJdbcDAO(url);
        subDAO = new SubscriptionJdbcDAO(url);

        cust1 = new Customer();
        cust1.setFirstName("Taine");
        cust1.setLastName("Bayly");
        cust1.setUsername("bayta267");
        cust1.setPassword("INFO310");
        cust1.setPhoneNumber("0273842");
        cust1.setEmailAddress("bayta@student.com");
        cust1.setCustomerId(1);

        cust2 = new Customer();
        cust2.setFirstName("Luke");
        cust2.setLastName("Tang");
        cust2.setUsername("tanlu824");
        cust2.setPassword("INFO310");
        cust2.setPhoneNumber("0276292");
        cust2.setEmailAddress("tanlu@student.com");
        cust2.setCustomerId(2);

        // comment out if using collections
        custDAO.saveCustomer(cust1);
        custDAO.saveCustomer(cust2);

        sub1 = new Subscription();
        this.sub1.setName("Netflix");
        this.sub1.setSubscriptionId(1000);
        this.sub1.setPaid(false);
        this.sub1.setCategory("Leisure");
        this.sub1.setSubscriptionPrice(BigDecimal.TEN);
        this.sub1.setDescription("Movies and TV");
        this.sub1.setCompanyName("Netflix Inc.");
        this.sub1.setCustomer(cust1);

        sub2 = new Subscription();
        this.sub2.setName("flix");
        this.sub2.setSubscriptionId(1001);
        this.sub2.setPaid(false);
        this.sub2.setCategory("Lei");
        this.sub2.setSubscriptionPrice(BigDecimal.TEN);
        this.sub2.setDescription("TV");
        this.sub2.setCompanyName("flix Inc.");
        this.sub2.setCustomer(cust1);

        sub3 = new Subscription();
        this.sub3.setName("Shares");
        this.sub3.setSubscriptionId(1002);
        this.sub3.setPaid(true);
        this.sub3.setCategory("Business");
        this.sub3.setSubscriptionPrice(BigDecimal.TEN);
        this.sub3.setDescription("Investing");
        this.sub3.setCompanyName("Sharsies");
        this.sub3.setCustomer(cust2);

        subDAO.saveSubscription(sub1);
        subDAO.saveSubscription(sub2);

    }

    @AfterEach
    public void tearDown() {
        subDAO.deleteSubscription(sub1);
        subDAO.deleteSubscription(sub2);
        subDAO.deleteSubscription(sub3);

        // comment out if using collections
        custDAO.deleteCustomer(cust1);
        custDAO.deleteCustomer(cust2);

        // reset auto increment
        try (
                Connection dbCon = DbConnection.getConnection(url);
                PreparedStatement stmt1 = dbCon.prepareStatement(resetCustomer);
                PreparedStatement stmt2 = dbCon.prepareStatement(resetSubscription);) {
            stmt1.execute();
            stmt2.execute();
        } catch (SQLException ex) {
            throw new DAOException(ex.getMessage(), ex);
        }
    }

    @Test
    public void testGetSubscriptionByUsername() {

        Collection<Subscription> collection = 
                subDAO.getSubscriptionsByUsername(sub1.getCustomer().getUsername());
        //assertThat(subDAO.hasItem(1));
        assertThat(collection, hasItem(sub1));
        assertThat(collection, hasItem(sub2));
        assertFalse(collection.contains(sub3));
    }
    
    @Test
    public void testGetSubscriptionById() {
        assertThat(subDAO.getSubscriptionById(1000), samePropertyValuesAs(sub1, "subscriptionPrice"));
        assertThat(subDAO.getSubscriptionById(1001), samePropertyValuesAs(sub2, "subscriptionPrice"));
        assertNull(subDAO.getSubscriptionById(1002));
    }

    @Test
    public void testSaveSubscription() {
        //Testing a new user saving subscription, whilst another user exists already. Test of user Independence
        subDAO.saveSubscription(sub3);
        Collection<Subscription> collection = 
                subDAO.getSubscriptionsByUsername(sub3.getCustomer().getUsername());
        assertTrue(collection.contains(sub3));
        assertFalse(collection.contains(sub2));
        assertFalse(collection.contains(sub1));
        System.out.println(sub3);

        //Testing the other independent user has saved items from setup
        Collection<Subscription> collection2 = 
                subDAO.getSubscriptionsByUsername(sub1.getCustomer().getUsername());
        assertThat(collection2, hasItem(sub1));
        assertThat(collection2, hasItem(sub2));
        assertFalse(collection2.contains(sub3));

    }

    @Test
    public void testDeleteSubscription() {
        subDAO.deleteSubscription(sub1);
        Collection<Subscription> collection = 
                subDAO.getSubscriptionsByUsername(sub1.getCustomer().getUsername());
        assertTrue(collection.contains(sub2));
        assertFalse(collection.contains(sub1));
        //assertNull(CustDAO.getCustomer("bayta267"));
        //assertThat(1 + 1, equalTo(2));
    }

    @Test
    public void testUpdateSubscription() {
        sub1.setDescription("SUBS");

        subDAO.updateSubscription(sub1);

        Collection<Subscription> collection = subDAO.getSubscriptionsByUsername(sub1.getCustomer().getUsername());

        assertTrue(collection.contains(sub1));
        assertThat(collection, hasSize(2));
        //assertThat(1 + 1, equalTo(2));
    }

}
