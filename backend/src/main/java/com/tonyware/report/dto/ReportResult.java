package com.tonyware.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResult {

    private String[] header;
    private List<String[]> data = new ArrayList<>();

    public void addRow(String[] row) {
        this.data.add(row);
    }
}
