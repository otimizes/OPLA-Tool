package arquitetura.main;

import arquitetura.io.ReaderConfig;
import arquitetura.representation.*;
import arquitetura.representation.Attribute;
import arquitetura.representation.Class;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.*;
import arquitetura.representation.relationship.Relationship;
import arquitetura.representation.TypeSmarty;
import arquitetura.touml.*;

import java.io.*;
import java.util.*;

public class GenerateArchitectureSMarty extends ArchitectureBase {


    private boolean typeSmartyExist(ArrayList<TypeSmarty> lst, String id){
        for(TypeSmarty ts: lst){
            if(ts.getId().equals(id))
                return true;
        }
        return  false;
    }

    private void createLogDir(){
        String directory = ReaderConfig.getDirExportTarget() + "/Logs";
        File file = new File(directory);
        file.mkdir();
    }


    public void verifyMandatory(Architecture architecture){
        for(Class clazz : architecture.getClasses()){
            if(clazz.getVariant() != null){
                if(clazz.getVariant().getVariantType().toLowerCase().equals("mandatory")){
                    clazz.setMandatory(true);
                }
                if(clazz.getVariant().getVariantType().toLowerCase().equals("optional")){
                    clazz.setMandatory(false);
                }
            }
        }
        for(Interface clazz : architecture.getInterfaces()){
            if(clazz.getVariant() != null){
                //System.out.println(clazz.getVariant().getVariantType());
                if(clazz.getVariant().getVariantType().toLowerCase().equals("mandatory")){
                    clazz.setMandatory(true);
                }
                if(clazz.getVariant().getVariantType().toLowerCase().equals("optional")){
                    clazz.setMandatory(false);
                }
            }
        }
        for(Package pkg : architecture.getAllPackages()){
            for(Class clazz : pkg.getAllClasses()){
                if(clazz.getVariant() != null){
                    if(clazz.getVariant().getVariantType().toLowerCase().equals("mandatory")){
                        clazz.setMandatory(true);
                    }
                    if(clazz.getVariant().getVariantType().toLowerCase().equals("optional")){
                        clazz.setMandatory(false);
                    }
                }
            }
            for(Interface clazz : pkg.getAllInterfaces()){
                if(clazz.getVariant() != null){
                    //System.out.println(clazz.getVariant().getVariantType());
                    if(clazz.getVariant().getVariantType().toLowerCase().equals("mandatory")){
                        clazz.setMandatory(true);

                    }
                    if(clazz.getVariant().getVariantType().toLowerCase().equals("optional")){
                        clazz.setMandatory(false);

                    }
                }
            }for(Package p :pkg.getNestedPackages()) {
                verifyMandatorySubPackage(p);
            }
        }
    }

    public void verifyMandatorySubPackage(Package pkg){
        for(Class clazz : pkg.getAllClasses()){
            if(clazz.getVariant() != null){
                if(clazz.getVariant().getVariantType().toLowerCase().equals("mandatory")){
                    clazz.setMandatory(true);
                }
                if(clazz.getVariant().getVariantType().toLowerCase().equals("optional")){
                    clazz.setMandatory(false);
                }
            }
        }
        for(Interface clazz : pkg.getAllInterfaces()){
            if(clazz.getVariant() != null){
                if(clazz.getVariant().getVariantType().toLowerCase().equals("mandatory")){
                    clazz.setMandatory(true);
                }
                if(clazz.getVariant().getVariantType().toLowerCase().equals("optional")){
                    clazz.setMandatory(false);
                }
            }
        }
        for(Package p :pkg.getNestedPackages()) {
            verifyMandatorySubPackage(p);
        }
    }


