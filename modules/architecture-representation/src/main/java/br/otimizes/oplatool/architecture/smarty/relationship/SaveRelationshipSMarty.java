package br.otimizes.oplatool.architecture.smarty.relationship;

import br.otimizes.oplatool.architecture.representation.Architecture;

import java.io.PrintWriter;

/**
 * This class save all relationship from an architecture
 * Association, Dependency, Usage, Abstraction, Generalization, Realization, Requires
 * But save Usage and Abstraction as Dependency because SMarty Modeling not has this relationship in this moment
 */
public class SaveRelationshipSMarty {

    public SaveRelationshipSMarty() {
    }

    private static final SaveRelationshipSMarty INSTANCE = new SaveRelationshipSMarty();

    public static SaveRelationshipSMarty getInstance() {
        return INSTANCE;
    }

    public void save(Architecture architecture, PrintWriter printWriter, String logPath) {
        SaveAbstractionSMarty.getInstance().Save(architecture, printWriter, logPath);
        SaveAssociationSMarty.getInstance().Save(architecture, printWriter, logPath);
        SaveDependencySMarty.getInstance().Save(architecture, printWriter, logPath);
        SaveGeneralizationSMarty.getInstance().Save(architecture, printWriter, logPath);
        SaveMutexSMarty.getInstance().Save(architecture, printWriter, logPath);
        SaveRealizationSMarty.getInstance().Save(architecture, printWriter, logPath);
        SaveRequiresSMarty.getInstance().save(architecture, printWriter, logPath);
        SaveUsageSMarty.getInstance().Save(architecture, printWriter, logPath);
    }
}
