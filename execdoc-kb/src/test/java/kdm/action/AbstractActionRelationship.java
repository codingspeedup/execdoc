package kdm.action;

import kdm.core.KDMEntity;
import kdm.core.KDMRelationship;
import kdm.kdm.KDMModel;

public abstract class AbstractActionRelationship<M extends KDMModel, F extends KDMEntity, T extends KDMEntity> extends KDMRelationship<M, F, T> {

    public AbstractActionRelationship(M kdmModel, F from, T to) {
        super(kdmModel, from, to);
    }

}
