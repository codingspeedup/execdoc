package xp.kdm.code;

import io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.concepts.code.ExportKind;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class MemberUnit extends DataElement {

    @Getter()
    @Setter()
    private ExportKind export;

    @Getter()
    @Setter()
    private Boolean isFinal;

    @Getter()
    @Setter()
    private Boolean isStatic;

}
