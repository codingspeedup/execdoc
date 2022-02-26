package kdm.kdm;

import kdm.core.AnnotatableElement;
import kdm.core.AnnotationElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor()
public class Attribute extends AnnotationElement {

    @Getter()
    @Setter()
    private AnnotatableElement owner;

    @Getter()
    @Setter()
    private String tag;

    @Getter()
    @Setter()
    private String[] value;

}
