package kdm.ui;

import kdm.core.KDMEntity;
import kdm.core.KDMRelationship;

public abstract class AbstractUIRelationship<F extends KDMEntity, T extends KDMEntity> extends KDMRelationship<UIModel, F, T> {

    public AbstractUIRelationship(UIModel kdmModel, F from, T to) {
        super(kdmModel, from, to);
    }

}
