package kdm.ui;

import lombok.NoArgsConstructor;
import kdm.action.ActionElement;
import kdm.code.AbstractCodeElement;
import kdm.core.KDMEntity;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor()
public abstract class AbstractUIElement extends KDMEntity {

    @Getter()
    @Setter()
    private Set<AbstractUIRelationship> uiRelation;

    @Getter()
    @Setter()
    private Set<AbstractCodeElement> implementation;

    @Getter()
    @Setter()
    private List<ActionElement> abstraction;
}
