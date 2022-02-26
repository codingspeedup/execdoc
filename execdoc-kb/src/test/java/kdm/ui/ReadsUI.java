package kdm.ui;

import kdm.action.ActionElement;
import kdm.kdm.KDMModel;
import kdm.action.AbstractActionRelationship;

public class ReadsUI<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, UIResource> {

    public ReadsUI(M kdmModel, ActionElement from, UIResource to) {
        super(kdmModel, from, to);
    }

}
