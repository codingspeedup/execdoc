package kdm.data;

import kdm.code.Datatype;

public class DatatypeOf extends AbstractDataRelationship<ComplexContentType, Datatype> {

    public DatatypeOf(DataModel kdmModel, ComplexContentType from, Datatype to) {
        super(kdmModel, from, to);
    }

}
