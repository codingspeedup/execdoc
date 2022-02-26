package kdm.source;

import lombok.NoArgsConstructor;
import kdm.core.AnnotatableElement;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public abstract class Region extends AnnotatableElement {

    @Getter()
    @Setter()
    private InventoryItem file;
}
