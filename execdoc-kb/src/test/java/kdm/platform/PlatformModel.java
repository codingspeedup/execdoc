package kdm.platform;

import lombok.NoArgsConstructor;
import kdm.kdm.KDMModel;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class PlatformModel extends KDMModel {

    @Getter()
    @Setter()
    private Set<AbstractPlatformElement> platformElement;
}
