package br.ufpr.dinf.gres.architecture.toSMarty;

import br.ufpr.dinf.gres.architecture.representation.*;
import br.ufpr.dinf.gres.architecture.representation.Package;

import java.io.PrintWriter;

public class SaveInterfaceSMarty {

    public SaveInterfaceSMarty(Architecture architecture, PrintWriter printWriter) {
        String halfTab = "  ";
        String tab = "    ";
        // save interface that not in package
        for (Interface inter : architecture.getInterfaces()) {
            printWriter.write("\n" + tab + "<interface id=\"" + inter.getId() + "\" name=\"" + inter.getName() + "\" mandatory=\"" + inter.isMandatory() + "\" x=\"" + inter.getPosX() + "\" y=\"" + inter.getPosY() + "\" globalX=\"" + inter.getGlobalPosX() + "\" globalY=\"" + inter.getGlobalPosY() + "\" height=\"" + inter.getHeight() + "\" width=\"" + inter.getWidth() + "\" parent=\"\">");

            for (Method m : inter.getMethods()) {
                // <method id="METHOD#45" name="getmedia" return="TYPE#9" visibility="public" constructor="false" static="false" final="false" abstract="false">
                TypeSmarty typeS = architecture.findReturnTypeSMartyByName(m.getReturnType());
                printWriter.write("\n" + tab + halfTab + "<method id=\"" + m.getId() + "\" name=\"" + m.getName() + "\" return=\"" + typeS.getId() + "\" visibility=\"" + m.getVisibility() + "\" constructor=\"" + m.isConstructor() + "\" static=\"" + m.isStatic() + "\" final=\"" + m.isFinal() + "\" abstract=\"" + m.isAbstract() + "\">");
                //printWriter.write("\n\t"+halfTab+"<method id=\""+m.getId()+"\" name=\""+m.getName()+"\" return=\""+m.getReturnType()+"\" visibility=\""+m.getVisibility()+"\" constructor=\""+m.isConstructor()+"\" static=\""+m.isStatic()+"\" final=\""+m.isFinal()+"\" abstract=\""+m.isAbstract()+"\">");
                for (ParameterMethod pm : m.getParameters()) {
                    if (pm.getName().length() == 0)
                        continue;
                    typeS = architecture.findTypeSMartyByName(pm.getType());
                    printWriter.write("\n" + tab + tab + "<parameter type=\"" + typeS.getId() + "\" name=\"" + pm.getName() + "\"/>");
                    //printWriter.write("\n\t\t<parameter type=\""+pm.getType()+"\" name=\""+pm.getName()+"\"/>");
                }
                printWriter.write("\n" + tab + halfTab + "</method>");
            }
            printWriter.write("\n" + tab + "</interface>");
        }
        for (Package pkg : architecture.getAllPackages()) {
            //System.out.println("Package:"+pkg.getName());
            for (Interface inter : pkg.getAllInterfaces()) {
                //System.out.println("Interface:"+inter.getName());
                printWriter.write("\n" + tab + "<interface id=\"" + inter.getId() + "\" name=\"" + inter.getName() + "\" mandatory=\"" + inter.isMandatory() + "\" x=\"" + inter.getPosX() + "\" y=\"" + inter.getPosY() + "\" globalX=\"" + inter.getGlobalPosX() + "\" globalY=\"" + inter.getGlobalPosY() + "\" height=\"" + inter.getHeight() + "\" width=\"" + inter.getWidth() + "\" parent=\"" + pkg.getId() + "\">");

                for (Method m : inter.getMethods()) {
                    // <method id="METHOD#45" name="getmedia" return="TYPE#9" visibility="public" constructor="false" static="false" final="false" abstract="false">
                    TypeSmarty typeS = architecture.findReturnTypeSMartyByName(m.getReturnType());
                    printWriter.write("\n" + tab + halfTab + "<method id=\"" + m.getId() + "\" name=\"" + m.getName() + "\" return=\"" + typeS.getId() + "\" visibility=\"" + m.getVisibility() + "\" constructor=\"" + m.isConstructor() + "\" static=\"" + m.isStatic() + "\" final=\"" + m.isFinal() + "\" abstract=\"" + m.isAbstract() + "\">");
                    //printWriter.write("\n\t"+halfTab+"<method id=\""+m.getId()+"\" name=\""+m.getName()+"\" return=\""+m.getReturnType()+"\" visibility=\""+m.getVisibility()+"\" constructor=\""+m.isConstructor()+"\" static=\""+m.isStatic()+"\" final=\""+m.isFinal()+"\" abstract=\""+m.isAbstract()+"\">");
                    for (ParameterMethod pm : m.getParameters()) {
                        if (pm.getName().length() == 0)
                            continue;
                        typeS = architecture.findTypeSMartyByName(pm.getType());
                        printWriter.write("\n" + tab + tab + "<parameter type=\"" + typeS.getId() + "\" name=\"" + pm.getName() + "\"/>");
                        //printWriter.write("\n\t\t<parameter type=\""+pm.getType()+"\" name=\""+pm.getName()+"\"/>");
                    }
                    printWriter.write("\n" + tab + halfTab + "</method>");
                }
                printWriter.write("\n" + tab + "</interface>");
            }
            saveInterfaceInSubPackage(pkg, printWriter, architecture);
        }
    }

