-- src/main/resources/schema.sql

CREATE TABLE TOOL (
                      tool_code VARCHAR(10) PRIMARY KEY,
                      tool_type VARCHAR(50),
                      brand VARCHAR(50),
                      daily_charge DECIMAL(5, 2),
                      weekday_charge BOOLEAN,
                      weekend_charge BOOLEAN,
                      holiday_charge BOOLEAN
);

CREATE TABLE RENTAL_AGREEMENT (
                                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  tool_code VARCHAR(10),
                                  rental_days INT,
                                  checkout_date DATE,
                                  due_date DATE,
                                  daily_charge DECIMAL(5, 2),
                                  charge_days INT,
                                  pre_discount_charge DECIMAL(10, 2),
                                  discount_percent INT,
                                  discount_amount DECIMAL(10, 2),
                                  final_charge DECIMAL(10, 2),
                                  FOREIGN KEY (tool_code) REFERENCES TOOL(tool_code)
);
