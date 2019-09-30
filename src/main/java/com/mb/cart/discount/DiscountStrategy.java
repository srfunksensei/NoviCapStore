package com.mb.cart.discount;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.mb.product.Product;

public interface DiscountStrategy {
    
    BigDecimal getDiscountPrice(final Map<String, List<Product>> items);

}
