package kdm.build;

import lombok.NoArgsConstructor;
import kdm.core.KDMEntity;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public abstract class AbstractBuildElement extends KDMEntity {

    @Getter()
    @Setter()
    private Set<AbstractBuildRelationship> buildRelation;
}
