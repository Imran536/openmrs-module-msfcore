package org.openmrs.module.msfcore.result;

import java.util.Date;
import java.util.List;

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
    @Builder.Default
    private Type type = Type.STRING;
    private List<CodedOption> codedOptions;
    private Date stopDate;

    public enum Type {
        STRING, DATE, NUMBER, BOOLEAN, CODED, STOP
    }

}
