package io.github.codingspeedup.execdoc.blueprint;

import io.github.codingspeedup.execdoc.blueprint.metamodel.BpKb;
import io.github.codingspeedup.execdoc.generators.utilities.GenCfg;
import io.github.codingspeedup.execdoc.kb.Kb;
import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.bootstrap.sql.XlsxBase;
import io.github.codingspeedup.execdoc.toolbox.documents.FolderWrapper;
import lombok.SneakyThrows;

import java.io.File;

public abstract class Blueprint<M extends BlueprintMaster> extends FolderWrapper {

    private final Class<M> masterClass;
    private M master;
    private XlsxBase sqlData;

    @SuppressWarnings({"unchecked"})
    public Blueprint(File repository) {
        this((Class<M>) BlueprintMaster.class, repository);
    }

    protected Blueprint(Class<M> masterClass, File repository) {
        super(repository);
        this.masterClass = masterClass;
    }

    public void normalize() {
        getMaster().normalize();
    }

    public Kb compileKb() {
        Kb kb = new BpKb();
        getMaster().train(kb);
        return kb;
    }

    @SneakyThrows
    public M getMaster() {
        if (master == null) {
            master = masterClass.getConstructor(File.class).newInstance(new File(getWrappedFile(), "_master.xlsx"));
        }
        return master;
    }

    public XlsxBase getSqlData() {
        if (sqlData == null) {
            sqlData = new XlsxBase(new File(getWrappedFile(), "_sql.xlsx"));
        }
        return sqlData;
    }

    public abstract void generate(GenCfg bpGenCfg);

    @Override
    protected void saveToWrappedFile() {
        getMaster().save();
        if (sqlData != null) {
            sqlData.save();
        }
    }

}
