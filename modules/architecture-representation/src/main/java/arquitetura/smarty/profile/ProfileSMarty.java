package arquitetura.smarty.profile;

import arquitetura.exceptions.EnumerationNotFoundException;
import arquitetura.exceptions.ModelNotFoundException;
import arquitetura.exceptions.StereotypeNotFoundException;
import arquitetura.helpers.Uml2Helper;
import arquitetura.helpers.Uml2HelperFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.*;

import java.io.IOException;

/**
 * Classe usada para gerar o arquivo de profile do smarty
 * <p>
 * Mude o Path onde vc quer salvar o arquivo.
 *
 * @author edipofederle<edipofederle@gmail.com>
 */
public class ProfileSMarty {


    private static Uml2Helper helper;

    static {
        helper = Uml2HelperFactory.getUml2Helper();
    }

    private Profile profile;
    private Enumeration bindingTime;

    public ProfileSMarty(String profileName) throws ModelNotFoundException {
        this.profile = helper.createProfile(profileName);
        this.profile.setName("smartyProfile");

        createBindingTimeEnumeration();
        createVariationPointStereotype();
        createVariantStereotype();
        createVariantSpecializationStereotypes();
        createVariabilityStereotype();
        createInterfaceStereotype();
        createConcernStereotype();

        try {
            URI profileURI = URI.createFileURI("/Users/edipofederle/Desktop/" + profileName); // Local para salvar o arquivo
            helper.saveResources(this.profile, profileURI);
            System.out.println("\n");
            System.out.println("Perfil " + profileName + " salvo com sucesso em: " + profileURI);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void createConcernStereotype() throws ModelNotFoundException {
        Stereotype concern = helper.createStereotype(this.profile, "concern", false);

        Class klassMetaClass = helper.referenceMetaclass(this.profile,
                UMLPackage.Literals.STEREOTYPE.getName());
        helper.createExtension(klassMetaClass, concern, false);
    }


    private void createInterfaceStereotype() throws ModelNotFoundException {
        Stereotype _interface = helper.createStereotype(this.profile, "interface", false);
        Class classMetaClass = helper.referenceMetaclass(this.profile,
                UMLPackage.Literals.CLASS.getName());
        helper.createExtension(classMetaClass, _interface, false);

    }


    private void createVariabilityStereotype() throws ModelNotFoundException {
        Stereotype variability = helper.createStereotype(this.profile, "variability", false);

        Class commentMetaClass = helper.referenceMetaclass(this.profile,
                UMLPackage.Literals.COMMENT.getName());
        helper.createExtension(commentMetaClass, variability, false);

        helper.createAttribute(variability, "name", helper.getPrimitiveType("String"),
                1, 1);
        helper.createAttribute(variability, "minSelection",
                helper.getPrimitiveType("Integer"), 1, 1);
        helper.createAttribute(variability, "maxSelection",
                helper.getPrimitiveType("Integer"), 1, 1);

        Enumeration bindingTime = null;
        try {
            bindingTime = (Enumeration) helper.getEnumerationByName(this.profile, "BindingTime");
        } catch (EnumerationNotFoundException e) {
            e.printStackTrace();
        }

        helper.createAttribute(variability, "bindingTime", bindingTime, 1, 1);
        helper.createAttribute(variability, "allowAddingVar", helper.getPrimitiveType("Boolean"), 1, 1);
        helper.createAttribute(variability, "variants", helper.getPrimitiveType("String"), 1, 1);

    }

    private void createBindingTimeEnumeration() {
        try {
            this.bindingTime = helper.createEnumeration(profile, "BindingTime");
            helper.createEnumerationLiteral(this.bindingTime, "DESIGN_TIME");
            helper.createEnumerationLiteral(this.bindingTime, "LINK_TIME");
            helper.createEnumerationLiteral(this.bindingTime, "COMPILE_TIME");
            helper.createEnumerationLiteral(this.bindingTime, "RUN_TIME");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void createVariationPointStereotype() throws ModelNotFoundException {
        Stereotype variantPoint = helper.createStereotype(this.profile, "variationPoint", false);

        Class classMetaClass = helper.referenceMetaclass(this.profile, UMLPackage.Literals.CLASS.getName());
        helper.createExtension(classMetaClass, variantPoint, false);

        helper.createAttribute(variantPoint, "numberOfVariants", helper.getPrimitiveType("Integer"), 1, 1);
        helper.createAttribute(variantPoint, "variants", helper.getPrimitiveType("String"), 1, 1);
        helper.createAttribute(variantPoint, "variabilities", helper.getPrimitiveType("String"), 1, 1);

        Enumeration bindingTime = null;
        try {
            bindingTime = (Enumeration) helper.getEnumerationByName(this.profile, "BindingTime");
        } catch (EnumerationNotFoundException e) {
            e.printStackTrace();
        }

        helper.createAttribute(variantPoint, "bindingTime", bindingTime, 1, 1);
    }

    private void createVariantStereotype() throws ModelNotFoundException {
        Stereotype variant = helper.createStereotype(this.profile, "variant", true);

        Class classMetaClass = helper.referenceMetaclass(this.profile, UMLPackage.Literals.CLASS.getName());
        helper.createExtension(classMetaClass, variant, false);

        helper.createAttribute(variant, "rootVP", helper.getPrimitiveType("String"), 1, 1);
        helper.createAttribute(variant, "variabilities", helper.getPrimitiveType("String"), 1, 1);
    }

    private void createVariantSpecializationStereotypes() {
        Stereotype mandatory = helper.createStereotype(this.profile, "mandatory", false);
        Stereotype optional = helper.createStereotype(this.profile, "optional", false);
        Stereotype alternative_OR = helper.createStereotype(this.profile, "alternative_OR", false);
        Stereotype alternative_XOR = helper.createStereotype(this.profile, "alternative_XOR", false);

        Stereotype variant = null;
        try {
            variant = (Stereotype) helper.getStereotypeByName(this.profile, "variant");
        } catch (StereotypeNotFoundException e) {
            e.printStackTrace();
        }
        helper.createGeneralization(mandatory, variant);
        helper.createGeneralization(optional, variant);
        helper.createGeneralization(alternative_OR, variant);
        helper.createGeneralization(alternative_XOR, variant);
    }


    public Profile getProfile() {
        return profile;
    }

}