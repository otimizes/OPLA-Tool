package br.ufpr.dinf.gres.architecture.toSMarty;

import br.ufpr.dinf.gres.architecture.representation.*;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Package;

import java.io.PrintWriter;
import java.util.ArrayList;

public class SaveConcernLinkSMart {

    public SaveConcernLinkSMart(Architecture architecture, PrintWriter printWriter) {
        String halfTab = "  ";
        String tab = "    ";

        printWriter.write("\n" + halfTab + "<links>");


        ArrayList<String> variantStereotype = new ArrayList<>();

        // verufy if the element is variation point or variant
        for (Variability variability : architecture.getAllVariabilities()) {
            VariationPoint vp = variability.getVariationPoint();
            // add na lista de variationpoint
            if (vp == null)
                continue;
            if (vp.getVariationPointElement() == null)
                continue;
            if (!variantStereotype.contains(vp.getVariationPointElement().getId()))
                variantStereotype.add(vp.getVariationPointElement().getId());
            //printWriter.write("\n\t<link element=\""+vp.getVariationPointElement().getId()+"\" stereotype=\"STEREOTYPE#3\"/>");
            for (Variant v : vp.getVariants()) {
                if (!variantStereotype.contains(v.getVariantElement().getId()))
                    variantStereotype.add(v.getVariantElement().getId());
            }
        }

        //<link element="CLASS#4" stereotype="STEREOTYPE#10"/>
        for (Class clazz : architecture.getClasses()) {

            String concernID = "";

            if (!variantStereotype.contains(clazz.getId()) && !variantStereotype.contains(clazz.getId())) {
                if (clazz.isMandatory())
                    printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#1\"/>");
                else
                    printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#2\"/>");
            }

            for (Concern c : clazz.getOwnConcerns()) {
                concernID = architecture.findConcernByName(c.getName()).getId();
                printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"" + concernID + "\"/>");
            }
            for (Method m : clazz.getAllMethods()) {
                for (Concern c : m.getOwnConcerns()) {
                    concernID = architecture.findConcernByName(c.getName()).getId();
                    printWriter.write("\n" + tab + "<link element=\"" + m.getId() + "\" stereotype=\"" + concernID + "\"/>");
                }
            }
            for (Attribute m : clazz.getAllAttributes()) {
                for (Concern c : m.getOwnConcerns()) {
                    concernID = architecture.findConcernByName(c.getName()).getId();
                    printWriter.write("\n" + tab + "<link element=\"" + m.getId() + "\" stereotype=\"" + concernID + "\"/>");
                }
            }
        }

