package kdm.code;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class MemberUnit extends DataElement {

    @Getter()
    @Setter()
    private Object export; // ExportKind

    @Getter()
    @Setter()
    private Boolean isFinal;

    @Getter()
    @Setter()
    private Boolean isStatic;

}
