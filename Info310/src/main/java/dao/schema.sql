/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  yeah2
 * Created: 15 Apr 2021
 */

create table Customer(
    Customer_ID int auto_increment,
    Username varchar(50) not null unique,
    Password varchar(200) not null,
    Firstname varchar(50) not null,
    Lastname varchar(50) not null,
    Phone_Number varchar(50) not null,
    Email_Address varchar(50) not null,

    constraint Customer_PK primary key (Customer_ID)
);

create table Subscription(
    Subscription_ID int auto_increment(1000),
    Name varchar(50) not null,
    Paid boolean not null,
    Subscription_Price decimal(10,2) not null,
    --subscriptionType varchar(50) not null,
    Category varchar(50) not null,
    Company_Name varchar(100) not null,
    Description varchar(100),
    Issue_Date date,
    Due_Date date,
    
    Customer_ID int,

    constraint Subscription_PK primary key (Subscription_ID),
    constraint Customer_FK foreign key (Customer_ID) references Customer
);