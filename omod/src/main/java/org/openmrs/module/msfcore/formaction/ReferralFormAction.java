package org.openmrs.module.msfcore.formaction;

import java.util.Arrays;
import java.util.List;

import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.msfcore.MSFCoreConfig;
import org.openmrs.module.msfcore.api.FormActionService;
import org.openmrs.module.msfcore.formaction.handler.FormAction;
import org.springframework.stereotype.Component;

@Component
public class ReferralFormAction implements FormAction {

    private static final List<String> VALID_FORM_UUIDS = Arrays.asList(MSFCoreConfig.FORM_NCD_BASELINE_REFER_PATIENT_UUID,
                    MSFCoreConfig.FORM_NCD_FOLLOWUP_REFER_PATIENT_UUID);

    @Override
    public void apply(String operation, FormEntrySession session) {
        if (VALID_FORM_UUIDS.contains(session.getForm().getUuid())) {
            Context.getService(FormActionService.class).saveReferralOrders(session.getEncounter());
        }

    }

}
