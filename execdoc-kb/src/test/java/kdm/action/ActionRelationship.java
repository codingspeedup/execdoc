package kdm.action;

import kdm.core.KDMEntity;
import kdm.kdm.KDMModel;

public class ActionRelationship<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, KDMEntity> {

    public ActionRelationship(M kdmModel, ActionElement from, KDMEntity to) {
        super(kdmModel, from, to);
    }

}
