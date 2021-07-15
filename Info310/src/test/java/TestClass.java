/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import domain.Customer;
import domain.Subscription;
import java.math.BigDecimal;
import java.util.Collection;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import net.sf.oval.constraint.AssertTrueCheck;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Luke Tang
 */
public class TestClass {

    @BeforeEach
    public void setUp() {
        
    }

    @AfterEach
    public void tearDown() {
       
    }

    @Test
    public void testSomething() {
        assertThat(1 + 1, equalTo(2));
    }

}