    public void generate(Architecture architecture, String name){

        /// criar pasta TEMP caso o nome venha com '/'
        if(name.contains("/")) {
            String directory = ReaderConfig.getDirExportTarget() + "/" + name.split("/")[0];
            File file = new File(directory);
            file.mkdir();
        }

        String path = ReaderConfig.getDirExportTarget()+"/"+name+".smty";

        createLogDir();
        String logPath = ReaderConfig.getDirExportTarget()+"/Logs/log_"+name+".txt";

        System.out.println(path);
        try {

            FileWriter fileWriter = new FileWriter(path);
            PrintWriter printWriter = new PrintWriter(fileWriter);


            String halfTab = "  ";
            String tab = "    ";

            // início project
            //printWriter.print("<project id=\""+architecture.getProjectID()+"\" name=\""+architecture.getProjectName()+"\" version=\""+architecture.getProjectVersion()+"\">");
            printWriter.print("<project id=\""+architecture.getProjectID()+"\" name=\""+architecture.getProjectName()+"\" version=\"1.0\">");

            //<type id="TYPE#29" path="java.util" name="Arrays" value="null" primitive="false" standard="true"/>

            // inic types
            printWriter.write("\n"+halfTab+"<types>");
            // add types
            if(architecture.getLstTypes().size() == 0){
                createTypesSMarty(architecture);
            }
            ArrayList<TypeSmarty> adicionedType = new ArrayList<>();

            for(TypeSmarty ts : architecture.getLstTypes()) {
                if (ts.isStandard()) {
                    printWriter.write("\n"+tab+"<type id=\"" + ts.getId() + "\" path=\"" + ts.getPath() + "\" name=\"" + ts.getName() + "\" value=\""+ts.getValue()+"\" primitive=\"" + ts.isPrimitive() + "\" standard=\"" + ts.isStandard() + "\"/>");
                    adicionedType.add(ts);
                } else {
                    if (architecture.findElementById(ts.getId()) != null) {
                        printWriter.write("\n"+tab+"<type id=\"" + ts.getId() + "\" path=\"" + ts.getPath() + "\" name=\"" + ts.getName() + "\" value=\""+ts.getValue()+"\" primitive=\"" + ts.isPrimitive() + "\" standard=\"" + ts.isStandard() + "\"/>");
                        adicionedType.add(ts);
                    }
                }
            }


            for(Class clazz : architecture.getClasses()){
                if(!typeSmartyExist(adicionedType, clazz.getId())){
                    printWriter.write("\n"+tab+"<type id=\"" + clazz.getId() + "\" path=\""+clazz.getName()+"\" name=\"" + clazz.getName() + "\" value=\"null\" primitive=\"false\" standard=\"false\"/>");
                }
            }
            for(Interface clazz : architecture.getInterfaces()){
                if(!typeSmartyExist(adicionedType, clazz.getId())){
                    printWriter.write("\n"+tab+"<type id=\"" + clazz.getId() + "\" path=\""+clazz.getName()+"\" name=\"" + clazz.getName() + "\" value=\"null\" primitive=\"false\" standard=\"false\"/>");
                }
            }
            for(Package pkg : architecture.getAllPackages()){
                for(Class clazz : pkg.getAllClasses()){
                    if(!typeSmartyExist(adicionedType, clazz.getId())){
                        printWriter.write("\n"+tab+"<type id=\"" + clazz.getId() + "\" path=\""+clazz.getName()+"\" name=\"" + clazz.getName() + "\" value=\"null\" primitive=\"false\" standard=\"false\"/>");
                    }
                }
                for(Interface clazz : pkg.getAllInterfaces()){
                    if(!typeSmartyExist(adicionedType, clazz.getId())){
                        printWriter.write("\n"+tab+"<type id=\"" + clazz.getId() + "\" path=\""+clazz.getName()+"\" name=\"" + clazz.getName() + "\" value=\"null\" primitive=\"false\" standard=\"false\"/>");
                    }
                }
                saveTypeSubPackage(pkg, adicionedType, printWriter);
            }
            printWriter.write("\n"+halfTab+"</types>");
            // end types

            if(architecture.getLstConcerns().size() == 0){
                createConcernList(architecture);
            }

            verifyMandatory(architecture);

            // inic stereotypes
            printWriter.write("\n"+halfTab+"<stereotypes>");
            // add stereotypes
            for(Concern ts : architecture.getLstConcerns()){
                //  <stereotype id="STEREOTYPE#10" name="albummanagement" primitive="false"/>
                printWriter.write("\n\t<stereotype id=\""+ts.getId()+"\" name=\""+ts.getName()+"\" primitive=\""+ts.getPrimitive()+"\"/>");
            }
            printWriter.write("\n"+halfTab+"</stereotypes>");
            // end stereotypes

            printWriter.write("\n"+halfTab+"<profile mandatory=\"STEREOTYPE#1\" optional=\"STEREOTYPE#2\" variationPoint=\"STEREOTYPE#3\" inclusive=\"STEREOTYPE#4\" exclusive=\"STEREOTYPE#5\" requires=\"STEREOTYPE#6\" mutex=\"STEREOTYPE#7\"/>");

            printWriter.write("\n"+halfTab+"<diagram id=\""+architecture.getDiagramID()+"\" name=\""+architecture.getDiagramName()+"\" type=\"Class\">");


            // resize and reorder elements
            arrangeArchitecture(architecture);

            // verify duplicate interfaces (duplicated interfaces cannot be open in SMarty Modeling)
            architecture.hasDuplicateInterface();

            ArrayList<Interface> duplicatedInterfaces = architecture.getDuplicateInterface();
            if(duplicatedInterfaces.size() > 0){
                for(Interface inter : duplicatedInterfaces) {
                    appendStrToFile(logPath, "\nInterface Duplicated: " +inter.getId() + " - " + inter.getName());
                }
            }

            // Salvar pacotes no arquivo
            for(Package pkg : architecture.getAllPackages()) {
                // <package id="PACKAGE#2" name="AlbumMgr" mandatory="true" x="300" y="2090" height="700" width="667"/>

                printWriter.write("\n"+tab+"<package id=\"" + pkg.getId() + "\" name=\"" + pkg.getName() + "\" mandatory=\"" + pkg.isMandatory() + "\" x=\"" + pkg.getPosX() + "\" y=\"" + pkg.getPosY() + "\" globalX=\""+pkg.getGlobalPosX()+"\" globalY=\""+pkg.getGlobalPosY()+"\" height=\"" + pkg.getHeight() + "\" width=\"" + pkg.getWidth() + "\"/>");

                saveSubPackage(pkg,printWriter);
            }



            for(Class clazz : architecture.getClasses()){
                printWriter.write("\n"+tab+"<class id=\""+clazz.getId()+"\" name=\""+clazz.getName()+"\" mandatory=\""+clazz.isMandatory()+"\" x=\""+clazz.getPosX()+"\" y=\""+clazz.getPosY()+"\" globalX=\""+clazz.getGlobalPosX()+"\" globalY=\""+clazz.getGlobalPosY()+"\"  abstract=\""+clazz.isAbstract()+"\" final=\""+clazz.isAbstract()+"\" height=\""+clazz.getHeight()+"\" width=\""+clazz.getWidth()+"\" parent=\"\">");
                for(Attribute att : clazz.getAllAttributes()){

                    TypeSmarty typeS = architecture.findTypeSMartyByName(att.getType());
                    if(att.getName().length() == 0){
                        att.setName(typeS.getName());
                    }
                    printWriter.write("\n"+tab+halfTab+"<attribute id=\""+att.getId()+"\" name=\""+att.getName()+"\" type=\""+typeS.getId()+"\" visibility=\""+att.getVisibility()+"\" static=\""+att.isStatic()+"\" final=\""+att.isFinal()+"\"/>");
                    //printWriter.write("\n\t"+halfTab+"<attribute id=\""+att.getId()+"\" name=\""+att.getName()+"\" type=\""+att.getType()+"\" visibility=\""+att.getVisibility()+"\" static=\""+att.isStatic()+"\" final=\""+att.isFinal()+"\"/>");

                }
                for(arquitetura.representation.Method m : clazz.getAllMethods()){
                    // <method id="METHOD#45" name="getmedia" return="TYPE#9" visibility="public" constructor="false" static="false" final="false" abstract="false">

                    TypeSmarty typeS = architecture.findReturnTypeSMartyByName(m.getReturnType());

                    printWriter.write("\n"+tab+halfTab+"<method id=\""+m.getId()+"\" name=\""+m.getName()+"\" return=\""+typeS.getId()+"\" visibility=\""+m.getVisibility()+"\" constructor=\""+m.isConstructor()+"\" static=\""+m.isStatic()+"\" final=\""+m.isFinal()+"\" abstract=\""+m.isAbstract()+"\">");
                    //printWriter.write("\n\t"+halfTab+"<method id=\""+m.getId()+"\" name=\""+m.getName()+"\" return=\""+m.getReturnType()+"\" visibility=\""+m.getVisibility()+"\" constructor=\""+m.isConstructor()+"\" static=\""+m.isStatic()+"\" final=\""+m.isFinal()+"\" abstract=\""+m.isAbstract()+"\">");
                    for(ParameterMethod pm : m.getParameters()){
                        if(pm.getName().length() == 0)
                            continue;
                        typeS = architecture.findTypeSMartyByName(pm.getType());
                        printWriter.write("\n"+tab+tab+"<parameter type=\""+typeS.getId()+"\" name=\""+pm.getName()+"\"/>");
                        //printWriter.write("\n\t\t<parameter type=\""+pm.getType()+"\" name=\""+pm.getName()+"\"/>");
                    }
                    printWriter.write("\n"+tab+halfTab+"</method>");
                }
                printWriter.write("\n"+tab+"</class>");
            }
            for(Package pkg : architecture.getAllPackages()){
                for(Class clazz : pkg.getAllClasses()){

                    printWriter.write("\n"+tab+"<class id=\""+clazz.getId()+"\" name=\""+clazz.getName()+"\" mandatory=\""+clazz.isMandatory()+"\" x=\""+clazz.getPosX()+"\" y=\""+clazz.getPosY()+"\" globalX=\""+clazz.getGlobalPosX()+"\" globalY=\""+clazz.getGlobalPosY()+"\" abstract=\""+clazz.isAbstract()+"\" final=\""+clazz.isAbstract()+"\" height=\""+clazz.getHeight()+"\" width=\""+clazz.getWidth()+"\" parent=\""+pkg.getId()+"\">");
                    for(Attribute att : clazz.getAllAttributes()){
                        TypeSmarty typeS = architecture.findTypeSMartyByName(att.getType());
                        if(att.getName().length() == 0){
                            att.setName(typeS.getName());
                        }
                        printWriter.write("\n"+tab+halfTab+"<attribute id=\""+att.getId()+"\" name=\""+att.getName()+"\" type=\""+typeS.getId()+"\" visibility=\""+att.getVisibility()+"\" static=\""+att.isStatic()+"\" final=\""+att.isFinal()+"\"/>");
                        //printWriter.write("\n\t"+halfTab+"<attribute id=\""+att.getId()+"\" name=\""+att.getName()+"\" type=\""+att.getType()+"\" visibility=\""+att.getVisibility()+"\" static=\""+att.isStatic()+"\" final=\""+att.isFinal()+"\"/>");

                    }
                    for(arquitetura.representation.Method m : clazz.getAllMethods()){
                        // <method id="METHOD#45" name="getmedia" return="TYPE#9" visibility="public" constructor="false" static="false" final="false" abstract="false">
                        TypeSmarty typeS = architecture.findReturnTypeSMartyByName(m.getReturnType());
                        printWriter.write("\n"+tab+halfTab+"<method id=\""+m.getId()+"\" name=\""+m.getName()+"\" return=\""+typeS.getId()+"\" visibility=\""+m.getVisibility()+"\" constructor=\""+m.isConstructor()+"\" static=\""+m.isStatic()+"\" final=\""+m.isFinal()+"\" abstract=\""+m.isAbstract()+"\">");
                        //printWriter.write("\n\t"+halfTab+"<method id=\""+m.getId()+"\" name=\""+m.getName()+"\" return=\""+m.getReturnType()+"\" visibility=\""+m.getVisibility()+"\" constructor=\""+m.isConstructor()+"\" static=\""+m.isStatic()+"\" final=\""+m.isFinal()+"\" abstract=\""+m.isAbstract()+"\">");
                        for(ParameterMethod pm : m.getParameters()){
                            if(pm.getName().length() == 0)
                                continue;
                            typeS = architecture.findTypeSMartyByName(pm.getType());
                            printWriter.write("\n"+tab+tab+"<parameter type=\""+typeS.getId()+"\" name=\""+pm.getName()+"\"/>");
                            //printWriter.write("\n\t\t<parameter type=\""+pm.getType()+"\" name=\""+pm.getName()+"\"/>");
                        }
                        printWriter.write("\n"+tab+halfTab+"</method>");
                    }
                    printWriter.write("\n"+tab+"</class>");
                }
                saveSubPackageClass(pkg,printWriter,architecture);
            }




            //// import interface
            //<interface id="INTERFACE#3" name="IManageAlbum" mandatory="true" x="357" y="129" height="216" width="280" parent="PACKAGE#3">
            for(Interface clazz : architecture.getInterfaces()){

                printWriter.write("\n"+tab+"<interface id=\""+clazz.getId()+"\" name=\""+clazz.getName()+"\" mandatory=\""+clazz.isMandatory()+"\" x=\""+clazz.getPosX()+"\" y=\""+clazz.getPosY()+"\" globalX=\""+clazz.getGlobalPosX()+"\" globalY=\""+clazz.getGlobalPosY()+"\" height=\""+clazz.getHeight()+"\" width=\""+clazz.getWidth()+"\" parent=\"\">");
                for(Attribute att : clazz.getAllAttributes()){
                    TypeSmarty typeS = architecture.findTypeSMartyByName(att.getType());
                    if(att.getName().length() == 0){
                        att.setName(typeS.getName());
                    }
                    printWriter.write("\n"+tab+halfTab+"<attribute id=\""+att.getId()+"\" name=\""+att.getName()+"\" type=\""+typeS.getId()+"\" visibility=\""+att.getVisibility()+"\" static=\""+att.isStatic()+"\" final=\""+att.isFinal()+"\"/>");
                    //printWriter.write("\n\t"+halfTab+"<attribute id=\""+att.getId()+"\" name=\""+att.getName()+"\" type=\""+att.getType()+"\" visibility=\""+att.getVisibility()+"\" static=\""+att.isStatic()+"\" final=\""+att.isFinal()+"\"/>");

                }
                for(arquitetura.representation.Method m : clazz.getMethods()){
                    // <method id="METHOD#45" name="getmedia" return="TYPE#9" visibility="public" constructor="false" static="false" final="false" abstract="false">
                    TypeSmarty typeS = architecture.findReturnTypeSMartyByName(m.getReturnType());
                    printWriter.write("\n"+tab+halfTab+"<method id=\""+m.getId()+"\" name=\""+m.getName()+"\" return=\""+typeS.getId()+"\" visibility=\""+m.getVisibility()+"\" constructor=\""+m.isConstructor()+"\" static=\""+m.isStatic()+"\" final=\""+m.isFinal()+"\" abstract=\""+m.isAbstract()+"\">");
                    //printWriter.write("\n\t"+halfTab+"<method id=\""+m.getId()+"\" name=\""+m.getName()+"\" return=\""+m.getReturnType()+"\" visibility=\""+m.getVisibility()+"\" constructor=\""+m.isConstructor()+"\" static=\""+m.isStatic()+"\" final=\""+m.isFinal()+"\" abstract=\""+m.isAbstract()+"\">");
                    for(ParameterMethod pm : m.getParameters()){
                        if(pm.getName().length() == 0)
                            continue;
                        typeS = architecture.findTypeSMartyByName(pm.getType());
                        printWriter.write("\n"+tab+tab+"<parameter type=\""+typeS.getId()+"\" name=\""+pm.getName()+"\"/>");
                        //printWriter.write("\n\t\t<parameter type=\""+pm.getType()+"\" name=\""+pm.getName()+"\"/>");
                    }
                    printWriter.write("\n"+tab+halfTab+"</method>");
                }
                printWriter.write("\n"+tab+"</interface>");
            }
            for(Package pkg : architecture.getAllPackages()){
                //System.out.println("Package:"+pkg.getName());
                for(Interface clazz : pkg.getAllInterfaces()){
                    //System.out.println("Interface:"+clazz.getName());
                    printWriter.write("\n"+tab+"<interface id=\""+clazz.getId()+"\" name=\""+clazz.getName()+"\" mandatory=\""+clazz.isMandatory()+"\" x=\""+clazz.getPosX()+"\" y=\""+clazz.getPosY()+"\" globalX=\""+clazz.getGlobalPosX()+"\" globalY=\""+clazz.getGlobalPosY()+"\" height=\""+clazz.getHeight()+"\" width=\""+clazz.getWidth()+"\" parent=\""+pkg.getId()+"\">");
                    for(Attribute att : clazz.getAllAttributes()){
                        TypeSmarty typeS = architecture.findTypeSMartyByName(att.getType());
                        if(att.getName().length() == 0){
                            att.setName(typeS.getName());
                        }
                        printWriter.write("\n"+tab+halfTab+"<attribute id=\""+att.getId()+"\" name=\""+att.getName()+"\" type=\""+typeS.getId()+"\" visibility=\""+att.getVisibility()+"\" static=\""+att.isStatic()+"\" final=\""+att.isFinal()+"\"/>");
                        //printWriter.write("\n\t"+halfTab+"<attribute id=\""+att.getId()+"\" name=\""+att.getName()+"\" type=\""+att.getType()+"\" visibility=\""+att.getVisibility()+"\" static=\""+att.isStatic()+"\" final=\""+att.isFinal()+"\"/>");

                    }
                    for(arquitetura.representation.Method m : clazz.getMethods()){
                        // <method id="METHOD#45" name="getmedia" return="TYPE#9" visibility="public" constructor="false" static="false" final="false" abstract="false">
                        TypeSmarty typeS = architecture.findReturnTypeSMartyByName(m.getReturnType());
                        printWriter.write("\n"+tab+halfTab+"<method id=\""+m.getId()+"\" name=\""+m.getName()+"\" return=\""+typeS.getId()+"\" visibility=\""+m.getVisibility()+"\" constructor=\""+m.isConstructor()+"\" static=\""+m.isStatic()+"\" final=\""+m.isFinal()+"\" abstract=\""+m.isAbstract()+"\">");
                        //printWriter.write("\n\t"+halfTab+"<method id=\""+m.getId()+"\" name=\""+m.getName()+"\" return=\""+m.getReturnType()+"\" visibility=\""+m.getVisibility()+"\" constructor=\""+m.isConstructor()+"\" static=\""+m.isStatic()+"\" final=\""+m.isFinal()+"\" abstract=\""+m.isAbstract()+"\">");
                        for(ParameterMethod pm : m.getParameters()){
                            if(pm.getName().length() == 0)
                                continue;
                            typeS = architecture.findTypeSMartyByName(pm.getType());
                            printWriter.write("\n"+tab+tab+"<parameter type=\""+typeS.getId()+"\" name=\""+pm.getName()+"\"/>");
                            //printWriter.write("\n\t\t<parameter type=\""+pm.getType()+"\" name=\""+pm.getName()+"\"/>");
                        }
                        printWriter.write("\n"+tab+halfTab+"</method>");
                    }
                    printWriter.write("\n"+tab+"</interface>");
                }
                saveSubPackageInterface(pkg,printWriter,architecture);
            }

            int id_rel = 1;

            for(Relationship r : architecture.getRelationshipHolder().getAllRelationships()){
                if(r instanceof AssociationRelationship){
                    //<association name="usermgr_user_1" category="aggregation" direction="true" source="CLASS#1" sourceName="UserMgr" sourceMin="1" sourceMax="2147483647" sourceX="140" sourceY="210" target="CLASS#2" targetName="User" targetMin="1" targetMax="2147483647" targetX="224" targetY="458"/>
                    AssociationRelationship ar = (AssociationRelationship)r;
                    AssociationEnd ae1 = ar.getParticipants().get(0);
                    AssociationEnd ae2 = ar.getParticipants().get(1);

                    Element e1 = architecture.findElementByName2(ae1.getCLSClass().getName());
                    if(e1 == null) {
                        System.out.println("Discart Association 1:"+ae1.getCLSClass().getId());
                        appendStrToFile(logPath, "\n\nDiscart Association "+ar.getId()+":");
                        appendStrToFile(logPath,"\nElement 1: " + ae1.getCLSClass().getId() + " - " + ae1.getCLSClass().getName() + " not found");
                        appendStrToFile(logPath,"\nElement 2: " + ae2.getCLSClass().getId() + " - " + ae2.getCLSClass().getName());
                        continue;
                    }
                    Element e2 = architecture.findElementByName2(ae2.getCLSClass().getName());
                    if(e2 == null) {
                        appendStrToFile(logPath, "\n\nDiscart Association "+ar.getId()+":");
                        appendStrToFile(logPath,"\nElement 1: " + ae1.getCLSClass().getId() + " - " + ae1.getCLSClass().getName());
                        appendStrToFile(logPath,"\nElement 2: " + ae2.getCLSClass().getId() + " - " + ae2.getCLSClass().getName() + " not found");
                        //System.out.println("Discart Association 2:"+ae2.getCLSClass().getId());
                        continue;
                    }

                    Multiplicity mult1 = ae1.getMultiplicity();
                    Multiplicity mult2 = ae2.getMultiplicity();
                    String category = "normal";
                    if(ae1.getAggregation().equals("shared")){
                        category = "aggregation";
                    }
                    if(ae1.getAggregation().equals("composite"))
                        category = "composition";
                    if(mult1 == null){
                        //System.out.println("\n\nMult1 == null");
                        mult1 = new Multiplicity("1","1");
                    }
                    if(mult2 == null){
                        //System.out.println("\n\nMult2 == null");
                        mult2 = new Multiplicity("1","1");
                    }

                    /*
                    Random rnd = new Random();
                    ae1.setPosX(""+(10+rnd.nextInt(100)));
                    ae1.setPosY(""+(10+rnd.nextInt(100)));
                    ae2.setPosX(""+(10+rnd.nextInt(100)));
                    ae2.setPosY(""+(10+rnd.nextInt(100)));
                     */
                    Random rnd = new Random();
                    // posição fica no centro da classe
                    //System.out.println(ae1.getCLSClass().getId());
                    //System.out.println(ae1.getCLSClass().getGlobalPosX());
                    //System.out.println(Integer.parseInt(ae1.getCLSClass().getGlobalPosX()));


                    int new_x = Integer.parseInt(ae1.getCLSClass().getGlobalPosX()) + Integer.parseInt(ae1.getCLSClass().getWidth())/2;
                    int new_y = Integer.parseInt(ae1.getCLSClass().getGlobalPosY()) + Integer.parseInt(ae1.getCLSClass().getHeight())/2;
                    ae1.setPosX(""+(new_x+rnd.nextInt(10)));
                    ae1.setPosY(""+(new_y+rnd.nextInt(10)));
                    new_x = Integer.parseInt(ae2.getCLSClass().getGlobalPosX()) + Integer.parseInt(ae2.getCLSClass().getWidth())/2;
                    new_y = Integer.parseInt(ae2.getCLSClass().getGlobalPosY()) + Integer.parseInt(ae2.getCLSClass().getHeight())/2;
                    ae2.setPosX(""+(new_x+rnd.nextInt(10)));
                    ae2.setPosY(""+(new_y+rnd.nextInt(10)));


                    if(ae1.getName().length()==0){
                        ae1.setName(ae1.getCLSClass().getName());
                    }if(ae2.getName().length() == 0){
                        ae2.setName(ae2.getCLSClass().getName());
                    }


                    if(ae1.getVisibility().length() == 0)
                        ae1.setVisibility("private");
                    if(ae2.getVisibility().length() == 0)
                        ae2.setVisibility("private");
                    if(ar.getId().length()==0) {
                        boolean existID = true;
                        while (existID) {
                            existID = false;
                            for (Relationship r2 : architecture.getRelationshipHolder().getAllRelationships()) {
                                if(r2.getId().equals("ASSOCIATION#" + id_rel)){
                                    id_rel++;
                                    existID=true;
                                    break;
                                }
                            }
                        }
                        ar.setId("ASSOCIATION#" + id_rel);
                        id_rel++;
                    }
                    //printWriter.write("\n"+tab+"<association name=\""+ar.getName()+"\" category=\""+category+"\" direction=\""+ae1.isNavigable()+"\" source=\""+e1.getId()+"\" sourceName=\""+ae1.getName()+"\" sourceMin=\""+mult1.getLowerValue()+"\" sourceMax=\""+mult1.getUpperValue()+"\" sourceX=\""+ae1.getPosX()+"\" sourceY=\""+ae1.getPosY()+"\" target=\""+e2.getId()+"\" targetName=\""+ae2.getName()+"\" targetMin=\""+mult2.getLowerValue()+"\" targetMax=\""+mult2.getUpperValue()+"\" targetX=\""+ae2.getPosX()+"\" targetY=\""+ae2.getPosY()+"\"/>");
                    printWriter.write("\n"+tab+"<association id=\""+ar.getId()+"\" name=\""+ar.getName()+"\" category=\""+category+"\" direction=\""+ae1.isNavigable()+"\">");
                    printWriter.write("\n"+tab+halfTab+"<source entity=\""+e1.getId()+"\" sourceVisibility=\""+ae1.getVisibility()+"\" sourceName=\""+ae1.getName()+"\" sourceMin=\""+mult1.getLowerValue()+"\" sourceMax=\""+mult1.getUpperValue()+"\" sourceX=\""+ae1.getPosX()+"\" sourceY=\""+ae1.getPosY()+"\"/>");
                    printWriter.write("\n"+tab+halfTab+"<target entity=\""+e2.getId()+"\" targetVisibility=\""+ae2.getVisibility()+"\" targetName=\""+ae2.getName()+"\" targetMin=\""+mult2.getLowerValue()+"\" targetMax=\""+mult2.getUpperValue()+"\" targetX=\""+ae2.getPosX()+"\" targetY=\""+ae2.getPosY()+"\"/>");
                    printWriter.write("\n"+tab+"</association>");

                }
            }
            for(Relationship r : architecture.getRelationshipHolder().getAllRelationships()){
                if(r instanceof DependencyRelationship){
                    // <dependency source="CLASS#12" target="INTERFACE#10"/>
                    DependencyRelationship dr = (DependencyRelationship)r;

                    Element e1 = architecture.findElementByName2(dr.getClient().getName());
                    if(e1 == null) {
                        System.out.println("Discart Dep 1:"+dr.getClient().getId());
                        appendStrToFile(logPath, "\n\nDiscart Dependency "+dr.getId()+":");
                        appendStrToFile(logPath,"\nSupplier: " + dr.getSupplier().getId() + " - " + dr.getSupplier().getName());
                        appendStrToFile(logPath,"\nClient: " + dr.getClient().getId() + " - " + dr.getClient().getName() +" not found");
                        continue;
                    }
                    Element e2 = architecture.findElementByName2(dr.getSupplier().getName());
                    if(e2 == null) {
                        System.out.println("Discart Dep 2:" + dr.getSupplier().getId());
                        appendStrToFile(logPath, "\n\nDiscart Dependency "+dr.getId()+":");
                        appendStrToFile(logPath,"\nSupplier: " + dr.getSupplier().getId() + " - " + dr.getSupplier().getName() + " not found");
                        appendStrToFile(logPath,"\nClient: " + dr.getClient().getId() + " - " + dr.getClient().getName());
                        continue;
                    }
                    if(dr.getId().length()==0) {
                        boolean existID = true;
                        while (existID) {
                            existID = false;
                            for (Relationship r2 : architecture.getRelationshipHolder().getAllRelationships()) {
                                if(r2.getId().equals("DEPENDENCY#" + id_rel)){
                                    id_rel++;
                                    existID=true;
                                    break;
                                }
                            }
                        }
                        dr.setId("DEPENDENCY#" + id_rel);
                        id_rel++;
                    }
                    printWriter.write("\n"+tab+"<dependency id=\""+dr.getId()+"\" source=\""+e1.getId()+"\" target=\""+e2.getId()+"\">");
                    printWriter.write("\n"+tab+"</dependency>");
                }
            }
            ///// AbstractionRelationship salvo como Dependency (SMarty Modeling não tem abstraction)
            for(Relationship r : architecture.getRelationshipHolder().getAllRelationships()){
                if(r instanceof AbstractionRelationship){
                    // <dependency source="CLASS#12" target="INTERFACE#10"/>
                    AbstractionRelationship dr = (AbstractionRelationship) r;

                    Element e1 = architecture.findElementByName2(dr.getClient().getName());
                    if(e1 == null) {
                        System.out.println("Discart Abstraction 1:"+dr.getClient().getId());
                        appendStrToFile(logPath, "\n\nDiscart Abstraction "+dr.getId()+":");
                        appendStrToFile(logPath,"\nSupplier: " + dr.getSupplier().getId() + " - " + dr.getSupplier().getName());
                        appendStrToFile(logPath,"\nClient: " + dr.getClient().getId() + " - " + dr.getClient().getName() +" not found");
                        continue;
                    }
                    Element e2 = architecture.findElementByName2(dr.getSupplier().getName());
                    if(e2 == null) {
                        System.out.println("Discart Abstraction 2:" + dr.getSupplier().getId());
                        appendStrToFile(logPath, "\n\nDiscart Abstraction "+dr.getId()+":");
                        appendStrToFile(logPath,"\nSupplier: " + dr.getSupplier().getId() + " - " + dr.getSupplier().getName() + " not found");
                        appendStrToFile(logPath,"\nClient: " + dr.getClient().getId() + " - " + dr.getClient().getName());
                        continue;
                    }
                    if(dr.getId().length()==0) {
                        boolean existID = true;
                        while (existID) {
                            existID = false;
                            for (Relationship r2 : architecture.getRelationshipHolder().getAllRelationships()) {
                                // if(r2.getId().equals("ABSTRACTION#" + id_rel)){
                                if(r2.getId().equals("DEPENDENCY#" + id_rel)){
                                    id_rel++;
                                    existID=true;
                                    break;
                                }
                            }
                        }
                        //dr.setId("ABSTRACTION#" + id_rel);
                        dr.setId("DEPENDENCY#" + id_rel);
                        id_rel++;
                    }
                    printWriter.write("\n"+tab+"<dependency id=\""+dr.getId()+"\" source=\""+e1.getId()+"\" target=\""+e2.getId()+"\">");
                    printWriter.write("\n"+tab+"</dependency>");

                    /*
                    printWriter.write("\n"+tab+"<abstraction id=\""+dr.getId()+"\" source=\""+e1.getId()+"\" target=\""+e2.getId()+"\">");
                    printWriter.write("\n"+tab+"</abstraction>");

                     */


                    appendStrToFile(logPath, "\n\nAbstraction "+dr.getId()+" salvo como Dependency");
                    appendStrToFile(logPath,"\nSupplier: " + dr.getSupplier().getId() + " - " + dr.getSupplier().getName());
                    appendStrToFile(logPath,"\nClient: " + dr.getClient().getId() + " - " + dr.getClient().getName());
                    continue;

                }
            }

            // usage relationship salvo como dependency (SMarty Modeling não tem usage)
            for(Relationship r : architecture.getRelationshipHolder().getAllRelationships()){
                if(r instanceof UsageRelationship){
                    // <dependency source="CLASS#12" target="INTERFACE#10"/>
                    UsageRelationship dr = (UsageRelationship) r;

                    Element e1 = architecture.findElementByName2(dr.getClient().getName());
                    if(e1 == null) {
                        System.out.println("Discart Usage 1:"+dr.getClient().getId());
                        appendStrToFile(logPath, "\n\nDiscart Usage "+dr.getId()+":");
                        appendStrToFile(logPath,"\nSupplier: " + dr.getSupplier().getId() + " - " + dr.getSupplier().getName());
                        appendStrToFile(logPath,"\nClient: " + dr.getClient().getId() + " - " + dr.getClient().getName() +" not found");
                        continue;
                    }
                    Element e2 = architecture.findElementByName2(dr.getSupplier().getName());
                    if(e2 == null) {
                        System.out.println("Discart USAGE 2:" + dr.getSupplier().getId());
                        appendStrToFile(logPath, "\n\nDiscart Usage "+dr.getId()+":");
                        appendStrToFile(logPath,"\nSupplier: " + dr.getSupplier().getId() + " - " + dr.getSupplier().getName() + " not found");
                        appendStrToFile(logPath,"\nClient: " + dr.getClient().getId() + " - " + dr.getClient().getName());
                        continue;
                    }
                    if(dr.getId().length()==0) {
                        boolean existID = true;
                        while (existID) {
                            existID = false;
                            for (Relationship r2 : architecture.getRelationshipHolder().getAllRelationships()) {
                                // if(r2.getId().equals("USAGE#" + id_rel)){
                                if(r2.getId().equals("DEPENDENCY#" + id_rel)){
                                    id_rel++;
                                    existID=true;
                                    break;
                                }
                            }
                        }
                        //dr.setId("USAGE#" + id_rel);
                        dr.setId("DEPENDENCY#" + id_rel);
                        id_rel++;
                    }
                    printWriter.write("\n"+tab+"<dependency id=\""+dr.getId()+"\" source=\""+e1.getId()+"\" target=\""+e2.getId()+"\">");
                    printWriter.write("\n"+tab+"</dependency>");

                    /*
                    printWriter.write("\n"+tab+"<usage id=\""+dr.getId()+"\" source=\""+e1.getId()+"\" target=\""+e2.getId()+"\">");
                    printWriter.write("\n"+tab+"</usage>");

                     */


                    appendStrToFile(logPath, "\n\nUsage "+dr.getId()+" salvo como Dependency");
                    appendStrToFile(logPath,"\nSupplier: " + dr.getSupplier().getId() + " - " + dr.getSupplier().getName());
                    appendStrToFile(logPath,"\nClient: " + dr.getClient().getId() + " - " + dr.getClient().getName());
                    continue;

                }
            }



            for(Relationship r : architecture.getRelationshipHolder().getAllRelationships()){
                if(r instanceof GeneralizationRelationship){
                    // <generalization source="CLASS#7" target="CLASS#3"/>
                    GeneralizationRelationship gr = (GeneralizationRelationship)r;


                    Element e1 = architecture.findElementByName2(gr.getChild().getName());
                    if(e1 == null) {
                        System.out.println("Discart Gen 1:"+gr.getChild().getId());

                        appendStrToFile(logPath, "\n\nDiscart Generealization "+gr.getId()+":");
                        appendStrToFile(logPath,"\nParent: " + gr.getParent().getId() + " - " + gr.getParent().getName());
                        appendStrToFile(logPath,"\nChild: " + gr.getChild().getId() + " - " + gr.getChild().getName() + " not found");
                        continue;
                    }
                    Element e2 = architecture.findElementByName2(gr.getParent().getName());
                    if(e2 == null) {
                        System.out.println("Discart Gen 2:" + gr.getParent().getId());
                        appendStrToFile(logPath, "\n\nDiscart Genelarization "+gr.getId()+":");
                        appendStrToFile(logPath,"\nParent: " + gr.getParent().getId() + " - " + gr.getParent().getName() + " not found");
                        appendStrToFile(logPath,"\nChild: " + gr.getChild().getId() + " - " + gr.getChild().getName());
                        continue;
                    }
                    if(gr.getId().length()==0) {
                        boolean existID = true;
                        while (existID) {
                            existID = false;
                            for (Relationship r2 : architecture.getRelationshipHolder().getAllRelationships()) {
                                if(r2.getId().equals("GENERALIZATION#" + id_rel)){
                                    id_rel++;
                                    existID=true;
                                    break;
                                }
                            }
                        }
                        gr.setId("GENERALIZATION#" + id_rel);
                        id_rel++;
                    }
                    printWriter.write("\n"+tab+"<generalization id=\""+gr.getId()+"\" source=\""+e1.getId()+"\" target=\""+e2.getId()+"\">");
                    printWriter.write("\n"+tab+"</generalization>");

                }
            }
            for(Relationship r : architecture.getRelationshipHolder().getAllRelationships()){
                if(r instanceof RealizationRelationship){
                    //<realization class="CLASS#6" interface="INTERFACE#5"/>
                    RealizationRelationship rr = (RealizationRelationship)r;

                    Element e1 = architecture.findElementByName2(rr.getSupplier().getName());
                    if(e1 == null) {
                        System.out.println("Discart Real 1:"+rr.getSupplier().getId());
                        appendStrToFile(logPath, "\n\nDiscart Realization "+rr.getId()+":");
                        appendStrToFile(logPath,"\nClient: " + rr.getClient().getId() + " - " + rr.getClient().getName());
                        appendStrToFile(logPath,"\nSupplier: " + rr.getSupplier().getId() + " - " + rr.getSupplier().getName() + " not found");
                        continue;
                    }
                    Element e2 = architecture.findElementByName2(rr.getClient().getName());
                    if(e2 == null) {
                        System.out.println("Discart Real 2:"+rr.getClient().getId());
                        appendStrToFile(logPath, "\n\nDiscart Realization "+rr.getId()+":");
                        appendStrToFile(logPath,"\nClint: " + rr.getClient().getId() + " - " + rr.getClient().getName() + " not found");
                        appendStrToFile(logPath,"\nSupplier: " + rr.getSupplier().getId() + " - " + rr.getSupplier().getName());
                        continue;
                    }
                    if(rr.getId().length()==0) {
                        boolean existID = true;
                        while (existID) {
                            existID = false;
                            for (Relationship r2 : architecture.getRelationshipHolder().getAllRelationships()) {
                                if(r2.getId().equals("REALIZATION#" + id_rel)){
                                    id_rel++;
                                    existID=true;
                                    break;
                                }
                            }
                        }
                        rr.setId("REALIZATION#" + id_rel);
                        id_rel++;
                    }
                    printWriter.write("\n"+tab+"<realization id=\""+rr.getId()+"\" class=\""+e2.getId()+"\" interface=\""+e1.getId()+"\">");
                    printWriter.write("\n"+tab+"</realization>");
                }
            }

            for(Relationship r : architecture.getRelationshipHolder().getAllRelationships()){
                if(r instanceof RequiresRelationship){
                    // <dependency source="CLASS#12" target="INTERFACE#10"/>
                    RequiresRelationship dr = (RequiresRelationship) r;

                    Element e1 = architecture.findElementByName2(dr.getClient().getName());
                    if(e1 == null) {
                        System.out.println("Discart Req 1:"+dr.getClient().getId());
                        appendStrToFile(logPath, "\n\nDiscart Requires "+dr.getId()+":");
                        appendStrToFile(logPath,"\nSupplier: " + dr.getSupplier().getId() + " - " + dr.getSupplier().getName());
                        appendStrToFile(logPath,"\nClient: " + dr.getClient().getId() + " - " + dr.getClient().getName() +" not found");
                        continue;
                    }
                    Element e2 = architecture.findElementByName2(dr.getSupplier().getName());
                    if(e2 == null) {
                        System.out.println("Discart Req 2:" + dr.getSupplier().getId());
                        appendStrToFile(logPath, "\n\nDiscart Requires "+dr.getId()+":");
                        appendStrToFile(logPath,"\nSupplier: " + dr.getSupplier().getId() + " - " + dr.getSupplier().getName() + " not found");
                        appendStrToFile(logPath,"\nClient: " + dr.getClient().getId() + " - " + dr.getClient().getName());
                        continue;
                    }
                    if(dr.getId().length()==0) {
                        boolean existID = true;
                        while (existID) {
                            existID = false;
                            for (Relationship r2 : architecture.getRelationshipHolder().getAllRelationships()) {
                                if(r2.getId().equals("REQUIRES#" + id_rel)){
                                    id_rel++;
                                    existID=true;
                                    break;
                                }
                            }
                        }
                        dr.setId("REQUIRES#" + id_rel);
                        id_rel++;
                    }
                    printWriter.write("\n"+tab+"<requires id=\""+dr.getId()+"\" source=\""+e1.getId()+"\" target=\""+e2.getId()+"\">");
                    printWriter.write("\n"+tab+"</requires>");
                }
            }


            //fim relacionamento


            for(Package pkg : architecture.getAllPackages())
                saveReference(pkg, printWriter);


            // variabilidade
            /*
            <variability id="VARIABILITY#1" name="ManaingMedia" variationPoint="CLASS#3" constraint="Inclusive" bindingTime="DESIGN_TIME" allowsBindingVar="true" min="1" max="3">
              <variant id="CLASS#8"/>
              <variant id="CLASS#9"/>
              <variant id="CLASS#7"/>
            </variability>
             */


            //System.out.println("Var1:"+architecture.getAllVariabilities());
            //System.out.println("Var2:"+architecture.getAllVariationPoints());
            //System.out.println("Var3:"+architecture.getAllVariants());

            ArrayList<String> discartedVariability = new ArrayList<>();

            int varID = 0;
            String varIDName; // = "VARIABILITY#" + varID;

            for(Variability variability : architecture.getAllVariabilities()){
                //System.out.println(variability.getVariants().get(0).getVariantType());
                VariationPoint vp = variability.getVariationPoint();
                if(vp==null) {
                    appendStrToFile(logPath, "\n\nDiscart Variability "+variability.getId()+" - "+variability.getName()+":");
                    appendStrToFile(logPath, "\nType: "+variability.getVariants().get(0).getVariantType()+", Binding Time: "+variability.getBindingTime()+", Allow Binding Var:"+variability.allowAddingVar());
                    appendStrToFile(logPath, "\nVariation Point NULL");
                    appendStrToFile(logPath, "\nVariabilidade do tipo optional será convertido para stereótipo optional");
                    appendStrToFile(logPath, "\nVariabilidade do tipo mandatory será convertido para stereótipo mandatory");
                    continue;
                }
                if(vp.getVariationPointElement()==null) {
                    appendStrToFile(logPath, "\n\nDiscart Variability "+variability.getId()+" - "+variability.getName()+":");
                    appendStrToFile(logPath, "\nType: "+variability.getVariants().get(0).getVariantType()+", Binding Time: "+variability.getBindingTime()+", Allow Binding Var:"+variability.allowAddingVar());
                    appendStrToFile(logPath, "\nVariation Point NULL");
                    appendStrToFile(logPath, "\nVariabilidade do tipo optional será convertido para stereótipo optional");
                    appendStrToFile(logPath, "\nVariabilidade do tipo mandatory será convertido para stereótipo mandatory");
                    continue;
                }
                if(architecture.findElementById(vp.getVariationPointElement().getId())==null) {

                    appendStrToFile(logPath, "\n\nDiscart Variability "+variability.getId()+" - "+variability.getName()+":");
                    appendStrToFile(logPath, "\nType: "+variability.getVariants().get(0).getVariantType()+", Binding Time: "+variability.getBindingTime()+", Allow Binding Var:"+variability.allowAddingVar());
                    appendStrToFile(logPath, "\nVariation Point: "+vp.getVariationPointElement().getId()+" - "+vp.getVariationPointElement().getName());
                    for(Variant v : variability.getVariants()){
                        appendStrToFile(logPath, "\nVariant: "+v.getVariantElement().getId()+" - "+v.getVariantElement().getName());
                    }
                    appendStrToFile(logPath,"\nVariation Point " + vp.getVariationPointElement().getId() + " - " + vp.getVariationPointElement().getName() + " not found");
                    discartedVariability.add(variability.getId());
                    continue;
                }
                boolean discart = false;
                for(Variant v : variability.getVariants()){
                    if(architecture.findElementById(v.getVariantElement().getId())==null) {
                        discart = true;
                        appendStrToFile(logPath, "\n\nDiscart Variability "+variability.getId()+" - "+variability.getName()+":");
                        appendStrToFile(logPath, "\nType: "+variability.getVariants().get(0).getVariantType()+", Binding Time: "+variability.getBindingTime()+", Allow Binding Var:"+variability.allowAddingVar());
                        appendStrToFile(logPath, "\nVariation Point: "+vp.getVariationPointElement().getId()+" - "+vp.getVariationPointElement().getName());
                        for(Variant v2 : variability.getVariants()){
                            appendStrToFile(logPath, "\nVariant: "+v2.getVariantElement().getId()+" - "+v2.getVariantElement().getName());
                        }
                        appendStrToFile(logPath,"\nVariant " + v.getVariantElement().getId() + " - " +v.getVariantElement().getName() + " not found");
                    }
                }
                if(discart) {
                    discartedVariability.add(variability.getId());
                    continue;
                }
                if(variability.constraint == null){
                    //System.out.println("\n\n");
                    for(Variant variant : variability.getVariants()){
                        //System.out.println(variant.getVariantType());
                        if(variant.getVariantType().equals("alternative_OR")) {
                            variability.setConstraint("Inclusive");
                            break;
                        }
                        if(variant.getVariantType().equals("alternative_XOR")){
                            variability.setConstraint("Exclusive");
                            break;
                        }

                    }

                }

                if(variability.getId() == null){
                    varID++;
                    varIDName = "VARIABILITY#" + varID;
                    //System.out.println("id null");
                    while(variabilityIDExist(architecture, varIDName)){
                        varID++;
                        varIDName = "VARIABILITY#" + varID;
                    }
                    variability.setId(varIDName);
                    //System.out.println("VaName: "+varIDName+" - "+varID);
                    //System.out.println("VaName: "+variability.getId());
                    //varID++;
                }
                /*else{
                    System.out.println("VaName: "+variability.getId());
                }

                 */

                //printWriter.write("\n"+tab+"<variability id=\""+variability.getId()+"\" name=\""+variability.getName()+"\" variationPoint=\""+vp.getVariationPointElement().getId()+"\" constraint=\""+variability.getConstraint()+"\" bindingTime=\""+variability.getBindingTime()+"\" allowsBindingVar=\""+variability.allowAddingVar()+"\" min=\""+variability.getMinSelection()+"\" max=\""+variability.getMaxSelection()+"\">");
                printWriter.write("\n"+tab+"<variability id=\""+variability.getId()+"\" name=\""+variability.getName()+"\" variationPoint=\""+vp.getVariationPointElement().getId()+"\" constraint=\""+variability.getConstraint()+"\" bindingTime=\""+variability.getBindingTime()+"\" allowsBindingVar=\""+variability.allowAddingVar()+"\" min=\""+variability.getMinSelection()+"\" max=\""+variability.getMaxSelection()+"\">");
                for(Variant v : variability.getVariants()){
                    //System.out.println("v:"+v.getVariantElement().getId());
                    printWriter.write("\n"+tab+halfTab+"<variant id=\""+v.getVariantElement().getId()+"\"/>");
                }
                printWriter.write("\n"+tab+"</variability>");
            }



            printWriter.write("\n"+halfTab+"</diagram>");
            printWriter.write("\n"+halfTab+"<links>");


            ArrayList<String> variantStereotype = new ArrayList<>();

            for(Variability variability : architecture.getAllVariabilities()){
                VariationPoint vp = variability.getVariationPoint();
                // add na lista de variationpoint
                if(vp == null)
                    continue;
                if(vp.getVariationPointElement() == null)
                    continue;
                if(!variantStereotype.contains(vp.getVariationPointElement().getId()))
                    variantStereotype.add(vp.getVariationPointElement().getId());
                //printWriter.write("\n\t<link element=\""+vp.getVariationPointElement().getId()+"\" stereotype=\"STEREOTYPE#3\"/>");
                for(Variant v : vp.getVariants()){
                    if(!variantStereotype.contains(v.getVariantElement().getId()))
                        variantStereotype.add(v.getVariantElement().getId());
                }
            }

            //System.out.println("VPStereotype:"+variantStereotype);


            //<link element="CLASS#4" stereotype="STEREOTYPE#10"/>
            for(Class clazz : architecture.getClasses()){

                String concernID = "";

                if(!variantStereotype.contains(clazz.getId()) && !variantStereotype.contains(clazz.getId())) {
                    if (clazz.isMandatory())
                        printWriter.write("\n"+tab+"<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#1\"/>");
                    else
                        printWriter.write("\n"+tab+"<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#2\"/>");
                }

                for(Concern c : clazz.getOwnConcerns()){
                    concernID = architecture.findConcernByName(c.getName()).getId();
                    printWriter.write("\n"+tab+"<link element=\""+clazz.getId()+"\" stereotype=\""+concernID+"\"/>");
                }
                for(arquitetura.representation.Method m : clazz.getAllMethods()){
                    for(Concern c : m.getOwnConcerns()){
                        concernID = architecture.findConcernByName(c.getName()).getId();
                        printWriter.write("\n"+tab+"<link element=\""+m.getId()+"\" stereotype=\""+concernID+"\"/>");
                    }
                }
                for(Attribute m : clazz.getAllAttributes()){
                    for(Concern c : m.getOwnConcerns()){
                        concernID = architecture.findConcernByName(c.getName()).getId();
                        printWriter.write("\n"+tab+"<link element=\""+m.getId()+"\" stereotype=\""+concernID+"\"/>");
                    }
                }
            }

            for(Interface clazz : architecture.getInterfaces()){

                String concernID = "";

                if (clazz.isMandatory())
                    printWriter.write("\n"+tab+"<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#1\"/>");
                else
                    printWriter.write("\n"+tab+"<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#2\"/>");


                for(Concern c : clazz.getOwnConcerns()){
                    concernID = architecture.findConcernByName(c.getName()).getId();
                    printWriter.write("\n"+tab+"<link element=\""+clazz.getId()+"\" stereotype=\""+concernID+"\"/>");
                }
                for(arquitetura.representation.Method m : clazz.getMethods()){
                    for(Concern c : m.getOwnConcerns()){
                        concernID = architecture.findConcernByName(c.getName()).getId();
                        printWriter.write("\n"+tab+"<link element=\""+m.getId()+"\" stereotype=\""+concernID+"\"/>");
                    }
                }
                for(Attribute m : clazz.getAllAttributes()){
                    for(Concern c : m.getOwnConcerns()){
                        concernID = architecture.findConcernByName(c.getName()).getId();
                        printWriter.write("\n"+tab+"<link element=\""+m.getId()+"\" stereotype=\""+concernID+"\"/>");
                    }
                }
            }


            for(Package pkg : architecture.getAllPackages()){

                /*
                if(pkg.isMandatory())
                    printWriter.write("\n\t<link element=\""+pkg.getId()+"\" stereotype=\"STEREOTYPE#1\"/>");
                else
                    printWriter.write("\n\t<link element=\""+pkg.getId()+"\" stereotype=\"STEREOTYPE#2\"/>");
                */

                for(Concern c : pkg.getOwnConcerns2()){
                    printWriter.write("\n"+tab+"<link element=\""+pkg.getId()+"\" stereotype=\""+c.getId()+"\"/>");
                }

                String concernID = "";

                for(Class clazz : pkg.getAllClasses()){

                    if(!variantStereotype.contains(clazz.getId()) && !variantStereotype.contains(clazz.getId())) {
                        if (clazz.isMandatory())
                            printWriter.write("\n"+tab+"<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#1\"/>");
                        else
                            printWriter.write("\n"+tab+"<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#2\"/>");
                    }
                    /*
                    else{
                        if(variantStereotype.contains(clazz.getId())) {
                            // verificar se é ponto de variação ou ambos
                            //if(clazz.getv)
                            System.out.println("VariantPoint or Variant:" + clazz.getId());
                            System.out.println("V:" + clazz.getVariantType());
                            System.out.println("F:" + clazz.getVariationPoint());
                            System.out.println("G:" + clazz.getVariant());
                            if (clazz.getVariantType() != null) {
                                printWriter.write("\n\t<link element=\"" + clazz.getId() + "\" stereotype=\"" + architecture.findConcernByName(clazz.getVariantType()).getId() + "\"/>");

                            }
                        }
                    }
                     */

                    for(Concern c : clazz.getOwnConcerns()){
                        concernID = architecture.findConcernByName(c.getName()).getId();
                        printWriter.write("\n"+tab+"<link element=\""+clazz.getId()+"\" stereotype=\""+concernID+"\"/>");
                    }
                    for(arquitetura.representation.Method m : clazz.getAllMethods()){
                        for(Concern c : m.getOwnConcerns()){
                            concernID = architecture.findConcernByName(c.getName()).getId();
                            printWriter.write("\n"+tab+"<link element=\""+m.getId()+"\" stereotype=\""+concernID+"\"/>");
                        }
                    }
                    for(Attribute m : clazz.getAllAttributes()){
                        for(Concern c : m.getOwnConcerns()){
                            concernID = architecture.findConcernByName(c.getName()).getId();
                            printWriter.write("\n"+tab+"<link element=\""+m.getId()+"\" stereotype=\""+concernID+"\"/>");
                        }
                    }
                }

                for(Interface clazz : pkg.getAllInterfaces()){

                    if (clazz.isMandatory())
                        printWriter.write("\n"+tab+"<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#1\"/>");
                    else
                        printWriter.write("\n"+tab+"<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#2\"/>");

                    for(Concern c : clazz.getOwnConcerns()){
                        concernID = architecture.findConcernByName(c.getName()).getId();
                        printWriter.write("\n"+tab+"<link element=\""+clazz.getId()+"\" stereotype=\""+concernID+"\"/>");
                    }
                    for(arquitetura.representation.Method m : clazz.getMethods()){
                        for(Concern c : m.getOwnConcerns()){
                            concernID = architecture.findConcernByName(c.getName()).getId();
                            printWriter.write("\n"+tab+"<link element=\""+m.getId()+"\" stereotype=\""+concernID+"\"/>");
                        }
                    }
                    for(Attribute m : clazz.getAllAttributes()){
                        for(Concern c : m.getOwnConcerns()){
                            concernID = architecture.findConcernByName(c.getName()).getId();
                            printWriter.write("\n"+tab+"<link element=\""+m.getId()+"\" stereotype=\""+concernID+"\"/>");
                        }
                    }
                }

                saveSubPackageLink(pkg, printWriter, architecture, variantStereotype);
            }



            // stereotype dos pontos de variação e variantes só se não tiver sido descartado
            ArrayList<String> variationPointStereotype = new ArrayList<>();
            variantStereotype = new ArrayList<>();

            for(Variability variability : architecture.getAllVariabilities()){
                if(discartedVariability.contains(variability.getId())){
                    continue;
                }
                VariationPoint vp = variability.getVariationPoint();
                if(vp == null)
                    continue;
                if(vp.getVariationPointElement() == null)
                    continue;;
                // add na lista de variationpoint
                if(!variationPointStereotype.contains(vp.getVariationPointElement().getId())) {
                    printWriter.write("\n"+tab+"<link element=\""+vp.getVariationPointElement().getId()+"\" stereotype=\"STEREOTYPE#3\"/>");
                    variationPointStereotype.add(vp.getVariationPointElement().getId());
                }
                //printWriter.write("\n\t<link element=\""+vp.getVariationPointElement().getId()+"\" stereotype=\"STEREOTYPE#3\"/>");
                for(Variant v : vp.getVariants()){
                    if(!variantStereotype.contains(v.getVariantElement().getId())) {
                        //System.out.println(v.getVariantElement().getId());
                        //System.out.println(v.getVariantType());
                        //System.out.println(architecture.findConcernByName(v.getVariantType()).getId());
                        printWriter.write("\n"+tab+"<link element=\""+v.getVariantElement().getId()+"\" stereotype=\""+architecture.findConcernByName(v.getVariantType()).getId()+"\"/>");
                        variantStereotype.add(v.getVariantElement().getId());
                    }
                }
            }
            // se for ponto de variação mas não for variant, tem que adicioanr o stereotype mandatory ou optional
            for(String vpID : variationPointStereotype){
                if(!variantStereotype.contains(vpID)){
                    Element clazz = architecture.findElementById(vpID);
                    if (clazz.isMandatory())
                        printWriter.write("\n"+tab+"<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#1\"/>");
                    else
                        printWriter.write("\n"+tab+"<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#2\"/>");
                }
            }


            printWriter.write("\n"+halfTab+"</links>");

            printWriter.write("\n"+halfTab+"<products>");
            printWriter.write("\n"+halfTab+"</products>");
            // end projects
            printWriter.write("\n</project>");


            printWriter.close();
            fileWriter.close();
        }catch (Exception ex){
            System.out.println(ex);
            ex.printStackTrace();
        }
    }

