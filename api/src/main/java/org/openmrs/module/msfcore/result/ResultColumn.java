package org.openmrs.module.msfcore.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultColumn {
    @Builder.Default
    private boolean editable = false;
    private Object value;
}
