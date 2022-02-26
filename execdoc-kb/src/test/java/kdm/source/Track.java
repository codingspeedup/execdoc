package kdm.source;

import lombok.NoArgsConstructor;
import kdm.core.KDMEntity;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class Track extends AbstractInventoryElement {

    @Getter()
    @Setter()
    private KDMEntity owner;

    @Getter()
    @Setter()
    private String description;
}
