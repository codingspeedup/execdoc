package kdm.data;

import kdm.action.ActionElement;
import lombok.NoArgsConstructor;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class DataAction extends AbstractDataElement {

    @Getter()
    @Setter()
    private Set<ActionElement> implementation;

    @Getter()
    @Setter()
    private Set<DataEvent> dataElement;

    @Getter()
    @Setter()
    private String kind;
}
