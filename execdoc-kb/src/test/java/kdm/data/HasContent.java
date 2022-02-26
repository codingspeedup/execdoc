package kdm.data;

import kdm.action.AbstractActionRelationship;
import kdm.action.ActionElement;
import kdm.kdm.KDMModel;

public class HasContent<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, AbstractDataElement> {

    public HasContent(M kdmModel, ActionElement from, AbstractDataElement to) {
        super(kdmModel, from, to);
    }

}