    public static void appendStrToFile(String fileName,
                                       String str)
    {
        try {
            // Open given file in append mode.
            BufferedWriter out = new BufferedWriter(
                    new FileWriter(fileName, true));
            out.write(str);
            out.close();
        }
        catch (IOException e) {
            System.out.println("exception occoured" + e);
        }
    }


    private void saveTypeSubPackage(Package pkg1, ArrayList<TypeSmarty> adicionedType, PrintWriter printWriter){
        String halfTab = "  ";
        String tab = "    ";
        for(Package pkg : pkg1.getNestedPackages()){
            for(Class clazz : pkg.getAllClasses()){
                if(!typeSmartyExist(adicionedType, clazz.getId())){
                    printWriter.write("\n"+tab+"<type id=\"" + clazz.getId() + "\" path=\"\" name=\"" + clazz.getName() + "\" value=\"null\" primitive=\"false\" standard=\"false\"/>");
                }
            }
            for(Interface clazz : pkg.getAllInterfaces()){
                if(!typeSmartyExist(adicionedType, clazz.getId())){
                    printWriter.write("\n"+tab+"<type id=\"" + clazz.getId() + "\" path=\"\" name=\"" + clazz.getName() + "\" value=\"null\" primitive=\"false\" standard=\"false\"/>");
                }
            }
            saveTypeSubPackage(pkg, adicionedType, printWriter);
        }
    }

