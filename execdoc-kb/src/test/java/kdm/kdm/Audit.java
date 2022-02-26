package kdm.kdm;

import kdm.core.ExtendableElement;
import kdm.core.ModelElement;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class Audit extends ExtendableElement {

    @Getter()
    @Setter()
    private ModelElement owner;

    @Getter()
    @Setter()
    private String description;

    @Getter()
    @Setter()
    private String author;

    @Getter()
    @Setter()
    private String date;
}
