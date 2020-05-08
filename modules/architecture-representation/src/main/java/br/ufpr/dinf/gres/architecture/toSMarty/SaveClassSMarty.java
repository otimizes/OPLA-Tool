package br.ufpr.dinf.gres.architecture.toSMarty;

import br.ufpr.dinf.gres.architecture.representation.*;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Package;

import java.io.PrintWriter;

public class SaveClassSMarty {

    public SaveClassSMarty(Architecture architecture, PrintWriter printWriter) {
        String halfTab = "  ";
        String tab = "    ";
        for (Class clazz : architecture.getClasses()) {
            printWriter.write("\n" + tab + "<class id=\"" + clazz.getId() + "\" name=\"" + clazz.getName() + "\" mandatory=\"" + clazz.isMandatory() + "\" x=\"" + clazz.getPosX() + "\" y=\"" + clazz.getPosY() + "\" globalX=\"" + clazz.getGlobalPosX() + "\" globalY=\"" + clazz.getGlobalPosY() + "\"  abstract=\"" + clazz.isAbstract() + "\" final=\"" + clazz.isAbstract() + "\" height=\"" + clazz.getHeight() + "\" width=\"" + clazz.getWidth() + "\" parent=\"\">");
            for (Attribute att : clazz.getAllAttributes()) {

                TypeSmarty typeS = architecture.findTypeSMartyByName(att.getType());
                if (att.getName().length() == 0) {
                    att.setName(typeS.getName());
                }
                printWriter.write("\n" + tab + halfTab + "<attribute id=\"" + att.getId() + "\" name=\"" + att.getName() + "\" type=\"" + typeS.getId() + "\" visibility=\"" + att.getVisibility() + "\" static=\"" + att.isStatic() + "\" final=\"" + att.isFinal() + "\"/>");
                //printWriter.write("\n\t"+halfTab+"<attribute id=\""+att.getId()+"\" name=\""+att.getName()+"\" type=\""+att.getType()+"\" visibility=\""+att.getVisibility()+"\" static=\""+att.isStatic()+"\" final=\""+att.isFinal()+"\"/>");

            }
            for (Method m : clazz.getAllMethods()) {
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
            printWriter.write("\n" + tab + "</class>");
        }
        for (Package pkg : architecture.getAllPackages()) {
            for (Class clazz : pkg.getAllClasses()) {

                printWriter.write("\n" + tab + "<class id=\"" + clazz.getId() + "\" name=\"" + clazz.getName() + "\" mandatory=\"" + clazz.isMandatory() + "\" x=\"" + clazz.getPosX() + "\" y=\"" + clazz.getPosY() + "\" globalX=\"" + clazz.getGlobalPosX() + "\" globalY=\"" + clazz.getGlobalPosY() + "\" abstract=\"" + clazz.isAbstract() + "\" final=\"" + clazz.isAbstract() + "\" height=\"" + clazz.getHeight() + "\" width=\"" + clazz.getWidth() + "\" parent=\"" + pkg.getId() + "\">");
                for (Attribute att : clazz.getAllAttributes()) {
                    TypeSmarty typeS = architecture.findTypeSMartyByName(att.getType());
                    if (att.getName().length() == 0) {
                        att.setName(typeS.getName());
                    }
                    printWriter.write("\n" + tab + halfTab + "<attribute id=\"" + att.getId() + "\" name=\"" + att.getName() + "\" type=\"" + typeS.getId() + "\" visibility=\"" + att.getVisibility() + "\" static=\"" + att.isStatic() + "\" final=\"" + att.isFinal() + "\"/>");
                    //printWriter.write("\n\t"+halfTab+"<attribute id=\""+att.getId()+"\" name=\""+att.getName()+"\" type=\""+att.getType()+"\" visibility=\""+att.getVisibility()+"\" static=\""+att.isStatic()+"\" final=\""+att.isFinal()+"\"/>");

                }
                for (Method m : clazz.getAllMethods()) {
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
                printWriter.write("\n" + tab + "</class>");
            }
            saveClassInSubpackage(pkg, printWriter, architecture);
        }
    }

    private void saveClassInSubpackage(Package pkg1, PrintWriter printWriter, Architecture architecture) {
        String halfTab = "  ";
        String tab = "    ";
        for (Package pkg : pkg1.getNestedPackages()) {
            for (Class clazz : pkg.getAllClasses()) {

                printWriter.write("\n" + tab + "<class id=\"" + clazz.getId() + "\" name=\"" + clazz.getName() + "\" mandatory=\"" + clazz.isMandatory() + "\" x=\"" + clazz.getPosX() + "\" y=\"" + clazz.getPosY() + "\" globalX=\"" + clazz.getGlobalPosX() + "\" globalY=\"" + clazz.getGlobalPosY() + "\" abstract=\"" + clazz.isAbstract() + "\" final=\"" + clazz.isAbstract() + "\" height=\"" + clazz.getHeight() + "\" width=\"" + clazz.getWidth() + "\" parent=\"" + pkg.getId() + "\">");
                for (Attribute att : clazz.getAllAttributes()) {
                    TypeSmarty typeS = architecture.findTypeSMartyByName(att.getType());

                    printWriter.write("\n" + tab + halfTab + "<attribute id=\"" + att.getId() + "\" name=\"" + att.getName() + "\" type=\"" + typeS.getId() + "\" visibility=\"" + att.getVisibility() + "\" static=\"" + att.isStatic() + "\" final=\"" + att.isFinal() + "\"/>");
                    //printWriter.write("\n\t"+halfTab+"<attribute id=\""+att.getId()+"\" name=\""+att.getName()+"\" type=\""+att.getType()+"\" visibility=\""+att.getVisibility()+"\" static=\""+att.isStatic()+"\" final=\""+att.isFinal()+"\"/>");

                }
                for (Method m : clazz.getAllMethods()) {
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
                printWriter.write("\n" + tab + "</class>");
            }
            saveClassInSubpackage(pkg, printWriter, architecture);
        }

    }



}
