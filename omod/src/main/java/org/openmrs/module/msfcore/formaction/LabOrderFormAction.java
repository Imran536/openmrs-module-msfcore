package org.openmrs.module.msfcore.formaction;

import java.util.Arrays;

import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.msfcore.MSFCoreConfig;
import org.openmrs.module.msfcore.api.FormActionService;
import org.openmrs.module.msfcore.formaction.handler.FormAction;
import org.springframework.stereotype.Component;

@Component
public class LabOrderFormAction implements FormAction {

    /**
     *  Generate test orders when request investigation is submited from html form
     */
    @Override
    public void apply(String operation, FormEntrySession session) {

        boolean isRequestInvestigationForm = Arrays.asList(MSFCoreConfig.FORM_NCD_BASELINE_REQUEST_INVESTIGATION_UUID,
                        MSFCoreConfig.FORM_NCD_FOLLOWUP_REQUEST_INVESTIGATION_UUID).contains(session.getForm().getUuid());

        if (isRequestInvestigationForm) {
            Context.getService(FormActionService.class).saveTestOrders(session.getEncounter());
        }
    }
}
