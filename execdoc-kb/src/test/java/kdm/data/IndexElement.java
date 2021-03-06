package kdm.data;

import lombok.NoArgsConstructor;
import kdm.code.ItemUnit;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class IndexElement extends DataResource {

    @Getter()
    @Setter()
    private Set<ItemUnit> implementation;
}