    private void saveSubPackage(Package pkg1, PrintWriter printWriter) {
        String halfTab = "  ";
        String tab = "    ";
        for (Package pkg : pkg1.getNestedPackages()) {
            // <package id="PACKAGE#2" name="AlbumMgr" mandatory="true" x="300" y="2090" height="700" width="667"/>

            printWriter.write("\n" + tab + "<package id=\"" + pkg.getId() + "\" name=\"" + pkg.getName() + "\" mandatory=\"" + pkg.isMandatory() + "\" x=\"" + pkg.getPosX() + "\" y=\"" + pkg.getPosY() + "\" globalX=\"" + pkg.getGlobalPosX() + "\" globalY=\"" + pkg.getGlobalPosY() + "\" height=\"" + pkg.getHeight() + "\" width=\"" + pkg.getWidth() + "\"/>");
            saveSubPackage(pkg, printWriter);
        }
    }

    private void saveSubPackageClass(Package pkg1, PrintWriter printWriter, Architecture architecture) {
        String halfTab = "  ";
        String tab = "    ";
        for(Package pkg : pkg1.getNestedPackages()){
            for(Class clazz : pkg.getAllClasses()){

                printWriter.write("\n"+tab+"<class id=\""+clazz.getId()+"\" name=\""+clazz.getName()+"\" mandatory=\""+clazz.isMandatory()+"\" x=\""+clazz.getPosX()+"\" y=\""+clazz.getPosY()+"\" globalX=\""+clazz.getGlobalPosX()+"\" globalY=\""+clazz.getGlobalPosY()+"\" abstract=\""+clazz.isAbstract()+"\" final=\""+clazz.isAbstract()+"\" height=\""+clazz.getHeight()+"\" width=\""+clazz.getWidth()+"\" parent=\""+pkg.getId()+"\">");
                for(Attribute att : clazz.getAllAttributes()){
                    TypeSmarty typeS = architecture.findTypeSMartyByName(att.getType());

                    printWriter.write("\n"+tab+halfTab+"<attribute id=\""+att.getId()+"\" name=\""+att.getName()+"\" type=\""+typeS.getId()+"\" visibility=\""+att.getVisibility()+"\" static=\""+att.isStatic()+"\" final=\""+att.isFinal()+"\"/>");
                    //printWriter.write("\n\t"+halfTab+"<attribute id=\""+att.getId()+"\" name=\""+att.getName()+"\" type=\""+att.getType()+"\" visibility=\""+att.getVisibility()+"\" static=\""+att.isStatic()+"\" final=\""+att.isFinal()+"\"/>");

                }
                for(arquitetura.representation.Method m : clazz.getAllMethods()){
                    // <method id="METHOD#45" name="getmedia" return="TYPE#9" visibility="public" constructor="false" static="false" final="false" abstract="false">
                    TypeSmarty typeS = architecture.findTypeSMartyByName(m.getReturnType());
                    printWriter.write("\n"+tab+halfTab+"<method id=\""+m.getId()+"\" name=\""+m.getName()+"\" return=\""+typeS.getId()+"\" visibility=\""+m.getVisibility()+"\" constructor=\""+m.isConstructor()+"\" static=\""+m.isStatic()+"\" final=\""+m.isFinal()+"\" abstract=\""+m.isAbstract()+"\">");
                    //printWriter.write("\n\t"+halfTab+"<method id=\""+m.getId()+"\" name=\""+m.getName()+"\" return=\""+m.getReturnType()+"\" visibility=\""+m.getVisibility()+"\" constructor=\""+m.isConstructor()+"\" static=\""+m.isStatic()+"\" final=\""+m.isFinal()+"\" abstract=\""+m.isAbstract()+"\">");
                    for(ParameterMethod pm : m.getParameters()){
                        typeS = architecture.findTypeSMartyByName(pm.getType());
                        printWriter.write("\n"+tab+tab+"<parameter type=\""+typeS.getId()+"\" name=\""+pm.getName()+"\"/>");
                        //printWriter.write("\n\t\t<parameter type=\""+pm.getType()+"\" name=\""+pm.getName()+"\"/>");
                    }
                    printWriter.write("\n"+tab+halfTab+"</method>");
                }
                printWriter.write("\n"+tab+"</class>");
            }
            saveSubPackageClass(pkg,printWriter,architecture);
        }

    }

