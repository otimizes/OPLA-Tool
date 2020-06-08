package br.otimizes.oplatool.domain.util;

import br.otimizes.oplatool.domain.entity.*;
import br.otimizes.oplatool.domain.entity.objectivefunctions.*;
import br.otimizes.oplatool.domain.entity.*;
import br.otimizes.oplatool.domain.entity.objectivefunctions.*;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaExport.Action;
import org.hibernate.tool.schema.TargetType;

import java.io.File;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Export br.otimizes.oplatool.core.jmetal4.database sql
 *
 * @author Fernando
 */
public class JpaExporter {

    public static void main(String[] args) {

        final String OUTPUT = "target/br.otimizes.oplatool.core.jmetal4.database.sql";

        Map<String, String> map = new HashMap<>();
        map.put("hibernate.dialect", "org.hibernate.dialect.SQLiteDialect");

        MetadataSources metadataSources = new MetadataSources(
                new StandardServiceRegistryBuilder().applySettings(map).build());

        metadataSources.addAnnotatedClass(CMObjectiveFunction.class);
        metadataSources.addAnnotatedClass(DistanceEuclidean.class);
        metadataSources.addAnnotatedClass(ELEGObjectiveFunction.class);
        metadataSources.addAnnotatedClass(Execution.class);
        metadataSources.addAnnotatedClass(Experiment.class);
        metadataSources.addAnnotatedClass(ExperimentConfiguration.class);
        metadataSources.addAnnotatedClass(FMObjectiveFunction.class);
        metadataSources.addAnnotatedClass(Info.class);
        metadataSources.addAnnotatedClass(MapObjectiveName.class);
        metadataSources.addAnnotatedClass(Objective.class);
        metadataSources.addAnnotatedClass(EXTObjectiveFunction.class);
        metadataSources.addAnnotatedClass(TVObjectiveFunction.class);
        metadataSources.addAnnotatedClass(RCCObjectiveFunction.class);
        metadataSources.addAnnotatedClass(SDObjectiveFunction.class);
        metadataSources.addAnnotatedClass(SVObjectiveFunction.class);
        metadataSources.addAnnotatedClass(WOCSCLASSObjectiveFunction.class);
        metadataSources.addAnnotatedClass(CSObjectiveFunction.class);

        new File(OUTPUT).delete();

        SchemaExport export = new SchemaExport();
        export.setDelimiter(";");
        export.setOutputFile(OUTPUT);
        export.execute(EnumSet.of(TargetType.SCRIPT), Action.CREATE, metadataSources.buildMetadata());
    }
}
