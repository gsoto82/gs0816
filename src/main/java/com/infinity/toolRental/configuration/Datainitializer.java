package com.infinity.toolRental.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Configuration
public class Datainitializer {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void initializeDatabase() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            // Insert the four records into the TOOL table
            statement.execute("INSERT INTO TOOL (tool_code, tool_type, brand, daily_charge, weekday_charge, weekend_charge, holiday_charge) " +
                    "VALUES ('CHNS', 'Chainsaw', 'Stihl', 1.49, true, false, true)");

            statement.execute("INSERT INTO TOOL (tool_code, tool_type, brand, daily_charge, weekday_charge, weekend_charge, holiday_charge) " +
                    "VALUES ('LADW', 'Ladder', 'Werner', 1.99, true, true, false)");

            statement.execute("INSERT INTO TOOL (tool_code, tool_type, brand, daily_charge, weekday_charge, weekend_charge, holiday_charge) " +
                    "VALUES ('JAKD', 'Jackhammer', 'DeWalt', 2.99, true, false, false)");

            statement.execute("INSERT INTO TOOL (tool_code, tool_type, brand, daily_charge, weekday_charge, weekend_charge, holiday_charge) " +
                    "VALUES ('JAKR', 'Jackhammer', 'Ridgid', 2.99, true, false, false)");

            System.out.println("Database initialized with four tool records.");


            // Add more inserts as needed
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
