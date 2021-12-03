package com.qa.ims;

import com.qa.ims.controller.*;
import com.qa.ims.persistence.dao.ItemDAO;
import com.qa.ims.persistence.dao.OrderDAO;
import com.qa.ims.persistence.dao.OrderItemDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qa.ims.persistence.dao.CustomerDAO;
import com.qa.ims.persistence.domain.Domain;
import com.qa.ims.utils.DBUtils;
import com.qa.ims.utils.Utils;

public class IMS {

	public static final Logger LOGGER = LogManager.getLogger();

	private final CustomerController customers;
	private final ItemController itemController;
	private final OrderController orderController;
	private final Utils utils;

	public IMS() {
		this.utils = new Utils();
		final CustomerDAO custDAO = new CustomerDAO();
		final ItemDAO itemDAO = new ItemDAO();
		final OrderDAO orderDAO = new OrderDAO();
		final OrderItemDAO orderItemDAO = new OrderItemDAO();

		this.customers = new CustomerController(custDAO, utils);
		this.itemController = new ItemController(itemDAO, utils);
		this.orderController = new OrderController(orderDAO, orderItemDAO, itemDAO, utils);

	}

	public void imsSystem() {
		LOGGER.info("Welcome to the Inventory Management System!");
		DBUtils dbUtils = DBUtils.connect();
		dbUtils.init("src\\main\\resources\\sql-schema.sql", "src\\main\\resources\\sql-data.sql");
		Domain domain = null;
		do {
			LOGGER.info("Which entity would you like to use?");
			Domain.printDomains();

			domain = Domain.getDomain(utils);

			domainAction(domain);

		} while (domain != Domain.STOP);
	}

	private void domainAction(Domain domain) {
		boolean changeDomain = false;
		do {

			CrudController<?> active = null;
			switch (domain) {
			case CUSTOMER:
				active = this.customers;
				break;
			case ITEM:
				active = this.itemController;
				break;
			case ORDER:
				active = this.orderController;
				break;
			case STOP:
				return;
			default:
				break;
			}

			LOGGER.info(() ->"What would you like to do with " + domain.name().toLowerCase() + ":");

			Action.printActions();
			Action action = Action.getAction(utils);

			if (action == Action.RETURN) {
				changeDomain = true;
			} else {
				doAction(active, action);
			}
		} while (!changeDomain);
	}

	public void doAction(CrudController<?> crudController, Action action) {
		switch (action) {
			case CREATE:
				crudController.create();
				break;
			case READ:
				crudController.readAll();
				break;
			case UPDATE:
				crudController.update();
				break;
			case DELETE:
				crudController.delete();
				break;
			case ADD:
				crudController.addItem();
				break;
			case REMOVE:
				crudController.removeItem();
				break;
			case CALCULATE:
				crudController.calculateCost();
				break;
			case RETURN:
				break;
		default:
			break;
		}
	}

}
