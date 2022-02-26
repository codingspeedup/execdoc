package kdm.action;

import kdm.code.ComputationalObject;
import kdm.kdm.KDMModel;

public class Addresses<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, ComputationalObject> {

    public Addresses(M kdmModel, ActionElement from, ComputationalObject to) {
        super(kdmModel, from, to);
    }

}
