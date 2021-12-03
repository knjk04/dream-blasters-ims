package com.qa.ims.persistence.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Order {
    private Long id;
    private float cost;
    private Long fkCustomerId;
    private Map<Item, Integer> items;

    public Order(Long id, Long fkCustomerId, Map<Item, Integer> items){
        this.id = id;
        this.fkCustomerId = fkCustomerId;
        this.items = items;
        calculateCost();
    }

    public Order(Long fkCustomerId, Map<Item, Integer> items){
        this.fkCustomerId = fkCustomerId;
        this.items = items;
        calculateCost();
    }

    public Order(Long id, Long fkCustomerId, float cost){
        this.id = id;
        this.fkCustomerId = fkCustomerId;
        this.cost = cost;
        items = new HashMap<>();
    }


    public Order(Long id, Long fkCustomerId){
        this.id = id;
        this.fkCustomerId = fkCustomerId;
    }

    public Order(Long fkCustomerId){
        this.fkCustomerId = fkCustomerId;
    }

    public float calculateCost(){
        cost = 0;
        for(Item item : items.keySet()){
            cost += item.getPrice() * items.get(item);
        }
        return cost;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return cost == order.cost && Objects.equals(id, order.id) && Objects.equals(fkCustomerId, order.fkCustomerId) && Objects.equals(items, order.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cost, fkCustomerId, items);
    }

    @Override
    public String toString() {
        return "Order: " + id +
                ", cost: " + cost +
                ", customer id: " + fkCustomerId +
                ", items: " + items +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public Long getFkCustomerId() {
        return fkCustomerId;
    }

    public void setFkCustomerId(Long fkCustomerId) {
        this.fkCustomerId = fkCustomerId;
    }

    public Map<Item, Integer> getItems() {
        return items;
    }

    public void setItems(Map<Item, Integer> items) {
        this.items = items;
    }

    public void addItem(Item item){
        System.out.println(item);
        if(!items.containsKey(item)){
            items.put(item, 0);
        }
        items.put(item, items.get(item) + 1);
        calculateCost();
    }


    public void removeItem(Item item){
        System.out.println(item);
        if(items.containsKey(item) && items.get(item) > 0){
            items.put(item, items.get(item) - 1);
        } else{
            System.out.println("Warning: Cannot delete item that is not in order!");
        }
        calculateCost();
    }
}
