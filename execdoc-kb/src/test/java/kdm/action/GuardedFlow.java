package kdm.action;

import kdm.kdm.KDMModel;

public class GuardedFlow<M extends KDMModel> extends ControlFlow<M> {

    public GuardedFlow(M kdmModel, ActionElement from, ActionElement to) {
        super(kdmModel, from, to);
    }

}
