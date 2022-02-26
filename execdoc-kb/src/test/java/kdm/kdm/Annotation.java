package kdm.kdm;

import kdm.core.AnnotatableElement;
import kdm.core.AnnotationElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor()
public class Annotation extends AnnotationElement {

    @Getter()
    @Setter()
    private AnnotatableElement owner;

    @Getter()
    @Setter()
    private String text;

    public Annotation(String text) {
        this.text = text;
    }

}
