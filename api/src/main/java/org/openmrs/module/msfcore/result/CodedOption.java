package org.openmrs.module.msfcore.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * Default wise this class replasents the openmrs understanding of a
 * coded/concept value but we also use it for any options of an openmrs object
 */
public class CodedOption {
    private String name;
    private String uuid;
}
