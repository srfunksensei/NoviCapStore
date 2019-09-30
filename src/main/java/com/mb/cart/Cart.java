package com.mb.cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mb.product.Product;

public class Cart {

    private Map<String, List<Product>> items;

    public Cart() {
        this.items = new HashMap<String, List<Product>>();
    }

    public Cart(final List<Product> items) {
        final List<Product> list = Optional.ofNullable(items) //
                .map(List::stream) //
                .orElseGet(Stream::empty) //
                .collect(Collectors.toList());
        
        this.items = list.stream().collect(Collectors.groupingBy(Product::getCode));
    }
    
    public boolean isEmpty() {
        boolean isEmpty = true;
        for (String code : items.keySet()) {
            isEmpty = isEmpty && items.get(code).isEmpty();
        }
        return isEmpty;
    }
    
    public void addItem(final Product item) {
        if (item == null) {
            throw new NullPointerException();
        }
        
        final String code = item.getCode();
        
        List<Product> products = items.get(code);
        if (products == null) {
            products = new ArrayList<>();
        }
        
        products.add(item);
        items.put(code, products);
    }
    
    public boolean removeItem(final Product item) {
        if (item == null) {
            throw new NullPointerException();
        }
        
        final String code = item.getCode();
        
        List<Product> products = items.get(code);
        if (products == null) {
            return false;
        }
        
        boolean isRemoved = products.remove(item);
        if (products.isEmpty()) {
            items.remove(code);
        }
        
        return isRemoved;
    }
    
    public int getNumberOfProducts(final String code) {
        return items.containsKey(code) ? items.get(code).size() : 0;
    }
    
    public BigDecimal getTotal() {
        BigDecimal total = new BigDecimal(0);
        for (String code : items.keySet()) {
            final int numOfItems = items.get(code).size();
            final BigDecimal singlePrice = items.get(code).stream().findFirst().get().getPrice();
            
            total = total.add(singlePrice.multiply(new BigDecimal(numOfItems)));
        }
        return total;
    }
    
    public void receipt() {
        for (String code : items.keySet()) {
            System.out.print(code + " " + items.get(code).size());
        }
    }
    
    @Override
    public String toString() {
        String s = "";
        for (String code : items.keySet()) {
            s += code + " " + items.get(code).size();
        }
        
        return s;
    }

}
