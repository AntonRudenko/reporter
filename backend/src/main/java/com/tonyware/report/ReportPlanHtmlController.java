package com.tonyware.report;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.tonyware.report.dao.ReportDao;
import com.tonyware.report.dto.ReportResult;
import com.tonyware.report.model.Report;
import com.tonyware.report.service.ReportService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/plain/report")
@Produces(MediaType.TEXT_HTML)
public class ReportPlanHtmlController {

    @Inject
    ReportDao reportDao;
    @Inject
    ReportService reportService;

    MustacheFactory mf = new DefaultMustacheFactory();

    @GET
    public String reportList() {
        List<Report> reports = reportDao.getAll();
        Mustache compile = mf.compile("plain/list.mustache");
        HashMap<String, Object> scopes = new HashMap<>();
        scopes.put("reports", reports);
        Writer execute = compile.execute(new StringWriter(), scopes);
        return execute.toString();
    }

    @GET
    @Path("{name}")
    public String getReport(@PathParam("name") String name) {
        Report report = reportDao.getByName(name);

        return mf.compile("plain/report.mustache")
                .execute(new StringWriter(), Collections.singletonMap("report", report))
                .toString();
    }


    @GET
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("{name}/data")
    public Response calculateReportWithParams(@PathParam("name") String name,
                                              @Context HttpServletRequest request) throws SQLException, IOException {

        Report report = reportDao.getByName(name);
        Map<String, String> filteredParams = filterParams(request.getParameterMap(), report);
        ReportResult reportResult = reportService.calculateReport(report, filteredParams);
        if (request.getParameterMap().containsKey("csv")) {
            StringWriter out = new StringWriter();
            CSVPrinter csv = new CSVPrinter(
                    out,
                    CSVFormat.DEFAULT.withHeader(reportResult.getHeader())
            );
            for (String[] record : reportResult.getData()) {
                csv.printRecord(record);
            }

            return Response.ok()
                    .entity(out.toString().getBytes())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM)
                    .build();
        } else {
            return Response.ok()
                    .entity(
                            mf.compile("plain/reportResult.mustache")
                                    .execute(new StringWriter(), Collections.singletonMap("result", reportResult))
                                    .toString()
                    )
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML)
                    .build();
        }

    }

    private Map<String, String> filterParams(Map<String, String[]> parameterMap, Report report) {
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

}
