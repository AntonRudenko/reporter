package com.tonyware.report.service;

import com.tonyware.report.dto.ReportResult;
import com.tonyware.report.model.Config;
import com.tonyware.report.model.Param;
import com.tonyware.report.model.Report;
import org.apache.commons.dbcp2.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

@ApplicationScoped
public class ReportService {

    private Config config;
    private QueryRunner queryRunner;

    public ReportService() throws IOException {

        // TODO externalise
        Path path = Paths.get("/home/antonrudenko/tmp/config.json");
        BufferedReader reader = Files.newBufferedReader(path);

        // read config
        Jsonb jsonb = JsonbBuilder.create();
        config = jsonb.fromJson(reader, Config.class);

        checkDbDriverExist(config.getDriver());
        DataSource dataSource = createDatasource(config);
        queryRunner = new QueryRunner(dataSource);

    }

    private void checkDbDriverExist(String className) {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Driver is not on classpath. Driver name: " + className);
        }
    }

    private DataSource createDatasource(Config config) {
        ConnectionFactory factory = new DriverManagerConnectionFactory(config.getUrl(),config.getUser(), config.getPassword());
        PoolableConnectionFactory poolableFactory = new PoolableConnectionFactory(factory, null);
        ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableFactory);
        poolableFactory.setPool(connectionPool);
        PoolingDataSource<PoolableConnection> dataSource = new PoolingDataSource<>(connectionPool);
        return dataSource;
    }

    public ReportResult calculateReport(Report report, Map<String, String> params) throws SQLException {
        return queryRunner.query(report.getSql(), rs -> {
            ReportResult result = new ReportResult();

            ResultSetMetaData meta = rs.getMetaData();

            int tableWidth = meta.getColumnCount();
            String[] header = new String[tableWidth];
            for (int i = 1; i < tableWidth + 1; i++) {
                header[i - 1] = meta.getColumnName(i);
            }
            result.setHeader(header);

            while (rs.next()) {
                String[] row = new String[tableWidth];
                for (int i = 1; i < tableWidth + 1; i++) {
                    row[i - 1] = rs.getString(i);
                }
                result.addRow(row);
            }

            return result;
        }, convertParams(report, params).values().toArray());
    }

    private LinkedHashMap<String, Object> convertParams(Report report, Map<String, String> params) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        for (Param reportParam : report.getParams()) {
            String submittedParam = params.get(reportParam.getName());
            if (submittedParam == null) throw new IllegalArgumentException("No mandatory param submitted: " + reportParam.getName());
            result.put(reportParam.getName(), reportParam.getType().convert(submittedParam));
        }

        return result;
    }
}
