package kdm.data;

import lombok.NoArgsConstructor;
import kdm.kdm.KDMModel;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class DataModel extends KDMModel {

    @Getter()
    @Setter()
    private Set<AbstractDataElement> dataElement;
}
