package kdm.action;

import kdm.code.AbstractCodeElement;
import kdm.kdm.KDMModel;

public class EntryFlow<M extends KDMModel> extends AbstractActionRelationship<M, AbstractCodeElement, ActionElement> {

    public EntryFlow(M kdmModel, AbstractCodeElement from, ActionElement to) {
        super(kdmModel, from, to);
    }

}
