package com.mb.cart;

import java.math.BigDecimal;
import java.util.*;

import org.junit.Assert;
import org.junit.Test;

import com.mb.cart.discount.BulkDiscountStrategy;
import com.mb.cart.discount.DiscountStrategy;
import com.mb.cart.discount.NForOneDiscountStrategy;
import com.mb.product.Product;

public class CartTest {

    final static Product PRODUCT_VOUCHER = new Product("VOUCHER", "NoviCap Voucher", new BigDecimal(5));
    final static Product PRODUCT_T_SHIRT = new Product("TSHIRT", "NoviCap T-Shirt", new BigDecimal(20));
    final static Product PRODUCT_MUG = new Product("MUG", "NoviCap Coffee Mug", new BigDecimal("7.5"));

    @Test
    public void testEmptyCart() {
        final Cart cart = new Cart();
        Assert.assertTrue(cart.isEmpty());
    }

    @Test
    public void testNonEmptyCart() {
        final List<Product> items = new ArrayList<>();
        items.add(PRODUCT_VOUCHER);

        final Cart cart = new Cart(items);
        Assert.assertFalse(cart.isEmpty());
    }

    @Test
    public void testNumberOfProductsEmptyCart() {
        final Cart cart = new Cart();
        Assert.assertTrue(cart.isEmpty());
        Assert.assertEquals(0, cart.getNumberOfProducts(PRODUCT_VOUCHER.getCode()));
    }

    @Test
    public void testNumberOfProductsOtherCode() {
        final List<Product> items = new ArrayList<>();
        items.add(PRODUCT_VOUCHER);

        final Cart cart = new Cart(items);
        Assert.assertFalse(cart.isEmpty());
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
        Assert.assertTrue(cart.isEmpty());

        cart.addItem(PRODUCT_VOUCHER);
        Assert.assertFalse(cart.isEmpty());
        Assert.assertEquals(1, cart.getNumberOfProducts(PRODUCT_VOUCHER.getCode()));
    }

    @Test
    public void testAddItemMultipleSameTypeProduct() {
        final Cart cart = new Cart();
        Assert.assertTrue(cart.isEmpty());

        cart.addItem(PRODUCT_VOUCHER);
        cart.addItem(PRODUCT_VOUCHER);
        cart.addItem(PRODUCT_VOUCHER);
        Assert.assertFalse(cart.isEmpty());
        Assert.assertEquals(3, cart.getNumberOfProducts(PRODUCT_VOUCHER.getCode()));
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveItemNull() {
        final Cart cart = new Cart();
        cart.removeItem(null);
    }

    @Test
    public void testRemoveItemNonExistingProduct() {
        final Cart cart = new Cart();
        Assert.assertTrue(cart.isEmpty());

        boolean isRemoved = cart.removeItem(PRODUCT_VOUCHER);
        Assert.assertFalse(isRemoved);
    }

    @Test
    public void testRemoveItemProduct() {
        final List<Product> items = new ArrayList<>();
        items.add(PRODUCT_VOUCHER);

        final Cart cart = new Cart(items);
        Assert.assertFalse(cart.isEmpty());

        cart.removeItem(PRODUCT_VOUCHER);
        Assert.assertTrue(cart.isEmpty());
    }

    @Test
    public void testRemoveItemProductFromMultipleSameType() {
        final List<Product> items = new ArrayList<>();
        items.add(PRODUCT_VOUCHER);
        items.add(PRODUCT_VOUCHER);

        final Cart cart = new Cart(items);
        Assert.assertFalse(cart.isEmpty());

        cart.removeItem(PRODUCT_VOUCHER);
        Assert.assertFalse(cart.isEmpty());
        Assert.assertEquals(1, cart.getNumberOfProducts(PRODUCT_VOUCHER.getCode()));
    }

    @Test
    public void testTotalEmptyCart() {
        final Cart cart = new Cart();
        Assert.assertTrue(cart.isEmpty());

        final BigDecimal total = cart.getTotal();
        Assert.assertEquals(new BigDecimal(0), total);
    }

    @Test
    public void testTotalNonEmptyCart() {
        final List<Product> items = new ArrayList<>();
        items.add(PRODUCT_VOUCHER);

        final Cart cart = new Cart(items);
        Assert.assertFalse(cart.isEmpty());

        final BigDecimal total = cart.getTotal();
        Assert.assertEquals(PRODUCT_VOUCHER.getPrice(), total);
    }

    @Test
    public void testTotalWithDiscountEmptyCart() {
        final DiscountStrategy discountStrategyVoucher = new NForOneDiscountStrategy(PRODUCT_VOUCHER.getCode(), 2),
                discountStrategyTshirt = new BulkDiscountStrategy(PRODUCT_T_SHIRT.getCode(), 3, new BigDecimal(19));

        final Map<String, List<DiscountStrategy>> discountStrategies = new HashMap<String, List<DiscountStrategy>>() {
            private static final long serialVersionUID = 1L;

            {
                put(PRODUCT_VOUCHER.getCode(), Collections.singletonList(discountStrategyVoucher));
                put(PRODUCT_T_SHIRT.getCode(), Collections.singletonList(discountStrategyTshirt));
            }
        };

        final Cart cart = new Cart(discountStrategies, new ArrayList<>());
        Assert.assertTrue(cart.isEmpty());

        final BigDecimal total = cart.getTotalDiscounted();
        Assert.assertEquals(new BigDecimal(0), total);
    }

    @Test
    public void testTotalWithDiscountNonEmptyCart() {
        final List<Product> items = new ArrayList<>();
        items.add(PRODUCT_VOUCHER);
        items.add(PRODUCT_T_SHIRT);
        items.add(PRODUCT_T_SHIRT);
        items.add(PRODUCT_T_SHIRT);
        items.add(PRODUCT_VOUCHER);
        items.add(PRODUCT_VOUCHER);
        items.add(PRODUCT_MUG);

        final DiscountStrategy discountStrategyVoucher = new NForOneDiscountStrategy(PRODUCT_VOUCHER.getCode(), 2),
                discountStrategyTshirt = new BulkDiscountStrategy(PRODUCT_T_SHIRT.getCode(), 3, new BigDecimal(19));

        final Map<String, List<DiscountStrategy>> discountStrategies = new HashMap<String, List<DiscountStrategy>>() {
            private static final long serialVersionUID = 1L;

            {
                put(PRODUCT_VOUCHER.getCode(), Collections.singletonList(discountStrategyVoucher));
                put(PRODUCT_T_SHIRT.getCode(), Collections.singletonList(discountStrategyTshirt));
            }
        };
        
        final Cart cart = new Cart(discountStrategies, items);
        Assert.assertFalse(cart.isEmpty());

        final BigDecimal total = cart.getTotalDiscounted();
        Assert.assertEquals(new BigDecimal("74.5"), total);
    }
}
