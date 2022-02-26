package io.github.codingspeedup.execdoc.poc.jhipster.blueprint.sheets;

import com.google.common.base.CaseFormat;
import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.blueprint.master.cells.CellMarkers;
import io.github.codingspeedup.execdoc.blueprint.metamodel.BpNames;
import io.github.codingspeedup.execdoc.kb.Kb;
import io.github.codingspeedup.execdoc.poc.jhipster.blueprint.JHipsterSheet;
import io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.individuals.structure.JdlApplication;
import io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.individuals.structure.JdlValue;
import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Arrays;
import java.util.List;

/**
 * https://www.jhipster.tech/jdl/applications
 */
public class AppSheet extends JHipsterSheet {

    public static final String NAME_MARKER = "APP" + BlueprintMaster.INSTANTIABLE_SHEET_MARKER;
    public static final String TOC_CHAPTER = "Global";

    public static final String ANCHOR_OPTION_NAME = CellMarkers.ANCHOR_MARKER + "Property name";
    public static final String ANCHOR_OPTION_VALUE = CellMarkers.ANCHOR_MARKER + "Property value(s)";

    public static final String ENTITIES = "entities";

    public static final String APPLICATION_TYPE = "applicationType";
    public static final String AUTHENTICATION_TYPE = "authenticationType";
    public static final String BASE_NAME = "baseName";
    public static final String BLUEPRINTS = "blueprints";
    public static final String BUILD_TOOL = "buildTool";
    public static final String CACHE_PROVIDER = "cacheProvider";
    public static final String CLIENT_FRAMEWORK = "clientFramework";
    public static final String CLIENT_PACKAGE_MANAGER = "clientPackageManager";
    public static final String CLIENT_THEME = "clientTheme";
    public static final String CLIENT_THEME_VARIANT = "clientThemeVariant";
    public static final String DATABASE_TYPE = "databaseType";
    public static final String DEV_DATABASE_TYPE = "devDatabaseType";
    public static final String DTO_SUFFIX = "dtoSuffix";
    public static final String ENABLE_HIBERNATE_CACHE = "enableHibernateCache";
    public static final String ENABLE_SWAGGER_CODEGEN = "enableSwaggerCodegen";
    public static final String ENABLE_TRANSLATION = "enableTranslation";
    public static final String ENTITY_SUFFIX = "entitySuffix";
    public static final String JHI_PREFIX = "jhiPrefix";
    public static final String LANGUAGES = "languages";
    public static final String MESSAGE_BROKER = "messageBroker";
    public static final String NATIVE_LANGUAGE = "nativeLanguage";
    public static final String PACKAGE_NAME = "packageName";
    public static final String PROD_DATABASE_TYPE = "prodDatabaseType";
    public static final String REACTIVE = "reactive";
    public static final String SEARCH_ENGINE = "searchEngine";
    public static final String SERVER_PORT = "serverPort";
    public static final String SERVICE_DISCOVERY_TYPE = "serviceDiscoveryType";
    public static final String SKIP_CLIENT = "skipClient";
    public static final String SKIP_SERVER = "skipServer";
    public static final String SKIP_USER_MANAGEMENT = "skipUserManagement";
    public static final String TEST_FRAMEWORKS = "testFrameworks";
    public static final String WEBSOCKET = "websocket";

    public static final List<String> APP_OPTIONS = Arrays.asList(
            APPLICATION_TYPE,
            AUTHENTICATION_TYPE,
            BASE_NAME,
            BLUEPRINTS,
            BUILD_TOOL,
            CACHE_PROVIDER,
            CLIENT_FRAMEWORK,
            CLIENT_PACKAGE_MANAGER,
            CLIENT_THEME,
            CLIENT_THEME_VARIANT,
            DATABASE_TYPE,
            DEV_DATABASE_TYPE,
            DTO_SUFFIX,
            ENABLE_HIBERNATE_CACHE,
            ENABLE_SWAGGER_CODEGEN,
            ENABLE_TRANSLATION,
            ENTITY_SUFFIX,
            JHI_PREFIX,
            LANGUAGES,
            MESSAGE_BROKER,
            NATIVE_LANGUAGE,
            PACKAGE_NAME,
            PROD_DATABASE_TYPE,
            REACTIVE,
            SEARCH_ENGINE,
            SERVER_PORT,
            SERVICE_DISCOVERY_TYPE,
            SKIP_CLIENT,
            SKIP_SERVER,
            SKIP_USER_MANAGEMENT,
            TEST_FRAMEWORKS,
            WEBSOCKET
    );

    public AppSheet(BlueprintMaster bp, Sheet sheet) {
        super(bp, sheet);
    }

