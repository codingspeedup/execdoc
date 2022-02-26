package kdm.kdm;

import kdm.core.ExtensionElement;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public abstract class ExtendedValue extends ExtensionElement {

    @Getter()
    @Setter()
    private TagDefinition tag;
}
