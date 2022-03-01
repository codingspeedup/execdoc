package io.github.codingspeedup.execdoc.blueprint.utilities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class NormEntry {

    private boolean error;
    private Cell cell;
    private String message;

}
