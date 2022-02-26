package kdm.action;

import kdm.code.Datatype;
import kdm.kdm.KDMModel;

public class Creates<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, Datatype> {

    public Creates(M kdmModel, ActionElement from, Datatype to) {
        super(kdmModel, from, to);
    }

}
