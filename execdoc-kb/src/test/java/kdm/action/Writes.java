package kdm.action;

import kdm.code.DataElement;
import kdm.kdm.KDMModel;

public class Writes<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, DataElement> {

    public Writes(M kdmModel, ActionElement from, DataElement to) {
        super(kdmModel, from, to);
    }

}