    @Override
    public int initialize() {
        int nameColIdx = 1;
        int valueColIdx = 2;
        int rowIdx = 0;

        setCellValue(rowIdx, nameColIdx, ANCHOR_OPTION_NAME);
        setCellValue(rowIdx, valueColIdx, ANCHOR_OPTION_VALUE);
        ++rowIdx;
        getSheet().createFreezePane(0, rowIdx);

        setCellValue(++rowIdx, nameColIdx, APPLICATION_TYPE);
        createValidation(rowIdx, rowIdx, valueColIdx, valueColIdx, UtilitySheet.APP_APPLICATION_TYPE_VALUES);
        setCellValue(rowIdx, valueColIdx, "monolith");

        setCellValue(++rowIdx, nameColIdx, AUTHENTICATION_TYPE);
        createValidation(rowIdx, rowIdx, valueColIdx, valueColIdx, UtilitySheet.APP_AUTHENTICATION_TYPE_VALUES);

        setCellValue(++rowIdx, nameColIdx, BASE_NAME);

        setCellValue(++rowIdx, nameColIdx, BLUEPRINTS);

        setCellValue(++rowIdx, nameColIdx, BUILD_TOOL);
        createValidation(rowIdx, rowIdx, valueColIdx, valueColIdx, UtilitySheet.APP_BUILD_TOOL_VALUES);

        setCellValue(++rowIdx, nameColIdx, CACHE_PROVIDER);
        createValidation(rowIdx, rowIdx, valueColIdx, valueColIdx, UtilitySheet.APP_CACHE_PROVIDER_VALUES);

        setCellValue(++rowIdx, nameColIdx, CLIENT_FRAMEWORK);
        createValidation(rowIdx, rowIdx, valueColIdx, valueColIdx, UtilitySheet.APP_CLIENT_FRAMEWORK_VALUES);

        setCellValue(++rowIdx, nameColIdx, CLIENT_PACKAGE_MANAGER);
        createValidation(rowIdx, rowIdx, valueColIdx, valueColIdx, UtilitySheet.APP_CLIENT_PACKAGE_MANAGER_VALUES);

        setCellValue(++rowIdx, nameColIdx, CLIENT_THEME);
        // setCellValue(rowIdx, valueColIdx, "none");

        setCellValue(++rowIdx, nameColIdx, CLIENT_THEME_VARIANT);

        setCellValue(++rowIdx, nameColIdx, DATABASE_TYPE);
        createValidation(rowIdx, rowIdx, valueColIdx, valueColIdx, UtilitySheet.APP_DATABASE_TYPE_VALUES);

        setCellValue(++rowIdx, nameColIdx, DEV_DATABASE_TYPE);
        createValidation(rowIdx, rowIdx, valueColIdx, valueColIdx, UtilitySheet.APP_DEV_DATABASE_TYPE_VALUES);

        setCellValue(++rowIdx, nameColIdx, DTO_SUFFIX);
        setCellValue(rowIdx, valueColIdx, "DTO");

        setCellValue(++rowIdx, nameColIdx, ENABLE_HIBERNATE_CACHE);
        createValidation(rowIdx, rowIdx, valueColIdx, valueColIdx, UtilitySheet.APP_TRUE_FALSE_VALUES);

        setCellValue(++rowIdx, nameColIdx, ENABLE_SWAGGER_CODEGEN);
        createValidation(rowIdx, rowIdx, valueColIdx, valueColIdx, UtilitySheet.APP_FALSE_TRUE_VALUES);

        setCellValue(++rowIdx, nameColIdx, ENABLE_TRANSLATION);
        createValidation(rowIdx, rowIdx, valueColIdx, valueColIdx, UtilitySheet.APP_TRUE_FALSE_VALUES);

        setCellValue(++rowIdx, nameColIdx, ENTITY_SUFFIX);

        setCellValue(++rowIdx, nameColIdx, JHI_PREFIX);
        setCellValue(rowIdx, valueColIdx, "jhi");

        setCellValue(++rowIdx, nameColIdx, LANGUAGES);
        setCellValue(rowIdx, valueColIdx, "en");
        setCellValue(rowIdx, valueColIdx + 1, "fr");

        setCellValue(++rowIdx, nameColIdx, MESSAGE_BROKER);
        createValidation(rowIdx, rowIdx, valueColIdx, valueColIdx, UtilitySheet.APP_MESSAGE_BROKER_VALUES);

        setCellValue(++rowIdx, nameColIdx, NATIVE_LANGUAGE);
        setCellValue(rowIdx, valueColIdx, "en");

        setCellValue(++rowIdx, nameColIdx, PACKAGE_NAME);
        setCellValue(rowIdx, valueColIdx, "com.mycompany.myapp");

        setCellValue(++rowIdx, nameColIdx, PROD_DATABASE_TYPE);
        createValidation(rowIdx, rowIdx, valueColIdx, valueColIdx, UtilitySheet.APP_PROD_DATABASE_TYPE_VALUES);

        setCellValue(++rowIdx, nameColIdx, REACTIVE);
        createValidation(rowIdx, rowIdx, valueColIdx, valueColIdx, UtilitySheet.APP_FALSE_TRUE_VALUES);

        setCellValue(++rowIdx, nameColIdx, SEARCH_ENGINE);
        createValidation(rowIdx, rowIdx, valueColIdx, valueColIdx, UtilitySheet.APP_SEARCH_ENGINE_VALUES);

        setCellValue(++rowIdx, nameColIdx, SERVER_PORT);

        setCellValue(++rowIdx, nameColIdx, SERVICE_DISCOVERY_TYPE);
        createValidation(rowIdx, rowIdx, valueColIdx, valueColIdx, UtilitySheet.APP_SEARCH_ENGINE_VALUES);

        setCellValue(++rowIdx, nameColIdx, SKIP_CLIENT);
        createValidation(rowIdx, rowIdx, valueColIdx, valueColIdx, UtilitySheet.APP_FALSE_TRUE_VALUES);

        setCellValue(++rowIdx, nameColIdx, SKIP_SERVER);
        createValidation(rowIdx, rowIdx, valueColIdx, valueColIdx, UtilitySheet.APP_FALSE_TRUE_VALUES);

        setCellValue(++rowIdx, nameColIdx, SKIP_USER_MANAGEMENT);
        createValidation(rowIdx, rowIdx, valueColIdx, valueColIdx, UtilitySheet.APP_FALSE_TRUE_VALUES);

        setCellValue(++rowIdx, nameColIdx, TEST_FRAMEWORKS);
        createValidation(rowIdx, rowIdx, valueColIdx, valueColIdx, UtilitySheet.APP_TEST_FRAMEWORKS_VALUES);
        createValidation(rowIdx, rowIdx, valueColIdx, valueColIdx + 1, UtilitySheet.APP_TEST_FRAMEWORKS_VALUES);
        createValidation(rowIdx, rowIdx, valueColIdx, valueColIdx + 2, UtilitySheet.APP_TEST_FRAMEWORKS_VALUES);
        createValidation(rowIdx, rowIdx, valueColIdx, valueColIdx + 3, UtilitySheet.APP_TEST_FRAMEWORKS_VALUES);

        setCellValue(++rowIdx, nameColIdx, WEBSOCKET);
        createValidation(rowIdx, rowIdx, valueColIdx, valueColIdx, UtilitySheet.APP_WEBSOCKET_VALUES);

        ++rowIdx;
        ++rowIdx;

        setCellValue(++rowIdx, nameColIdx, ENTITIES);
        setCellValue(rowIdx, valueColIdx, "*");

        autoSizeColumns(nameColIdx, valueColIdx);
        return rowIdx + 1;
    }

