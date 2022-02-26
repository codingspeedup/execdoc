package kdm.event;

import kdm.action.AbstractActionRelationship;
import kdm.action.ActionElement;
import kdm.kdm.KDMModel;

public class ReadsState<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, State> {

    public ReadsState(M kdmModel, ActionElement from, State to) {
        super(kdmModel, from, to);
    }

}
