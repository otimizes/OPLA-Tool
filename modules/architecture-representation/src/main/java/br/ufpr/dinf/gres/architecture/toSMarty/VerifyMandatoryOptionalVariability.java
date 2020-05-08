package br.ufpr.dinf.gres.architecture.toSMarty;


import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Interface;
import br.ufpr.dinf.gres.architecture.representation.Package;

// if input PLA is from Papyrus, need to verify variability to verify if the element is mandatory or optional
public class VerifyMandatoryOptionalVariability {

    public VerifyMandatoryOptionalVariability(Architecture architecture) {
        for (Class clazz : architecture.getClasses()) {
            if (clazz.getVariant() != null) {
                if (clazz.getVariant().getVariantType().toLowerCase().equals("mandatory")) {
                    clazz.setMandatory(true);
                }
                if (clazz.getVariant().getVariantType().toLowerCase().equals("optional")) {
                    clazz.setMandatory(false);
                }
            }
        }
        for (Interface clazz : architecture.getInterfaces()) {
            if (clazz.getVariant() != null) {
                //System.out.println(clazz.getVariant().getVariantType());
                if (clazz.getVariant().getVariantType().toLowerCase().equals("mandatory")) {
                    clazz.setMandatory(true);
                }
                if (clazz.getVariant().getVariantType().toLowerCase().equals("optional")) {
                    clazz.setMandatory(false);
                }
            }
        }
        for (Package pkg : architecture.getAllPackages()) {
            for (Class clazz : pkg.getAllClasses()) {
                if (clazz.getVariant() != null) {
                    if (clazz.getVariant().getVariantType().toLowerCase().equals("mandatory")) {
                        clazz.setMandatory(true);
                    }
                    if (clazz.getVariant().getVariantType().toLowerCase().equals("optional")) {
                        clazz.setMandatory(false);
                    }
                }
            }
            for (Interface clazz : pkg.getAllInterfaces()) {
                if (clazz.getVariant() != null) {
                    //System.out.println(clazz.getVariant().getVariantType());
                    if (clazz.getVariant().getVariantType().toLowerCase().equals("mandatory")) {
                        clazz.setMandatory(true);

                    }
                    if (clazz.getVariant().getVariantType().toLowerCase().equals("optional")) {
                        clazz.setMandatory(false);

                    }
                }
            }
            for (Package p : pkg.getNestedPackages()) {
                verifyMandatorySubPackage(p);
            }
        }
    }

    public void verifyMandatorySubPackage(Package pkg) {
        for (Class clazz : pkg.getAllClasses()) {
            if (clazz.getVariant() != null) {
                if (clazz.getVariant().getVariantType().toLowerCase().equals("mandatory")) {
                    clazz.setMandatory(true);
                }
                if (clazz.getVariant().getVariantType().toLowerCase().equals("optional")) {
                    clazz.setMandatory(false);
                }
            }
        }
        for (Interface clazz : pkg.getAllInterfaces()) {
            if (clazz.getVariant() != null) {
                if (clazz.getVariant().getVariantType().toLowerCase().equals("mandatory")) {
                    clazz.setMandatory(true);
                }
                if (clazz.getVariant().getVariantType().toLowerCase().equals("optional")) {
                    clazz.setMandatory(false);
                }
            }
        }
        for (Package p : pkg.getNestedPackages()) {
            verifyMandatorySubPackage(p);
        }
    }

}
