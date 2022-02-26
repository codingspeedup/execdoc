package io.github.codingspeedup.execdoc.apps;

import io.github.codingspeedup.execdoc.toolbox.files.Folder;
import io.github.codingspeedup.execdoc.toolbox.security.SymmetricEndec;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.crypto.SecretKey;
import java.io.*;
import java.util.Properties;

public final class AppCtx {

    public static final String ENCRYPTED_PROPERTY_SUFFIX = ".encrypted";

    private static AppCtxProvider envProvider = null;

    @Getter(lazy = true)
    private static final AppCtx instance = loadEnvironmentProperties();

    @Getter
    private final Folder bpProjectFolder;

    @Getter
    private final Folder configFolder;

    @Getter
    private final Folder tempFolder;

    @Getter
    private final String dotPath;

    @Getter
    private final Folder localCopyLogFolder;

    private final File bpCtxPropertiesEndecFile;
    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    private final SymmetricEndec bpCtxPropertiesEndec = initializeBpCtxEndec();

    private final File bpCtxPropertiesFile;
    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    private final Properties bpCtxProperties = initializeBpCtxProperties();

    private AppCtx(AppCtxProvider envProvider) {
        this.dotPath = envProvider.getDotPath();
        this.bpProjectFolder = envProvider.getBpProjectRoot();
        this.configFolder = new Folder(envProvider.getConfigFolder());
        this.bpCtxPropertiesFile = new File(configFolder, "bp.ctx.properties");
        this.bpCtxPropertiesEndecFile = new File(configFolder, "bp.ctx.keychain");
        this.tempFolder = new Folder(envProvider.getTempFolder());
        this.localCopyLogFolder = new Folder(envProvider.getLocalCopyLogFolder());
    }

    public static void registerEnvironmentProvider(AppCtxProvider envProvider) {
        AppCtx.envProvider = envProvider;
    }

    private static AppCtx loadEnvironmentProperties() {
        if (envProvider == null) {
            System.err.println(AppCtx.class.getName() + ": no registered environment provider - quitting");
            System.exit(-1);
        }
        return new AppCtx(envProvider);
    }

    @SneakyThrows
    private Properties initializeBpCtxProperties() {
        Properties properties = new Properties();
        if (bpCtxPropertiesFile != null && bpCtxPropertiesFile.exists()) {
            try (InputStream propStream = new FileInputStream(bpCtxPropertiesFile)) {
                properties.load(propStream);
            }
        }
        return properties;
    }

    private SymmetricEndec initializeBpCtxEndec() {
        if (!bpCtxPropertiesEndecFile.exists()) {
            Pair<SecretKey, byte[]> keyChain = SymmetricEndec.generateKeyChain();
            SymmetricEndec.storeKeyChain(bpCtxPropertiesEndecFile, keyChain.getLeft(), keyChain.getRight());
        }
        return SymmetricEndec.from(SymmetricEndec.readKeyChain(bpCtxPropertiesEndecFile));
    }

    public Folder getDefaultDiffReportsFolder() {
        return Folder.of(new File(getTempFolder(), "diff-reports"));
    }

    public Folder getDefaultXrayReportsFolder() {
        return Folder.of(new File(getTempFolder(), "xray-reports"));
    }

    @SneakyThrows
    public synchronized String getProperty(String key) {
        Properties properties = getBpCtxProperties();
        String value = properties.getProperty(key);
        if (key.endsWith(ENCRYPTED_PROPERTY_SUFFIX) && StringUtils.isNotEmpty(value)) {
            value = getBpCtxPropertiesEndec().decrypt64(value);
        }
        return value;
    }

    @SneakyThrows
    public synchronized void setProperty(String key, String value) {
        if (key.endsWith(ENCRYPTED_PROPERTY_SUFFIX) && StringUtils.isNotEmpty(value)) {
            value = getBpCtxPropertiesEndec().encrypt64(value);
        }
        Properties properties = getBpCtxProperties();
        properties.put(key, value);
        try (OutputStream propStream = new FileOutputStream(bpCtxPropertiesFile)) {
            properties.store(propStream, "");
        }
    }

}
