package lucadipietro;

import com.github.javafaker.Faker;
import lucadipietro.entities.Costumer;
import lucadipietro.entities.Order;
import lucadipietro.entities.Product;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        Faker faker = new Faker();
        Random random = new Random();

        System.out.println("Lista Clienti");

        Supplier<Costumer> costumersSupplier = () -> new Costumer(faker.number().randomNumber(), faker.howIMetYourMother().character(), faker.number().numberBetween(1, 5));

        List<Costumer> costumers = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            costumers.add(costumersSupplier.get());
        }

        costumers.forEach(System.out::println);

        System.out.println();

        System.out.println("Lista Prodotti");

        Supplier<Product> productSupplier = () -> new Product(faker.number().randomNumber(), faker.commerce().productName(), faker.commerce().department(), Double.valueOf(faker.commerce().price().replace(",", ".")));

        List<Product> products = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            products.add(productSupplier.get());
        }

        products.forEach(System.out::println);

        System.out.println();

        System.out.println("Lista Ordini");

        Supplier<Order> orderSupplier = () -> {
            LocalDate orderDate = LocalDate.now().minusDays(faker.number().numberBetween(1, 30));
            LocalDate deliveryDate = orderDate.plusDays(faker.number().numberBetween(1, 10));
            List<Product> orderProducts = new ArrayList<>();
            int numRandomProducts = faker.number().numberBetween(1, 5);
            for (int i = 0; i < numRandomProducts; i++) {
                int randomIndex = faker.number().numberBetween(0, products.size() - 1);
                orderProducts.add(products.get(randomIndex));
            }
            return new Order(faker.number().randomNumber(), "Delivering", orderDate, deliveryDate, orderProducts, costumers.get(faker.number().numberBetween(0, costumers.size())));
        };

        List<Order> orders = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            orders.add(orderSupplier.get());
        }

        orders.forEach(System.out::println);

        System.out.println();

        System.out.println("Esercizio 1");

        Map<Costumer, List<Order>> ordersByCostumer = orders.stream()
                .collect(Collectors.groupingBy(Order::getCostumer));

        ordersByCostumer.forEach((costumer, orderList) -> {
            System.out.println("Il cliente: " + costumer + " ha fatto " + orderList.size() + " ordini");
            System.out.println("Ordini: " + orderList);
        });

        System.out.println();

        System.out.println("Esercizio 2");

        Map<Costumer, Double> totalSalesByCustomer = orders.stream()
                .collect(Collectors.groupingBy(Order::getCostumer,
                        Collectors.summingDouble(order -> order.getProducts().stream()
                                .mapToDouble(Product::getPrice)
                                .sum())));

        totalSalesByCustomer.forEach((costumer, totalSales) -> {
            System.out.println("Il cliente: " + costumer + " ha speso " + totalSales + " $");
        });

        System.out.println();

        System.out.println("Esercizio 3");

        List<Product> mostExpensiveProduct = products.stream().sorted(Comparator.comparing(Product::getPrice).reversed()).limit(5).toList();

        System.out.println("Prodotti Più costosi");
        mostExpensiveProduct.forEach(System.out::println);

    }
}
