package com.mb.cart.discount;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.mb.product.Product;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class NForOneDiscountStrategy implements DiscountStrategy {
    
    final String productCode;
    final int n;

    @Override
    public BigDecimal getDiscountPrice(@NonNull final Map<String, List<Product>> items) {
        final List<Product> products = items.get(productCode);
        if (products == null || products.isEmpty()) {
            return new BigDecimal(0);
        }
        
        final int numOfProducts = products.size();
        final int twoForOne = numOfProducts / n;
        final int rest = numOfProducts % n;
        
        final BigDecimal price = products.stream().findFirst().get().getPrice();
        return price.multiply(new BigDecimal(twoForOne))
                .add(price.multiply(new BigDecimal(rest)));
    }

}