    private void saveSubPackageInterface(Package pkg1, PrintWriter printWriter, Architecture architecture) {
        String halfTab = "  ";
        String tab = "    ";
        for(Package pkg : pkg1.getNestedPackages()){
            //System.out.println("Package:"+pkg.getName());
            for(Interface clazz : pkg.getAllInterfaces()){
                //System.out.println("Interface:"+clazz.getName());
                printWriter.write("\n"+tab+"<interface id=\""+clazz.getId()+"\" name=\""+clazz.getName()+"\" mandatory=\""+clazz.isMandatory()+"\" x=\""+clazz.getPosX()+"\" y=\""+clazz.getPosY()+"\" globalX=\""+clazz.getGlobalPosX()+"\" globalY=\""+clazz.getGlobalPosY()+"\" height=\""+clazz.getHeight()+"\" width=\""+clazz.getWidth()+"\" parent=\""+pkg.getId()+"\">");
                for(Attribute att : clazz.getAllAttributes()){
                    TypeSmarty typeS = architecture.findTypeSMartyByName(att.getType());
                    printWriter.write("\n"+tab+halfTab+"<attribute id=\""+att.getId()+"\" name=\""+att.getName()+"\" type=\""+typeS.getId()+"\" visibility=\""+att.getVisibility()+"\" static=\""+att.isStatic()+"\" final=\""+att.isFinal()+"\"/>");
                    //printWriter.write("\n\t"+halfTab+"<attribute id=\""+att.getId()+"\" name=\""+att.getName()+"\" type=\""+att.getType()+"\" visibility=\""+att.getVisibility()+"\" static=\""+att.isStatic()+"\" final=\""+att.isFinal()+"\"/>");

                }
                for(arquitetura.representation.Method m : clazz.getMethods()){
                    // <method id="METHOD#45" name="getmedia" return="TYPE#9" visibility="public" constructor="false" static="false" final="false" abstract="false">
                    TypeSmarty typeS = architecture.findTypeSMartyByName(m.getReturnType());
                    printWriter.write("\n"+tab+halfTab+"<method id=\""+m.getId()+"\" name=\""+m.getName()+"\" return=\""+typeS.getId()+"\" visibility=\""+m.getVisibility()+"\" constructor=\""+m.isConstructor()+"\" static=\""+m.isStatic()+"\" final=\""+m.isFinal()+"\" abstract=\""+m.isAbstract()+"\">");
                    //printWriter.write("\n\t"+halfTab+"<method id=\""+m.getId()+"\" name=\""+m.getName()+"\" return=\""+m.getReturnType()+"\" visibility=\""+m.getVisibility()+"\" constructor=\""+m.isConstructor()+"\" static=\""+m.isStatic()+"\" final=\""+m.isFinal()+"\" abstract=\""+m.isAbstract()+"\">");
                    for(ParameterMethod pm : m.getParameters()){
                        typeS = architecture.findTypeSMartyByName(pm.getType());
                        printWriter.write("\n"+tab+tab+"<parameter type=\""+typeS.getId()+"\" name=\""+pm.getName()+"\"/>");
                        //printWriter.write("\n\t\t<parameter type=\""+pm.getType()+"\" name=\""+pm.getName()+"\"/>");
                    }
                    printWriter.write("\n"+tab+halfTab+"</method>");
                }
                printWriter.write("\n"+tab+"</interface>");
            }
            saveSubPackageInterface(pkg,printWriter,architecture);
        }
    }

