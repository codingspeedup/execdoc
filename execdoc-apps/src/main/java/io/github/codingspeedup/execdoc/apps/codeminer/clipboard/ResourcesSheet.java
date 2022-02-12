package io.github.codingspeedup.execdoc.apps.codeminer.clipboard;

import io.github.codingspeedup.execdoc.toolbox.resources.Resource;
import io.github.codingspeedup.execdoc.toolbox.resources.ResourceGroup;
import io.github.codingspeedup.execdoc.toolbox.resources.ResourceGroupBuilder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;

public class ResourcesSheet extends ClipboardSheet<Resource> {

    public static final String NAME = "RESOURCES";

    public ResourcesSheet(CodeMinerClipboard clipboard, Sheet sheet) {
        super(clipboard, sheet);
    }

    public ResourceGroup getResourceGroup() {
        ResourceGroupBuilder builder = ResourceGroup.builder();
        builder.name(NAME);
        getResourcesByParent(null).forEach(builder::res);
        return builder.build();
    }

    @Override
    protected List<Cell> selectParentsByGroup(Cell groupCell) {
        List<Cell> parents = new ArrayList<>();
        parents.add(null);
        return parents;
    }

}
