package kdm.event;

import kdm.action.ActionElement;
import lombok.NoArgsConstructor;
import kdm.code.AbstractCodeElement;
import kdm.core.KDMEntity;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor()
public abstract class AbstractEventElement extends KDMEntity {

    @Getter()
    @Setter()
    private Set<AbstractEventRelationship> eventRelation;

    @Getter()
    @Setter()
    private List<ActionElement> abstraction;

    @Getter()
    @Setter()
    private Set<AbstractCodeElement> implementation;
}
