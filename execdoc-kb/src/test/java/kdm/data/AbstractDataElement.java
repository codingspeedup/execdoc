package kdm.data;

import kdm.action.ActionElement;
import lombok.NoArgsConstructor;
import kdm.core.KDMEntity;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor()
public abstract class AbstractDataElement extends KDMEntity {

    @Getter()
    @Setter()
    private Set<AbstractDataRelationship> dataRelation;

    @Getter()
    @Setter()
    private List<ActionElement> abstraction;
}
