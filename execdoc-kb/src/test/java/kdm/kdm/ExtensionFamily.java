package kdm.kdm;

import kdm.core.ExtensionElement;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@NoArgsConstructor()
public class ExtensionFamily extends ExtensionElement {

    @Getter()
    @Setter()
    private FrameworkElement owner;

    @Getter()
    @Setter()
    private Collection<Stereotype> stereotype;

    @Getter()
    @Setter()
    private String name;
}
