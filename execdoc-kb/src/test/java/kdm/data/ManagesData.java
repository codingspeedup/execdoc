package kdm.data;

import kdm.action.AbstractActionRelationship;
import kdm.action.ActionElement;
import kdm.kdm.KDMModel;

public class ManagesData<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, AbstractDataElement> {

    public ManagesData(M kdmModel, ActionElement from, AbstractDataElement to) {
        super(kdmModel, from, to);
    }

}
