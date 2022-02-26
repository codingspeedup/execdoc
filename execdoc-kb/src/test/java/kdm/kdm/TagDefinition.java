package kdm.kdm;

import kdm.core.ExtensionElement;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class TagDefinition extends ExtensionElement {

    @Getter()
    @Setter()
    private Stereotype owner;

    @Getter()
    @Setter()
    private String tag;

    @Getter()
    @Setter()
    private String type;
}
