package kdm.build;

import lombok.NoArgsConstructor;
import kdm.kdm.KDMModel;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class BuildModel extends KDMModel {

    @Getter()
    @Setter()
    private Set<AbstractBuildElement> buildElement;
}
