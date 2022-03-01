package io.github.codingspeedup.execdoc.spring.blueprint.sheets;

import io.github.codingspeedup.execdoc.blueprint.utilities.NormReport;
import io.github.codingspeedup.execdoc.kb.Kb;
import io.github.codingspeedup.execdoc.blueprint.metamodel.individuals.BpSheet;
import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.blueprint.master.cells.CellMarkers;
import io.github.codingspeedup.execdoc.spring.blueprint.SpringSheet;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.JdlDto;
import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;

public class DtoSheet extends SpringSheet {

    public static final String NAME_MARKER = "DTOs" + BlueprintMaster.INSTANTIABLE_SHEET_MARKER;
    public static final String TOC_CHAPTER = "Presentation";

    public static final String ANCHOR_NAME = CellMarkers.ANCHOR_MARKER + "ClassName / fieldName";
    public static final String ANCHOR_TYPE = CellMarkers.ANCHOR_MARKER + "Extends/FieldType";
    public static final String ANCHOR_DERIVATION = CellMarkers.ANCHOR_MARKER + "DerivedType";

    public static final Class<?>[] PREDEFINED_TYPES = {
    };

    public static final Class<?>[] DERIVED_TYPES = {
    };

    public DtoSheet(BlueprintMaster xlsx, Sheet sheet) {
        super(xlsx, sheet);
    }

    @Override
    public int initialize() {
        int rowIdx = 0;
        int colIdx = 0;
        setCellValue(rowIdx, ++colIdx, ANCHOR_NAME);
        setCellValue(rowIdx, ++colIdx, ANCHOR_TYPE);
        setCellValue(rowIdx, ++colIdx, ANCHOR_DERIVATION);
        autoSizeColumns("0-" + colIdx);
        getSheet().createFreezePane(0, rowIdx + 1);

        rowIdx = getAnchors().getLastAnchorRow() + 2;
        colIdx = getAnchors().getColumn(ANCHOR_NAME);
        getSheet().setActiveCell(new CellAddress(rowIdx, colIdx));

        colIdx = getAnchors().getColumn(ANCHOR_TYPE);
        createValidation(rowIdx, rowIdx + 100, colIdx, colIdx, UtilitySheet.DTO_PREDEFINED_TYPES);

        colIdx = getAnchors().getColumn(ANCHOR_DERIVATION);
        createValidation(rowIdx, rowIdx + 100, colIdx, colIdx, UtilitySheet.DTO_DERIVED_TYPES);

        return rowIdx;
    }

    @Override
    public void normalize(NormReport normReport) {
    }

//    private void addFacts(Theory theory) {
//        Struct classUnit = null;
//        for (int rowIdx = getAnchors().getLastAnchorRow() + 1; rowIdx <= getSheet().getLastRowNum(); ++rowIdx) {
//            Row row = getSheet().getRow(rowIdx);
//
//            Cell nameCell = getCell(rowIdx, getAnchors().getColumn(ANCHOR_NAME));
//            String nameString = XlsxUtil.getCellValue(nameCell, String.class);
//            if (StringUtils.isBlank(nameString)) {
//                classUnit = null;
//                continue;
//            }
//
//            if (classUnit == null) {
//                classUnit = PrologKb.atom(nameCell);
//                theory.plus(PrologKb.clause(Bp.C_DTO_CLASS_UNIT, classUnit));
//                theory.plus(PrologKb.clause(Kdm.F_NAME, classUnit, nameString));
//                theory.plus(PrologKb.clause(Kdm.F_OWNER, classUnit, getInstanceName()));
//                PrologKb.addCommentFacts(theory, classUnit, XlsxUtil.getCellComment(nameCell));
//
//                Cell typeCell = row.getCell(getAnchors().getColumn(ANCHOR_TYPE));
//                CellFormula typeFormula = XlsxUtil.getCellValue(typeCell, CellFormula.class);
//                if (typeFormula != null) {
//                    Cell extendedCell = XlsxUtil.backtraceCellBySimpleFormulaReference(typeCell);
//                    theory.plus(PrologKb.clause(Kdm.R_EXTENDS, classUnit, PrologKb.atom(extendedCell)));
//                }
//
//                if (XlsxUtil.isFormula(nameCell)) {
//                    Cell entityCell = XlsxUtil.backtraceCellBySimpleFormulaReference(nameCell);
//                    BlueprintSheet bpSheet = getMaster().getSheet(entityCell);
//                    if (bpSheet.getSheetMarker().equals(EntitySheet.NAME_MARKER)
//                                && getMaster().isOwnerUnit(entityCell, EntitySheet.class, EntitySheet.ANCHOR_NAME)) {
//                        theory.plus(PrologKb.clause(Bp.C_DTO_CLASS_UNIT, classUnit, entityCell));
//                    }
//                }
//            } else {
//                Struct memberUnit = PrologKb.atom(nameCell);
//                theory.plus(PrologKb.clause(Kdm.C_MEMBER_UNIT, memberUnit));
//                theory.plus(PrologKb.clause(Kdm.F_NAME, memberUnit, nameString));
//                theory.plus(PrologKb.clause(Kdm.F_OWNER, memberUnit, classUnit));
//                PrologKb.addCommentFacts(theory, memberUnit, XlsxUtil.getCellComment(nameCell));
//
//                Struct type;
//                Cell typeCell = row.getCell(getAnchors().getColumn(ANCHOR_TYPE));
//                Cell extendedCell = XlsxUtil.backtraceCellBySimpleFormulaReference(typeCell);
//                if (XlsxUtil.areEqual(typeCell, extendedCell)) {
//                    String typeString = XlsxUtil.getCellValue(typeCell, String.class);
//                    type = Struct.of(typeString + CellMarkers.TYPE_SUFFIX);
//                } else {
//                    type = PrologKb.atom(extendedCell);
//                }
//
//                Struct derivedType = null;
//                Cell derivedTypeCell = row.getCell(getAnchors().getColumn(ANCHOR_DERIVATION));
//                String derivedTypeString = XlsxUtil.getCellValue(derivedTypeCell, String.class);
//                if (StringUtils.isBlank(derivedTypeString)) {
//                    derivedType = Struct.of(derivedTypeString + CellMarkers.TYPE_SUFFIX);
//                }
//
//                theory.plus(PrologKb.clause(Kdm.F_TYPE, memberUnit, type, derivedType));
//            }
//        }
//    }

    @Override
    public void expand(Kb kb) {
        BpSheet owner = new BpSheet(getSheet());
        JdlDto dtoUnit = null;
        for (int rowIdx = getAnchors().getLastAnchorRow() + 1; rowIdx <= getSheet().getLastRowNum(); ++rowIdx) {
            Cell nameCell = getCell(rowIdx, getAnchors().getColumn(ANCHOR_NAME));
            String nameString = XlsxUtil.getCellValue(nameCell, String.class);
            if (StringUtils.isBlank(nameString)) {
                kb.learn(dtoUnit);
                dtoUnit = null;
                continue;
            }

            if (dtoUnit == null) {
                dtoUnit = new JdlDto(nameCell);
                dtoUnit.setOwner(owner);
            }
        }
        kb.learn(dtoUnit);
    }

}
