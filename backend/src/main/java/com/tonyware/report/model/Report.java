package com.tonyware.report.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    private String name;
    private String description;
    private String sql;
    private List<Param> params;


}
