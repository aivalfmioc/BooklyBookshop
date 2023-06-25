package com.bookly.service;

import com.bookly.dao.CustomerRepository;
import com.bookly.dao.ProductRepository;
import com.bookly.dto.PaymentInfo;
import com.bookly.dto.Purchase;
import com.bookly.dto.PurchaseResponse;
import com.bookly.entity.Customer;
import com.bookly.entity.Order;
import com.bookly.entity.OrderItem;
import com.bookly.entity.Product;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class CheckoutServiceImpl implements CheckoutService {

	@Autowired
	private ProductRepository productRepository;
	
    private CustomerRepository customerRepository;

    public CheckoutServiceImpl(CustomerRepository customerRepository,
                               @Value("${stripe.key.secret}")String secretKey){
        this.customerRepository= customerRepository;
        //initialize Stripe API with the secret key
        Stripe.apiKey = secretKey;
    }
    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {
        Order order = purchase.getOrder(); //retrieve the order info from dto
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);
        Set<OrderItem> orderItems = purchase.getOrderItems(); //populate order with orderItems
        orderItems.forEach(item -> order.add(item));
        //decreasing unit functionality.
        for (OrderItem orderItem : orderItems) {
			Optional<Product> prod = productRepository.findById(orderItem.getProductId());
			if(prod.isPresent()) {
				Product p = prod.get();
				p.setUnitsInStock((p.getUnitsInStock() - orderItem.getQuantity()));
				productRepository.save(p);
			}
		}
        order.setShippingAddress(purchase.getShippingAddress());//populate order with shipping address
        Customer customer = purchase.getCustomer();
        String theEmail = customer.getEmail();
        Customer customerFromDB = customerRepository.findByEmail(theEmail); //check if this is an existing customer
        if(customerFromDB != null){
            customer = customerFromDB;
        }
        customer.add(order);
        customerRepository.save(customer);//save to the database
        return new PurchaseResponse(orderTrackingNumber);
    }

    @Override
    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {
        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentInfo.getAmount());
        params.put("currency", paymentInfo.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);
        params.put("description", "Bookshop purchase");
        params.put("receipt_email", paymentInfo.getReceiptEmail());
        return PaymentIntent.create(params);
    }

    private String generateOrderTrackingNumber() {
        // generate a random UUID number (UUID version-4)
        return UUID.randomUUID().toString();
    }
}
