package kdm.conceptual;

import lombok.NoArgsConstructor;
import kdm.core.KDMEntity;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import kdm.action.ActionElement;

@NoArgsConstructor()
public abstract class AbstractConceptualElement extends KDMEntity {

    @Getter()
    @Setter()
    private Set<KDMEntity> implementation;

    @Getter()
    @Setter()
    private Set<AbstractConceptualRelationship> conceptualRelation;

    @Getter()
    @Setter()
    private Set<ActionElement> abstraction;
}
