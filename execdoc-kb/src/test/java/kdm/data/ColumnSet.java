package kdm.data;

import lombok.NoArgsConstructor;
import kdm.code.ItemUnit;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class ColumnSet extends DataContainer {

    @Getter()
    @Setter()
    private List<ItemUnit> itemUnit;
}
