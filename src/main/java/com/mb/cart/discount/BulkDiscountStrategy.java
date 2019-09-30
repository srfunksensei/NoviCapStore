package com.mb.cart.discount;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.mb.product.Product;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BulkDiscountStrategy implements DiscountStrategy {
    
    final String productCode;
    final int n;
    final BigDecimal productReducedPrice;

    @Override
    public BigDecimal getDiscountPrice(Map<String, List<Product>> items) {
        final List<Product> products = items.get(productCode);
        if (products == null || products.isEmpty()) {
            return new BigDecimal(0);
        }
        
        final int numOfProducts = products.size();
        final BigDecimal price = numOfProducts >= n ? 
                productReducedPrice :
                products.stream().findFirst().get().getPrice();
        
        return price.multiply(new BigDecimal(numOfProducts));
    }

}
