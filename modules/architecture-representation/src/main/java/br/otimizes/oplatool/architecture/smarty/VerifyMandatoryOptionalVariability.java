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
    public void verify(Architecture architecture) {
        for (Class aClass : architecture.getClasses()) {
            if (aClass.getVariant() != null) {
                if (aClass.getVariant().getVariantType().equalsIgnoreCase("mandatory")) {
                    aClass.setMandatory(true);
                }
                if (aClass.getVariant().getVariantType().equalsIgnoreCase("optional")) {
                    aClass.setMandatory(false);
                }
            }
        }
        for (Interface anInterface : architecture.getInterfaces()) {
            if (anInterface.getVariant() != null) {
                if (anInterface.getVariant().getVariantType().equalsIgnoreCase("mandatory")) {
                    anInterface.setMandatory(true);
                }
                if (anInterface.getVariant().getVariantType().equalsIgnoreCase("optional")) {
                    anInterface.setMandatory(false);
                }
            }
        }
        for (Package aPackage : architecture.getAllPackages()) {
            setVariantInElements(aPackage);
        }
    }

    public void verifyMandatorySubPackage(Package pkg) {
        setVariantInElements(pkg);
    }

    private void setVariantInElements(Package pkg) {
        for (Class aClass : pkg.getAllClasses()) {
            if (aClass.getVariant() != null) {
                if (aClass.getVariant().getVariantType().equalsIgnoreCase("mandatory")) {
                    aClass.setMandatory(true);
                }
                if (aClass.getVariant().getVariantType().equalsIgnoreCase("optional")) {
                    aClass.setMandatory(false);
                }
            }
        }
        for (Interface anInterface : pkg.getAllInterfaces()) {
            if (anInterface.getVariant() != null) {
                if (anInterface.getVariant().getVariantType().equalsIgnoreCase("mandatory")) {
                    anInterface.setMandatory(true);
                }
                if (anInterface.getVariant().getVariantType().equalsIgnoreCase("optional")) {
                    anInterface.setMandatory(false);
                }
            }
        }
        for (Package aPackage : pkg.getNestedPackages()) {
            verifyMandatorySubPackage(aPackage);
        }
    }
}
