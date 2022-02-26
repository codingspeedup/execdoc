package kdm.core;

import lombok.NoArgsConstructor;
import kdm.kdm.ExtendedValue;
import kdm.kdm.Stereotype;

import java.util.Collection;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public abstract class ExtendableElement extends AnnotatableElement {

    @Getter()
    @Setter()
    private Collection<Stereotype> stereotype;

    @Getter()
    @Setter()
    private Collection<ExtendedValue> taggedValue;
}
