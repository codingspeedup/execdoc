package kdm.platform;

import kdm.core.KDMEntity;
import kdm.core.KDMRelationship;

public abstract class AbstractPlatformRelationship<F extends KDMEntity, T extends KDMEntity> extends KDMRelationship<PlatformModel, F, T> {

    public AbstractPlatformRelationship(PlatformModel kdmModel, F from, T to) {
        super(kdmModel, from, to);
    }

}
