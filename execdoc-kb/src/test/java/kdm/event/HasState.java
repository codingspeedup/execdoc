package kdm.event;

import kdm.action.AbstractActionRelationship;
import kdm.action.ActionElement;
import kdm.kdm.KDMModel;

public class HasState<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, AbstractEventElement> {

    public HasState(M kdmModel, ActionElement from, AbstractEventElement to) {
        super(kdmModel, from, to);
    }

}
