package com.josevi.gastos.utils;

import android.support.v4.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class Utils {
//
//    public static Map<String, Pair<Integer, Double>> parseShipping(String shipping) {
//        Map<String, Pair<Integer, Double>> shippingParsed = new HashMap<String, Pair<Integer, Double>>();
//        for(String productBought: shipping.split("-")) {
//            Double prize = Double.parseDouble(productBought.split(",")[2]);
//            if (prize == -1)
//                prize = null;
//            shippingParsed.put(productBought.split(",")[0],
//                    new Pair(Integer.parseInt(productBought.split(",")[1]), prize));
//        }
//        return shippingParsed;
//    }
//
//    public static String formatShipping(Map<String, Pair<Integer, Double>> shipping) {
//        String shippingFormatted = "";
//        for(String productBought: shipping.keySet())
//            shippingFormatted += productBought +","
//                    +String.valueOf(shipping.get(productBought).first) +","
//                    +(shipping.get(productBought).second != null ?
//                    String.format("%.2f", shipping.get(productBought).second) : -1) +"-";
//        return shippingFormatted.substring(0, shippingFormatted.length() - 1);
//    }

}
