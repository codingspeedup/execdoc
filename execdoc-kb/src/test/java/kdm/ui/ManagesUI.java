package kdm.ui;

import kdm.action.AbstractActionRelationship;
import kdm.action.ActionElement;
import kdm.kdm.KDMModel;

public class ManagesUI<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, UIResource> {

    public ManagesUI(M kdmModel, ActionElement from, UIResource to) {
        super(kdmModel, from, to);
    }

}
