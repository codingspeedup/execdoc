package kdm.ui;

import kdm.action.AbstractActionRelationship;
import kdm.action.ActionElement;
import kdm.kdm.KDMModel;

public class WritesUI<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, UIResource> {

    public WritesUI(M kdmModel, ActionElement from, UIResource to) {
        super(kdmModel, from, to);
    }

}
