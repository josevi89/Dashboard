package com.josevi.gastos.repositories;

import com.josevi.gastos.models.Product;
import com.josevi.gastos.models.enums.Group;
import com.josevi.gastos.models.enums.Store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductRepository {

    private Map<Store, Map<Group, List<Product>>> productsMap;

    public ProductRepository() {
        List<Product> mercadonaMeat = new ArrayList<Product>();
        mercadonaMeat.add(new Product("Tacos Jamón (x2)", "MME0", 0.45, Store.MERCADONA, Group.MEAT));
        mercadonaMeat.add(new Product("Maxi Lomo", "MME1", 2.00, Store.MERCADONA, Group.MEAT));
        List<Product> mercadonaFish = new ArrayList<Product>();
        List<Product> mercadonaBread = new ArrayList<Product>();
        mercadonaBread.add(new Product("Barra de pan (x2)", "MBR0", 0.45, Store.MERCADONA, Group.BREAD));
        mercadonaBread.add(new Product("Picos de pan (x2)", "MBR1", 1.08, Store.MERCADONA, Group.BREAD));
        List<Product> mercadonaPrepared = new ArrayList<Product>();
        mercadonaPrepared.add(new Product("Hummus clásico", "MPR0", 1.39, Store.MERCADONA, Group.PREPARED));
        List<Product> mercadonaDrinks = new ArrayList<Product>();
        mercadonaDrinks.add(new Product("Lata Steinburg (x12)", "MDR0", 2.62, Store.MERCADONA, Group.DRINKS));
        mercadonaDrinks.add(new Product("Lata Steinburg Suave (x12)", "MDR1", 2.62, Store.MERCADONA, Group.DRINKS));
        mercadonaDrinks.add(new Product("Lata Cerveza con Tequila", "MDR2", 0.50, Store.MERCADONA, Group.DRINKS));
        mercadonaDrinks.add(new Product("Zumo Florida", "MDR3", 1.00, Store.MERCADONA, Group.DRINKS));
        mercadonaDrinks.add(new Product("Zumo Bahamas", "MDR4", 1.00, Store.MERCADONA, Group.DRINKS));
        mercadonaDrinks.add(new Product("Batido Chocolate (1l)", "MDR5", 1.05, Store.MERCADONA, Group.DRINKS));
        List<Product> mercadonaFrozen = new ArrayList<Product>();
        mercadonaFrozen.add(new Product("Pizza Pesto", "MFR0", 2.00, Store.MERCADONA, Group.FROZEN));
        mercadonaFrozen.add(new Product("Croquetas Cocido", "MFR1", 1.50, Store.MERCADONA, Group.FROZEN));
        mercadonaFrozen.add(new Product("Salteado Patatas", "MFR2", 1.47, Store.MERCADONA, Group.FROZEN));
        mercadonaFrozen.add(new Product("Patatas Congeladas (2kg)", "MFR3", 1.80, Store.MERCADONA, Group.FROZEN));
        mercadonaFrozen.add(new Product("Croquetas Jamón", "MFR4", 0.99, Store.MERCADONA, Group.FROZEN));
        mercadonaFrozen.add(new Product("San Jacobos", "MFR5", 0.99, Store.MERCADONA, Group.FROZEN));
        mercadonaFrozen.add(new Product("Palitos de Merluza", "MFR6", 1.65, Store.MERCADONA, Group.FROZEN));
        List<Product> mercadonaBreakfast = new ArrayList<Product>();
        mercadonaBreakfast.add(new Product("Croissant Chocolate", "MBF0", 1.20, Store.MERCADONA, Group.BREAKFAST));
        List<Product> mercadonaAnimal = new ArrayList<Product>();
        mercadonaAnimal.add(new Product("Bocaditos en salsa, Salmon", "MAN0", 1.20, Store.MERCADONA, Group.ANIMALS));
        List<Product> mercadonaVegetable = new ArrayList<Product>();
        mercadonaVegetable.add(new Product("Champiñón laminas", "MVG0", 1.70, Store.MERCADONA, Group.VEGETABLES));
        List<Product> mercadonaOther = new ArrayList<Product>();
        mercadonaOther.add(new Product("Huevos L (x12)", "MOT0", 1.70, Store.MERCADONA, Group.OTHER));
        mercadonaOther.add(new Product("Bote Tomate frito casero", "MOT1", 1.70, Store.MERCADONA, Group.OTHER));
        mercadonaOther.add(new Product("Aceite freir", "MOT2", 1.48, Store.MERCADONA, Group.OTHER));
        mercadonaOther.add(new Product("Aceite oliva", "MOT3", 3.60, Store.MERCADONA, Group.OTHER));
        List<Product> mercadonaMilk = new ArrayList<Product>();

        Map<Group, List<Product>> mercadonaMap = new HashMap<Group, List<Product>>();
        mercadonaMap.put(Group.MEAT, mercadonaMeat);
        mercadonaMap.put(Group.FISH, mercadonaFish);
        mercadonaMap.put(Group.BREAD, mercadonaBread);
        mercadonaMap.put(Group.PREPARED, mercadonaPrepared);
        mercadonaMap.put(Group.DRINKS, mercadonaDrinks);
        mercadonaMap.put(Group.FROZEN, mercadonaFrozen);
        mercadonaMap.put(Group.BREAKFAST, mercadonaBreakfast);
        mercadonaMap.put(Group.ANIMALS, mercadonaAnimal);
        mercadonaMap.put(Group.VEGETABLES, mercadonaVegetable);
        mercadonaMap.put(Group.OTHER, mercadonaOther);
        mercadonaMap.put(Group.MILK, mercadonaMilk);

        List<Product> estancoTobacco = new ArrayList<Product>();
        estancoTobacco.add(new Product("Pueblo azul (30g)", "ETO0", 5.20, Store.ESTANCO, Group.TOBACCO));
        estancoTobacco.add(new Product("Camel Cajetilla", "ETO1", 4.85, Store.ESTANCO, Group.TOBACCO));
        List<Product> estancoPaper = new ArrayList<Product>();
        estancoPaper.add(new Product("Greengo", "EPA0", 0.50, Store.ESTANCO, Group.PAPER));
        List<Product> estancoFilters = new ArrayList<Product>();

        Map<Group, List<Product>> estancoMap = new HashMap<Group, List<Product>>();
        estancoMap.put(Group.TOBACCO, estancoTobacco);
        estancoMap.put(Group.PAPER, estancoPaper);
        estancoMap.put(Group.FILTERS, estancoFilters);

        Map<Store, Map<Group, List<Product>>> productsMap = new HashMap<Store, Map<Group, List<Product>>>();
        productsMap.put(Store.MERCADONA, mercadonaMap);
        productsMap.put(Store.ESTANCO, estancoMap);

        this.productsMap = Collections.unmodifiableMap(productsMap);
    }

    public List<Product> findProductsFromStoreAndGroups(Store store, List<Group> groups, String text) {
        List<Product> productsToReturn = new ArrayList<Product>();
        if (store != null && text != null && !text.isEmpty())
            if (groups == null || groups.isEmpty())
                groups = Arrays.asList(Group.getGroupsFromStore(store));
            for (Group group: groups)
                if (group != null)
                    for(Product product: this.productsMap.get(store).get(group))
                        if (product.getName().toLowerCase().trim().contains(text.toLowerCase().trim()))
                            productsToReturn.add(product);
        return productsToReturn;
    }

    public Product findProductByCode(String code) {
        String storeCode = code.substring(0, 1);
        String groupCode = code.substring(1, 3);
        int productIdx = Integer.parseInt(code.substring(3));
        Store store = null;
        Group group = null;
        switch (storeCode) {
            case "M":
                store = Store.MERCADONA;
                switch (groupCode) {
                    case "ME":
                        group = Group.MEAT;
                        break;
                    case "FI":
                        group = Group.FISH;
                        break;
                    case "BR":
                        group = Group.BREAD;
                        break;
                    case "PR":
                        group = Group.PREPARED;
                        break;
                    case "DR":
                        group = Group.DRINKS;
                        break;
                    case "FR":
                        group = Group.FROZEN;
                        break;
                    case "BF":
                        group = Group.BREAKFAST;
                        break;
                    case "AN":
                        group = Group.ANIMALS;
                        break;
                    case "VE":
                        group = Group.VEGETABLES;
                        break;
                    case "OT":
                        group = Group.OTHER;
                        break;
                    default:
                        return null;
                }
                break;
            case "E":
                store = Store.ESTANCO;
                switch(groupCode) {
                    case "TO":
                        group = Group.TOBACCO;
                        break;
                    case "PA":
                        group = Group.PAPER;
                        break;
                    case "FI":
                        group = Group.FILTERS;
                        break;
                    default:
                        return null;
                }
                break;
            default:
                return null;
        }
        if (store != null && group != null && productIdx >= 0 && productIdx < productsMap.get(store).get(group).size())
            return productsMap.get(store).get(group).get(productIdx);
        else
            return null;
    }
}
