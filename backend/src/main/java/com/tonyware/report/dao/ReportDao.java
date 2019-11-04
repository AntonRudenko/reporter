package com.tonyware.report.dao;

import com.tonyware.report.model.Report;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class ReportDao {

    private List<Report> reports = new ArrayList<>();

    private static final String REPORTS_FILE_NAME = "reports.json";

    public ReportDao() throws IOException {
        readReportList();
    }

    public void reloadReportList() throws IOException {
        readReportList();
    }

    private void readReportList() throws IOException {
        // TODO externalise base path
        String basePath = "/home/antonrudenko/tmp/jsondb/";

        Path path = Paths.get(basePath, REPORTS_FILE_NAME);
        if (!Files.exists(path)) {
            Files.createFile(path);
            Files.write(path, "[]".getBytes());
        }

        BufferedReader reader = Files.newBufferedReader(path);

        Jsonb jsonb = JsonbBuilder.create();
        List<Report> list = Arrays.asList(jsonb.fromJson(reader, Report[].class));
        reader.close();

        reports = list;
    }

    public List<Report> getAll() {
        return reports;
    }

    public Report getByName(String name) {
        return reports.stream().filter(r -> r.getName().equals(name))
                .findFirst()
                .get();
    }
}
