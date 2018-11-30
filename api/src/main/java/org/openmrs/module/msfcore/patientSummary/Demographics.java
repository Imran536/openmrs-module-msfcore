package org.openmrs.module.msfcore.patientSummary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Demographics {
    private String name;
    private Age age;
    private String gender;
}
