package com.tonyware.report;

import com.tonyware.report.dao.ReportDao;
import com.tonyware.report.dto.ReportResult;
import com.tonyware.report.model.Report;
import com.tonyware.report.service.ReportService;
import com.tonyware.report.util.ReportUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/report")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReportRestController {

    @Inject
    ReportDao reportDao;
    @Inject
    ReportService reportService;

    @GET
    public List<Report> reportList() {
        return reportDao.getAll();
    }

    @GET
    @Path("{name}")
    public Report getReport(@PathParam("name") String name) {
        return reportDao.getByName(name);
    }

    @GET
    @Path("{name}/data")
    public ReportResult calculateReport(@PathParam("name") String name) throws SQLException {
        Report report = reportDao.getByName(name);
        if (report.getParams().size() > 0) throw new IllegalArgumentException("This reports need params (use POST): " + report.getParams());
        return reportService.calculateReport(report, Collections.emptyMap());
    }

    @POST
    @Path("{name}/data")
    public ReportResult calculateReportWithParams(@PathParam("name") String name, Map<String, String> params) throws SQLException {
        System.out.println(JsonbBuilder.create().toJson(params));
        Report report = reportDao.getByName(name);
        return reportService.calculateReport(report, params);
    }

    @POST
    @Path("{name}/csv")
    public Response csvForReportWithParams(@PathParam("name") String name, Map<String, String> params) throws SQLException, IOException {
        Report report = reportDao.getByName(name);
        Map<String, String> filteredParams = ReportUtils.filterUniqueParams(params, report);
        ReportResult reportResult = reportService.calculateReport(report, filteredParams);

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

    }

    @POST
    @Path("reload")
    public void reloadReportData() throws IOException {
        reportDao.reloadReportList();
    }

}
