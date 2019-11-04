package com.tonyware.report;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.tonyware.report.dao.ReportDao;
import com.tonyware.report.dto.ReportResult;
import com.tonyware.report.model.Report;
import com.tonyware.report.service.ReportService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/plain")
@Produces(MediaType.TEXT_HTML)
public class PlanHtmlUtilController {

    MustacheFactory mf = new DefaultMustacheFactory();

    @GET
    @Path("/about")
    public String about() {
        return mf.compile("plain/about.mustache")
                .execute(new StringWriter(), Collections.emptyMap())
                .toString();
    }

}
