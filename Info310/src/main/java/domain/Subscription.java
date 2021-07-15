/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author yeah2
 */
public class Subscription {

    private Integer subscriptionId = 0;
    private String name = "defaultName";
    private Boolean paid = true; // rename this to paid?
    private String category = "defaultCategory";
    private BigDecimal subscriptionPrice = new BigDecimal(0);
    private String description = "defaultDescription";
    private String companyName = "defaultCompanyName";
    private String dueDate = LocalDate.now().plusDays(30).toString();
    private String issueDate = LocalDate.now().toString();

    private Customer customer;

    public Subscription() {
    }

    public Subscription(Integer subscriptionId, String name, Boolean paid, String category, BigDecimal subscriptionPrice, String description, String companyName, String dueDate, String issueDate, Integer customerId) {
        this.subscriptionId = subscriptionId;
        this.name = name;
        this.paid = paid;
        this.category = category;
        this.subscriptionPrice = subscriptionPrice;
        this.description = description;
        this.companyName = companyName;
        this.dueDate = dueDate;
        this.issueDate = issueDate;
        customerId = customer.getCustomerId();
    
}
@Override
        public String toString() {
        return "Subscription{" + "subscriptionId=" + subscriptionId + ", name=" + name + ", paid=" + paid + ", category=" + category + ", subscriptionPrice=" + subscriptionPrice + ", description=" + description + ", companyName=" + companyName + ", dueDate=" + dueDate + ", issueDate=" + issueDate + ", customer=" + customer + '}';
    }

    public Integer getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Integer subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getSubscriptionPrice() {
        return subscriptionPrice;
    }

    public void setSubscriptionPrice(BigDecimal subscriptionPrice) {
        this.subscriptionPrice = subscriptionPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.subscriptionId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Subscription other = (Subscription) obj;
        if (!Objects.equals(this.subscriptionId, other.subscriptionId)) {
            return false;
        }
        return true;
    }

}
