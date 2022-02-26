package kdm.platform;

import kdm.action.AbstractActionRelationship;
import kdm.action.ActionElement;
import kdm.kdm.KDMModel;

public class WritesResource<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, PlatformResource> {

    public WritesResource(M kdmModel, ActionElement from, PlatformResource to) {
        super(kdmModel, from, to);
    }

}
