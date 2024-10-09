package com.mysite.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BillCardModel {

    private static final Logger log = LoggerFactory.getLogger(BillCardModel.class);

    @Inject
    @Via("resource")
    public List<Bill> bill;

    public List<Bill> getBill() {
        return bill;
    }

    public double getTotalPrice() {
        double totalPrice = 0.0;
        if (bill != null) {
            for (Bill billItem : bill) {
                try {
                    String priceStr = billItem.getDishPrice();
                    log.info("priceStr "+priceStr);
                    if (priceStr != null && !priceStr.isEmpty()) {
                        totalPrice += Double.parseDouble(priceStr);
                        log.info("Total price "+totalPrice);
                    }
                } catch (NumberFormatException e) {
                    log.info("Invalid price: " + billItem.getDishPrice());
                }
            }
        }
        return totalPrice;
    }

    public int getSize(){
        return bill.size();
    }
}

