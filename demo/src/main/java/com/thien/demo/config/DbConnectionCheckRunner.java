package com.thien.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Component
public class DbConnectionCheckRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DbConnectionCheckRunner.class);
    private final DataSource dataSource;

    public DbConnectionCheckRunner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM DUAL");
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                log.info("Oracle connection successful. SELECT 1 FROM DUAL = {}", rs.getInt(1));
            } else {
                log.error("Oracle connection failed: no result from DUAL");
            }
        }
    }
}