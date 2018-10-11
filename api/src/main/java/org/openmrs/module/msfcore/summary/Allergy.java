package org.openmrs.module.msfcore.summary;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Allergy {
    private String name;
    @Builder.Default
    private List<String> reactions = new ArrayList<String>();
    private String severity;
}
