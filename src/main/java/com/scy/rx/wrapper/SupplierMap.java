package com.scy.rx.wrapper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SupplierMap {
    private LinkedHashMap<Integer, Supplier> supplierMap = new LinkedHashMap<Integer, Supplier>() {
        private static final long serialVersionUID = 1L;
        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, Supplier> eldest) {
            return size() > 100;
        }
    };

    public Supplier put(Integer key, Supplier supplier) {
        return supplierMap.putIfAbsent(key, supplier);
    }

    public Supplier get(Integer key) {
        return supplierMap.get(key);
    }
}