    @Override
    public void normalize() {
        int nameColIdx = getAnchors().getColumn(ANCHOR_OPTION_NAME);
        int valueColIdx = getAnchors().getColumn(ANCHOR_OPTION_VALUE);
        for (Row row : getSheet()) {
            if (BASE_NAME.equals(XlsxUtil.getCellValue(row.getCell(nameColIdx), String.class))) {
                String value = XlsxUtil.getCellValue(row.getCell(valueColIdx), String.class);
                if (StringUtils.isBlank(value)) {
                    value = StringUtils.trimToEmpty(getInstanceName()).replaceAll("\\s+", "-");
                    value = CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, value);
                    XlsxUtil.setCellValue(maybeMakeCell(row.getRowNum(), valueColIdx), value);
                }
            }
        }
        autoSizeColumns(nameColIdx, valueColIdx);
    }

    @Override
    public void expand(Kb kb) {
        JdlApplication ss = new JdlApplication(this);
        int nameColIdx = getAnchors().getColumn(ANCHOR_OPTION_NAME);
        int valueColIdx = getAnchors().getColumn(ANCHOR_OPTION_VALUE);
        for (int rowIdx = getAnchors().getLastAnchorRow() + 1; rowIdx <= getSheet().getLastRowNum(); ++rowIdx) {
            String nameString = XlsxUtil.getCellValue(getCell(rowIdx, nameColIdx), String.class);
            if (StringUtils.isNotBlank(nameString)) {
                JdlValue value = new JdlValue(getCell(rowIdx, valueColIdx));
                if (!value.isEmpty()) {
                    if (ENTITIES.equals(nameString)) {
                        Row row = getSheet().getRow(rowIdx);
                        for (int colIdx = valueColIdx; colIdx <= row.getLastCellNum(); ++colIdx) {
                            Cell cell = row.getCell(colIdx);
                            if (cell != null) {
                                if (cell.getCellType() == CellType.FORMULA) {
                                    cell = XlsxUtil.backtraceCellBySimpleFormulaReference(cell);
                                    ss.getEntities().add(BpNames.getAtom(cell));
                                } else if ("*".equals(XlsxUtil.getCellValue(cell, String.class))) {
                                    ss.getEntities().clear();
                                    ss.getEntities().add("*");
                                    break;
                                }
                            }
                        }
                        ss.putConfig(nameString, value);
                    } else if (APP_OPTIONS.contains(nameString)) {
                        ss.putConfig(nameString, value);
                    }
                }
            }
        }
        kb.learn(ss);
    }

}
