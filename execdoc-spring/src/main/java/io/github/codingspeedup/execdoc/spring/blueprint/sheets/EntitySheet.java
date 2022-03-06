package io.github.codingspeedup.execdoc.spring.blueprint.sheets;

import com.google.common.base.CaseFormat;
import io.github.codingspeedup.execdoc.blueprint.utilities.NormReport;
import io.github.codingspeedup.execdoc.spring.blueprint.SpringSheet;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.BpEnum;
import io.github.codingspeedup.execdoc.kb.Kb;
import io.github.codingspeedup.execdoc.blueprint.metamodel.individuals.BpSheet;
import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.blueprint.master.SemanticTriple;
import io.github.codingspeedup.execdoc.blueprint.master.cells.CellComment;
import io.github.codingspeedup.execdoc.blueprint.master.cells.CellMarkers;
import io.github.codingspeedup.execdoc.kb.KbNames;
import io.github.codingspeedup.execdoc.bootstrap.sql.metamodel.SqlCatalog;
import io.github.codingspeedup.execdoc.bootstrap.sql.metamodel.SqlSchema;
import io.github.codingspeedup.execdoc.bootstrap.sql.metamodel.SqlTable;
import io.github.codingspeedup.execdoc.bootstrap.sql.metamodel.SqlTableColumn;
import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxUtil;
import io.github.codingspeedup.execdoc.toolbox.utilities.NamingUtility;
import io.github.codingspeedup.execdoc.toolbox.utilities.SqlTypesMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.vocabulary.concepts.code.BpFieldType;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.BpType;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.data.BpEntity;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.vocabulary.relations.data.BpEntityRelation;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.data.BpField;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntitySheet extends SpringSheet {

    public static final String NAME_MARKER = "Entities" + BlueprintMaster.INSTANTIABLE_SHEET_MARKER;
    public static final String TOC_CHAPTER = "Integration";

    public static final String ANCHOR_NAME = CellMarkers.ANCHOR_MARKER + "TABLE_NAME / column_name";
    public static final String ANCHOR_TYPE = CellMarkers.ANCHOR_MARKER + "ColumnType";
    public static final String ANCHOR_KEY = CellMarkers.ANCHOR_MARKER + "@Id";
    public static final String ANCHOR_REQUIRED = CellMarkers.ANCHOR_MARKER + "Required";
    public static final String ANCHOR_MIN = CellMarkers.ANCHOR_MARKER + "min";
    public static final String ANCHOR_MAX = CellMarkers.ANCHOR_MARKER + "MAX";
    public static final String ANCHOR_PATTERN = CellMarkers.ANCHOR_MARKER + "Pattern";
    public static final String ANCHOR_UNIQUE = CellMarkers.ANCHOR_MARKER + "Unique";

    public static final String ANCHOR_RELATIONSHIP = CellMarkers.ANCHOR_MARKER + "Relationship";

    public static final String ATTRIBUTE_CLASS_NAME = "class-name";
    public static final String ATTRIBUTE_MEMBER_NAME = "member-name";

    public static final Set<String> ENTITY_RELATIONSHIPS = Stream.of(BpEntityRelation.ENTITY_RELATIONSHIPS).map(KbNames::getFunctor).collect(Collectors.toSet());

    public static final String OPT_ANGULAR_SUFFIX = "angularSuffix";
    public static final String OPT_CLIENT_ROOT_FOLDER = "clientRootFolder";
    public static final String OPT_FILTER = "filter";
    public static final String OPT_NO_FLUENT_METHOD = "noFluentMethod";
    public static final String OPT_PAGINATE = "paginate";
    public static final String OPT_READ_ONLY = "readOnly";
    public static final String OPT_SEARCH = "search";
    public static final String OPT_SERVICE = "service";
    public static final String OPT_SKIP_CLIENT = "skipClient";
    public static final String OPT_SKIP_SERVER = "skipServer";

    public static final List<String> ENTITY_OPTIONS = Arrays.asList(
            OPT_ANGULAR_SUFFIX,
            OPT_CLIENT_ROOT_FOLDER,
            OPT_FILTER,
            OPT_NO_FLUENT_METHOD,
            OPT_PAGINATE,
            OPT_READ_ONLY,
            OPT_SEARCH,
            OPT_SERVICE,
            OPT_SKIP_CLIENT,
            OPT_SKIP_SERVER
    );

    public EntitySheet(BlueprintMaster bp, Sheet sheet) {
        super(bp, sheet);
    }

    @Override
    public int initialize() {
        int rowIdx = 0;
        int colIdx = 0;
        setCellValue(rowIdx, ++colIdx, ANCHOR_NAME);
        setCellValue(rowIdx, ++colIdx, ANCHOR_TYPE);
        setCellValue(rowIdx, ++colIdx, ANCHOR_KEY);
        setCellValue(rowIdx, ++colIdx, ANCHOR_REQUIRED);
        setCellValue(rowIdx, ++colIdx, ANCHOR_MIN);
        setCellValue(rowIdx, ++colIdx, ANCHOR_MAX);
        setCellValue(rowIdx, ++colIdx, ANCHOR_PATTERN);
        setCellValue(rowIdx, ++colIdx, ANCHOR_UNIQUE);
        ++colIdx;
        ++colIdx;
        setCellValue(rowIdx, ++colIdx, ANCHOR_RELATIONSHIP);
        autoSizeColumns("0-" + colIdx);
        getSheet().createFreezePane(0, rowIdx + 1);

        rowIdx = getAnchors().getLastAnchorRow() + 2;
        colIdx = getAnchors().getColumn(ANCHOR_NAME);
        getSheet().setActiveCell(new CellAddress(rowIdx, colIdx));

        colIdx = getAnchors().getColumn(ANCHOR_TYPE);
        createValidation(rowIdx, rowIdx + 100, colIdx, colIdx, UtilitySheet.ENTITY_PREDEFINED_TYPES);

        colIdx = getAnchors().getColumn(ANCHOR_KEY);
        createValidation(rowIdx, rowIdx + 100, colIdx, colIdx, UtilitySheet.ENTITY_BINARY_VALUE);

        colIdx = getAnchors().getColumn(ANCHOR_REQUIRED);
        createValidation(rowIdx, rowIdx + 100, colIdx, colIdx, UtilitySheet.ENTITY_BINARY_VALUE);

        colIdx = getAnchors().getColumn(ANCHOR_UNIQUE);
        createValidation(rowIdx, rowIdx + 100, colIdx, colIdx, UtilitySheet.ENTITY_BINARY_VALUE);

        colIdx = getAnchors().getColumn(ANCHOR_RELATIONSHIP);
        createValidation(rowIdx - 1, rowIdx + 100, colIdx, colIdx, UtilitySheet.ENTITY_RELATIONSHIPS);

        return rowIdx;
    }

    @Override
    public void normalize(NormReport normReport) {
        Cell tableCell = null;
        for (int rowIdx = getAnchors().getLastAnchorRow() + 1; rowIdx <= getSheet().getLastRowNum(); ++rowIdx) {
            Row row = getSheet().getRow(rowIdx);

            Cell nameCell = getCell(rowIdx, getAnchors().getColumn(ANCHOR_NAME));
            String nameString = XlsxUtil.getCellValue(nameCell, String.class);
            if (StringUtils.isBlank(nameString)) {
                tableCell = null;
                continue;
            }

            CellComment cc = getCellComment(nameCell).orElse(new CellComment());
            if (tableCell == null) {
                tableCell = nameCell;
                String normalizedNameString = NamingUtility.toUpperUnderscore(nameString);
                if (!nameString.equals(normalizedNameString)) {
                    nameString = normalizedNameString;
                    XlsxUtil.setCellValue(nameCell, nameString);
                }
                cc.getAnnotations().putIfAbsent(OPT_SKIP_CLIENT, "false");
                cc.getAnnotations().putIfAbsent(OPT_SKIP_SERVER, "false");
                cc.getAnnotations().putIfAbsent(OPT_NO_FLUENT_METHOD, "false");
                cc.getAnnotations().putIfAbsent(OPT_FILTER, "false");
                cc.getAnnotations().putIfAbsent(OPT_READ_ONLY, "false");
                cc.getAnnotations().putIfAbsent(OPT_SERVICE, "no");
                cc.getAnnotations().putIfAbsent(OPT_PAGINATE, "no");
                cc.getAnnotations().putIfAbsent(OPT_SEARCH, "no");
                cc.getAnnotations().putIfAbsent(OPT_ANGULAR_SUFFIX, "");
                cc.getAnnotations().putIfAbsent(OPT_CLIENT_ROOT_FOLDER, "");
                cc.getAttributes().putIfAbsent(ATTRIBUTE_CLASS_NAME, CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, nameString));
            } else {
                String normalizedNameString = NamingUtility.toLowerUnderscore(nameString);
                if (!nameString.equals(normalizedNameString)) {
                    nameString = normalizedNameString;
                    XlsxUtil.setCellValue(nameCell, nameString);
                }
                cc.getAttributes().putIfAbsent(ATTRIBUTE_MEMBER_NAME, CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, nameString));
            }
            setCellComment(nameCell, cc);
        }
        autoSizeColumns(
                getAnchors().getColumn(ANCHOR_NAME),
                getAnchors().getColumn(ANCHOR_TYPE),
                getAnchors().getColumn(ANCHOR_KEY),
                getAnchors().getColumn(ANCHOR_REQUIRED),
                getAnchors().getColumn(ANCHOR_UNIQUE),
                getAnchors().getColumn(ANCHOR_MIN),
                getAnchors().getColumn(ANCHOR_MAX)
        );
    }

    @Override
    public void expand(Kb kb) {
        BpSheet owner = new BpSheet(getSheet());

        BpEntity bpEntity = null;
        for (int rowIdx = getAnchors().getLastAnchorRow() + 1; rowIdx <= getSheet().getLastRowNum(); ++rowIdx) {
            Cell nameCell = getCell(rowIdx, getAnchors().getColumn(ANCHOR_NAME));
            if (XlsxUtil.isBlank(nameCell)) {
                kb.learn(bpEntity);
                bpEntity = null;
                continue;
            }

            Row row = getSheet().getRow(rowIdx);

            if (bpEntity == null) {
                bpEntity = new BpEntity(nameCell);
                bpEntity.setOwner(owner);
            } else {
                BpField bpField = new BpField(nameCell);
                bpField.setType(toJdlFieldType(row.getCell(getAnchors().getColumn(ANCHOR_TYPE))));
                bpField.setPrimaryKey(XlsxUtil.getCellValue(row.getCell(getAnchors().getColumn(ANCHOR_KEY)), Boolean.class));
                bpField.setRequired(XlsxUtil.getCellValue(row.getCell(getAnchors().getColumn(ANCHOR_REQUIRED)), Boolean.class));
                bpField.setMin(XlsxUtil.getCellValue(row.getCell(getAnchors().getColumn(ANCHOR_MIN)), BigDecimal.class));
                bpField.setMax(XlsxUtil.getCellValue(row.getCell(getAnchors().getColumn(ANCHOR_MAX)), BigDecimal.class));
                bpField.setExt(XlsxUtil.getCellValue(row.getCell(getAnchors().getColumn(ANCHOR_PATTERN)), String.class));
                bpField.setUnique(XlsxUtil.getCellValue(row.getCell(getAnchors().getColumn(ANCHOR_UNIQUE)), Boolean.class));
                bpEntity.getItemUnit().add(bpField);
            }

        }
        kb.learn(bpEntity);
        for (int rowIdx = getAnchors().getLastAnchorRow() + 1; rowIdx <= getSheet().getLastRowNum(); ++rowIdx) {
            Cell nameCell = getCell(rowIdx, getAnchors().getColumn(ANCHOR_NAME));
            if (XlsxUtil.isNotBlank(nameCell)) {
                Cell relationshipCell = getCell(rowIdx, getAnchors().getColumn(ANCHOR_RELATIONSHIP));
                if (ENTITY_RELATIONSHIPS.contains(SemanticTriple.getPredicateName(relationshipCell))) {
                    SemanticTriple triple = new SemanticTriple(relationshipCell);
                    if (CollectionUtils.isEmpty(triple.getSubject())) {
                        triple.setSubject(nameCell);
                    }
                    kb.learn(BpEntityRelation.from(triple));
                }
            }
        }
    }

    private BpFieldType toJdlFieldType(Cell typeCell) {
        String typeName = XlsxUtil.getCellValue(typeCell, String.class);
        if (StringUtils.isNotBlank(typeName)) {
            if (ArrayUtils.contains(BpType.NAMES, typeName)) {
                return new BpType(typeName);
            }
            Cell refTypeCell = XlsxUtil.backtraceCellBySimpleFormulaReference(typeCell);
            SpringSheet sheet = (SpringSheet) getMaster().getSheet(refTypeCell);
            if (sheet instanceof EnumsSheet && sheet.isOwnerUnit(refTypeCell.getRowIndex(), refTypeCell.getColumnIndex())) {
                return new BpEnum(refTypeCell);
            }
        }
        return null;
    }

    public SqlSchema readSchema(SqlCatalog catalog) {
        SqlSchema schema = new SqlSchema(getInstanceName(), catalog);
        return schema;
    }

    public String mergeSchema(SqlSchema schema, String... selectedTables) {
        StringBuilder conflictReport = new StringBuilder();
        List<SqlTable> tablesToMerge = new ArrayList<>();
        if (ArrayUtils.isEmpty(selectedTables)) {
            schema.getTableNames().stream().map(schema::getTable).filter(t -> !t.getColumnNames().isEmpty()).forEach(tablesToMerge::add);
        } else {
            Set<String> argTables = Arrays.stream(selectedTables).map(StringUtils::trim).filter(StringUtils::isNotBlank).map(String::toUpperCase).collect(Collectors.toSet());
            schema.getTableNames().stream().map(String::toUpperCase).filter(argTables::contains).map(schema::getTable).filter(t -> !t.getColumnNames().isEmpty()).forEach(tablesToMerge::add);
        }

        List<SqlTable> tablesToAdd = new ArrayList<>(tablesToMerge);
        SqlSchema oldSchema = readSchema(null);
        for (String tableName : oldSchema.getTableNames()) {
            for (SqlTable table : tablesToMerge) {
                if (table.getName().equalsIgnoreCase(tableName)) {
                    tablesToAdd.remove(table);
                    compare(conflictReport, oldSchema.getTable(tableName), table);
                }
            }
        }
        if (tablesToAdd.isEmpty()) {
            return conflictReport.toString();
        }

        int rowIdx = Math.max(getAnchors().getLastAnchorRow() + 1, getSheet().getLastRowNum());
        while (rowIdx > getAnchors().getLastAnchorRow() + 1) {
            Cell nameCell = getCell(rowIdx, getAnchors().getColumn(ANCHOR_NAME));
            String nameString = XlsxUtil.getCellValue(nameCell, String.class);
            if (StringUtils.isBlank(nameString)) {
                --rowIdx;
            } else {
                ++rowIdx;
                break;
            }
        }

        for (SqlTable table : tablesToAdd) {
            ++rowIdx;
            Cell cell = XlsxUtil.setCellValue(maybeMakeCell(rowIdx, getAnchors().getColumn(ANCHOR_NAME)), table.getName().toUpperCase(Locale.ROOT));
            if (StringUtils.isNotBlank(table.getRemarks())) {
                CellComment cellComment = getCellComment(cell).orElse(new CellComment());
                cellComment.setDocumentation(table.getRemarks());
                setCellComment(cell, cellComment);
            }
            List<String> pkColumns = table.getPrimaryKey() == null ? Collections.emptyList() : table.getPrimaryKey().getColumnNames();
            for (String columnName : table.getColumnNames()) {
                SqlTableColumn column = table.getColumn(columnName);
                ++rowIdx;
                cell = XlsxUtil.setCellValue(maybeMakeCell(rowIdx, getAnchors().getColumn(ANCHOR_NAME)), columnName.toLowerCase(Locale.ROOT));
                if (StringUtils.isNotBlank(column.getRemarks())) {
                    CellComment cellComment = getCellComment(cell).orElse(new CellComment());
                    cellComment.setDocumentation(table.getRemarks());
                    setCellComment(cell, cellComment);
                }
                if (column.getDataType() != null) {
                    XlsxUtil.setCellValue(maybeMakeCell(rowIdx, getAnchors().getColumn(ANCHOR_TYPE)), SqlTypesMapper.toTypesName(column.getDataType()));
                } else {
                    XlsxUtil.setCellValue(maybeMakeCell(rowIdx, getAnchors().getColumn(ANCHOR_TYPE)), column.getTypeName());
                }
                if (pkColumns.contains(columnName)) {
                    XlsxUtil.setCellValue(maybeMakeCell(rowIdx, getAnchors().getColumn(ANCHOR_KEY)), CellMarkers.CHECK_MARKER);
                }
                if (!BooleanUtils.toBoolean(column.getNullable())) {
                    XlsxUtil.setCellValue(maybeMakeCell(rowIdx, getAnchors().getColumn(ANCHOR_REQUIRED)), CellMarkers.CHECK_MARKER);
                }
                XlsxUtil.setCellValue(maybeMakeCell(rowIdx, getAnchors().getColumn(ANCHOR_MIN)), column.getColumnSize());
                if (column.getDecimalDigits() > 0) {
                    XlsxUtil.setCellValue(maybeMakeCell(rowIdx, getAnchors().getColumn(ANCHOR_MAX)), column.getDecimalDigits());
                }
            }
            ++rowIdx;
        }

        return conflictReport.toString();
    }

    private void compare(StringBuilder report, SqlTable oldTable, SqlTable newTable) {
    }

}
