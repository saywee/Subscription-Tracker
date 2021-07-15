package web;

/**
 *
 * @author emmabrothers
 */
import dao.SubscriptionDAO;
import domain.Customer;
import domain.Subscription;
import domain.Total;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.jooby.Jooby;
import org.jooby.Result;
import org.jooby.Status;

/**
 *
 * @author emmabrothers
 */
public class SubscriptionModule extends Jooby {
    // saveSubscription
    // getSubscriptionsByUsername

    public SubscriptionModule(SubscriptionDAO subscriptionDao) {
        get("/api/subscriptions/:username", (req) -> {
            String username = req.param("username").value();
            if (subscriptionDao.getSubscriptionsByUsername(username) == null) {
                return new Result().status(Status.NOT_FOUND);
            } else {
                return subscriptionDao.getSubscriptionsByUsername(username);
            }
        });

        post("/api/subscriptions", (req, rsp) -> {
            Subscription subscription = req.body().to(Subscription.class);

            // perform date conversion to avoid errors when storing in database
            LocalDate dueDate = LocalDate.parse(subscription.getDueDate().substring(0, 10));
            dueDate = dueDate.plusDays(1); // for accurate date
            System.out.println(dueDate);
            subscription.setDueDate(dueDate.toString());

            // decide whether a subscription is paid or not based on price
            if (subscription.getSubscriptionPrice().equals(BigDecimal.ZERO)) {
                subscription.setPaid(false);
            }

            subscriptionDao.saveSubscription(subscription);
            rsp.status(Status.CREATED);

            Customer c = subscription.getCustomer();
            String customerEmail = c.getEmailAddress();
            String fName = c.getFirstName();
            String lName = c.getLastName();
            String subName = subscription.getName();
            String date = subscription.getDueDate();

            CompletableFuture.runAsync(() -> {
                try {
                    Email email = new SimpleEmail();
                    email.setHostName("localhost");
                    email.setSmtpPort(2525);
                    email.setFrom("SubTrack@gmail.com");
                    email.setSubject("Subscription Renewal Warning");
                    email.setMsg("Hi " + fName + ", \n\nThis is an email to "
                            + "warn you that your " + subName + " subscription "
                                    + "expires soon on " 
                            + date + ".\n\nRemember to renew or delete your "
                                    + "subscription before it's too late!\n\n"
                                    + "This message was automatically generated "
                                    + "by SubTrack. Change your account settings "
                                    + "if you wish to receive notifications at a "
                                    + "different time.");
                    //email.setMsg("hey");
                    email.addTo(customerEmail);
                    //email.addTo("foo@bar.com");
                    email.send();
                } catch (EmailException ex) {
//                    Logger.getLogger(SubscriptionModule.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println(ex.getMessage());
                    System.out.println("Couldn't send an email to the FakeSMTP server. Make sure that the FakeSMTP server is active and listening on port 2525.");
                    System.out.println("Don't have FakeSMTP? Download it from http://nilhcem.com/FakeSMTP/");
                }
            });
        });

        get("api/categories/:username", (req) -> {
            String username = req.param("username").value();
            if (subscriptionDao.getCategories(username) == null) {
                return new Result().status(Status.NOT_FOUND);
            } else {
                return subscriptionDao.getCategories(username);
            }
        });
        get("api/categories/:category/:username", (req) -> {
            String username = req.param("username").value();
            String category = req.param("category").value();
            return subscriptionDao.filterByCategory(category,username);
        });
        delete("/api/subscriptions/:id", (req, rsp) -> {
            Integer id = Integer.valueOf(req.param("id").value());
            Subscription subscription = subscriptionDao.getSubscriptionById(id);

            subscriptionDao.deleteSubscription(subscription);
            rsp.status(Status.NO_CONTENT);
        });

        get("/api/total/:username", (req) -> {
            String username = req.param("username").value();
            return new Total(subscriptionDao.getTotal(username));
        });

        put("/api/subscriptions/:id", (req, rsp) -> {
            Integer id = Integer.valueOf(req.param("id").value());
//            Subscription subscription = subscriptionDao.getSubscriptionById(id);
            Subscription subscription = req.body().to(Subscription.class);
            
            // perform date conversion to avoid errors when storing in database
            LocalDate dueDate = LocalDate.parse(subscription.getDueDate().substring(0, 10));
            dueDate = dueDate.plusDays(1); // for accurate date
            System.out.println(dueDate);
            subscription.setDueDate(dueDate.toString());

            subscriptionDao.updateSubscription(subscription);
            rsp.status(Status.NO_CONTENT);
        });
        
        get("api/sort/:username", (req)->{
            String username = req.param("username").value();
            return subscriptionDao.sortAscending(username);
        });
    }
}
