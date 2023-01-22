package io.github.codingspeedup.execdoc.space;

import io.github.codingspeedup.execdoc.space.model.SpaceNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.StreamSupport;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Space {

    private static final String PATH_SEP = "/";

    public static List<String> toPath(String fqn) {
        return List.of(fqn.split(PATH_SEP));
    }

    public static String toFqn(Iterable<? extends CharSequence> elements) {
        return String.join(PATH_SEP, elements);
    }

    @SneakyThrows
    public static Space of(InputStream ins) {
        var space = new Space();
        var xml = XMLInputFactory.newInstance().createXMLStreamReader(ins);
        var names = new ArrayList<String>();
        var nodes = new ArrayList<SpaceNode>();
        while (xml.hasNext()) {
            if (xml.isStartElement()) {
                var name = StringUtils.EMPTY;
                var attributes = new LinkedHashMap<String, String>();
                for (int i = 0; i < xml.getAttributeCount(); ++i) {
                    var aName = xml.getAttributeLocalName(i);
                    var aValue = xml.getAttributeValue(i);
                    if ("name".equals(aName)) {
                        name = aValue;
                    } else {
                        attributes.put(aName, aValue);
                    }
                }
                String content = null;
                try{
                    content = xml.getElementText();
                } catch (XMLStreamException rEx) {
                    // ignore
                }
                names.add(name);
                var node = SpaceNode.builder()
                        .fqn(toFqn(names))
                        .type(xml.getLocalName())
                        .attributes(attributes)
                        .content(content)
                        .build();
                if (!nodes.isEmpty()) {
                    nodes.get(nodes.size() - 1).getChildren().add(node);
                }
                space.lookupTable.put(node.getFqn(), node);
                space.nameMap.computeIfAbsent(name, key -> new ArrayList<>()).add(node.getFqn());
                nodes.add(node);
            } else if (xml.isCharacters()) {
                var chars = xml.getText();
                System.out.println(chars);
            } else if (xml.isEndElement()) {
                names.remove(names.size() - 1);
                nodes.remove(names.size());
            }
            xml.next();
        }
        return space;
    }

    private final Map<String, SpaceNode> lookupTable = new HashMap<>();
    private final Map<String, List<String>> nameMap = new HashMap<>();

    public Optional<SpaceNode> getNode(String fqn) {
        return Optional.ofNullable(lookupTable.get(fqn));
    }

    public Optional<SpaceNode> findNode(String name, String fqnContext) {
        return findNode(name, toPath(fqnContext));
    }

    public Optional<SpaceNode> findNode(String name, Iterable<? extends CharSequence> context) {
        var parentPath = StreamSupport.stream(context.spliterator(), false)
                .map(CharSequence::toString)
                .flatMap(part -> toPath(part).stream())
                .toList();

        var candidates = nameMap.get(name);
        if (CollectionUtils.isEmpty(candidates)) {
            return Optional.empty();
        }

        var path = (List<String>) new ArrayList<>(parentPath);
        while (true) {
            var fqn = toFqn(path) + PATH_SEP + name;
            if (candidates.contains(fqn)) {
                return Optional.of(lookupTable.get(fqn));
            }
            if (path.isEmpty()) {
                break;
            }
            path = path.subList(0, path.size() - 1);
        }

        var parentFqn = toFqn(parentPath) + PATH_SEP;

        var fqn = candidates.stream()
                .filter(candidate -> candidate.startsWith(parentFqn))
                .findFirst();
        return fqn.map(lookupTable::get);
    }

}
