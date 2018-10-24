package br.ufpr.dinf.gres.opla.util;

import br.ufpr.dinf.gres.opla.entity.*;
import br.ufpr.dinf.gres.opla.entity.metric.ConventionalMetric;
import br.ufpr.dinf.gres.opla.entity.metric.EleganceMetric;
import br.ufpr.dinf.gres.opla.entity.metric.FeatureDrivenMetric;
import br.ufpr.dinf.gres.opla.entity.metric.PLAExtensibilityMetric;

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
 * Export database sql
 *
 * @author Fernando
 */
public class JpaExporter {

    public static void main(String[] args) {

        final String OUTPUT = "target/database.sql";

        Map<String, String> map = new HashMap<>();
        map.put("hibernate.dialect", "org.hibernate.dialect.SQLiteDialect");

        MetadataSources metadataSources = new MetadataSources(
                new StandardServiceRegistryBuilder().applySettings(map).build());

        metadataSources.addAnnotatedClass(ConventionalMetric.class);
        metadataSources.addAnnotatedClass(DistanceEuclidean.class);
        metadataSources.addAnnotatedClass(EleganceMetric.class);
        metadataSources.addAnnotatedClass(Execution.class);
        metadataSources.addAnnotatedClass(Experiment.class);
        metadataSources.addAnnotatedClass(ExperimentConfiguration.class);
        metadataSources.addAnnotatedClass(FeatureDrivenMetric.class);
        metadataSources.addAnnotatedClass(Info.class);
        metadataSources.addAnnotatedClass(MapObjectiveName.class);
        metadataSources.addAnnotatedClass(Objective.class);
        metadataSources.addAnnotatedClass(PLAExtensibilityMetric.class);

        new File(OUTPUT).delete();

        SchemaExport export = new SchemaExport();
        export.setDelimiter(";");
        export.setOutputFile(OUTPUT);
        export.execute(EnumSet.of(TargetType.SCRIPT), Action.CREATE, metadataSources.buildMetadata());
    }
}
