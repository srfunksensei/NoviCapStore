package com.mb.cart.discount;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import com.mb.product.Product;

public class NForOneDiscountStrategyTest {
    
    final static int N = 2;
    final static String PRODUCT_CODE = "VOUCHER";
    final static Product PRODUCT_VOUCHER = new Product(PRODUCT_CODE, "NoviCap Voucher", new BigDecimal(5));
    final static int PRODUCT_NUMBER_LIMIT = 50;
    final static Random RANDOM_GENERATOR = new Random();
    
    @Test
    public void testNoVoucherItems() {
        final Map<String, List<Product>> items = new HashMap<>();
        
        final NForOneDiscountStrategy strategy = new NForOneDiscountStrategy(PRODUCT_CODE, N);
        final BigDecimal discountPrice = strategy.getDiscountPrice(items);
        
        Assert.assertEquals(new BigDecimal(0), discountPrice);
    }
    
    @Test
    public void testOneVoucherItem() {
        final int numOfItems = 1;
        
        final Map<String, List<Product>> items = new HashMap<>();
        final List<Product> products = getProductList(numOfItems);
        items.put(PRODUCT_VOUCHER.getCode(), products);
        
        final NForOneDiscountStrategy strategy = new NForOneDiscountStrategy(PRODUCT_CODE, N);
        final BigDecimal discountPrice = strategy.getDiscountPrice(items);
        
        Assert.assertEquals(PRODUCT_VOUCHER.getPrice(), discountPrice);
    }

    @Test
    public void testTwoForOneDiscountEvenNumberOfItems() {
        final int numOfItems = getEvenNumOfItems(PRODUCT_NUMBER_LIMIT);
        
        final Map<String, List<Product>> items = new HashMap<>();
        final List<Product> products = getProductList(numOfItems);
        items.put(PRODUCT_VOUCHER.getCode(), products);
        
        final NForOneDiscountStrategy strategy = new NForOneDiscountStrategy(PRODUCT_CODE, N);
        final BigDecimal discountPrice = strategy.getDiscountPrice(items);
        
        final int expectedNumOfItems = (numOfItems / N) + (numOfItems % N);
        final BigDecimal expectedPrice = PRODUCT_VOUCHER.getPrice().multiply(new BigDecimal(expectedNumOfItems));
        Assert.assertEquals(expectedPrice, discountPrice);
    }
    
    @Test
    public void testTwoForOneDiscountOddNumberOfItems() {
        final int numOfItems = getOddNumOfItems(PRODUCT_NUMBER_LIMIT);
        
        final Map<String, List<Product>> items = new HashMap<>();
        final List<Product> products = getProductList(numOfItems);
        items.put(PRODUCT_VOUCHER.getCode(), products);
        
        final NForOneDiscountStrategy strategy = new NForOneDiscountStrategy(PRODUCT_CODE, N);
        final BigDecimal discountPrice = strategy.getDiscountPrice(items);
        
        final int expectedNumOfItems = (numOfItems / N) + (numOfItems % N);
        final BigDecimal expectedPrice = PRODUCT_VOUCHER.getPrice().multiply(new BigDecimal(expectedNumOfItems));
        Assert.assertEquals(expectedPrice, discountPrice);
    }
    
    @Test
    public void testThreeForOneDiscountOddNumberOfItems() {
        final int n = 3;
        final int numOfItems = getOddNumOfItems(PRODUCT_NUMBER_LIMIT);
        
        final Map<String, List<Product>> items = new HashMap<>();
        final List<Product> products = getProductList(numOfItems);
        items.put(PRODUCT_VOUCHER.getCode(), products);
        
        final NForOneDiscountStrategy strategy = new NForOneDiscountStrategy(PRODUCT_CODE, n);
        final BigDecimal discountPrice = strategy.getDiscountPrice(items);
        
        final int expectedNumOfItems = (numOfItems / n) + (numOfItems % n);
        final BigDecimal expectedPrice = PRODUCT_VOUCHER.getPrice().multiply(new BigDecimal(expectedNumOfItems));
        Assert.assertEquals(expectedPrice, discountPrice);
    }
    
    private final int getEvenNumOfItems(final int limit) {
        return RANDOM_GENERATOR.nextInt(limit / 2) * 2;
    }
    
    private final int getOddNumOfItems(final int limit) {
        return getEvenNumOfItems(limit) + 1;
    }
    
    private final List<Product> getProductList(final int numOfItems) {
        final List<Product> products = new ArrayList<>();
        for (int i = 0; i < numOfItems; i++) {
            products.add(PRODUCT_VOUCHER);
        }
        return products;
    }
}
