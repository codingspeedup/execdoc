package kdm.core;

import lombok.NoArgsConstructor;
import kdm.kdm.Audit;

import java.util.Collection;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public abstract class ModelElement extends ExtendableElement {

    @Getter()
    @Setter()
    private Collection<Audit> audit;
}