    private void saveReference(Package pkg1, PrintWriter printWriter) {
        String halfTab = "  ";
        String tab = "    ";
        for(Package pkg : pkg1.getNestedPackages()){
            printWriter.write("\n"+tab+"<reference package=\""+pkg.getId()+"\" parent=\""+pkg1.getId()+"\" />");
            saveReference(pkg,printWriter);
        }
    }

    private void saveSubPackageLink(Package pkg1, PrintWriter printWriter, Architecture architecture, ArrayList<String> variantStereotype) {
        String halfTab = "  ";
        String tab = "    ";
        String concernID = "";
        for(Package pkg : pkg1.getNestedPackages()){

                /*
                if(pkg.isMandatory())
                    printWriter.write("\n\t<link element=\""+pkg.getId()+"\" stereotype=\"STEREOTYPE#1\"/>");
                else
                    printWriter.write("\n\t<link element=\""+pkg.getId()+"\" stereotype=\"STEREOTYPE#2\"/>");
                */

            for(Concern c : pkg.getOwnConcerns2()){
                concernID = architecture.findConcernByName(c.getName()).getId();
                printWriter.write("\n"+tab+"t<link element=\""+pkg.getId()+"\" stereotype=\""+concernID+"\"/>");
            }


            for(Class clazz : pkg.getAllClasses()){

                if(!variantStereotype.contains(clazz.getId()) && !variantStereotype.contains(clazz.getId())) {
                    if (clazz.isMandatory())
                        printWriter.write("\n"+tab+"<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#1\"/>");
                    else
                        printWriter.write("\n"+tab+"<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#2\"/>");
                }
                    /*
                    else{
                        if(variantStereotype.contains(clazz.getId())) {
                            // verificar se é ponto de variação ou ambos
                            //if(clazz.getv)
                            System.out.println("VariantPoint or Variant:" + clazz.getId());
                            System.out.println("V:" + clazz.getVariantType());
                            System.out.println("F:" + clazz.getVariationPoint());
                            System.out.println("G:" + clazz.getVariant());
                            if (clazz.getVariantType() != null) {
                                printWriter.write("\n\t<link element=\"" + clazz.getId() + "\" stereotype=\"" + architecture.findConcernByName(clazz.getVariantType()).getId() + "\"/>");

                            }
                        }
                    }
                     */

                for(Concern c : clazz.getOwnConcerns()){
                    concernID = architecture.findConcernByName(c.getName()).getId();
                    printWriter.write("\n"+tab+"<link element=\""+clazz.getId()+"\" stereotype=\""+concernID+"\"/>");
                }
                for(arquitetura.representation.Method m : clazz.getAllMethods()){
                    for(Concern c : m.getOwnConcerns()){
                        concernID = architecture.findConcernByName(c.getName()).getId();
                        printWriter.write("\n"+tab+"<link element=\""+m.getId()+"\" stereotype=\""+concernID+"\"/>");
                    }
                }
                for(Attribute m : clazz.getAllAttributes()){
                    for(Concern c : m.getOwnConcerns()){
                        concernID = architecture.findConcernByName(c.getName()).getId();
                        printWriter.write("\n"+tab+"<link element=\""+m.getId()+"\" stereotype=\""+concernID+"\"/>");
                    }
                }
            }

            for(Interface clazz : pkg.getAllInterfaces()){


                if (clazz.isMandatory())
                    printWriter.write("\n"+tab+"<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#1\"/>");
                else
                    printWriter.write("\n"+tab+"<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#2\"/>");


                for(Concern c : clazz.getOwnConcerns()){
                    concernID = architecture.findConcernByName(c.getName()).getId();
                    printWriter.write("\n"+tab+"<link element=\""+clazz.getId()+"\" stereotype=\""+concernID+"\"/>");
                }
                for(arquitetura.representation.Method m : clazz.getMethods()){
                    for(Concern c : m.getOwnConcerns()){
                        concernID = architecture.findConcernByName(c.getName()).getId();
                        printWriter.write("\n"+tab+"<link element=\""+m.getId()+"\" stereotype=\""+concernID+"\"/>");
                    }
                }
                for(Attribute m : clazz.getAllAttributes()){
                    for(Concern c : m.getOwnConcerns()){
                        concernID = architecture.findConcernByName(c.getName()).getId();
                        printWriter.write("\n"+tab+"<link element=\""+m.getId()+"\" stereotype=\""+concernID+"\"/>");
                    }
                }
            }

            saveSubPackageLink(pkg,printWriter, architecture, variantStereotype);
        }
    }


    private  void arrangeArchitecture(Architecture architecture){
        reSizeElements(architecture);
        rePosElementsInPackage2(architecture);
        resizePackage(architecture);
        rePosAllElements(architecture);
        getGlobalPosElements(architecture);
    }

    private void reSizeElements(Architecture architecture){
        // atualizar tamanho de classe e interface:

        for(Class clazz : architecture.getClasses()) {

            clazz.setHeight("" + ((clazz.getAllMethods().size() + clazz.getOwnConcerns().size() + clazz.getAllAttributes().size()) * 20 + 200));

            int width = 400;
            for (arquitetura.representation.Method m1 : clazz.getAllMethods()) {
                if (width < (400 + (100 * m1.getOwnConcerns().size()) + (100 * m1.getParameters().size()))) {
                    width = 400 + (100 * m1.getOwnConcerns().size()) + (100 * m1.getParameters().size());
                }
            }
            clazz.setWidth("" + width);
        }
        for(Interface clazz : architecture.getInterfaces()) {
            clazz.setHeight("" + ((clazz.getMethods().size() + clazz.getOwnConcerns().size() + clazz.getAllAttributes().size()) * 20 + 200));
            int width = 400;
            for (arquitetura.representation.Method m1 : clazz.getMethods()) {
                if (width < (400 + (100 * m1.getOwnConcerns().size()) + (100 * m1.getParameters().size()))) {
                    width = 400 + (100 * m1.getOwnConcerns().size()) + (100 * m1.getParameters().size());
                }
            }
            clazz.setWidth("" + width);

        }
        for(Package pkg : architecture.getAllPackages()) {
            for (Class clazz : pkg.getAllClasses()) {
                clazz.setHeight("" + ((clazz.getAllMethods().size() + clazz.getOwnConcerns().size() + clazz.getAllAttributes().size()) * 20 + 100));

                int width = 400;
                for (arquitetura.representation.Method m1 : clazz.getAllMethods()) {
                    if (width < (400 + (100 * m1.getOwnConcerns().size()) + (100 * m1.getParameters().size()))) {
                        width = 400 + (100 * m1.getOwnConcerns().size()) + (100 * m1.getParameters().size());
                    }
                }
                clazz.setWidth("" + width);
            }
            for (Interface clazz : pkg.getAllInterfaces()) {
                clazz.setHeight("" + ((clazz.getMethods().size() + clazz.getOwnConcerns().size() + clazz.getAllAttributes().size()) * 20 + 100));

                int width = 400;
                for (arquitetura.representation.Method m1 : clazz.getMethods()) {
                    if (width < (400 + (100 * m1.getOwnConcerns().size()) + (100 * m1.getParameters().size()))) {
                        width = 400 + (100 * m1.getOwnConcerns().size()) + (100 * m1.getParameters().size());
                    }
                }
                clazz.setWidth("" + width);
            }
            reSizeElementsInSubPackage(pkg);
        }
    }

    private void reSizeElementsInSubPackage(Package pkg1){
        // atualizar tamanho de classe e interface:


        for(Package pkg : pkg1.getNestedPackages()) {
            for (Class clazz : pkg.getAllClasses()) {
                clazz.setHeight("" + ((clazz.getAllMethods().size() + clazz.getOwnConcerns().size() + clazz.getAllAttributes().size()) * 20 + 100));

                int width = 400;
                for (arquitetura.representation.Method m1 : clazz.getAllMethods()) {
                    if (width < (400 + (100 * m1.getOwnConcerns().size()) + (100 * m1.getParameters().size()))) {
                        width = 400 + (100 * m1.getOwnConcerns().size()) + (100 * m1.getParameters().size());
                    }
                }
                clazz.setWidth("" + width);
            }
            for (Interface clazz : pkg.getAllInterfaces()) {
                clazz.setHeight("" + ((clazz.getMethods().size() + clazz.getOwnConcerns().size() + clazz.getAllAttributes().size()) * 20 + 100));

                //clazz.setWidth(""+((clazz.getMethods().size()+clazz.getAllAttributes().size())*10 + 120));
                int width = 400;
                for (arquitetura.representation.Method m1 : clazz.getMethods()) {
                    if (width < (400 + (100 * m1.getOwnConcerns().size()) + (100 * m1.getParameters().size()))) {
                        width = 400 + (100 * m1.getOwnConcerns().size()) + (100 * m1.getParameters().size());
                    }
                }
                clazz.setWidth("" + width);
            }
            reSizeElementsInSubPackage(pkg);
        }
    }


