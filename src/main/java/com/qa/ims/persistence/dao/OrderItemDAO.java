package com.qa.ims.persistence.dao;

import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.persistence.domain.OrderItem;
import com.qa.ims.utils.DBUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderItemDAO implements  Dao<OrderItem>{

    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public OrderItem modelFromResultSet(ResultSet resultSet) throws SQLException {
        Long fk_order_id = resultSet.getLong("fk_order_id");
        Long fk_item_id = resultSet.getLong("fk_item_id");
        return new OrderItem(fk_order_id, fk_item_id);
    }

    /**
     * Reads all customers from the database
     *
     * @return A list of customers
     */
    @Override
    public List<OrderItem> readAll() {
        try (Connection connection = DBUtils.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM order_item");) {
            List<OrderItem> orderItems = new ArrayList<>();
            while (resultSet.next()) {
                orderItems.add(modelFromResultSet(resultSet));
            }
            return orderItems;
        } catch (SQLException e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    public OrderItem readLatest() {
        try (Connection connection = DBUtils.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM order_item ORDER BY id DESC LIMIT 1");) {
            resultSet.next();
            return modelFromResultSet(resultSet);
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    /**
     * Creates a customer in the database
     *
     * @param  - takes in a customer object. id will be ignored
     */
    @Override
    public OrderItem create(OrderItem orderItem) {
        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("INSERT INTO order_item(fk_order_id, fk_item_id) VALUES (?, ?)");) {
            statement.setLong(1, orderItem.getFk_order_id());
            statement.setLong(2, orderItem.getFk_item_id());
            statement.executeUpdate();
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return readLatest();
    }

    @Override
    public OrderItem read(Long id) {
        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM order_item WHERE id = ?");) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery();) {
                resultSet.next();
                return modelFromResultSet(resultSet);
            }
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    /**
     * Updates a customer in the database
     *
     * @param  - takes in a customer object, the id field will be used to
     *                 update that customer in the database
     * @return
     */
    @Override
    public OrderItem update(OrderItem orderItem) {
        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("UPDATE order_item SET fk_order_id = ?, fk_item_id = ? WHERE id = ?");) {
            statement.setLong(1, orderItem.getFk_order_id());
            statement.setLong(2, orderItem.getFk_item_id());
            statement.setLong(3, orderItem.getId());
            statement.executeUpdate();
            return read(orderItem.getId());
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    /**
     * Deletes a customer in the database
     *
     * @param id - id of the customer
     */
    @Override
    public int delete(long id) {
        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM order_item WHERE id = ?");) {
            statement.setLong(1, id);
            return statement.executeUpdate();
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return 0;
    }

    public int deleteOrder(long fk_order_id) {
        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM order_item WHERE fk_order_id = ?");) {
            statement.setLong(1, fk_order_id);
            return statement.executeUpdate();
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return 0;
    }

    public int deleteItemFromOrder(long fk_order_id, long fk_item_id) {
        try (Connection connection = DBUtils.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM order_item WHERE fk_order_id = ? AND fk_item_id = ? LIMIT 1");) {
            statement.setLong(1, fk_order_id);
            statement.setLong(2, fk_item_id);
            return statement.executeUpdate();
        } catch (Exception e) {
            LOGGER.debug(e);
            LOGGER.error(e.getMessage());
        }
        return 0;
    }
}
