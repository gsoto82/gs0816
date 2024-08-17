-- src/main/resources/data.sql

INSERT INTO TOOL (tool_code, tool_type, brand, daily_charge, weekday_charge, weekend_charge, holiday_charge)
VALUES ('CHNS', 'Chainsaw', 'Stihl', 1.49, true, false, true);

INSERT INTO TOOL (tool_code, tool_type, brand, daily_charge, weekday_charge, weekend_charge, holiday_charge)
VALUES ('LADW', 'Ladder', 'Werner', 1.99, true, true, false);

INSERT INTO TOOL (tool_code, tool_type, brand, daily_charge, weekday_charge, weekend_charge, holiday_charge)
VALUES ('JAKD', 'Jackhammer', 'DeWalt', 2.99, true, false, false);

INSERT INTO TOOL (tool_code, tool_type, brand, daily_charge, weekday_charge, weekend_charge, holiday_charge)
VALUES ('JAKR', 'Jackhammer', 'Ridgid', 2.99, true, false, false);
