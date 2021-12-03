drop schema ims;

CREATE SCHEMA IF NOT EXISTS `ims`;

USE `ims` ;

CREATE TABLE IF NOT EXISTS `ims`.`customers` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `first_name` VARCHAR(40) DEFAULT NULL,
    `surname` VARCHAR(40) DEFAULT NULL,
    PRIMARY KEY (`id`)
);


CREATE TABLE IF NOT EXISTS `ims`.`orders` (
    id int primary key auto_increment,
    cost float not null,
    fk_customer_id int not null,
    foreign key (fk_customer_id) references customers(id)
);

CREATE TABLE IF NOT EXISTS `ims`.`items` (
    id int primary key auto_increment,
    name varchar(50) not null,
    price float not null,
    stock int not null
);

CREATE TABLE IF NOT EXISTS `ims`.`order_item` (
    id int primary key auto_increment,
    fk_order_id int not null,
    fk_item_id int not null,
    foreign key (fk_order_id) references orders(id),
    foreign key (fk_item_id) references items(id)
);