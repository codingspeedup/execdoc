package kdm.build;

import kdm.core.KDMEntity;

public class BuildRelationship extends AbstractBuildRelationship<AbstractBuildElement, KDMEntity> {

    public BuildRelationship(BuildModel kdmModel, AbstractBuildElement from, KDMEntity to) {
        super(kdmModel, from, to);
    }

}
