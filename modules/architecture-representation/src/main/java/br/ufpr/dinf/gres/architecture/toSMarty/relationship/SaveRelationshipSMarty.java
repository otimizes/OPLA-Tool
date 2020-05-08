package br.ufpr.dinf.gres.architecture.toSMarty.relationship;

import br.ufpr.dinf.gres.architecture.representation.Architecture;

import java.io.PrintWriter;

public class SaveRelationshipSMarty {

    public SaveRelationshipSMarty(Architecture architecture, PrintWriter printWriter, String logPath) {
        new SaveAssociationSMarty(architecture, printWriter, logPath);
        new SaveDependencySMarty(architecture, printWriter, logPath);
        new SaveGeneralizationSMarty(architecture, printWriter, logPath);
        new SaveRealizationSMarty(architecture, printWriter, logPath);
        new SaveRequiresSMarty(architecture, printWriter, logPath);
        new SaveUsageSMarty(architecture, printWriter, logPath);
        new SaveAbstractionSMarty(architecture, printWriter, logPath);
    }

}
