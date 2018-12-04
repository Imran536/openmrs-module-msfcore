/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * 
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.msfcore;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.dataexchange.DataImporter;
import org.openmrs.module.emrapi.EmrApiConstants;
import org.openmrs.module.idgen.IdentifierSource;
import org.openmrs.module.idgen.service.IdentifierSourceService;
import org.openmrs.module.metadatadeploy.api.MetadataDeployService;
import org.openmrs.module.metadatamapping.MetadataTermMapping;
import org.openmrs.module.metadatamapping.api.MetadataMappingService;
import org.openmrs.module.msfcore.activator.HtmlFormsInitializer;
import org.openmrs.module.msfcore.api.DHISService;
import org.openmrs.module.msfcore.api.MSFCoreService;
import org.openmrs.module.msfcore.metadata.MSFMetadataBundle;
import org.openmrs.module.msfcore.metadata.PatientIdentifierTypes;
import org.openmrs.module.msfcore.report.BaseMSFReportManager;
import org.openmrs.module.referencemetadata.ReferenceMetadataConstants;
import org.openmrs.scheduler.TaskDefinition;

/**
 * This class contains the logic that is run every time this module is either
 * started or shutdown
 */
public class MSFCoreActivator extends BaseModuleActivator {

    private Log log = LogFactory.getLog(getClass());

    /**
     * @see #started()
     */
    public void started() {
        log.info("Started MSF Core Module");

        try {
            Context.getService(DHISService.class).transferDHISMappingsToDataDirectory();
            Context.getService(DHISService.class).installDHIS2Metadata();
        } catch (Exception e) {
            log.info("Error thrown when configuring Dhis2 metadata: " + e.getMessage());
        }

        Context.getService(MSFCoreService.class).overwriteSync2Configuration();

        triggerMSFApps(true);
        installMSFMeta();

        // ensure 'Auto Close Visits Task' is started
        TaskDefinition autoCloseVisits = Context.getSchedulerService().getTaskByName(MSFCoreConfig.TASK_AUTO_CLOSE_VISIT);
        if (autoCloseVisits != null && !autoCloseVisits.getStartOnStartup()) {
            autoCloseVisits.setStartOnStartup(true);
            Context.getSchedulerService().saveTaskDefinition(autoCloseVisits);
        }
    }

    private void triggerMSFApps(boolean on) {
        if (on) {
            log.info("Replacing default reference application registration app");
            Context.getService(AppFrameworkService.class).disableApp(MSFCoreConfig.REGISTRATION_APP_EXTENSION_ID);

            log.info("Disabling default reports app");
            Context.getService(AppFrameworkService.class).disableApp(MSFCoreConfig.REPORTS_APP_EXTENSION_ID);

            // disable the default find patient app to provide one which allows
            // searching for patients at the footer of the search for patients
            // page
            Context.getService(AppFrameworkService.class).disableApp(MSFCoreConfig.SEARCH_APP_EXTENSION_ID);

            Context.getService(AppFrameworkService.class).disableApp(MSFCoreConfig.SEARCH_APP_EXTENSION_ID);
            Context.getService(AppFrameworkService.class).disableApp(MSFCoreConfig.CONDITIONS_EXTENSION_ID);
            Context.getService(AppFrameworkService.class).disableApp(MSFCoreConfig.RELATIONSHIP_EXTENSION_ID);
            Context.getService(AppFrameworkService.class).disableApp(MSFCoreConfig.LATEST_OBS_EXTENSION_ID);
            Context.getService(AppFrameworkService.class).disableApp(MSFCoreConfig.DIAGNOSIS_EXTENSION_ID);
            Context.getService(AppFrameworkService.class).disableApp(MSFCoreConfig.MOST_RECENT_VITALS_EXTENSION_ID);
            Context.getService(AppFrameworkService.class).disableApp(MSFCoreConfig.VISIT_BY_ENCOUNTER_TYPE_EXTENSION_ID);
            Context.getService(AppFrameworkService.class).disableApp(MSFCoreConfig.OBS_GRAPH_EXTENSION_ID);
            Context.getService(AppFrameworkService.class).disableExtension(MSFCoreConfig.CHART_SEARCH_EXTENSION_ID);
            Context.getService(AppFrameworkService.class).disableExtension(MSFCoreConfig.ADMIT_TO_INPATIENT_EXTENSION_ID);
            Context.getService(AppFrameworkService.class).disableExtension(MSFCoreConfig.VISIT_NOTE_EXTENSION_ID);
        } else {
            log.info("Enabling default reference application apps");
            Context.getService(AppFrameworkService.class).enableApp(MSFCoreConfig.REGISTRATION_APP_EXTENSION_ID);

            // disable the MSF find patient app & enable the default core apps
            Context.getService(AppFrameworkService.class).enableApp(MSFCoreConfig.SEARCH_APP_EXTENSION_ID);
            Context.getService(AppFrameworkService.class).enableApp(MSFCoreConfig.CONDITIONS_EXTENSION_ID);
            Context.getService(AppFrameworkService.class).enableApp(MSFCoreConfig.RELATIONSHIP_EXTENSION_ID);
            Context.getService(AppFrameworkService.class).enableApp(MSFCoreConfig.LATEST_OBS_EXTENSION_ID);
            Context.getService(AppFrameworkService.class).enableApp(MSFCoreConfig.DIAGNOSIS_EXTENSION_ID);
            Context.getService(AppFrameworkService.class).enableApp(MSFCoreConfig.MOST_RECENT_VITALS_EXTENSION_ID);
            Context.getService(AppFrameworkService.class).enableApp(MSFCoreConfig.VISIT_BY_ENCOUNTER_TYPE_EXTENSION_ID);
            Context.getService(AppFrameworkService.class).enableApp(MSFCoreConfig.OBS_GRAPH_EXTENSION_ID);
            Context.getService(AppFrameworkService.class).enableExtension(MSFCoreConfig.CHART_SEARCH_EXTENSION_ID);
            Context.getService(AppFrameworkService.class).enableExtension(MSFCoreConfig.ADMIT_TO_INPATIENT_EXTENSION_ID);
            Context.getService(AppFrameworkService.class).enableExtension(MSFCoreConfig.VISIT_NOTE_EXTENSION_ID);
        }
    }

