package dao;

import domain.Customer;
import java.util.HashMap;
import java.util.Map;

public final class CustomerCollectionsDAO implements CustomerDAO {

    private static final Map<String, Customer> customers = new HashMap<>();

    public CustomerCollectionsDAO() {
        // some dummy data for testing
//        Customer boris = new Customer();
//        boris.setUsername("boris");
//        boris.setFirstName("Boris");
//        boris.setLastName("McNorris");
//        boris.setPassword("guest");
//        boris.setPhoneNumber("123456789");
//        boris.setEmailAddress("boris@example.net");
//
//        Customer doris = new Customer();
//        doris.setUsername("doris");
//        doris.setFirstName("Doris");
//        doris.setLastName("Dolores");
//        doris.setPassword("guest");
//        doris.setPhoneNumber("987654321");
//        doris.setEmailAddress("doris@example.net");
//
//        saveCustomer(boris);
//        saveCustomer(doris);
    }

    @Override
    public void saveCustomer(Customer customer) {
        System.out.println("Saving customer: " + customer);
        customers.put(customer.getUsername(), customer);
    }

    @Override
    public Customer getCustomer(String username) {
        return customers.get(username);
    }

    @Override
    public Boolean validateCredentials(String username, String password) {
        if (customers.containsKey(username)) {
            return customers.get(username).getPassword().equals(password);
        } else {
            return false;
        }
    }

    @Override
    public void deleteCustomer(Customer customer) {
        System.out.println("Deleting customer: " + customer);
        customers.remove(customer.getUsername());
    }

    @Override
    public void updateCustomer(Customer customer) {
        customers.put(customer.getUsername(), customer);
    }
    
}
