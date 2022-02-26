package kdm.platform;

import kdm.action.AbstractActionRelationship;
import kdm.action.ActionElement;
import kdm.code.CodeItem;
import kdm.kdm.KDMModel;

public class DefinedBy<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, CodeItem> {

    public DefinedBy(M kdmModel, ActionElement from, CodeItem to) {
        super(kdmModel, from, to);
    }

}
