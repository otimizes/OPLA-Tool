package br.otimizes.oplatool.architecture.smarty;


import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.*;

import java.util.ArrayList;

/**
 * Create a new list of types using default types + new types created by elements
 */
public class GenerateTypeSMarty {


    public GenerateTypeSMarty() {
    }

    private static final GenerateTypeSMarty INSTANCE = new GenerateTypeSMarty();

    public static GenerateTypeSMarty getInstance() {
        return INSTANCE;
    }

    /**
     * Create a new list of types to be added to lstTypes in the architecture
     *
     * @param architecture - the architecture to be decoded
     */
    public void generate(Architecture architecture){
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

        for (Class clazz : architecture.getClasses()) {
            typeS = new TypeSmarty();
            typeS.setId(clazz.getId());
            typeS.setPath(clazz.getName());
            typeS.setName(clazz.getName());
            typeS.setValue("null");
            typeS.setPrimitive(false);
            typeS.setStandard(false);
            lstSMarty.add(typeS);
        }
        for (Package pkg : architecture.getAllPackages()) {
            for (Class clazz : pkg.getAllClasses()) {
                typeS = new TypeSmarty();
                typeS.setId(clazz.getId());
                typeS.setPath(clazz.getName());
                typeS.setName(clazz.getName());
                typeS.setValue("null");
                typeS.setPrimitive(false);
                typeS.setStandard(false);
                lstSMarty.add(typeS);
            }
            for (Interface clazz : pkg.getAllInterfaces()) {
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
        for (Interface clazz : architecture.getInterfaces()) {
            typeS = new TypeSmarty();
            typeS.setId(clazz.getId());
            typeS.setPath(clazz.getName());
            typeS.setName(clazz.getName());
            typeS.setValue("null");
            typeS.setPrimitive(false);
            typeS.setStandard(false);
            lstSMarty.add(typeS);
        }
        architecture.setTypes(lstSMarty);
    }
}
