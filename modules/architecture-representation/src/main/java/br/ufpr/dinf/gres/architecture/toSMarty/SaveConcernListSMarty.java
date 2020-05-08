package br.ufpr.dinf.gres.architecture.toSMarty;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Concern;

import java.io.PrintWriter;

public class SaveConcernListSMarty {

    public SaveConcernListSMarty(Architecture architecture, PrintWriter printWriter) {
        String halfTab = "  ";
        String tab = "    ";
        // create concern list if not exists
        if (architecture.getLstConcerns().size() == 0) {
            new GenerateConcernListSMarty(architecture);
        }
        // save concerns
        printWriter.write("\n" + halfTab + "<stereotypes>");
        // add stereotypes
        for (Concern ts : architecture.getLstConcerns()) {
            //  <stereotype id="STEREOTYPE#10" name="albummanagement" primitive="false"/>
            printWriter.write("\n\t<stereotype id=\"" + ts.getId() + "\" name=\"" + ts.getName() + "\" primitive=\"" + ts.getPrimitive() + "\"/>");
        }
        printWriter.write("\n" + halfTab + "</stereotypes>");
    }
}
