package com.tonyware.report.util;

import com.tonyware.report.model.Report;

import java.util.HashMap;
import java.util.Map;

public class ReportUtils {

    public static Map<String, String> filterParams(Map<String, String[]> parameterMap, Report report) {
        Map<String, String> filteredParams = new HashMap<>();
        for (Map.Entry<String, String[]> submittedParam : parameterMap.entrySet()) {
            String paramName = submittedParam.getKey();
            boolean reportHasParam = report.getParams().stream().anyMatch(p -> p.getName().equals(paramName));
            if (reportHasParam) {
                filteredParams.put(submittedParam.getKey(), submittedParam.getValue()[0]);
            }
        }
        return filteredParams;
    }

    public static Map<String, String> filterUniqueParams(Map<String, String> parameterMap, Report report) {
        Map<String, String> filteredParams = new HashMap<>();
        for (Map.Entry<String, String> submittedParam : parameterMap.entrySet()) {
            String paramName = submittedParam.getKey();
            boolean reportHasParam = report.getParams().stream().anyMatch(p -> p.getName().equals(paramName));
            if (reportHasParam) {
                filteredParams.put(submittedParam.getKey(), submittedParam.getValue());
            }
        }
        return filteredParams;
    }
}
