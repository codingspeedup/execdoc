package io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code;

import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.vocabulary.concepts.code.BpFieldType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;

public class BpType implements BpFieldType {

    public static final String[] NAMES = {
            "String",
            "Integer",
            "Long",
            "BigDecimal",
            "Float",
            "Double",
            "Boolean",
            "LocalDate",
            "ZonedDateTime",
            "Instant",
            "Duration",
            "UUID",
            "Blob",
            "AnyBlob",
            "ImageBlob",
            "TextBlob",
    };

    public static final String PREFIX = "jld_";

    @Getter
    @Setter
    private String kbId;

    public BpType(String name) {
        if (ArrayUtils.contains(NAMES, name)) {
            setName(name);
        }
    }

    @Override
    public String getName() {
        return kbId.substring(PREFIX.length());
    }

    @Override
    public void setName(String name) {
        kbId = PREFIX + name;
    }

}
