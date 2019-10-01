package com.mb.cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mb.cart.discount.DiscountStrategy;
import com.mb.product.Product;

public class Cart {

    private Map<String, List<Product>> items;
    private Map<String, List<DiscountStrategy>> discountStrategies;

    public Cart() {
        this.items = new HashMap<>();
        this.discountStrategies = new HashMap<>();
    }
    
    public Cart(final List<Product> items) {
        this(new HashMap<>(), items);
    }

    public Cart(final Map<String, List<DiscountStrategy>> discountStrategies) {
        this(discountStrategies, new ArrayList<>());
    }
    
    public Cart(final Map<String, List<DiscountStrategy>> discountStrategies, final List<Product> items) {
        final List<Product> list = Optional.ofNullable(items) //
                .map(List::stream) //
                .orElseGet(Stream::empty) //
                .collect(Collectors.toList());
        
        this.items = list.stream().collect(Collectors.groupingBy(Product::getCode));
        this.discountStrategies = discountStrategies;
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
            final BigDecimal productTotal = getTotalForProductType(items.get(code));
            
            total = total.add(productTotal);
        }
        return total;
    }
    
    public BigDecimal getTotalDiscounted() {
        BigDecimal total = new BigDecimal(0);
        for (String code : items.keySet()) {
            BigDecimal productTotal = getTotalForProductType(items.get(code));
            
            final List<DiscountStrategy> discountStrategiesList = discountStrategies.get(code);
            if (discountStrategiesList != null && !discountStrategiesList.isEmpty()) {
                for (DiscountStrategy s : discountStrategiesList) {
                    final BigDecimal discountedPrice = s.getDiscountPrice(items);
                    if (discountedPrice.compareTo(productTotal) < 0) {
                        productTotal = discountedPrice;
                    }
                }
            }
            
            total = total.add(productTotal);
        }
        return total;
    }
    
    private BigDecimal getTotalForProductType(final List<Product> products) {
        final int numOfItems = products.size();
        final BigDecimal singlePrice = products.stream().findFirst().get().getPrice();
        return singlePrice.multiply(new BigDecimal(numOfItems));
    }
    
    public void receipt() {
        for (String code : items.keySet()) {
            System.out.println(code + " " + items.get(code).size());
        }
        
        System.out.println("\nTotal price: " + getTotalDiscounted());
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
