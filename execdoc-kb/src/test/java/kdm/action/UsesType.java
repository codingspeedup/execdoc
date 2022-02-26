package kdm.action;

import kdm.code.Datatype;
import kdm.kdm.KDMModel;

public class UsesType<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, Datatype> {

    public UsesType(M kdmModel, ActionElement from, Datatype to) {
        super(kdmModel, from, to);
    }

}
