package kdm.ui;

import kdm.action.AbstractActionRelationship;
import kdm.action.ActionElement;
import kdm.kdm.KDMModel;

public class ProducesUIEvent<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, UIEvent> {

    public ProducesUIEvent(M kdmModel, ActionElement from, UIEvent to) {
        super(kdmModel, from, to);
    }

}
