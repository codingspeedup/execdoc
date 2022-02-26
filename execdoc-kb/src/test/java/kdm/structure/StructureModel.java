package kdm.structure;

import lombok.NoArgsConstructor;
import kdm.kdm.KDMModel;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class StructureModel extends KDMModel {

    @Getter()
    @Setter()
    private Set<AbstractStructureElement> structureElement;
}
