/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.Customer;
import web.auth.CredentialsValidator;

/**
 *
 * @author Luke Tang
 */
public interface CustomerDAO extends CredentialsValidator {

    void saveCustomer(Customer customer);

    Customer getCustomer(String username);

//    Boolean validateCredentials(String username, String password);
    
    void deleteCustomer(Customer customer);
    
    void updateCustomer(Customer customer);
}