        for (Interface clazz : architecture.getInterfaces()) {

            String concernID = "";

            if (clazz.isMandatory())
                printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#1\"/>");
            else
                printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#2\"/>");


            for (Concern c : clazz.getOwnConcerns()) {
                concernID = architecture.findConcernByName(c.getName()).getId();
                printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"" + concernID + "\"/>");
            }
            for (Method m : clazz.getMethods()) {
                for (Concern c : m.getOwnConcerns()) {
                    concernID = architecture.findConcernByName(c.getName()).getId();
                    printWriter.write("\n" + tab + "<link element=\"" + m.getId() + "\" stereotype=\"" + concernID + "\"/>");
                }
            }
        }


        for (Package pkg : architecture.getAllPackages()) {
            for (Concern c : pkg.getConcernsOnlyFromElementWithoutMethodOrAttribute()) {
                printWriter.write("\n" + tab + "<link element=\"" + pkg.getId() + "\" stereotype=\"" + c.getId() + "\"/>");
            }

            String concernID = "";

            for (Class clazz : pkg.getAllClasses()) {

                if (!variantStereotype.contains(clazz.getId()) && !variantStereotype.contains(clazz.getId())) {
                    if (clazz.isMandatory())
                        printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#1\"/>");
                    else
                        printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#2\"/>");
                }
                for (Concern c : clazz.getOwnConcerns()) {
                    concernID = architecture.findConcernByName(c.getName()).getId();
                    printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"" + concernID + "\"/>");
                }
                for (Method m : clazz.getAllMethods()) {
                    for (Concern c : m.getOwnConcerns()) {
                        concernID = architecture.findConcernByName(c.getName()).getId();
                        printWriter.write("\n" + tab + "<link element=\"" + m.getId() + "\" stereotype=\"" + concernID + "\"/>");
                    }
                }
                for (Attribute m : clazz.getAllAttributes()) {
                    for (Concern c : m.getOwnConcerns()) {
                        concernID = architecture.findConcernByName(c.getName()).getId();
                        printWriter.write("\n" + tab + "<link element=\"" + m.getId() + "\" stereotype=\"" + concernID + "\"/>");
                    }
                }
            }

            for (Interface clazz : pkg.getAllInterfaces()) {

                if (clazz.isMandatory())
                    printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#1\"/>");
                else
                    printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#2\"/>");

                for (Concern c : clazz.getOwnConcerns()) {
                    concernID = architecture.findConcernByName(c.getName()).getId();
                    printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"" + concernID + "\"/>");
                }
                for (Method m : clazz.getMethods()) {
                    for (Concern c : m.getOwnConcerns()) {
                        concernID = architecture.findConcernByName(c.getName()).getId();
                        printWriter.write("\n" + tab + "<link element=\"" + m.getId() + "\" stereotype=\"" + concernID + "\"/>");
                    }
                }
            }

            saveSubPackageLink(pkg, printWriter, architecture, variantStereotype);
        }

        // stereotype dos pontos de variação e variantes só se não tiver sido descartado
        ArrayList<String> variationPointStereotype = new ArrayList<>();
        variantStereotype = new ArrayList<>();
        ArrayList<String> discardedVariability = getDiscardedVariability(architecture);

        for (Variability variability : architecture.getAllVariabilities()) {
            if (discardedVariability.contains(variability.getId())) {
                continue;
            }
            VariationPoint vp = variability.getVariationPoint();
            if (vp == null)
                continue;
            if (vp.getVariationPointElement() == null)
                continue;
            ;
            // add na lista de variationpoint
            if (!variationPointStereotype.contains(vp.getVariationPointElement().getId())) {
                printWriter.write("\n" + tab + "<link element=\"" + vp.getVariationPointElement().getId() + "\" stereotype=\"STEREOTYPE#3\"/>");
                variationPointStereotype.add(vp.getVariationPointElement().getId());
            }
            //printWriter.write("\n\t<link element=\""+vp.getVariationPointElement().getId()+"\" stereotype=\"STEREOTYPE#3\"/>");
            for (Variant v : vp.getVariants()) {
                if (!variantStereotype.contains(v.getVariantElement().getId())) {
                    //System.out.println(v.getVariantElement().getId());
                    //System.out.println(v.getVariantType());
                    //System.out.println(architecture.findConcernByName(v.getVariantType()).getId());
                    printWriter.write("\n" + tab + "<link element=\"" + v.getVariantElement().getId() + "\" stereotype=\"" + architecture.findConcernByName(v.getVariantType()).getId() + "\"/>");
                    variantStereotype.add(v.getVariantElement().getId());
                }
            }
        }
        // se for ponto de variação mas não for variant, tem que adicioanr o stereotype mandatory ou optional
        for (String vpID : variationPointStereotype) {
            if (!variantStereotype.contains(vpID)) {
                Element clazz = architecture.findElementById(vpID);
                if (clazz.isMandatory())
                    printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#1\"/>");
                else
                    printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#2\"/>");
            }
        }

        printWriter.write("\n" + halfTab + "</links>");

    }

    public ArrayList<String> getDiscardedVariability(Architecture architecture){
        ArrayList<String> discardedVariability = new ArrayList<>();
        for (Variability variability : architecture.getAllVariabilities()) {
            VariationPoint vp = variability.getVariationPoint();
            if (vp == null) {
                continue;
            }
            if (vp.getVariationPointElement() == null) {
                continue;
            }
            if (architecture.findElementById(vp.getVariationPointElement().getId()) == null) {
                discardedVariability.add(variability.getId());
                continue;
            }
            for (Variant v : variability.getVariants()) {
                if (architecture.findElementById(v.getVariantElement().getId()) == null) {
                    discardedVariability.add(variability.getId());
                    break;
                }
            }
        }
        return discardedVariability;
    }

    private void saveSubPackageLink(Package pkg1, PrintWriter printWriter, Architecture architecture, ArrayList<String> variantStereotype) {
        String halfTab = "  ";
        String tab = "    ";
        String concernID = "";
        for (Package pkg : pkg1.getNestedPackages()) {

            for (Concern c : pkg.getConcernsOnlyFromElementWithoutMethodOrAttribute()) {
                concernID = architecture.findConcernByName(c.getName()).getId();
                printWriter.write("\n" + tab + "t<link element=\"" + pkg.getId() + "\" stereotype=\"" + concernID + "\"/>");
            }

            for (Class clazz : pkg.getAllClasses()) {

                if (!variantStereotype.contains(clazz.getId()) && !variantStereotype.contains(clazz.getId())) {
                    if (clazz.isMandatory())
                        printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#1\"/>");
                    else
                        printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#2\"/>");
                }
                for (Concern c : clazz.getOwnConcerns()) {
                    concernID = architecture.findConcernByName(c.getName()).getId();
                    printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"" + concernID + "\"/>");
                }
                for (Method m : clazz.getAllMethods()) {
                    for (Concern c : m.getOwnConcerns()) {
                        concernID = architecture.findConcernByName(c.getName()).getId();
                        printWriter.write("\n" + tab + "<link element=\"" + m.getId() + "\" stereotype=\"" + concernID + "\"/>");
                    }
                }
                for (Attribute m : clazz.getAllAttributes()) {
                    for (Concern c : m.getOwnConcerns()) {
                        concernID = architecture.findConcernByName(c.getName()).getId();
                        printWriter.write("\n" + tab + "<link element=\"" + m.getId() + "\" stereotype=\"" + concernID + "\"/>");
                    }
                }
            }

            for (Interface inter : pkg.getAllInterfaces()) {

                if (inter.isMandatory())
                    printWriter.write("\n" + tab + "<link element=\"" + inter.getId() + "\" stereotype=\"STEREOTYPE#1\"/>");
                else
                    printWriter.write("\n" + tab + "<link element=\"" + inter.getId() + "\" stereotype=\"STEREOTYPE#2\"/>");

                for (Concern c : inter.getOwnConcerns()) {
                    concernID = architecture.findConcernByName(c.getName()).getId();
                    printWriter.write("\n" + tab + "<link element=\"" + inter.getId() + "\" stereotype=\"" + concernID + "\"/>");
                }
                for (Method m : inter.getMethods()) {
                    for (Concern c : m.getOwnConcerns()) {
                        concernID = architecture.findConcernByName(c.getName()).getId();
                        printWriter.write("\n" + tab + "<link element=\"" + m.getId() + "\" stereotype=\"" + concernID + "\"/>");
                    }
                }
            }
            saveSubPackageLink(pkg, printWriter, architecture, variantStereotype);
        }
    }


}
