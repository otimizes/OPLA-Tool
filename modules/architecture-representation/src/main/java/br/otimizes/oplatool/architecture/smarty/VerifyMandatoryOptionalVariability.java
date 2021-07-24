package br.otimizes.oplatool.architecture.smarty;


import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.Package;

/**
 * Verify if an element is mandatory or optional using the variability
 * if the element is an variant and the type is optional, then the element is optional
 */
public class VerifyMandatoryOptionalVariability {

    public VerifyMandatoryOptionalVariability() {
    }

    private static final VerifyMandatoryOptionalVariability INSTANCE = new VerifyMandatoryOptionalVariability();

    public static VerifyMandatoryOptionalVariability getInstance() {
        return INSTANCE;
    }

    /**
     * Verify if an element is mandatory or optional using the variability
     * if the element is an variant and the type is optional, then the element is optional
     * if the element is an variant and the type is mandatory, then the element is mandatory
     *
     * @param architecture - the architecture to be decoded
     */
    public void Verify(Architecture architecture) {
        for (Class clazz : architecture.getClasses()) {
            if (clazz.getVariant() != null) {
                if (clazz.getVariant().getVariantType().equalsIgnoreCase("mandatory")) {
                    clazz.setMandatory(true);
                }
                if (clazz.getVariant().getVariantType().equalsIgnoreCase("optional")) {
                    clazz.setMandatory(false);
                }
            }
        }
        for (Interface clazz : architecture.getInterfaces()) {
            if (clazz.getVariant() != null) {
                if (clazz.getVariant().getVariantType().equalsIgnoreCase("mandatory")) {
                    clazz.setMandatory(true);
                }
                if (clazz.getVariant().getVariantType().equalsIgnoreCase("optional")) {
                    clazz.setMandatory(false);
                }
            }
        }
        for (Package pkg : architecture.getAllPackages()) {
            for (Class clazz : pkg.getAllClasses()) {
                if (clazz.getVariant() != null) {
                    if (clazz.getVariant().getVariantType().equalsIgnoreCase("mandatory")) {
                        clazz.setMandatory(true);
                    }
                    if (clazz.getVariant().getVariantType().equalsIgnoreCase("optional")) {
                        clazz.setMandatory(false);
                    }
                }
            }
            for (Interface clazz : pkg.getAllInterfaces()) {
                if (clazz.getVariant() != null) {
                    if (clazz.getVariant().getVariantType().equalsIgnoreCase("mandatory")) {
                        clazz.setMandatory(true);
                    }
                    if (clazz.getVariant().getVariantType().equalsIgnoreCase("optional")) {
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
                if (clazz.getVariant().getVariantType().equalsIgnoreCase("mandatory")) {
                    clazz.setMandatory(true);
                }
                if (clazz.getVariant().getVariantType().equalsIgnoreCase("optional")) {
                    clazz.setMandatory(false);
                }
            }
        }
        for (Interface clazz : pkg.getAllInterfaces()) {
            if (clazz.getVariant() != null) {
                if (clazz.getVariant().getVariantType().equalsIgnoreCase("mandatory")) {
                    clazz.setMandatory(true);
                }
                if (clazz.getVariant().getVariantType().equalsIgnoreCase("optional")) {
                    clazz.setMandatory(false);
                }
            }
        }
        for (Package p : pkg.getNestedPackages()) {
            verifyMandatorySubPackage(p);
        }
    }

}
