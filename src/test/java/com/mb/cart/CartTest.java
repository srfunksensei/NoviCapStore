package com.mb.cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.mb.product.Product;

public class CartTest {
    
    final static Product PRODUCT_1 = new Product("VOUCHER", "NoviCap Voucher", new BigDecimal(5));

    @Test
    public void testEmptyCart() {
        final Cart cart = new Cart();
        Assert.assertEquals(true, cart.isEmpty());
    }

    @Test
    public void testNonEmptyCart() {
        final List<Product> items = new ArrayList<>();
        items.add(PRODUCT_1);
        
        final Cart cart = new Cart(items);
        Assert.assertEquals(false, cart.isEmpty());
    }
    
    @Test
    public void testNumberOfProductsEmptyCart() {
        final Cart cart = new Cart();
        Assert.assertEquals(true, cart.isEmpty());
        Assert.assertEquals(0, cart.getNumberOfProducts(PRODUCT_1.getCode()));
    }
    
    @Test
    public void testNumberOfProductsOtherCode() {
        final List<Product> items = new ArrayList<>();
        items.add(PRODUCT_1);
        
        final Cart cart = new Cart(items);
        Assert.assertEquals(false, cart.isEmpty());
        Assert.assertEquals(0, cart.getNumberOfProducts("TEST"));
    }
    
    @Test(expected = NullPointerException.class)
    public void testAddItemNull() {
        final Cart cart = new Cart();
        cart.addItem(null);
    }
    
    @Test
    public void testAddItemProduct() {
        final Cart cart = new Cart();
        Assert.assertEquals(true, cart.isEmpty());
        
        cart.addItem(PRODUCT_1);
        Assert.assertEquals(false, cart.isEmpty());
        Assert.assertEquals(1, cart.getNumberOfProducts(PRODUCT_1.getCode()));
    }
    
    @Test
    public void testAddItemMultipleSameTypeProduct() {
        final Cart cart = new Cart();
        Assert.assertEquals(true, cart.isEmpty());
        
        cart.addItem(PRODUCT_1);
        cart.addItem(PRODUCT_1);
        cart.addItem(PRODUCT_1);
        Assert.assertEquals(false, cart.isEmpty());
        Assert.assertEquals(3, cart.getNumberOfProducts(PRODUCT_1.getCode()));
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveItemNull() {
        final Cart cart = new Cart();
        cart.removeItem(null);
    }
    
    @Test
    public void testRemoveItemNonExistingProduct() {
        final Cart cart = new Cart();
        Assert.assertEquals(true, cart.isEmpty());
        
        boolean isRemoved = cart.removeItem(PRODUCT_1);
        Assert.assertEquals(false, isRemoved);
    }
    
    @Test
    public void testRemoveItemProduct() {
        final List<Product> items = new ArrayList<>();
        items.add(PRODUCT_1);
        
        final Cart cart = new Cart(items);
        Assert.assertEquals(false, cart.isEmpty());
        
        cart.removeItem(PRODUCT_1);
        Assert.assertEquals(true, cart.isEmpty());
    }
    
    @Test
    public void testRemoveItemProductFromMultipleSameType() {
        final List<Product> items = new ArrayList<>();
        items.add(PRODUCT_1);
        items.add(PRODUCT_1);
        
        final Cart cart = new Cart(items);
        Assert.assertEquals(false, cart.isEmpty());
        
        cart.removeItem(PRODUCT_1);
        Assert.assertEquals(false, cart.isEmpty());
        Assert.assertEquals(1, cart.getNumberOfProducts(PRODUCT_1.getCode()));
    }
    
    @Test
    public void testTotalEmptyCart() {
        final Cart cart = new Cart();
        Assert.assertEquals(true, cart.isEmpty());
        
        final BigDecimal total = cart.getTotal();
        Assert.assertEquals(new BigDecimal(0), total);
    }
    
    @Test
    public void testTotalNonEmptyCart() {
        final List<Product> items = new ArrayList<>();
        items.add(PRODUCT_1);
        
        final Cart cart = new Cart(items);
        Assert.assertEquals(false, cart.isEmpty());
        
        final BigDecimal total = cart.getTotal();
        Assert.assertEquals(PRODUCT_1.getPrice(), total);
    }
}
