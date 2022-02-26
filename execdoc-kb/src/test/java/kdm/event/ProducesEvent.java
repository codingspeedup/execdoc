package kdm.event;

import kdm.action.AbstractActionRelationship;
import kdm.action.ActionElement;
import kdm.kdm.KDMModel;

public class ProducesEvent<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, Event> {

    public ProducesEvent(M kdmModel, ActionElement from, Event to) {
        super(kdmModel, from, to);
    }

}
