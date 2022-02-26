package kdm.data;

import kdm.action.AbstractActionRelationship;
import kdm.action.ActionElement;
import kdm.kdm.KDMModel;

public class ReadsColumnSet<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, ColumnSet> {

    public ReadsColumnSet(M kdmModel, ActionElement from, ColumnSet to) {
        super(kdmModel, from, to);
    }

}
