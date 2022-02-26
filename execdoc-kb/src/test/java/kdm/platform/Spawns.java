package kdm.platform;

import kdm.action.ActionElement;

public class Spawns extends AbstractPlatformRelationship<ActionElement, RuntimeResource> {

    public Spawns(PlatformModel kdmModel, ActionElement from, RuntimeResource to) {
        super(kdmModel, from, to);
    }

}
