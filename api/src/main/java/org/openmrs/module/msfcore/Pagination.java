package org.openmrs.module.msfcore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pagination {
    @Builder.Default
    private Integer fromResultNumber = 0;
    @Builder.Default
    private Integer toResultNumber = 10;
    @Builder.Default
    private Integer totalResultNumber = 0;
}