    private void rePosElementsInPackage2(Architecture architecture){
        int sizeC;
        int sizeI;
        int sizeP;

        int size;
        int div;
        int x;
        int y;
        int nexty;
        int posLista;


        for(Package pkg : architecture.getAllPackages()) {
            x = 40;
            y = 60;
            nexty = 60;
            posLista = 0;

            sizeP = pkg.getNestedPackages().size();
            sizeC = pkg.getAllClasses().size();
            sizeI = pkg.getAllInterfaces().size();

            size = sizeP+sizeC+sizeI;

            resizeSubPackage(pkg);


            if(size == 1){
                for (Package pkx : pkg.getNestedPackages()) {
                    pkx.setPosX("40");
                    pkx.setPosY("60");
                }
                for (Class clazz : pkg.getAllClasses()) {
                    clazz.setPosX("40");
                    clazz.setPosY("60");
                }
                for (Interface clazz : pkg.getAllInterfaces()) {
                    clazz.setPosX("40");
                    clazz.setPosY("60");
                }
            }
            if(size == 2){
                for(Package pkx : pkg.getNestedPackages()){
                    pkx.setPosX(""+x);
                    pkx.setPosY(""+y);
                    x = x + Integer.parseInt(pkx.getWidth()) + 40;
                }
                for(Class clazz : pkg.getAllClasses()){
                    clazz.setPosX(""+x);
                    clazz.setPosY(""+y);
                    x = x + Integer.parseInt(clazz.getWidth()) + 40;
                }
                for(Interface clazz : pkg.getAllInterfaces()){
                    clazz.setPosX(""+x);
                    clazz.setPosY(""+y);
                    x = x + Integer.parseInt(clazz.getWidth()) + 40;
                }
            }
            if(size > 2){
                div = (int)Math.ceil(Math.sqrt(size));

                for(Package subP : pkg.getNestedPackages()){
                    if(posLista != 0) {
                        if ((posLista % div)==0){
                            x = 40;
                            y = nexty + 40;
                        }
                    }
                    subP.setPosX(""+x);
                    subP.setPosY(""+y);
                    x = x + Integer.parseInt(subP.getWidth()) + 40;
                    if(nexty < y + Integer.parseInt(subP.getHeight())){
                        nexty = y + Integer.parseInt(subP.getHeight());
                    }
                    posLista++;
                }

                for(Class clazz : pkg.getAllClasses()){
                    if(posLista != 0) {
                        if ((posLista % div)==0){
                            x = 40;
                            y = nexty + 40;
                        }
                    }
                    clazz.setPosX(""+x);
                    clazz.setPosY(""+y);
                    x = x + Integer.parseInt(clazz.getWidth()) + 40;
                    if(nexty < y + Integer.parseInt(clazz.getHeight())){
                        nexty = y + Integer.parseInt(clazz.getHeight());
                    }
                    posLista++;
                }
                for(Interface clazz : pkg.getAllInterfaces()){
                    if(posLista != 0) {
                        if ((posLista % div)==0){
                            x = 40;
                            y = nexty + 40;
                        }
                    }
                    clazz.setPosX(""+x);
                    clazz.setPosY(""+y);
                    x = x + Integer.parseInt(clazz.getWidth()) + 40;
                    if(nexty < y + Integer.parseInt(clazz.getHeight())){
                        nexty = y + Integer.parseInt(clazz.getHeight());
                    }
                    posLista++;
                }
            }
        }
    }

    private void resizeSubPackage(Package pkg){
        if(pkg.getNestedPackages().size() == 0){
            int height = 300;
            for (Class clazz : pkg.getAllClasses()) {
                if (height < (Integer.parseInt(clazz.getPosY()) + Integer.parseInt(clazz.getHeight()) + 20)) {
                    height = (Integer.parseInt(clazz.getPosY()) + Integer.parseInt(clazz.getHeight()) + 20);
                }
            }
            for (Interface clazz : pkg.getAllInterfaces()) {
                if (height < (Integer.parseInt(clazz.getPosY()) + Integer.parseInt(clazz.getHeight()) + 20)) {
                    height = (Integer.parseInt(clazz.getPosY()) + Integer.parseInt(clazz.getHeight()) + 20);
                }
            }
            pkg.setHeight("" + height);
            int width = 300;
            for (Class clazz : pkg.getAllClasses()) {
                if (width < (Integer.parseInt(clazz.getPosX()) + Integer.parseInt(clazz.getWidth()) + 20)) {
                    width = (Integer.parseInt(clazz.getPosX()) + Integer.parseInt(clazz.getWidth()) + 20);
                }
            }
            for (Interface clazz : pkg.getAllInterfaces()) {
                if (width < (Integer.parseInt(clazz.getPosX()) + Integer.parseInt(clazz.getWidth()) + 20)) {
                    width = (Integer.parseInt(clazz.getPosX()) + Integer.parseInt(clazz.getWidth()) + 20);
                }
            }
            pkg.setWidth("" + width);
        }
        else {
            for (Package pkg1 : pkg.getNestedPackages()) {
                resizeSubPackage(pkg1);
            }
            int x = 40;
            int y = 60;
            int nexty = 60;
            int posLista = 0;

            int sizeP = pkg.getNestedPackages().size();
            int sizeC = pkg.getAllClasses().size();
            int sizeI = pkg.getAllInterfaces().size();
            int size = sizeP+sizeC+sizeI;

            if(size == 1){
                for (Package pkx : pkg.getNestedPackages()) {
                    pkx.setPosX("40");
                    pkx.setPosY("60");
                }
                for (Class clazz : pkg.getAllClasses()) {
                    clazz.setPosX("40");
                    clazz.setPosY("60");
                }
                for (Interface clazz : pkg.getAllInterfaces()) {
                    clazz.setPosX("40");
                    clazz.setPosY("60");
                }
            }
            if(size == 2){
                for(Package pkx : pkg.getNestedPackages()){
                    pkx.setPosX(""+x);
                    pkx.setPosY(""+y);
                    x = x + Integer.parseInt(pkx.getWidth()) + 40;
                }
                for(Class clazz : pkg.getAllClasses()){
                    clazz.setPosX(""+x);
                    clazz.setPosY(""+y);
                    x = x + Integer.parseInt(clazz.getWidth()) + 40;
                }
                for(Interface clazz : pkg.getAllInterfaces()){
                    clazz.setPosX(""+x);
                    clazz.setPosY(""+y);
                    x = x + Integer.parseInt(clazz.getWidth()) + 40;
                }
            }
            if(size > 2){
                int div = (int)Math.ceil(Math.sqrt(size));

                for(Package subP : pkg.getNestedPackages()){
                    if(posLista != 0) {
                        if ((posLista % div)==0){
                            x = 40;
                            y = nexty + 40;
                        }
                    }
                    subP.setPosX(""+x);
                    subP.setPosY(""+y);
                    x = x + Integer.parseInt(subP.getWidth()) + 40;
                    if(nexty < y + Integer.parseInt(subP.getHeight())){
                        nexty = y + Integer.parseInt(subP.getHeight());
                    }
                    posLista++;
                }

                for(Class clazz : pkg.getAllClasses()){
                    if(posLista != 0) {
                        if ((posLista % div)==0){
                            x = 40;
                            y = nexty + 40;
                        }
                    }
                    clazz.setPosX(""+x);
                    clazz.setPosY(""+y);
                    x = x + Integer.parseInt(clazz.getWidth()) + 40;
                    if(nexty < y + Integer.parseInt(clazz.getHeight())){
                        nexty = y + Integer.parseInt(clazz.getHeight());
                    }
                    posLista++;
                }
                for(Interface clazz : pkg.getAllInterfaces()){
                    if(posLista != 0) {
                        if ((posLista % div)==0){
                            x = 40;
                            y = nexty + 40;
                        }
                    }
                    clazz.setPosX(""+x);
                    clazz.setPosY(""+y);
                    x = x + Integer.parseInt(clazz.getWidth()) + 40;
                    if(nexty < y + Integer.parseInt(clazz.getHeight())){
                        nexty = y + Integer.parseInt(clazz.getHeight());
                    }
                    posLista++;
                }
            }



            int height = 300;
            for (Package pkx : pkg.getNestedPackages()) {
                if (height < (Integer.parseInt(pkx.getPosY()) + Integer.parseInt(pkx.getHeight()) + 20)) {
                    height = (Integer.parseInt(pkx.getPosY()) + Integer.parseInt(pkx.getHeight()) + 20);
                }
            }
            for (Class clazz : pkg.getAllClasses()) {
                if (height < (Integer.parseInt(clazz.getPosY()) + Integer.parseInt(clazz.getHeight()) + 20)) {
                    height = (Integer.parseInt(clazz.getPosY()) + Integer.parseInt(clazz.getHeight()) + 20);
                }
            }
            for (Interface clazz : pkg.getAllInterfaces()) {
                if (height < (Integer.parseInt(clazz.getPosY()) + Integer.parseInt(clazz.getHeight()) + 20)) {
                    height = (Integer.parseInt(clazz.getPosY()) + Integer.parseInt(clazz.getHeight()) + 20);
                }
            }
            pkg.setHeight("" + height);
            int width = 300;
            for (Package pkx : pkg.getNestedPackages()) {
                if (width < (Integer.parseInt(pkx.getPosX()) + Integer.parseInt(pkx.getWidth()) + 20)) {
                    width = (Integer.parseInt(pkx.getPosX()) + Integer.parseInt(pkx.getWidth()) + 20);
                }
            }
            for (Class clazz : pkg.getAllClasses()) {
                if (width < (Integer.parseInt(clazz.getPosX()) + Integer.parseInt(clazz.getWidth()) + 20)) {
                    width = (Integer.parseInt(clazz.getPosX()) + Integer.parseInt(clazz.getWidth()) + 20);
                }
            }
            for (Interface clazz : pkg.getAllInterfaces()) {
                if (width < (Integer.parseInt(clazz.getPosX()) + Integer.parseInt(clazz.getWidth()) + 20)) {
                    width = (Integer.parseInt(clazz.getPosX()) + Integer.parseInt(clazz.getWidth()) + 20);
                }
            }
            pkg.setWidth("" + width);

        }
    }


    private void resizePackage(Architecture architecture){
        for(Package pkg : architecture.getAllPackages()) {
            int height = 300;
            for (Package clazz : pkg.nestedPackages) {
                if (height < (Integer.parseInt(clazz.getPosY()) + Integer.parseInt(clazz.getHeight()) + 20)) {
                    height = (Integer.parseInt(clazz.getPosY()) + Integer.parseInt(clazz.getHeight()) + 20);
                }
            }
            for (Class clazz : pkg.getAllClasses()) {
                if (height < (Integer.parseInt(clazz.getPosY()) + Integer.parseInt(clazz.getHeight()) + 20)) {
                    height = (Integer.parseInt(clazz.getPosY()) + Integer.parseInt(clazz.getHeight()) + 20);
                }
            }
            for (Interface clazz : pkg.getAllInterfaces()) {
                if (height < (Integer.parseInt(clazz.getPosY()) + Integer.parseInt(clazz.getHeight()) + 20)) {
                    height = (Integer.parseInt(clazz.getPosY()) + Integer.parseInt(clazz.getHeight()) + 20);
                }
            }
            pkg.setHeight("" + height);
            int width = 300;
            for (Package clazz : pkg.getNestedPackages()) {
                if (width < (Integer.parseInt(clazz.getPosX()) + Integer.parseInt(clazz.getWidth()) + 20)) {
                    width = (Integer.parseInt(clazz.getPosX()) + Integer.parseInt(clazz.getWidth()) + 20);
                }
            }
            for (Class clazz : pkg.getAllClasses()) {
                if (width < (Integer.parseInt(clazz.getPosX()) + Integer.parseInt(clazz.getWidth()) + 20)) {
                    width = (Integer.parseInt(clazz.getPosX()) + Integer.parseInt(clazz.getWidth()) + 20);
                }
            }
            for (Interface clazz : pkg.getAllInterfaces()) {
                if (width < (Integer.parseInt(clazz.getPosX()) + Integer.parseInt(clazz.getWidth()) + 20)) {
                    width = (Integer.parseInt(clazz.getPosX()) + Integer.parseInt(clazz.getWidth()) + 20);
                }
            }
            pkg.setWidth("" + width);
        }
    }

    private void rePosAllElements(Architecture architecture){
        int sizeC;
        int sizeI;

        int size;
        int div;
        int x;
        int y;
        int nexty;
        int posLista;

        x = 40;
        y = 40;
        nexty = 40;
        posLista = 0;

        size = architecture.getAllPackages().size();

        if(size == 1){
            for (Package clazz : architecture.getAllPackages()) {
                //clazz.setPosX("500");
                //clazz.setPosY("500");
                clazz.setPosX(""+x);
                clazz.setPosY(""+y);
                if(nexty < y + Integer.parseInt(clazz.getHeight())){
                    nexty = y + Integer.parseInt(clazz.getHeight());
                }
            }
        }
        if(size == 2){
            for (Package clazz : architecture.getAllPackages()) {
                clazz.setPosX(""+x);
                clazz.setPosY(""+y);
                x = x + Integer.parseInt(clazz.getWidth()) + 40;
                if(nexty < y + Integer.parseInt(clazz.getHeight())){
                    nexty = y + Integer.parseInt(clazz.getHeight());
                }
            }
        }
        if(size > 2){
            div = (int)Math.ceil(Math.sqrt(size));
            for (Package clazz : architecture.getAllPackages()) {
                if(posLista != 0) {
                    if ((posLista % div)==0){
                        x = 40;
                        y = nexty + 40;
                    }
                }
                clazz.setPosX(""+x);
                clazz.setPosY(""+y);
                x = x + Integer.parseInt(clazz.getWidth()) + 40;
                if(nexty < y + Integer.parseInt(clazz.getHeight())){
                    nexty = y + Integer.parseInt(clazz.getHeight());
                }
                posLista++;
            }
        }

        if(architecture.getClasses().size() > 0) {
            y = nexty + 40;
            x = 40;
            for (Class clazz : architecture.getClasses()) {

                clazz.setPosX(""+x);
                clazz.setPosY(""+y);
                x = x + Integer.parseInt(clazz.getWidth()) + 40;
                if(nexty < y + Integer.parseInt(clazz.getHeight())){
                    nexty = y + Integer.parseInt(clazz.getHeight());
                }
            }
        }
        if(architecture.getInterfaces().size() > 0) {
            y = nexty + 40;
            x = 40;
            for (Interface clazz : architecture.getInterfaces()) {

                clazz.setPosX(""+x);
                clazz.setPosY(""+y);
                x = x + Integer.parseInt(clazz.getWidth()) + 40;
                if(nexty < y + Integer.parseInt(clazz.getHeight())){
                    nexty = y + Integer.parseInt(clazz.getHeight());
                }
            }
        }
    }

