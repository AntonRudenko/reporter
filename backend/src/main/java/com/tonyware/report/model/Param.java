package com.tonyware.report.model;

import lombok.Data;

@Data
public class Param {

    private String name;
    private ParamType type;
    private Boolean required = false;

}