    private void saveInterfaceInSubPackage(Package pkg1, PrintWriter printWriter, Architecture architecture) {
        String halfTab = "  ";
        String tab = "    ";
        for (Package pkg : pkg1.getNestedPackages()) {
            //System.out.println("Package:"+pkg.getName());
            for (Interface inter : pkg.getAllInterfaces()) {
                //System.out.println("Interface:"+inter.getName());
                printWriter.write("\n" + tab + "<interface id=\"" + inter.getId() + "\" name=\"" + inter.getName() + "\" mandatory=\"" + inter.isMandatory() + "\" x=\"" + inter.getPosX() + "\" y=\"" + inter.getPosY() + "\" globalX=\"" + inter.getGlobalPosX() + "\" globalY=\"" + inter.getGlobalPosY() + "\" height=\"" + inter.getHeight() + "\" width=\"" + inter.getWidth() + "\" parent=\"" + pkg.getId() + "\">");

                for (Method m : inter.getMethods()) {
                    // <method id="METHOD#45" name="getmedia" return="TYPE#9" visibility="public" constructor="false" static="false" final="false" abstract="false">
                    TypeSmarty typeS = architecture.findTypeSMartyByName(m.getReturnType());
                    printWriter.write("\n" + tab + halfTab + "<method id=\"" + m.getId() + "\" name=\"" + m.getName() + "\" return=\"" + typeS.getId() + "\" visibility=\"" + m.getVisibility() + "\" constructor=\"" + m.isConstructor() + "\" static=\"" + m.isStatic() + "\" final=\"" + m.isFinal() + "\" abstract=\"" + m.isAbstract() + "\">");
                    //printWriter.write("\n\t"+halfTab+"<method id=\""+m.getId()+"\" name=\""+m.getName()+"\" return=\""+m.getReturnType()+"\" visibility=\""+m.getVisibility()+"\" constructor=\""+m.isConstructor()+"\" static=\""+m.isStatic()+"\" final=\""+m.isFinal()+"\" abstract=\""+m.isAbstract()+"\">");
                    for (ParameterMethod pm : m.getParameters()) {
                        typeS = architecture.findTypeSMartyByName(pm.getType());
                        printWriter.write("\n" + tab + tab + "<parameter type=\"" + typeS.getId() + "\" name=\"" + pm.getName() + "\"/>");
                        //printWriter.write("\n\t\t<parameter type=\""+pm.getType()+"\" name=\""+pm.getName()+"\"/>");
                    }
                    printWriter.write("\n" + tab + halfTab + "</method>");
                }
                printWriter.write("\n" + tab + "</interface>");
            }
            saveInterfaceInSubPackage(pkg, printWriter, architecture);
        }
    }
}
