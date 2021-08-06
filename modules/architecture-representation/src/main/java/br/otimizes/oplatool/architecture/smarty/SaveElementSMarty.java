package br.otimizes.oplatool.architecture.smarty;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Method;
import br.otimizes.oplatool.architecture.representation.ParameterMethod;
import br.otimizes.oplatool.architecture.representation.TypeSmarty;

import java.io.PrintWriter;

public class SaveElementSMarty {

    public static void addStaticMethod(PrintWriter printWriter, Architecture architecture, String halfTab, String tab, Method method) {
        TypeSmarty typeS = architecture.findTypeSMartyByName(method.getReturnType());
        printWriter.write("\n" + tab + halfTab + "<method id=\"" + method.getId() + "\" name=\"" + method.getName()
                + "\" return=\"" + typeS.getId() + "\" visibility=\"" + method.getVisibility()
                + "\" constructor=\"" + method.isConstructor() + "\" static=\"" + method.isStatic()
                + "\" final=\"" + method.isFinal() + "\" abstract=\"" + method.isAbstract() + "\">");
        for (ParameterMethod parameterMethod : method.getParameters()) {
            typeS = architecture.findTypeSMartyByName(parameterMethod.getType());
            printWriter.write("\n" + tab + tab + "<parameter type=\"" + typeS.getId() + "\" name=\"" + parameterMethod.getName() + "\"/>");
        }
        printWriter.write("\n" + tab + halfTab + "</method>");
    }

    public static void addAbstractMethod(Architecture architecture, PrintWriter printWriter, String halfTab, String tab, Method method) {
        TypeSmarty typeS = architecture.findReturnTypeSMartyByName(method.getReturnType());
        printWriter.write("\n" + tab + halfTab + "<method id=\"" + method.getId() + "\" name=\"" + method.getName()
                + "\" return=\"" + typeS.getId() + "\" visibility=\"" + method.getVisibility()
                + "\" constructor=\"" + method.isConstructor() + "\" static=\"" + method.isStatic()
                + "\" final=\"" + method.isFinal() + "\" abstract=\"" + method.isAbstract() + "\">");
        for (ParameterMethod parameterMethod : method.getParameters()) {
            if (parameterMethod.getName().length() == 0)
                continue;
            typeS = architecture.findTypeSMartyByName(parameterMethod.getType());
            printWriter.write("\n" + tab + tab + "<parameter type=\"" + typeS.getId() + "\" name=\"" + parameterMethod.getName() + "\"/>");
        }
        printWriter.write("\n" + tab + halfTab + "</method>");
    }
}
