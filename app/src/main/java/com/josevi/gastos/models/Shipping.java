package com.josevi.gastos.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.josevi.gastos.db.DBContract;
import com.josevi.gastos.models.enums.Group;
import com.josevi.gastos.models.enums.Store;
import com.josevi.gastos.repositories.ProductRepository;
import com.josevi.gastos.utils.Utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.josevi.gastos.utils.Constantes.dateFormat;
import static com.josevi.gastos.utils.Constantes.dayDateFormat;
import static com.josevi.gastos.utils.Constantes.shortDateFormat;

public class Shipping implements Parcelable, Comparable<Shipping>, Map<String, Pair<Integer, Double>>{

    private String id;
    private Map<String, Pair<Integer, Double>> shipping;
    private Store store;
    private Date date;
    private double others;
    
    private final String OTHERS_PRODUCT_CODE = "OTHERS";

    private ProductRepository productRepository;

    public Shipping(Store store) {
        shipping = new HashMap<String, Pair<Integer, Double>>();
        date = Calendar.getInstance().getTime();
        others = 0;
        productRepository = new ProductRepository();
        this.store = store;
    }

    public Shipping(Map<String, Pair<Integer, Double>> shipping, Store store, double others) {
        this.shipping = shipping;
        productRepository = new ProductRepository();
        date = Calendar.getInstance().getTime();
        this.others = others;
        this.store = store;
        generateId();
    }

    public Shipping(Cursor cur) {
        this.store = Store.values()[cur.getInt(cur.getColumnIndex(DBContract.ShippingsEntry.COLUMN_STORE))];
        try {
            this.date = dateFormat.parse(cur.getString(cur.getColumnIndex(DBContract.ShippingsEntry.COLUMN_DATE)));
        }
        catch (ParseException pe) {
            String id = cur.getString(cur.getColumnIndex(DBContract.ShippingsEntry.COLUMN_ID));
            try {
                this.date = shortDateFormat.parse(id.substring(3, id.length()));
            }
            catch (ParseException pe2) {}
        }
        this.others = cur.getDouble(cur.getColumnIndex(DBContract.ShippingsEntry.COLUMN_OTHERS));
        this.setShippingParsed(cur.getString(cur.getColumnIndex(DBContract.ShippingsEntry.COLUMN_SHIPPING)));
    }

    public String getId() {
        if (this.id == null)
            generateId();
        return this.id;
    }

    public Store getStore() {
        return store;
    }

    public double getOthers() {
        return others;
    }

    private void generateId() {
        if (store != null && date != null)
            this.id = "SH" +String.valueOf(store.ordinal()) +shortDateFormat.format(date);
    }

    public List<Product> getProducts() {
        List<Product> products = new ArrayList<Product>();
        for (String code: shipping.keySet())
            products.add(productRepository.findProductByCode(code));
        if (others > 0)
            products.add(new Product("Otros", OTHERS_PRODUCT_CODE, others, store, Group.OTHER));
        return products;
    }

