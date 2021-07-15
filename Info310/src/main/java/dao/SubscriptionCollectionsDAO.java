/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import domain.Subscription;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author Luke Tang
 */
public class SubscriptionCollectionsDAO implements SubscriptionDAO {

    private static final Multimap<String, Subscription> subscriptions
            = HashMultimap.create();
    private static Collection<String> categories = new HashSet<>();
    private static Multimap<String, Subscription> mmSub = HashMultimap.create();

    @Override
    public void saveSubscription(Subscription subscription) {
        subscriptions.put(subscription.getCustomer().getUsername(), 
                subscription);
        categories.add(subscription.getCategory());
        mmSub.put(subscription.getCategory(), subscription);
    }

    @Override
    public Collection<Subscription> getSubscriptionsByUsername(String username) {
        System.out.println(subscriptions.get(username));
        return subscriptions.get(username);
    }

    @Override
    public Subscription getSubscriptionById(Integer id) {
        // loop through stored subscriptions and find the one with the right ID
        for (Subscription sub : subscriptions.values()) {
            if (sub.getSubscriptionId().equals(id)) {
                return sub;
            }
        }
        // ***there is probably a better way to find a matching sub***
        return null; // if no matching sub was found
    }

    @Override
    public void deleteSubscription(Subscription subscription) {
        if (subscriptions.containsKey(subscription.getCustomer().getUsername())) {
            subscriptions.remove(subscription.getCustomer().getUsername(), subscription);
        }
    }

    @Override
    public void updateSubscription(Subscription subscription) {
        subscriptions.put(subscription.getCustomer().getUsername(), subscription);
    }
    
    @Override
    public Collection<String> getCategories(String username){
        
        return categories;
    }
    
    @Override
    public Collection<Subscription> filterByCategory(String category, String username){
        Collection<Subscription> subs = mmSub.get(category);
        return subs;
    }
    
    @Override
    public BigDecimal getTotal(String Username){
        BigDecimal total = new BigDecimal(0);
        
        return total;
    }
    
    @Override
    public Collection<Subscription> sortAscending(String username){
        
        
        return subscriptions.values();
    }
}
