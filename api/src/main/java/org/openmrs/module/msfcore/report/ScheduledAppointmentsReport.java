package org.openmrs.module.msfcore.report;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.api.context.Context;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.manager.ReportManagerUtil;
import org.openmrs.module.reporting.report.util.ReportUtil;
import org.springframework.stereotype.Component;

/**
 * List of patients scheduled for appointments in given date range at specific location
 */
@Component
public class ScheduledAppointmentsReport extends BaseMSFReportManager {

    @Override
    public String getName() {
        return Context.getMessageSourceService().getMessage("msfcore.reports.scheduledAppointments");
    }

    @Override
    public String getUuid() {
        return ReportConstants.SCHEDULED_APPOINTMENTS_REPORT_UUID;
    }

    @Override
    public String getDescription() {
        return Context.getMessageSourceService().getMessage("msfcore.reports.scheduledAppointments.description");
    }

    @Override
    public List<Parameter> getParameters() {
        List<Parameter> parameterArrayList = new ArrayList<Parameter>();
        parameterArrayList.add(ReportConstants.START_DATE_PARAMETER);
        parameterArrayList.add(ReportConstants.END_DATE_PARAMETER);
        parameterArrayList.add(ReportConstants.LOCATION_PARAMETER);
        return parameterArrayList;
    }

    @Override
    public ReportDefinition constructReportDefinition() {
        ReportDefinition reportDef = new ReportDefinition();
        reportDef.setUuid(getUuid());
        reportDef.setName(getName());
        reportDef.setDescription(getDescription());
        reportDef.setParameters(getParameters());

        // Using sql is the easiest way here
        SqlDataSetDefinition sqlDataDef = new SqlDataSetDefinition();
        sqlDataDef.setUuid("dc2b093b-531a-484a-aa6e-b185f61215cb");
        sqlDataDef.setName(getDatasetName(reportDef));
        sqlDataDef.addParameters(getParameters());
        sqlDataDef.setSqlQuery(getReportSQLQuery());
        reportDef.addDataSetDefinition(sqlDataDef.getName(), Mapped.mapStraightThrough(sqlDataDef));
        return reportDef;
    }

    @Override
    public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
        List<ReportDesign> l = new ArrayList<ReportDesign>();
        ReportDesign design = ReportManagerUtil.createExcelTemplateDesign("be2118d8-c8f1-4948-ae65-ec63237e6a95", reportDefinition,
                        "excel_template_appointments.xls");
        design.addPropertyValue("repeatingSections", "sheet:1,row:6,dataset:" + getDatasetName(reportDefinition));
        design.setName(Context.getMessageSourceService().getMessage("msfcore.reports.scheduledAppointments.exceltemplate"));
        l.add(design);
        return l;
    }

    private String getReportSQLQuery() {
        return ReportUtil.readStringFromResource("query_appointments.sql");
    }

}
