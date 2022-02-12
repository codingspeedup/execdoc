package io.github.codingspeedup.execdoc.apps.codeminer;

import io.github.codingspeedup.execdoc.apps.codeminer.clipboard.CodeMinerClipboard;
import io.github.codingspeedup.execdoc.apps.codeminer.clipboard.FilesSheet;
import io.github.codingspeedup.execdoc.miners.resources.filesystem.FilesystemMiner;
import io.github.codingspeedup.execdoc.toolbox.resources.ResourceGroup;
import io.github.codingspeedup.execdoc.toolbox.resources.filesystem.FileOrFolderResource;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;

public class FileResourcesManager {

    private final CodeMinerClipboard clipboard;

    public FileResourcesManager(CodeMinerClipboard clipboard) {
        this.clipboard = clipboard;
    }

    @SneakyThrows
    public List<FileOrFolderResource> explore(FileOrFolderResource group) {
        Cell groupCell = clipboard.getResourcesSheet().selectByDescriptor(group.getDescriptor());
        FilesSheet sheet = clipboard.getFilesSheet();
        List<FileOrFolderResource> selection = sheet.getResourcesByGroup(groupCell);
        if (CollectionUtils.isEmpty(selection)) {
            ResourceGroup rGroup = ResourceGroup.builder().res(group).build();
            FilesystemMiner miner = new FilesystemMiner(rGroup, resource -> {
                if (resource instanceof FileOrFolderResource) {
                    System.out.println("Discovered: " + resource.getDescription());
                    sheet.insert(groupCell, resource);
                    selection.add((FileOrFolderResource) resource);
                }
            });
            miner.scan(null);
            sheet.normalize();
        }
        return selection;
    }

}
