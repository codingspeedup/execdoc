package io.github.codingspeedup.execdoc.space.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class SpaceNode {

    private String fqn;
    private String type;
    private String content;
    private Map<String, String> attributes;
    @Builder.Default
    private final List<SpaceNode> children = new ArrayList<>();

}