    private void installMSFMeta() {
        // install concepts
        DataImporter dataImporter = Context.getRegisteredComponent("dataImporter", DataImporter.class);

        log.info("Importing MSF CIEL Concepts");
        dataImporter.importData("CIELConcepts.xml");
        log.info("MSF CIEL Concepts imported");

        log.info("Importing MSF Custom Concepts");
        dataImporter.importData("MSFCustomConcepts.xml");
        log.info("MSF Custom Concepts imported");

        log.info("Importing MSF Drugs");
        dataImporter.importData("MSFDrugs.xml");
        log.info("MSF Drugs imported");

        log.info("Installing MSF metadata bundle");
        Context.getService(MetadataDeployService.class).installBundle(Context.getRegisteredComponents(MSFMetadataBundle.class).get(0));

        log.info("Installing MSF Reports");
        for (BaseMSFReportManager msfReport : Context.getRegisteredComponents(BaseMSFReportManager.class)) {
            msfReport.setup();
        }

        log.info("Installation and configuration of default MSF Identifier");
        Context.getService(MSFCoreService.class).msfIdentifierGeneratorInstallation();

        log.info("Un requiring OpenMRS ID if not done");
        PatientIdentifierType openmrsIdType = Context.getPatientService().getPatientIdentifierTypeByName(
                        ReferenceMetadataConstants.OPENMRS_ID_NAME);
        if (openmrsIdType.getRequired()) {
            openmrsIdType.setRequired(false);
            Context.getPatientService().savePatientIdentifierType(openmrsIdType);
        }

        log.info("Configuting primary Identifier");
        MetadataMappingService metadataMappingService = Context.getService(MetadataMappingService.class);
        MetadataTermMapping primaryIdentifierTypeMapping = metadataMappingService.getMetadataTermMapping(
                        EmrApiConstants.EMR_METADATA_SOURCE_NAME, EmrApiConstants.PRIMARY_IDENTIFIER_TYPE);
        PatientIdentifierType msfIdType = Context.getPatientService().getPatientIdentifierTypeByUuid(
                        PatientIdentifierTypes.MSF_PRIMARY_ID_TYPE.uuid());
        if (!msfIdType.getUuid().equals(primaryIdentifierTypeMapping.getMetadataUuid())) {
            primaryIdentifierTypeMapping.setMappedObject(msfIdType);
            metadataMappingService.saveMetadataTermMapping(primaryIdentifierTypeMapping);
        }

        log.info("Setting primary identifier source");
        IdentifierSource msfIdSource = Context.getService(IdentifierSourceService.class).getIdentifierSourceByUuid(
                        MSFCoreConfig.PATIENT_ID_TYPE_SOURCE_UUID);
        if (msfIdSource != null) {
            Context.getAdministrationService().updateGlobalProperty(MSFCoreConfig.GP_OPENMRS_IDENTIFIER_SOURCE_ID,
                            String.valueOf(msfIdSource.getId()));
        }

        log.info("Installing MSF Forms");
        installMsfForms();

        // create a map between visit types and encounter types to enable the
        // autocreation of visits
        // all encounters are mapped to the default Facility Visit Type - change
        // this mapping to match encounter types to facility types
        // see https://issues.openmrs.org/browse/EA-116 for more information
        Context.getAdministrationService().setGlobalProperty(
                        MSFCoreConfig.GP_EMRAPI_EMRAPIVISITSASSIGNMENTHANDLER_ENCOUNTERTYPETONEWVISITTYPEMAP,
                        "default:7b0f5697-27e3-40c4-8bae-f4049abfb4ed");

    }

