package kdm.data;

import kdm.action.AbstractActionRelationship;
import kdm.action.ActionElement;
import kdm.kdm.KDMModel;

public class ProducesDataEvent<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, DataEvent> {

    public ProducesDataEvent(M kdmModel, ActionElement from, DataEvent to) {
        super(kdmModel, from, to);
    }

}
