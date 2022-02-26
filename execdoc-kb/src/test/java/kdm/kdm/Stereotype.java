package kdm.kdm;

import kdm.core.ExtensionElement;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@NoArgsConstructor()
public class Stereotype extends ExtensionElement {

    @Getter()
    @Setter()
    private ExtensionFamily owner;

    @Getter()
    @Setter()
    private Collection<TagDefinition> tag;

    @Getter()
    @Setter()
    private String name;

    @Getter()
    @Setter()
    private String type;
}
