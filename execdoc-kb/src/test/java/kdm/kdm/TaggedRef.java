package kdm.kdm;

import kdm.core.ExtendableElement;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class TaggedRef extends ExtendedValue {

    @Getter()
    @Setter()
    private ExtendableElement reference;
}
