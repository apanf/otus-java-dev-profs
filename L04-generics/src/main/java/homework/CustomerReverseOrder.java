package homework;

import java.util.LinkedList;

public class CustomerReverseOrder {
    private final LinkedList<Customer> holder = new LinkedList<>();

    public void add(Customer customer) {
        holder.add(customer);
    }

    public Customer take() {
        return holder.removeLast();
    }
}
