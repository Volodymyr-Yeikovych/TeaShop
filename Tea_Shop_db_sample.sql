CREATE TABLE shops (
	sh_id BIGSERIAL PRIMARY KEY NOT NULL,
	street VARCHAR(25) NOT NULL,
	city VARCHAR(25) NOT NULL,
	country VARCHAR(25) NOT NULL,
	work_hours CHAR(5) NOT NULL,
	size INT NOT NULL,
	staff INT NOT NULL,
	CONSTRAINT size CHECK (size BETWEEN 1 AND 5),
	CONSTRAINT work_hours CHECK (work_hours LIKE '[0-9][0-9]-[0-9][0-9]'));

CREATE TABLE stock (
	tea_id SERIAL4 PRIMARY KEY NOT NULL,
	tea_name VARCHAR(50) NOT NULL,
	country VARCHAR(25) NOT NULL,
	av_kg INT8 NOT NULL,
	price_kg DECIMAL(5,2) NOT NULL,
	sh_in_stock INT4[],
	ret_id INT4 NOT NULL,
	CONSTRAINT av_kd CHECK (av_kg >= 0),
	CONSTRAINT price_kg CHECK (price_kg > 0),
	CONSTRAINT ret_id CHECK (ret_id > 0));

CREATE TABLE retailers (
	ret_id SERIAL4 PRIMARY KEY NOT NULL,
	ret_name VARCHAR(50) NOT NULL,
	country VARCHAR(25) NOT NULL);

ALTER TABLE stock
	DROP CONSTRAINT ret_id;

ALTER TABLE stock
	ADD CONSTRAINT ret_id FOREIGN KEY (ret_id) REFERENCES retailers (ret_id);

CREATE TABLE employees (
	emp_id SERIAL4 PRIMARY KEY NOT NULL,
	emp_name VARCHAR(25) NOT NULL,
	position VARCHAR(25) NOT NULL,
	position_acronym CHAR(3) NOT NULL,
	authority INT2 NOT NULL,
	salary DECIMAL(8,2) NOT NULL,
	sh_id INT8 REFERENCES shops (sh_id) NOT NULL,
	CONSTRAINT position_acronym CHECK (position_acronym LIKE '[A-Z][A-Z][A-Z]'),
	CONSTRAINT authority CHECK (authority BETWEEN 1 AND 9),
	CONSTRAINT salary CHECK (salary >= 0));

CREATE TABLE clients (
	cl_id SERIAL4 PRIMARY KEY NOT NULL,
	cl_name VARCHAR(50) NOT NULL,
	city VARCHAR(25) NOT NULL,
	country VARCHAR(25) NOT NULL,
	email VARCHAR(50) NOT NULL,
	CONSTRAINT email CHECK (email SIMILAR TO '[a-zA-Z0-9]+[@]([a-zA-Z0-9]+[.])+([a-zA-Z0-9]+)?'));

ALTER TABLE shops 
DROP CONSTRAINT work_hours;

ALTER TABLE shops
ADD CONSTRAINT work_hours CHECK (work_hours SIMILAR TO '[0-9]{2}[-][0-9]{2}');

INSERT INTO shops (street, city, country, work_hours, size, staff) VALUES ('Stepana Bandery 147', 'Kyiv', 'Ukraine', '07-23', 5, 12);
INSERT INTO shops (street, city, country, work_hours, size, staff) VALUES ('Cathedral 5', 'Lviv', 'Ukraine', '07-23', 2, 4);
INSERT INTO shops (street, city, country, work_hours, size, staff) VALUES ('Jewish 3', 'Odesa', 'Ukraine', '09-21', 3, 6);
INSERT INTO shops (street, city, country, work_hours, size, staff) VALUES ('Świętokrzyska 45', 'Warsaw', 'Poland', '08-19', 4, 7);

ALTER TABLE employees
DROP CONSTRAINT position_acronym;

ALTER TABLE employees
ADD CONSTRAINT position_acronym CHECK (position_acronym SIMILAR TO '[A-Z]{3}');

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Gerald Wuxian', 'Chief Executive Officer', 'CEO', 9, 13500.00, 1);

