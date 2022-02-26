package kdm.platform;

import kdm.action.AbstractActionRelationship;
import kdm.action.ActionElement;
import kdm.kdm.KDMModel;

public class ProducesPlatformEvent<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, PlatformEvent> {

    public ProducesPlatformEvent(M kdmModel, ActionElement from, PlatformEvent to) {
        super(kdmModel, from, to);
    }

}
