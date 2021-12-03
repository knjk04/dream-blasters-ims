package com.qa.ims.controller;

import com.qa.ims.persistence.dao.ItemDAO;
import com.qa.ims.persistence.dao.OrderDAO;
import com.qa.ims.persistence.dao.OrderItemDAO;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.persistence.domain.OrderItem;
import com.qa.ims.utils.Utils;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderController implements CrudController<Order> {

    public static final Logger LOGGER = LogManager.getLogger();

    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private ItemDAO itemDAO;
    private Utils utils;

    public OrderController(OrderDAO orderDAO, OrderItemDAO orderItemDAO, ItemDAO itemDAO, Utils utils) {
        super();
        this.orderItemDAO = orderItemDAO;
        this.orderDAO = orderDAO;
        this.itemDAO = itemDAO;
        this.utils = utils;
    }

    /**
     * Reads all customers to the logger
     */
    @Override
    public List<Order> readAll() {
        List<Order> orders = orderDAO.readAll();
        List<OrderItem> orderItems = orderItemDAO.readAll();

        Map<Long, List<Long>> orderToItemIds = new HashMap<>();
        for(OrderItem orderItem : orderItems){
            Long order_id = orderItem.getFk_order_id();
            Long item_id = orderItem.getFk_item_id();
            if(!orderToItemIds.containsKey(order_id)){
                orderToItemIds.put(order_id, new ArrayList<>());
            }
            orderToItemIds.get(order_id).add(item_id);
        }
        List<Order> ordersWithItems = new ArrayList<>();
        for(Order order : orders){
            Order orderWithItems = new Order(order.getId(), order.getFkCustomerId(), order.getCost());
            List<Long> itemInOrder = orderToItemIds.get(order.getId());
            for (Long itemId : itemInOrder) {
                Item item = itemDAO.read(itemId);
                orderWithItems.addItem(item);
            }
            ordersWithItems.add(orderWithItems);
        }

        for (Order order : ordersWithItems) {
            LOGGER.info(order);
        }
        return ordersWithItems;
    }

    /**
     * Creates a customer by taking in user input
     */
    @Override
    public Order create() {
        LOGGER.info("Please enter the id of the customer");
        Long id = utils.getLong();
        Order order = orderDAO.create(new Order(id));
        LOGGER.info("Order created");
        return order;
    }

    /**
     * Updates an existing customer by taking in user input
     */
    @Override
    public Order update() {
        return null;
    }

    /**
     * Deletes an existing customer by the id of the customer
     *
     * @return
     */
    @Override
    public int delete() {
        LOGGER.info("Please enter the id of the order you would like to delete");
        Long id = utils.getLong();
        orderItemDAO.deleteOrder(id);
        return orderDAO.delete(id);
    }

    public Order addItemToOrder(Long orderId, Long itemId){
        Item item = itemDAO.read(itemId);
        Order order = orderDAO.read(orderId);
        order.addItem(item);
        orderDAO.update(order);
        orderItemDAO.create(new OrderItem(orderId, itemId));
        return order;
    }

    public Order removeItemFromOrder(Long orderId, Long itemId){
        Item item = itemDAO.read(itemId);
        Order order = orderDAO.read(orderId);
        order.removeItem(item);
        orderDAO.update(order);
        orderItemDAO.deleteItemFromOrder(orderId, itemId);
        return order;
    }
}
