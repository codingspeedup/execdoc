package kdm.code;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor()
public class MethodUnit extends ControlElement {

    @Getter()
    @Setter()
    private Object kind; // MethodKind

    @Getter()
    @Setter()
    private Object export; // ExportKind

    @Getter()
    @Setter()
    private Boolean isFinal;

    @Getter()
    @Setter()
    private Boolean isStatic;

    @Getter()
    @Setter()
    private Boolean isVirtual;

    @Getter()
    @Setter()
    private Boolean isAbstract;

}