ALTER TABLE employees 
ALTER COLUMN position TYPE VARCHAR(50);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('John Potz', 'Risk Manager', 'RMN', 6, 3500.00, 1);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Alli Nazek', 'Sales Manager', 'SMN', 4, 2500.00, 1);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Olex Wanarziski', 'Tea Quality Assurance', 'TQA', 4, 2300.00, 1);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Carl Marks', 'Advertising & Marketing Manager', 'AMM', 5, 4000.00, 2);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Sophie Plamart', 'Human Resources Manager', 'HRM', 4, 2100.00, 1);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Vlad Mechnikov', 'Chief Finance Officer', 'CFO', 7, 6700.00, 3);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Max Volynov', 'Tea Serving Assistant', 'TSA', 2, 600.00, 1);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Peter Kalabodko', 'Tea Serving Assistant', 'TSA', 2, 600.00, 1);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Oleg Podbereznyi', 'Tea Serving Assistant', 'TSA', 2, 600.00, 1);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Vasil Maximenko', 'Tea Serving Assistant', 'TSA', 2, 600.00, 1);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Nicola Podnebesnyi', 'Tea Serving Assistant', 'TSA', 2, 600.00, 1);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Max Alexandrovych', 'Head of Tea Serving Department', 'HSD', 3, 900.00, 1);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Artem Pyvnyi', 'Tea Serving Assistant', 'TSA', 2, 700.00, 2);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Nicolay Fugasnyi', 'Tea Serving Assistant', 'TSA', 2, 700.00, 2);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Pasha Melnik', 'Tea Serving Assistant', 'TSA', 2, 700.00, 2);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Arsen Kruchenyi', 'Tea Serving Assistant', 'TSA', 2, 650.00, 3);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Dima Rybolov', 'Tea Serving Assistant', 'TSA', 2, 650.00, 3);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Valera Fiskalnyi', 'Tea Serving Assistant', 'TSA', 2, 650.00, 3);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Anton Morskyi', 'Head of Tea Serving Department', 'HSD', 3, 1000.00, 3);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Sasha Sergiivna', 'Tea Serving Assistant', 'TSA', 2, 650.00, 3);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Ryszard Stahowski', 'Tea Serving Assistant', 'TSA', 2, 900.00, 4);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Agata Przesowska', 'Tea Serving Assistant', 'TSA', 2, 900.00, 4);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Marek Dzyćic', 'Tea Serving Assistant', 'TSA', 2, 900.00, 4);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Stephania Dobra', 'Tea Serving Assistant', 'TSA', 2, 900.00, 4);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Serz Przękowski', 'Tea Serving Assistant', 'TSA', 2, 900.00, 4);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Antek Makowicky', 'Head of Tea Serving Department', 'HSD', 2, 1300.00, 4);

INSERT INTO employees (emp_name, position, position_acronym, authority, salary, sh_id) 
VALUES ('Andzej Wyjackiy', 'Head of Polish Branch', 'HPB', 4, 2200.00, 4);

ALTER TABLE stock 
ADD COLUMN profit_percentage_kg DECIMAL(3,2) NOT NULL,
ADD COLUMN profit_sum_kg DECIMAL(5,2) NOT NULL,
ADD COLUMN ret_price_kg DECIMAL(5,2) NOT NULL;

INSERT INTO retailers (ret_name, country) VALUES ('Golden Tea', 'Bangladesh');
INSERT INTO retailers (ret_name, country) VALUES ('Mangust Touch', 'Pakistan');
INSERT INTO retailers (ret_name, country) VALUES ('Tea-Shrine', 'Japan');
INSERT INTO retailers (ret_name, country) VALUES ('Xiao Mei Inc', 'China');

INSERT INTO stock (tea_name, country, av_kg, price_kg, sh_in_stock, ret_id, profit_percentage_kg, profit_sum_kg, ret_price_kg)
VALUES ('Earl Grey', 'United Kingdom', 157, 47.99, '{"2", "4", "5"}', 2, 
		CAST (((47.99-39.99) / 47.99) AS DECIMAL (3,2)), (47.99-39.99), 39.99);

INSERT INTO stock (tea_name, country, av_kg, price_kg, sh_in_stock, ret_id, profit_percentage_kg, profit_sum_kg, ret_price_kg)
VALUES ('Gong Mei', 'China', 298, 99.99, '{"2", "3", "4", "5"}', 4,
		CAST (((99.99-78.56) / 99.99) AS DECIMAL (3,2)), (99.99-78.56), 78.56);

INSERT INTO stock (tea_name, country, av_kg, price_kg, sh_in_stock, ret_id, profit_percentage_kg, profit_sum_kg, ret_price_kg)
VALUES ('Ancient Tree Dan Cong', 'China', 148, 159.99, '{"2", "5"}', 4,
		CAST (((159.99-105.78) / 159.99) AS DECIMAL (3,2)), (159.99-105.78), 105.78);

INSERT INTO stock (tea_name, country, av_kg, price_kg, sh_in_stock, ret_id, profit_percentage_kg, profit_sum_kg, ret_price_kg)
VALUES ('English Breakfast', 'United Kingdom', 490, 34.99, '{"2", "5"}', 1,
		CAST (((34.99-25.99) / 34.99) AS DECIMAL (3,2)), (34.99-25.99), 25.99);


INSERT INTO stock (tea_name, country, av_kg, price_kg, sh_in_stock, ret_id, profit_percentage_kg, profit_sum_kg, ret_price_kg)
VALUES ('Ceylon', 'Bangladesh', 134, 79.99, '{"2", "3", "5"}', 1,
		CAST (((79.99-54.67) / 79.99) AS DECIMAL (3,2)), (79.99-54.67), 54.67);

ALTER TABLE stock
RENAME COLUMN av_kg TO avaliable_kg;

ALTER TABLE clients 
ADD COLUMN cl_password VARCHAR(50) NOT NULL CHECK (LENGTH(cl_password) > 4);

GRANT ALL ON ALL TABLES IN SCHEMA public TO tea_shop_owner;

ALTER TABLE clients 
ADD CONSTRAINT cl_name UNIQUE(cl_name);

GRANT USAGE, SELECT ON SEQUENCE clients_cl_id_seq TO tea_shop_owner;

ALTER TABLE clients
DROP CONSTRAINT email;
