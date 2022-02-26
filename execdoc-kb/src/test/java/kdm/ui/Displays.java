package kdm.ui;

import kdm.action.ActionElement;

public class Displays extends AbstractUIRelationship<ActionElement, UIResource> {

    public Displays(UIModel kdmModel, ActionElement from, UIResource to) {
        super(kdmModel, from, to);
    }

}
