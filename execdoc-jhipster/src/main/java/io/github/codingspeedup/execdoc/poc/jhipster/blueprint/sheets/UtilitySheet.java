package io.github.codingspeedup.execdoc.poc.jhipster.blueprint.sheets;

import io.github.codingspeedup.execdoc.kb.BpKb;
import io.github.codingspeedup.execdoc.blueprint.kb.BpNames;
import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.blueprint.master.cells.CellMarkers;
import io.github.codingspeedup.execdoc.kb.KbNames;
import io.github.codingspeedup.execdoc.poc.jhipster.blueprint.JHipsterSheet;
import io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.code.JdlType;
import io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.data.JdlEntityRelationship;
import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxUtil;
import io.github.codingspeedup.execdoc.toolbox.utilities.NamingUtility;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UtilitySheet extends JHipsterSheet {

    public static final String NAME_MARKER = UtilitySheet.class.getSimpleName();

    public static final String APP_APPLICATION_TYPE_VALUES = "APP_APPLICATION_TYPE_VALUES";
    public static final String APP_AUTHENTICATION_TYPE_VALUES = "APP_AUTHENTICATION_TYPE_VALUES";
    public static final String APP_BUILD_TOOL_VALUES = "APP_BUILD_TOOL_VALUES";
    public static final String APP_CACHE_PROVIDER_VALUES = "APP_CACHE_PROVIDER_VALUES";
    public static final String APP_CLIENT_FRAMEWORK_VALUES = "APP_CLIENT_FRAMEWORK_VALUES";
    public static final String APP_CLIENT_PACKAGE_MANAGER_VALUES = "APP_CLIENT_PACKAGE_MANAGER_VALUES";
    public static final String APP_DATABASE_TYPE_VALUES = "APP_DATABASE_TYPE_VALUES";
    public static final String APP_DEV_DATABASE_TYPE_VALUES = "APP_DEV_DATABASE_TYPE_VALUES";
    public static final String APP_MESSAGE_BROKER_VALUES = "APP_MESSAGE_BROKER_VALUES";
    public static final String APP_PROD_DATABASE_TYPE_VALUES = "APP_PROD_DATABASE_TYPE_VALUES";
    public static final String APP_SEARCH_ENGINE_VALUES = "APP_SEARCH_ENGINE_VALUES";
    public static final String APP_SERVICE_DISCOVERY_TYPE_VALUES = "APP_SERVICE_DISCOVERY_TYPE_VALUES";
    public static final String APP_TEST_FRAMEWORKS_VALUES = "APP_TEST_FRAMEWORKS_VALUES";
    public static final String APP_WEBSOCKET_VALUES = "APP_WEBSOCKET_VALUES";

    public static final String APP_FALSE_TRUE_VALUES = "APP_FALSE_TRUE_VALUES";
    public static final String APP_TRUE_FALSE_VALUES = "APP_TRUE_FALSE_VALUES";

    public static final String DTO_DERIVED_TYPES = "DTO_DERIVED_TYPES";
    public static final String DTO_PREDEFINED_TYPES = "DTO_PREDEFINED_TYPES";

    public static final String ENTITY_BINARY_VALUE = "ENTITY_BINARY_VALUE";
    public static final String ENTITY_PREDEFINED_TYPES = "ENTITY_PREDEFINED_TYPES";
    public static final String ENTITY_RELATIONSHIPS = "ENTITY_RELATIONSHIPS";

    public UtilitySheet(BlueprintMaster master, Sheet sheet) {
        super(master, sheet);
    }

    public boolean isHidden() {
        return true;
    }

    @Override
    public int initialize() {
        List<String> values;
        int rowIdx = 1;
        int colIdx = -1;

        colIdx += 2;
        values = Stream.of("monolith", "microservice", "gateway").collect(Collectors.toList());
        createNamedDomain(rowIdx, colIdx, APP_APPLICATION_TYPE_VALUES, values);

        colIdx += 2;
        values = Stream.of("jwt", "session", "oauth2").collect(Collectors.toList());
        values.add(0, "");
        createNamedDomain(rowIdx, colIdx, APP_AUTHENTICATION_TYPE_VALUES, values);

        colIdx += 2;
        values = Stream.of("maven", "gradle").collect(Collectors.toList());
        values.add(0, "");
        createNamedDomain(rowIdx, colIdx, APP_BUILD_TOOL_VALUES, values);

        colIdx += 2;
        values = Stream.of("ehcache", "hazelcast", "caffeine", "infinispan", "memcached", "redis", "no").collect(Collectors.toList());
        values.add(0, "");
        createNamedDomain(rowIdx, colIdx, APP_CACHE_PROVIDER_VALUES, values);

        colIdx += 2;
        values = Stream.of("angularX", "angular", "react", "vue", "svelte", "no").collect(Collectors.toList());
        values.add(0, "");
        createNamedDomain(rowIdx, colIdx, APP_CLIENT_FRAMEWORK_VALUES, values);

        colIdx += 2;
        values = Stream.of("npm", "yarn").collect(Collectors.toList());
        values.add(0, "");
        createNamedDomain(rowIdx, colIdx, APP_CLIENT_PACKAGE_MANAGER_VALUES, values);

        colIdx += 2;
        values = Stream.of("sql", "mongodb", "cassandra", "couchbase", "no").collect(Collectors.toList());
        values.add(0, "");
        createNamedDomain(rowIdx, colIdx, APP_DATABASE_TYPE_VALUES, values);

        colIdx += 2;
        values = Stream.of("h2Disk", "h2Memory", "*").collect(Collectors.toList());
        values.add(0, "");
        createNamedDomain(rowIdx, colIdx, APP_DEV_DATABASE_TYPE_VALUES, values);

        colIdx += 2;
        values = Stream.of("false", "kafka").collect(Collectors.toList());
        values.add(0, "");
        createNamedDomain(rowIdx, colIdx, APP_MESSAGE_BROKER_VALUES, values);

        colIdx += 2;
        values = Stream.of("mysql", "mariadb", "mssql", "postgresql", "oracle", "no").collect(Collectors.toList());
        values.add(0, "");
        createNamedDomain(rowIdx, colIdx, APP_PROD_DATABASE_TYPE_VALUES, values);

        colIdx += 2;
        values = Stream.of("false", "elasticsearch").collect(Collectors.toList());
        values.add(0, "");
        createNamedDomain(rowIdx, colIdx, APP_SEARCH_ENGINE_VALUES, values);

        colIdx += 2;
        values = Stream.of("eureka", "consul", "no").collect(Collectors.toList());
        values.add(0, "");
        createNamedDomain(rowIdx, colIdx, APP_SERVICE_DISCOVERY_TYPE_VALUES, values);

        colIdx += 2;
        values = Stream.of("cypress", "protractor", "cucumber", "gatling").collect(Collectors.toList());
        values.add(0, "");
        createNamedDomain(rowIdx, colIdx, APP_TEST_FRAMEWORKS_VALUES, values);

        colIdx += 2;
        values = Stream.of("false", "spring-websocket").collect(Collectors.toList());
        values.add(0, "");
        createNamedDomain(rowIdx, colIdx, APP_WEBSOCKET_VALUES, values);

        colIdx += 2;
        values = Stream.of("false", "true").collect(Collectors.toList());
        values.add(0, "");
        createNamedDomain(rowIdx, colIdx, APP_FALSE_TRUE_VALUES, values);

        colIdx += 2;
        values = Stream.of("true", "false").collect(Collectors.toList());
        values.add(0, "");
        createNamedDomain(rowIdx, colIdx, APP_TRUE_FALSE_VALUES, values);

        colIdx += 2;
        values = Arrays.stream(DtoSheet.DERIVED_TYPES).map(NamingUtility::toTypeLabel).collect(Collectors.toList());
        values.add(0, "");
        createNamedDomain(rowIdx, colIdx, DTO_DERIVED_TYPES, values);

        colIdx += 2;
        values = Arrays.stream(DtoSheet.PREDEFINED_TYPES).map(NamingUtility::toTypeLabel).collect(Collectors.toList());
        values.add(0, "");
        createNamedDomain(rowIdx, colIdx, DTO_PREDEFINED_TYPES, values);

        colIdx += 2;
        values = Stream.of(CellMarkers.CHECK_MARKER).collect(Collectors.toList());
        values.add(0, "");
        createNamedDomain(rowIdx, colIdx, ENTITY_BINARY_VALUE, values);

        colIdx += 2;
        values = Arrays.stream(JdlType.NAMES).collect(Collectors.toList());
        values.add(0, "");
        createNamedDomain(rowIdx, colIdx, ENTITY_PREDEFINED_TYPES, values);

        colIdx += 2;
        values = Stream.of(JdlEntityRelationship.ENTITY_RELATIONSHIPS).map(cls -> CellMarkers.PREDICATE_MARKER + KbNames.getFunctor(cls)).collect(Collectors.toList());
        values.add(0, "");
        createNamedDomain(rowIdx, colIdx, ENTITY_RELATIONSHIPS, values);

        return rowIdx;
    }

    @Override
    public void normalize() {
    }

    @Override
    public void expand(BpKb bpKb) {
    }

    private void createNamedDomain(int rowIdx, int colIdx, String name, List<String> values) {
        setCellValue(rowIdx, colIdx, name);
        rowIdx += 1;
        for (int i = 0; i < values.size(); ++i) {
            setCellValue(rowIdx + i, colIdx, values.get(i));
        }
        Name namedRange = getWorkbook().createName();
        namedRange.setNameName(name);
        namedRange.setRefersToFormula(XlsxUtil.createAbsoluteAreaReference(getSheet(),
                rowIdx, rowIdx + values.size() - 1, colIdx, colIdx));
        autoSizeColumns(colIdx);
    }

}
