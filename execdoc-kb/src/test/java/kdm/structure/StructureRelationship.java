package kdm.structure;

import kdm.core.KDMEntity;

public class StructureRelationship extends AbstractStructureRelationship<AbstractStructureElement , KDMEntity> {

    public StructureRelationship(StructureModel kdmModel, AbstractStructureElement from, KDMEntity to) {
        super(kdmModel, from, to);
    }

}