    private void removeMSFMeta() {
        log.info("Requiring OpenMRS ID if not done");
        PatientIdentifierType openmrsIdType = Context.getPatientService().getPatientIdentifierTypeByName(
                        ReferenceMetadataConstants.OPENMRS_ID_NAME);
        PatientIdentifierType msfIdType = Context.getPatientService().getPatientIdentifierTypeByUuid(
                        PatientIdentifierTypes.MSF_PRIMARY_ID_TYPE.uuid());
        if (!openmrsIdType.getRequired()) {
            openmrsIdType.setRequired(true);
            Context.getPatientService().savePatientIdentifierType(openmrsIdType);
        }
        if (msfIdType.getRequired()) {
            msfIdType.setRequired(false);
            Context.getPatientService().savePatientIdentifierType(msfIdType);
        }

        log.info("Configuring primary Identifier");
        MetadataMappingService metadataMappingService = Context.getService(MetadataMappingService.class);
        MetadataTermMapping primaryIdentifierTypeMapping = metadataMappingService.getMetadataTermMapping(
                        EmrApiConstants.EMR_METADATA_SOURCE_NAME, EmrApiConstants.PRIMARY_IDENTIFIER_TYPE);
        if (!openmrsIdType.getUuid().equals(primaryIdentifierTypeMapping.getMetadataUuid())) {
            primaryIdentifierTypeMapping.setMappedObject(openmrsIdType);
            metadataMappingService.saveMetadataTermMapping(primaryIdentifierTypeMapping);
        }

        log.info("Setting primary identifier source");
        IdentifierSource sourceForPrimaryType = Context.getService(IdentifierSourceService.class).getAutoGenerationOption(openmrsIdType)
                        .getSource();
        if (sourceForPrimaryType != null) {
            Context.getAdministrationService().updateGlobalProperty(MSFCoreConfig.GP_OPENMRS_IDENTIFIER_SOURCE_ID,
                            String.valueOf(sourceForPrimaryType.getId()));
        }

        // remove a map between visit types and encounter types to enable the
        // autocreation of visits
        Context.getAdministrationService().setGlobalProperty(
                        MSFCoreConfig.GP_EMRAPI_EMRAPIVISITSASSIGNMENTHANDLER_ENCOUNTERTYPETONEWVISITTYPEMAP, "");
    }

    /**
     * @see #shutdown()
     */
    public void shutdown() {
        log.info("Shutdown MSF Core");
    }

    @Override
    public void willStop() {
        triggerMSFApps(false);
        removeMSFMeta();
        Context.getAdministrationService().updateGlobalProperty(MSFCoreConfig.GP_MANADATORY, "false");
        removeMsfForms();
        super.willStop();
    }

    @Override
    public void willStart() {
        Context.getAdministrationService().updateGlobalProperty(MSFCoreConfig.GP_MANADATORY, "true");
        super.willStart();
    }

    /**
     * Installing MSF Forms
     */
    private void installMsfForms() {
        try {
            HtmlFormsInitializer htmlFormsInitializer = new HtmlFormsInitializer("msfcore");
            htmlFormsInitializer.started();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Installing MSF Forms
     */
    private void removeMsfForms() {
        try {
            HtmlFormsInitializer htmlFormsInitializer = new HtmlFormsInitializer("msfcore");
            htmlFormsInitializer.stopped();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
