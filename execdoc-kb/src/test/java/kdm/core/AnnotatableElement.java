package kdm.core;

import kdm.Element;
import kdm.kdm.Annotation;
import kdm.kdm.Attribute;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor()
public abstract class AnnotatableElement extends Element {

    private Set<Annotation> annotation;

    private Map<String, Attribute> attribute;

    public Set<Annotation> getAnnotation() {
        if (annotation == null) {
            annotation = new LinkedHashSet<>();
        }
        return annotation;
    }

    public Map<String, Attribute> getAttribute() {
        if (attribute == null) {
            attribute = new LinkedHashMap<>();
        }
        return attribute;
    }

}
