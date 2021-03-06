package kdm.data;

import lombok.NoArgsConstructor;
import kdm.code.CodeItem;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class RelationalSchema extends DataContainer {

    @Getter()
    @Setter()
    private Set<CodeItem> codeElement;
}
