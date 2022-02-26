package kdm.data;

import kdm.core.KDMEntity;
import kdm.core.KDMRelationship;

public abstract class AbstractDataRelationship<F extends KDMEntity, T extends KDMEntity> extends KDMRelationship<DataModel, F, T> {

    public AbstractDataRelationship(DataModel kdmModel, F from, T to) {
        super(kdmModel, from, to);
    }

}
