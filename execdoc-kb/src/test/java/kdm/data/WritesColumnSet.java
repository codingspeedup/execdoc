package kdm.data;

import kdm.action.AbstractActionRelationship;
import kdm.action.ActionElement;
import kdm.kdm.KDMModel;

public class WritesColumnSet<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, ColumnSet> {

    public WritesColumnSet(M kdmModel, ActionElement from, ColumnSet to) {
        super(kdmModel, from, to);
    }

}
