/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package INFO310;

import dao.CustomerDAO;
import dao.CustomerJdbcDAO;
import dao.SubscriptionCollectionsDAO;
import dao.SubscriptionDAO;
import dao.SubscriptionJdbcDAO;
import domain.Customer;
import domain.Subscription;
import java.math.BigDecimal;

/**
 *
 * @author yeah2
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Starting Main.java...");    
        System.out.println();
        
        // Create customer objects using data from test classes
        Customer cust1 = new Customer();
        cust1.setFirstName("Taine");
        cust1.setLastName("Bayly");
        cust1.setUsername("bayta267");
        cust1.setPassword("INFO310");
        cust1.setPhoneNumber("0273842");
        cust1.setEmailAddress("bayta@student.com");
        cust1.setCustomerId(1);
        
        Customer cust2 = new Customer();
        cust2.setFirstName("ne");
        cust2.setLastName("ly");
        cust2.setUsername("a267");
        cust2.setPassword("1234");
        cust2.setPhoneNumber("042");
        cust2.setEmailAddress("tudent.com");
        cust2.setCustomerId(2);

        // Create subscription objects using data from test classes
        Subscription sub1 = new Subscription();
        sub1.setName("Netflix");
        sub1.setSubscriptionId(123);
        sub1.setPaid(false);
        sub1.setCategory("Leisure");
        sub1.setSubscriptionPrice(BigDecimal.TEN);
        sub1.setDescription("Movies and TV");
        sub1.setCompanyName("Netflix Inc.");
        // issue date and due dates are set to default values
        sub1.setCustomer(cust1);

        Subscription sub2 = new Subscription();
        sub2.setName("flix");
        sub2.setSubscriptionId(1234);
        sub2.setPaid(false);
        sub2.setCategory("Lei");
        sub2.setSubscriptionPrice(BigDecimal.TEN);
        sub2.setDescription("TV");
        sub2.setCompanyName("flix Inc.");
        // issue date and due dates are set to default values
        sub2.setCustomer(cust1);
        
        Subscription sub3 = new Subscription();
        sub2.setName("hbo");
        sub2.setSubscriptionId(1235);
        sub2.setPaid(false);
        sub2.setCategory("Lei");
        sub2.setSubscriptionPrice(BigDecimal.TEN);
        sub2.setDescription("TV");
        sub2.setCompanyName("flix Inc.");
        // issue date and due dates are set to default values
        sub2.setCustomer(cust1);
        
        // Create DAOs
        CustomerDAO customerDao = new CustomerJdbcDAO();
        SubscriptionDAO subscriptionDao = new SubscriptionJdbcDAO();
        
        // Test customer DAO's save method
        System.out.println("Saving cust1 and cust2...");
        customerDao.saveCustomer(cust1);
        customerDao.saveCustomer(cust2);
        System.out.println();
        
        // Test DAO's get customer method
        System.out.print("Cust 1 details: ");
        System.out.println(customerDao.getCustomer(cust1.getUsername()));
        
        System.out.print("Cust 2 details: ");
        System.out.println(customerDao.getCustomer(cust2.getUsername()));
        System.out.println();
        
        // Test DAO's delete customer method
        System.out.println("Deleting cust2 with username " + cust2.getUsername() + "...");
        customerDao.deleteCustomer(cust2);
        
        System.out.print("Cust 2 details: ");
        System.out.println(customerDao.getCustomer(cust2.getUsername()));
        System.out.println();
        
        // Test DAO's validate credentials method
        System.out.println("Validating credentials with "
                + "correct and incorrect password...");
        System.out.println(customerDao.validateCredentials(cust1.getUsername(), 
                cust1.getPassword()));
        System.out.println(customerDao.validateCredentials(cust1.getUsername(), 
                "wrong"));
        System.out.println();
        
        // Test subscription DAO's save operation
        System.out.println("Saving sub1 and sub2...");
        subscriptionDao.saveSubscription(sub1);
        subscriptionDao.saveSubscription(sub2);

        System.out.print("Subscriptions belonging to " + cust1.getUsername() + ": ");
        System.out.println(subscriptionDao.getSubscriptionsByUsername(cust1.getUsername()));
        System.out.println();
        
        // Test subscription DAO's delete operation
        System.out.println("Deleting sub2 from " + sub2.getCustomer().getUsername() + "...");
        subscriptionDao.deleteSubscription(sub2);
        
        System.out.print("Subscriptions belonging to " + sub2.getCustomer().getUsername() + ": ");
        System.out.println(subscriptionDao.getSubscriptionsByUsername(sub2.getCustomer().getUsername()));
        System.out.println();
        
        System.out.print("Subscriptions based on category: " + sub3.getCategory() + " from customer: " + sub3.getCustomer().getUsername());
        System.out.println(subscriptionDao.filterByCategory(sub3.getCategory(), sub3.getCustomer().getUsername()));
        System.out.println();
    }
    
}
