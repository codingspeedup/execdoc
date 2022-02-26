package kdm.kdm;

import kdm.core.KDMEntity;
import kdm.core.KDMRelationship;
import lombok.Getter;

import java.util.Set;

public abstract class KDMModel extends FrameworkElement {


    @Getter()
    private Set<KDMEntity> ownedElement;

    public KDMModel() {
    }

    public KDMEntity getRelationshipFrom(KDMRelationship kdmRelationship) {
        return null;
    }

    public KDMEntity getRelationshipTo(KDMRelationship kdmRelationship) {
        return null;
    }

    public boolean addEntity(KDMEntity kdmEntity) {
        kdmEntity.setModel(this);
        return false;
    }

    public boolean addRelationship(KDMEntity fromEntity, KDMRelationship kdmRelationship, KDMEntity toEntity) {
        if (kdmRelationship.getModel() != null) {
            throw new UnsupportedOperationException();
        }
        addEntity(fromEntity);
        addEntity(toEntity);
        return false;
    }

}
