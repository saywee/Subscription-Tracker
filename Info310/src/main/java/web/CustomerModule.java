/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import dao.CustomerDAO;
import domain.Customer;
import org.jooby.Jooby;
import org.jooby.Result;
import org.jooby.Status;

/**
 *
 * @author yeah2
 */
public class CustomerModule extends Jooby{
    public CustomerModule(CustomerDAO customerDao){
        /*get("/api/customers/:username", () -> customerDao.getCustomer(username));
        */get("/api/customers/:username", (req) -> {
            String username = req.param("username").value();
            if(customerDao.getCustomer(username) == null){
                return new Result().status(Status.NOT_FOUND);
            }else{
                return customerDao.getCustomer(username);
            }
        });
        post("/api/register", (req, rsp) -> {
            Customer customer = req.body().to(Customer.class);
            customerDao.saveCustomer(customer);
            rsp.status(Status.CREATED);
        });
        
        put("/api/customers/:username", (req, rsp) -> {
            String username = req.param("username").value();
            Customer customer = req.body().to(Customer.class);
//            Customer customer = customerDao.getCustomer(username);

            customerDao.updateCustomer(customer);
            rsp.status(Status.NO_CONTENT);
        });
        
        delete("/api/customers/:username", (req, rsp) -> {
            String username = req.param("username").value();
            Customer customer = customerDao.getCustomer(username);

            customerDao.deleteCustomer(customer);
            rsp.status(Status.NO_CONTENT);
        });
    }
}