    public static final Creator<Shipping> CREATOR = new Creator<Shipping>() {
        @Override
        public Shipping createFromParcel(Parcel in) {
            return new Shipping(in);
        }

        @Override
        public Shipping[] newArray(int size) {
            return new Shipping[size];
        }
    };

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        generateId();
    }

    public String getShippingFormated() {
        String shippingFormatted = "";
        for(String productBought: shipping.keySet())
            shippingFormatted += productBought +","
                    +String.valueOf(shipping.get(productBought).first) +","
                    +(shipping.get(productBought).second != null ?
                    String.format("%.2f", shipping.get(productBought).second) : -1) +"-";
        return shippingFormatted.substring(0, shippingFormatted.length() - 1);
    }

    public void setShippingParsed(String shipping) {
        Map<String, Pair<Integer, Double>> shippingParsed = new HashMap<String, Pair<Integer, Double>>();
        for(String productBought: shipping.split("-")) {
            Double prize = Double.parseDouble(productBought.split(",")[2]);
            if (prize == -1)
                prize = null;
            shippingParsed.put(productBought.split(",")[0],
                    new Pair(Integer.parseInt(productBought.split(",")[1]), prize));
        }
        this.shipping = shippingParsed;
    }

    public void addProduct(String code) {
        if (shipping.containsKey(code))
            shipping.put(code, new Pair(shipping.get(code).first + 1, shipping.get(code).second));
        else {
            shipping.put(code, new Pair(1, null));
        }
    }

    public void deleteProduct(String code) {
        if (shipping.containsKey(code)) {
            if (shipping.get(code).first > 1)
                shipping.put(code, new Pair(shipping.get(code).first - 1, shipping.get(code).second));
            else {
                shipping.remove(code);
            }
        }
    }

    public double getTotalPrize() {
        ProductRepository productRepository = new ProductRepository();
        double total = others;
        for (String code: shipping.keySet()) {
            Product product = productRepository.findProductByCode(code);
            total += shipping.get(code).first *
                    (shipping.get(code).second != null ? shipping.get(code).second : product.getPrize());
        }

        return total;
    }

    public void setOthers(double others) {
        this.others = others;
    }

    public String toExport() {
        String dateParsed = shortDateFormat.format(date);
        String shippingName = "shipping" +dateParsed;
        String mapName = shippingName +"Map";
        String toExport = "Map<String, Integer> " +mapName +" = new HashMap<String, Integer>();\n";
        for (String code: shipping.keySet())
            toExport += mapName +".put(\"" +code +"\", " +String.valueOf(shipping.get(code)) +");\n";
        toExport += "Shipping " +shippingName +" = new Shipping(newShippingMap, Store." +store.toString()
                +", " +String.valueOf(others) +");\n";
        toExport += "try {\n";
        toExport += "\t" +shippingName +".setDate(shortDateFormat.parse(\"" +dateParsed +"\"));\n";
        toExport += "} catch (ParseException pe) {}\n";
        toExport += "addToMap(" + dayDateFormat.format(date) +", "+shippingName +");";
        return toExport;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(shortDateFormat.format(date));
        if (shipping.isEmpty())
            parcel.writeInt(0);
        else {
            parcel.writeInt(shipping.size());
            for (String code: shipping.keySet()) {
                parcel.writeInt(shipping.get(code).first);
                parcel.writeDouble(shipping.get(code).second != null ? shipping.get(code).second : -1);
                parcel.writeString(code);
            }
        }
        parcel.writeDouble(others);
    }

    public Shipping (Parcel in) {
        try {
            this.date = shortDateFormat.parse(in.readString());
        }
        catch (ParseException pe) {
            this.date = Calendar.getInstance().getTime();
        }
        this.shipping = new HashMap<String, Pair<Integer, Double>>();
        int N = in.readInt();
        if (N != 0) {
            for (int n = 0; n < N; n++) {
                Integer qty = in.readInt();
                Double prize = in.readDouble();
                if (prize == -1)
                    prize = null;
                this.shipping.put(in.readString(),
                        new Pair(qty, prize));
            }
        }
        this.others = in.readDouble();
    }

    @Override
    public int compareTo(@NonNull Shipping other) {
        Calendar thisDate = Calendar.getInstance(), otherDate = Calendar.getInstance();
        if (this.date != null && other.getDate() != null) {
            thisDate.setTime(this.date);
            otherDate.setTime(other.getDate());
            if (thisDate.after(otherDate))
                return 1;
            else if (thisDate.before(otherDate))
                return -1;
            else
                return 0;
        }
        else if (thisDate == null && otherDate == null)
            return 0;
        else if (thisDate != null)
            return 1;
        else
            return -1;
    }

    @Override
    public int size() {
        return shipping.size();
    }

    @Override
    public boolean isEmpty() {
        if (shipping == null)
            shipping = new HashMap<String, Pair<Integer, Double>>();
        return shipping.isEmpty();
    }

    @Override
    public boolean containsKey(Object o) {
        return shipping.containsKey(o);
    }

    @Override
    public boolean containsValue(Object o) {
        return shipping.containsValue(o);
    }

    @Override
    public Pair<Integer, Double> get(Object o) {
        return shipping.get(o);
    }

    @Override
    public Pair<Integer, Double> put(String s, Pair<Integer, Double> integerDoublePair) {
        return shipping.put(s, integerDoublePair);
    }

    @Override
    public Pair<Integer, Double> remove(Object o) {
        return shipping.remove(o);
    }

    @Override
    public void putAll(@NonNull Map<? extends String, ? extends Pair<Integer, Double>> map) {
        shipping.putAll(map);
    }

    @Override
    public void clear() {
        shipping.clear();
    }

    @NonNull
    @Override
    public Set<String> keySet() {
        return shipping.keySet();
    }

    @NonNull
    @Override
    public Collection<Pair<Integer, Double>> values() {
        return shipping.values();
    }

    @NonNull
    @Override
    public Set<Entry<String, Pair<Integer, Double>>> entrySet() {
        return shipping.entrySet();
    }
}
