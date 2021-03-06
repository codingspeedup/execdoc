package kdm.conceptual;

import kdm.core.KDMEntity;

public class ConceptualRelationship extends AbstractConceptualRelationship<AbstractConceptualElement, KDMEntity> {

    public ConceptualRelationship(ConceptualModel kdmModel, AbstractConceptualElement from, KDMEntity to) {
        super(kdmModel, from, to);
    }

}
