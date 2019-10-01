package com.mb;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import com.mb.cart.Cart;
import com.mb.cart.discount.BulkDiscountStrategy;
import com.mb.cart.discount.DiscountStrategy;
import com.mb.cart.discount.NForOneDiscountStrategy;
import com.mb.product.JsonProductInventory;
import com.mb.product.Product;

public class App {
    public static void main(String[] args) {
        final PrintStream out = System.out;
        
        out.println("Welcome to NoviCap store\n");
        out.println("At out shop you can buy the following products:");
        
        Set<Product> items = new HashSet<>();
        try {
            final JsonProductInventory inventory = new JsonProductInventory();
            items = inventory.getItems();
            for (Product item : items) {
                out.println(item.toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            
            out.print("We are experiencing technical difficulties, please try again later");
            System.exit(-1);
        }
        
        out.println("\nDo you want to make the purchase? (y/n)");
        
        final Scanner in = new Scanner(System.in);
        final String answer = in.next();
        if (answer.equalsIgnoreCase("y")) {
            final String codes = "(" + String.join(",", items.stream().map(Product::getCode).collect(Collectors.toSet())) + ")";
            final Cart cart = createCart();
            
            String more = "y";
            while (more.equalsIgnoreCase("y")) {
                out.println("Which product you wish to buy? " + codes);
                final String code = in.next();
                
                Optional<Product> item = items.stream().filter(i -> i.getCode().equalsIgnoreCase(code)).findAny();
                if (item.isPresent()) {
                    cart.addItem(item.get());
                } else {
                    out.println("This product is not available in our shop");
                }
            
                out.println("Do you wish to buy more? (y/n)");
                more = in.next();
            }
            
            out.println("Your cart: ");
            cart.receipt();
            
        } 
        
        out.println("Thank you for visiting our store!");
        
        in.close();
        System.exit(0);
    }
    
    private static Cart createCart() {
        final String voucher = "VOUCHER",
                tshirt = "TSHIRT";
        
        final DiscountStrategy discountStrategyVoucher = new NForOneDiscountStrategy(voucher, 2),
                discountStrategyTshirt = new BulkDiscountStrategy(tshirt, 3, new BigDecimal(19));

        final Map<String, List<DiscountStrategy>> discountStrategies = new HashMap<String, List<DiscountStrategy>>() {
            private static final long serialVersionUID = 1L;

            {
                put(voucher, Arrays.asList(discountStrategyVoucher));
                put(tshirt, Arrays.asList(discountStrategyTshirt));
            }
        };

        
        return new Cart(discountStrategies);
    }
}
