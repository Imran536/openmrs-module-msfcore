package org.openmrs.module.msfcore.report;

import org.openmrs.api.context.Context;
import org.openmrs.module.Module;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.reporting.cohort.definition.library.BuiltInCohortDefinitionLibrary;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.manager.BaseReportManager;
import org.openmrs.module.reporting.report.manager.ReportManagerUtil;

public abstract class BaseMSFReportManager extends BaseReportManager {
    BuiltInCohortDefinitionLibrary getBuiltInCohortDefinitions() {
        return Context.getRegisteredComponents(BuiltInCohortDefinitionLibrary.class).get(0);
    }

    public void setup() {
        ReportManagerUtil.setupReport(this);
    }

    public String getDatasetName(ReportDefinition definition) {
        return definition.getName().toLowerCase().replace(" ", "_");
    }

    @Override
    public String getVersion() {
        Module thisModule = ModuleFactory.getModuleByPackage("org.openmrs.module.msfcore");
        return thisModule.getVersion();
    }
}
