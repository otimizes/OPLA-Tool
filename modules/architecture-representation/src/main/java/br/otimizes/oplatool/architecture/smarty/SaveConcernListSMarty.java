package br.otimizes.oplatool.architecture.smarty;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Concern;

import java.io.PrintWriter;

/**
 * Save the list of stereotypes in the file
 * if the list not exist, create a new list using default concerns + new ones
 *
 */
public class SaveConcernListSMarty {

    public SaveConcernListSMarty() {
    }

    private static final SaveConcernListSMarty INSTANCE = new SaveConcernListSMarty();

    public static SaveConcernListSMarty getInstance() {
        return INSTANCE;
    }

    /**
     * Save the list of stereotypes in the file
     * if not exists, create a new list of stereotypes
     *
     * @param architecture - the architecture to be decoded
     * @param printWriter - used to save the type to file
     */
    public void Save(Architecture architecture, PrintWriter printWriter) {
        String halfTab = "  ";
        printWriter.write("\n" + halfTab + "<stereotypes>");
        if (architecture.getConcerns().size() == 0) {
            GenerateConcernListSMarty.getInstance().Generate(architecture);
        }
        for (Concern ts : architecture.getConcerns()) {
            printWriter.write("\n\t<stereotype id=\"" + ts.getId() + "\" name=\"" + ts.getName() + "\" primitive=\"" + ts.getPrimitive() + "\"/>");
        }
        printWriter.write("\n" + halfTab + "</stereotypes>");
    }
}
