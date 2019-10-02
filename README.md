# Shopping Cart 

The cart example represents a shopping cart in an online store. The client can add an item to the cart, remove an item, or retrieve the cart’s contents. 

The products are constantly changing by adding, removing, and repricing, so they are configurable with a json file (`products.json`). 

The checkout process allows for items to be scanned in any order, and should return the total amount to be paid. Total cost of a cart can be discounted by applying the following discounts:
* 2-for-1 promotions (buy two of the same product, get one free)
* bulk purchases (buying x or more of a product, the price of that product is reduced)

## Prerequisites

1. Java 8
2. Maven 3.3 (or higher)

## How to run application

To be able to see the application in action you must follow these steps:

1. run `mvn assembly:single`
2. run `java -jar target/novi-cap-store-0.0.1`

## License

This project is licensed under the MIT License - see the LICENSE.md file for details. 

