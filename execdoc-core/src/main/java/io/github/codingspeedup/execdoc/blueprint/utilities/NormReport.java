package io.github.codingspeedup.execdoc.blueprint.utilities;

import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxUtil;
import org.apache.poi.ss.usermodel.Cell;

import java.util.ArrayList;
import java.util.List;

public class NormReport {

    private final List<NormEntry> issues = new ArrayList<>();

    public void addError(Cell cell, String message) {
        add(true, cell, message);
    }

    public void addInfo(Cell cell, String message) {
        add(false, cell, message);
    }

    public int getErrorCount() {
        return (int) issues.stream().filter(NormEntry::isError).count();
    }

    public int getIssueCount() {
        return issues.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        issues.forEach(issue -> {
            sb.append(issue.isError() ? "E" : " ").append(" ");
            Cell cell = issue.getCell();
            if (cell == null) {
                sb.append(String.format("%-4s %-15s %s%n",
                        XlsxUtil.toCellName(cell),
                        cell.getSheet().getSheetName(),
                        issue.getMessage()));
            } else {
                sb.append(issue.getMessage()).append("\n");
            }
        });
        sb.append(issues.size()).append(" error(s) / ").append(issues.size()).append(" issue(s)");
        return sb.toString();
    }

    private void add(boolean error, Cell cell, String message) {
        issues.add(new NormEntry(error, cell, message));
    }

}
