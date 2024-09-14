SHOW DATABASES;

CREATE DATABASE library_db;

USE library_db;

CREATE TABLE books (book_id INT PRIMARY KEY AUTO_INCREMENT,title VARCHAR(255) NOT NULL,author VARCHAR(255) NOT NULL,publisher VARCHAR(255),year_published INT);

SHOW TABLES;

SELECT*FROM books;

CREATE TABLE members (member_id INT PRIMARY KEY AUTO_INCREMENT,name VARCHAR(255) NOT NULL,email VARCHAR(255) UNIQUE NOT NULL,phone VARCHAR(20));

SELECT*FROM members;

CREATE TABLE loans (loan_id INT PRIMARY KEY AUTO_INCREMENT,book_id INT,member_id INT,loan_date DATE,return_date DATE,FOREIGN KEY (book_id) REFERENCES books(book_id),FOREIGN KEY (member_id) REFERENCES members(member_id));

SELECT*FROM loans;



