package kdm.platform;

import lombok.NoArgsConstructor;
import kdm.code.Module;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class DeployedComponent extends DeploymentElement {

    @Getter()
    @Setter()
    private Set<Module> groupedCode;
}
