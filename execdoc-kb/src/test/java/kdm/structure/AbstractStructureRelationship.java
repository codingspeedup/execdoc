package kdm.structure;

import kdm.core.KDMEntity;
import kdm.core.KDMRelationship;

public abstract class AbstractStructureRelationship<F extends KDMEntity, T extends KDMEntity> extends KDMRelationship<StructureModel, F, T> {

    public AbstractStructureRelationship(StructureModel kdmModel, F from, T to) {
        super(kdmModel, from, to);
    }

}
