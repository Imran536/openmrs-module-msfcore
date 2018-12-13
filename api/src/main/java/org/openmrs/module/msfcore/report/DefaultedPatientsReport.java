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
 * Number of unique patients per diagnosis by gender report, auto installed in
 * MSFCoreActivator
 */
@Component
public class DefaultedPatientsReport extends BaseMSFReportManager {

    @Override
    public String getName() {
        return Context.getMessageSourceService().getMessage("msfcore.reports.defaultedPatients");
    }

    public String getDatasetName(ReportDefinition definition) {
        return definition.getName().toLowerCase().replace(" ", "_");
    }

    @Override
    public String getUuid() {
        return ReportConstants.DEFAULTED_PATIENTS_REPORT_UUID;
    }

    @Override
    public String getDescription() {
        return Context.getMessageSourceService().getMessage("msfcore.reports.defaultedPatients.description");
    }

    @Override
    public String getVersion() {
        return "1.0-SNAPSHOT";// remove '-SNAPSHOT' to release
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
        sqlDataDef.setUuid("304f741c-60a2-4a44-bf3b-89cd1308f10a");
        sqlDataDef.setName(getDatasetName(reportDef));
        sqlDataDef.addParameters(getParameters());
        sqlDataDef.setSqlQuery(getReportSQLQuery());
        reportDef.addDataSetDefinition(sqlDataDef.getName(), Mapped.mapStraightThrough(sqlDataDef));
        return reportDef;
    }

    @Override
    public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
        List<ReportDesign> l = new ArrayList<ReportDesign>();
        ReportDesign design = ReportManagerUtil.createExcelTemplateDesign("618a3b96-4879-4234-8967-5dac6b2b3e83", reportDefinition,
                        "excel_template_defaulters.xls");
        design.addPropertyValue("repeatingSections", "sheet:1,row:6,dataset:" + getDatasetName(reportDefinition));
        design.setName("excel_template_defaulters");
        l.add(design);
        return l;
    }

    private String getReportSQLQuery() {
        return ReportUtil.readStringFromResource("query_defaulters.sql");
    }

}
