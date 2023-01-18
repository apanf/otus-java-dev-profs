package homework;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {
    private final TreeMap<Customer, String> holder =
            new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        return getSimpleEntryWithCloneKey(holder.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return getSimpleEntryWithCloneKey(holder.higherEntry(customer));
    }

    private Map.Entry<Customer, String> getSimpleEntryWithCloneKey(Map.Entry<Customer, String> entry) {
        return entry == null
                ? null
                : new AbstractMap.SimpleEntry<>(entry.getKey().clone(), entry.getValue());
    }

    public void add(Customer customer, String data) {
        holder.put(customer, data);
    }
}
