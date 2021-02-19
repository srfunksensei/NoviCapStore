package com.mb.cart.discount;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.mb.product.Product;

public class BulkDiscountStrategyTest {

    private final static String PRODUCT_CODE = "TSHIRT";
    private final static int N = 3;
    private final static BigDecimal PRODUCT_REDUCED_PRICE = new BigDecimal(19);
    private final static Product PRODUCT_T_SHIRT = new Product(PRODUCT_CODE, "NoviCap T-Shirt", new BigDecimal(20));
    
    @Test
    public void testNoVoucherItems() {
        final Map<String, List<Product>> items = new HashMap<>();
        
        final BulkDiscountStrategy strategy = new BulkDiscountStrategy(PRODUCT_CODE, N, PRODUCT_REDUCED_PRICE);
        final BigDecimal discountPrice = strategy.getDiscountPrice(items);
        
        Assert.assertEquals(new BigDecimal(0), discountPrice);
    }
    
    @Test
    public void testLessThanNVoucherItems() {
        final int numOfItems = N - 1;
        
        final Map<String, List<Product>> items = new HashMap<>();
        final List<Product> products = getProductList(numOfItems);
        items.put(PRODUCT_T_SHIRT.getCode(), products);
        
        final BulkDiscountStrategy strategy = new BulkDiscountStrategy(PRODUCT_CODE, N, PRODUCT_REDUCED_PRICE);
        final BigDecimal discountPrice = strategy.getDiscountPrice(items);
        
        Assert.assertEquals(PRODUCT_T_SHIRT.getPrice().multiply(new BigDecimal(numOfItems)), discountPrice);
    }
    
    @Test
    public void testMoreThanNVoucherItems() {
        final int numOfItems = N + 1;
        
        final Map<String, List<Product>> items = new HashMap<>();
        final List<Product> products = getProductList(numOfItems);
        items.put(PRODUCT_T_SHIRT.getCode(), products);
        
        final BulkDiscountStrategy strategy = new BulkDiscountStrategy(PRODUCT_CODE, N, PRODUCT_REDUCED_PRICE);
        final BigDecimal discountPrice = strategy.getDiscountPrice(items);
        
        Assert.assertEquals(PRODUCT_REDUCED_PRICE.multiply(new BigDecimal(numOfItems)), discountPrice);
    }
    
    private List<Product> getProductList(final int numOfItems) {
        final List<Product> products = new ArrayList<>();
        for (int i = 0; i < numOfItems; i++) {
            products.add(PRODUCT_T_SHIRT);
        }
        return products;
    }
    
}
