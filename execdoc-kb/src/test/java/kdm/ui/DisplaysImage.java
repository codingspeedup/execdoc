package kdm.ui;

import kdm.action.ActionElement;
import kdm.source.ImageFile;

public class DisplaysImage extends AbstractUIRelationship<ActionElement, ImageFile> {

    public DisplaysImage(UIModel kdmModel, ActionElement from, ImageFile to) {
        super(kdmModel, from, to);
    }

}
