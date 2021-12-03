package com.qa.ims.persistence.domain;

import java.util.Objects;

public class OrderItem {
    private Long id;
    private Long fk_order_id;
    private Long fk_item_id;

    public OrderItem(Long id, Long fk_order_id, Long fk_order_item){
        this.id = id;
        this.fk_order_id = fk_order_id;
        this.fk_item_id = fk_order_item;
    }

    public OrderItem(Long fk_order_id, Long fk_order_item){
        this.fk_order_id = fk_order_id;
        this.fk_item_id = fk_order_item;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFk_order_id() {
        return fk_order_id;
    }

    public void setFk_order_id(Long fk_order_id) {
        this.fk_order_id = fk_order_id;
    }

    public Long getFk_item_id() {
        return fk_item_id;
    }

    public void setFk_item_id(Long fk_item_id) {
        this.fk_item_id = fk_item_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(fk_order_id, orderItem.fk_order_id) && Objects.equals(fk_item_id, orderItem.fk_item_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fk_order_id, fk_item_id);
    }
}