    private void getGlobalPosElements(Architecture architecture){
        // atualizar tamanho de classe e interface:

        for(Class clazz : architecture.getClasses()) {

            clazz.setGlobalPosX(clazz.getPosX());
            clazz.setGlobalPosY(clazz.getPosY());
        }
        for(Interface clazz : architecture.getInterfaces()) {
            clazz.setGlobalPosX(clazz.getPosX());
            clazz.setGlobalPosY(clazz.getPosY());

        }
        int pkgPosX = 0;
        int pkgPosY = 0;
        for(Package pkg : architecture.getAllPackages()) {
            pkg.setGlobalPosX(pkg.getPosX());
            pkg.setGlobalPosY(pkg.getPosY());

            pkgPosX = Integer.parseInt(pkg.getPosX());
            pkgPosY = Integer.parseInt(pkg.getPosY());

            for (Class clazz : pkg.getAllClasses()) {
                clazz.setGlobalPosX(""+(Integer.parseInt(clazz.getPosX())+pkgPosX));
                clazz.setGlobalPosY(""+(Integer.parseInt(clazz.getPosY())+pkgPosY));
            }
            for (Interface clazz : pkg.getAllInterfaces()) {
                clazz.setGlobalPosX(""+(Integer.parseInt(clazz.getPosX())+pkgPosX));
                clazz.setGlobalPosY(""+(Integer.parseInt(clazz.getPosY())+pkgPosY));
            }
            getGlobalPosElementsSubPackage(pkg);
        }
    }

    private void getGlobalPosElementsSubPackage(Package pkg1){
        // atualizar tamanho de classe e interface:


        int pkgPosX = 0;
        int pkgPosY = 0;
        for(Package pkg : pkg1.getNestedPackages()) {
            pkgPosX = Integer.parseInt(pkg1.getPosX());
            pkgPosY = Integer.parseInt(pkg1.getPosY());

            pkg.setGlobalPosX(""+(Integer.parseInt(pkg.getPosX())+pkgPosX));
            pkg.setGlobalPosY(""+(Integer.parseInt(pkg.getPosY())+pkgPosY));

            pkgPosX = Integer.parseInt(pkg.getPosX()) + Integer.parseInt(pkg1.getPosX());
            pkgPosY = Integer.parseInt(pkg.getPosY()) + Integer.parseInt(pkg1.getPosY());

            for (Class clazz : pkg.getAllClasses()) {
                clazz.setGlobalPosX(""+(Integer.parseInt(clazz.getPosX())+pkgPosX));
                clazz.setGlobalPosY(""+(Integer.parseInt(clazz.getPosY())+pkgPosY));
            }
            for (Interface clazz : pkg.getAllInterfaces()) {
                clazz.setGlobalPosX(""+(Integer.parseInt(clazz.getPosX())+pkgPosX));
                clazz.setGlobalPosY(""+(Integer.parseInt(clazz.getPosY())+pkgPosY));
            }
        }
    }


    private boolean variabilityIDExist(Architecture architecture, String id){
        for(Variability variability : architecture.getAllVariabilities()){
            if(variability.getId() != null){
                if(variability.getId().equals(id))
                    return true;
            }
        }
        return  false;
    }


    private void createTypesSMarty(Architecture architecture){
        ArrayList<TypeSmarty> lstSMarty = new ArrayList<>();
        TypeSmarty typeS;

        typeS = new TypeSmarty();
        typeS.setId("TYPE#1");
        typeS.setPath("");
        typeS.setName("boolean");
        typeS.setValue("false");
        typeS.setPrimitive(true);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#2");
        typeS.setPath("");
        typeS.setName("byte");
        typeS.setValue("'0'");
        typeS.setPrimitive(true);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#3");
        typeS.setPath("");
        typeS.setName("char");
        typeS.setValue("' '");
        typeS.setPrimitive(true);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#4");
        typeS.setPath("");
        typeS.setName("double");
        typeS.setValue("0.0d");
        typeS.setPrimitive(true);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#5");
        typeS.setPath("");
        typeS.setName("float");
        typeS.setValue("0.0f");
        typeS.setPrimitive(true);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#6");
        typeS.setPath("");
        typeS.setName("int");
        typeS.setValue("0");
        typeS.setPrimitive(true);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#7");
        typeS.setPath("");
        typeS.setName("long");
        typeS.setValue("0.0l");
        typeS.setPrimitive(true);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#8");
        typeS.setPath("");
        typeS.setName("short");
        typeS.setValue("0");
        typeS.setPrimitive(true);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#9");
        typeS.setPath("");
        typeS.setName("void");
        typeS.setValue("");
        typeS.setPrimitive(true);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#10");
        typeS.setPath("java.lang");
        typeS.setName("Boolean");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#11");
        typeS.setPath("java.lang");
        typeS.setName("Byte");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#12");
        typeS.setPath("java.lang");
        typeS.setName("Character");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#13");
        typeS.setPath("java.lang");
        typeS.setName("Double");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#14");
        typeS.setPath("java.lang");
        typeS.setName("Enum");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#15");
        typeS.setPath("java.lang");
        typeS.setName("Exception");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#16");
        typeS.setPath("java.lang");
        typeS.setName("Float");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#17");
        typeS.setPath("java.lang");
        typeS.setName("Integer");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#18");
        typeS.setPath("java.lang");
        typeS.setName("Long");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#19");
        typeS.setPath("java.lang");
        typeS.setName("Math");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#20");
        typeS.setPath("java.lang");
        typeS.setName("Number");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#21");
        typeS.setPath("java.lang");
        typeS.setName("Object");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#22");
        typeS.setPath("java.lang");
        typeS.setName("Package");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#23");
        typeS.setPath("java.lang");
        typeS.setName("Short");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#24");
        typeS.setPath("java.lang");
        typeS.setName("String");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#25");
        typeS.setPath("java.lang");
        typeS.setName("StringBuffer");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#26");
        typeS.setPath("java.lang");
        typeS.setName("StringBuilder");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#27");
        typeS.setPath("java.lang");
        typeS.setName("Thread");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#28");
        typeS.setPath("java.lang");
        typeS.setName("Void");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#29");
        typeS.setPath("java.util");
        typeS.setName("Arrays");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#30");
        typeS.setPath("java.util");
        typeS.setName("Collections");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#31");
        typeS.setPath("java.util");
        typeS.setName("Date");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#32");
        typeS.setPath("java.util");
        typeS.setName("EnumMap");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#33");
        typeS.setPath("java.util");
        typeS.setName("EnumSet");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#34");
        typeS.setPath("java.util");
        typeS.setName("EventListener");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#35");
        typeS.setPath("java.util");
        typeS.setName("HashMap");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#36");
        typeS.setPath("java.util");
        typeS.setName("HashSet");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#37");
        typeS.setPath("java.util");
        typeS.setName("HashTable");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#38");
        typeS.setPath("java.util");
        typeS.setName("LinkedHashMap");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#39");
        typeS.setPath("java.util");
        typeS.setName("LinkedHashSet");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#40");
        typeS.setPath("java.util");
        typeS.setName("LinkedList");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#41");
        typeS.setPath("java.util");
        typeS.setName("List");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#42");
        typeS.setPath("java.util");
        typeS.setName("Map");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#43");
        typeS.setPath("java.util");
        typeS.setName("Queue");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#44");
        typeS.setPath("java.util");
        typeS.setName("Random");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#45");
        typeS.setPath("java.util");
        typeS.setName("Scanner");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#46");
        typeS.setPath("java.util");
        typeS.setName("Set");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#47");
        typeS.setPath("java.util");
        typeS.setName("Stack");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#48");
        typeS.setPath("java.util");
        typeS.setName("Timer");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#49");
        typeS.setPath("java.util");
        typeS.setName("TreeMap");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#50");
        typeS.setPath("java.util");
        typeS.setName("TreeSet");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        typeS = new TypeSmarty();
        typeS.setId("TYPE#51");
        typeS.setPath("java.util");
        typeS.setName("Vector");
        typeS.setValue("null");
        typeS.setPrimitive(false);
        typeS.setStandard(true);
        lstSMarty.add(typeS);

        for (Class clazz : architecture.getClasses()){
            typeS = new TypeSmarty();
            typeS.setId(clazz.getId());
            typeS.setPath(clazz.getName());
            typeS.setName(clazz.getName());
            typeS.setValue("null");
            typeS.setPrimitive(false);
            typeS.setStandard(false);
            lstSMarty.add(typeS);
        }
        for(Package pkg : architecture.getAllPackages()){
            for (Class clazz : pkg.getAllClasses()){
                typeS = new TypeSmarty();
                typeS.setId(clazz.getId());
                typeS.setPath(clazz.getName());
                typeS.setName(clazz.getName());
                typeS.setValue("null");
                typeS.setPrimitive(false);
                typeS.setStandard(false);
                lstSMarty.add(typeS);
            }
            for (Interface clazz : pkg.getAllInterfaces()){
                typeS = new TypeSmarty();
                typeS.setId(clazz.getId());
                typeS.setPath(clazz.getName());
                typeS.setName(clazz.getName());
                typeS.setValue("null");
                typeS.setPrimitive(false);
                typeS.setStandard(false);
                lstSMarty.add(typeS);
            }
        }

        for (Interface clazz : architecture.getInterfaces()){
            typeS = new TypeSmarty();
            typeS.setId(clazz.getId());
            typeS.setPath(clazz.getName());
            typeS.setName(clazz.getName());
            typeS.setValue("null");
            typeS.setPrimitive(false);
            typeS.setStandard(false);
            lstSMarty.add(typeS);
        }

        architecture.setLstTypes(lstSMarty);
    }

    private void createConcernList(Architecture architecture){
        ArrayList<Concern> lstConcern = new ArrayList<>();

        Concern newCon;
        newCon = new Concern();
        newCon.setId("STEREOTYPE#1");
        newCon.setName("mandatory");
        newCon.setPrimitive(true);
        lstConcern.add(newCon);

        newCon = new Concern();
        newCon.setId("STEREOTYPE#2");
        newCon.setName("optional");
        newCon.setPrimitive(true);
        lstConcern.add(newCon);

        newCon = new Concern();
        newCon.setId("STEREOTYPE#3");
        newCon.setName("variationPoint");
        newCon.setPrimitive(true);
        lstConcern.add(newCon);

        newCon = new Concern();
        newCon.setId("STEREOTYPE#4");
        newCon.setName("alternative_OR");
        newCon.setPrimitive(true);
        lstConcern.add(newCon);

        newCon = new Concern();
        newCon.setId("STEREOTYPE#5");
        newCon.setName("alternative_XOR");
        lstConcern.add(newCon);

        newCon = new Concern();
        newCon.setId("STEREOTYPE#6");
        newCon.setName("requires");
        newCon.setPrimitive(true);
        lstConcern.add(newCon);


        newCon = new Concern();
        newCon.setId("STEREOTYPE#7");
        newCon.setName("mutex");
        newCon.setPrimitive(true);
        lstConcern.add(newCon);
/*
        for(Class clazz: architecture.getAllClasses()){
            for(Concern c : clazz.getAllConcerns()){
                if(!concernExist(lstConcern, c)){
                    c.setPrimitive(false);
                    lstConcern.add(c);
                }
            }
        }

 */
        for(Concern c : architecture.getAllConcerns()) {
            if (!concernExist(lstConcern, c)) {
                c.setPrimitive(false);
                lstConcern.add(c);
            }
        }

        int id = 8;
        String newID = "STEREOTYPE#"+id;
        for(Concern c : lstConcern){
            if(c.getId() == null){
                while (concernIDExist(lstConcern, newID)){
                    id++;
                    newID = "STEREOTYPE#"+id;
                }
                c.setId(newID);
                //id++;
            }
        }
        architecture.setLstConcerns(lstConcern);
    }

    private boolean concernExist(ArrayList<Concern> lstConcern, Concern concern){
        for(Concern c : lstConcern){
            if(c.getName().equals(concern.getName()))
                return true;

        }
        return  false;
    }

    private boolean concernIDExist(ArrayList<Concern> lstConcern, String id){
        for(Concern c : lstConcern) {
            if (c.getId() != null) {
                if (c.getId().equals(id))
                    return true;

            }
        }
        return  false;
    }
}
