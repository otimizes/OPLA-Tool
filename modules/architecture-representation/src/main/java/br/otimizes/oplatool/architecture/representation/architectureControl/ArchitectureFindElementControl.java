package br.otimizes.oplatool.architecture.representation.architectureControl;

import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class has the implementation of all the method from Architecture that is used to find an specific element from architecture
 */
public class ArchitectureFindElementControl {
    public ArchitectureFindElementControl() {
    }

    private static final ArchitectureFindElementControl INSTANCE = new ArchitectureFindElementControl();

    public static ArchitectureFindElementControl getInstance() {
        return INSTANCE;
    }

    public Concern findConcernByName(Architecture architecture, String name) {
        for (Concern c : architecture.getConcerns()) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    public TypeSmarty findTypeSMartyByID(Architecture architecture, String id) {
        for (TypeSmarty typeSmarty : architecture.getTypes()) {
            if (typeSmarty.getId().equals(id))
                return typeSmarty;
        }
        return null;
    }

    public TypeSmarty findTypeSMartyByName(Architecture architecture, String name) {
        for (TypeSmarty typeSmarty : architecture.getTypes()) {
            if (typeSmarty.getName().equals(name))
                return typeSmarty;
        }
        return findObjectType(architecture);
    }

    public TypeSmarty findReturnTypeSMartyByName(Architecture architecture, String name) {
        if (name == null) {
            return findVoidType(architecture);
        }
        if (name.length() == 0) {
            return findVoidType(architecture);
        }
        for (TypeSmarty typeSmarty : architecture.getTypes()) {
            if (typeSmarty.getName().equals(name))
                return typeSmarty;
        }
        return findObjectType(architecture);
    }

    private TypeSmarty findObjectType(Architecture architecture) {
        for (TypeSmarty typeSmarty : architecture.getTypes()) {
            if (typeSmarty.getName().equals("Object"))
                return typeSmarty;
        }
        return null;
    }

    private TypeSmarty findVoidType(Architecture architecture) {
        for (TypeSmarty typeSmarty : architecture.getTypes()) {
            if (typeSmarty.getName().equals("void"))
                return typeSmarty;
        }
        return null;
    }

    public Element findElementByNameInPackageAndSubPackage(Architecture architecture, String elementName) {
        for (Class clazz_ : architecture.getAllClasses()) {
            if (clazz_.getName().equals(elementName))
                return clazz_;
        }
        for (Interface inter : architecture.getAllInterfaces()) {
            if (inter.getName().equals(elementName))
                return inter;
        }

        for (Package pkg : architecture.getAllPackages()) {
            Element clazz_ = findElementInPackage(elementName, pkg);
            if (clazz_ != null) return clazz_;
        }
        return null;
    }

    private Element findElementByNameInSubPackage(Package pkg1, String elementName) {
        for (Package pkg : pkg1.getNestedPackages()) {
            Element clazz_ = findElementInPackage(elementName, pkg);
            if (clazz_ != null) return clazz_;
        }
        return null;
    }

    private Element findElementInPackage(String elementName, Package pkg) {
        for (Class clazz_ : pkg.getAllClasses()) {
            if (clazz_.getName().equals(elementName))
                return clazz_;
        }
        for (Interface inter : pkg.getAllInterfaces()) {
            if (inter.getName().equals(elementName))
                return inter;
        }
        Element e1 = findElementByNameInSubPackage(pkg, elementName);
        if (e1 != null)
            return e1;
        return null;
    }

    public Element findElement(Architecture architecture, String name, String type) {
        if (type.equalsIgnoreCase("class")) {
            for (Element element : architecture.getAllClasses()) {
                if (element.getName().equalsIgnoreCase(name))
                    return element;
            }
            for (Package p : architecture.getAllPackages()) {
                for (Element element : p.getElements()) {
                    if (element.getName().equalsIgnoreCase(name))
                        return element;
                }
            }
        }
        if (type.equalsIgnoreCase("interface")) {
            for (Element element : architecture.getAllInterfaces()) {
                if (element.getName().equalsIgnoreCase(name))
                    return element;
            }
            for (Package p : architecture.getAllPackages()) {
                for (Element element : p.getElements()) {
                    if (element.getName().equalsIgnoreCase(name))
                        return element;
                }
            }
        }
        if (type.equalsIgnoreCase("package")) {
            for (Element element : architecture.getAllPackages())
                if (element.getName().equalsIgnoreCase(name))
                    return element;
        }
        return null;
    }

    public List<Class> findClassByName(Architecture architecture, String className) {
        List<Class> classesFound = new ArrayList<>();
        for (Class klass : architecture.getAllClasses())
            if (className.trim().equalsIgnoreCase(klass.getName().trim()))
                classesFound.add(klass);

        for (Package p : architecture.getAllPackages())
            for (Class klass : p.getAllClasses())
                if (className.trim().equalsIgnoreCase(klass.getName().trim()))
                    classesFound.add(klass);

        if (classesFound.isEmpty())
            return null;
        return classesFound;
    }

    public Element findElementByName(Architecture architecture, String elementName) {
        Element element = searchRecursivelyInPackage(architecture.getAllPackages(), elementName);
        if (element == null) {
            for (Class klass : architecture.getAllClasses())
                if (klass.getName().equals(elementName))
                    return klass;
            for (Interface inter : architecture.getAllInterfaces())
                if (inter.getName().equals(elementName))
                    return inter;
        }
        if (element == null)
            System.out.println("No element called: " + elementName + " found");
        return element;
    }

    private Element searchRecursivelyInPackage(Set<Package> packages, String elementName) {
        for (Package p : packages) {
            for (Element element : p.getElements()) {
                if (element.getName().equals(elementName))
                    return element;
                Element tempElement = searchRecursivelyInPackage(p.getNestedPackages(), elementName);
                if (tempElement != null)
                    return tempElement;
            }
            if (p.getName().equals(elementName))
                return p;
            searchRecursivelyInPackage(p.getNestedPackages(), elementName);
        }
        return null;
    }

    public Interface findInterfaceByName(Architecture architecture, String interfaceName) {
        for (Interface interfaceFromArchitecture : architecture.getAllInterfaces())
            if (interfaceName.equalsIgnoreCase(interfaceFromArchitecture.getName()))
                return interfaceFromArchitecture;
        for (Package p : architecture.getAllPackages())
            for (Interface anInterface : p.getAllInterfaces())
                if (interfaceName.equalsIgnoreCase(anInterface.getName()))
                    return anInterface;

        return null;
    }

    public Package findPackageByName(Architecture architecture, String packageName) {
        for (Package pkg : architecture.getAllPackages())
            if (packageName.equalsIgnoreCase(pkg.getName()))
                return pkg;

        return null;
    }

    public Package findPackageByID(Architecture architecture, String id) {
        for (Package pkg : architecture.getAllPackages()) {
            if (id.equalsIgnoreCase(pkg.getId()))
                return pkg;
            for (Package subP : pkg.getNestedPackages()) {
                Package subPkg = findSubPackageByID(subP, id);
                if (subPkg != null)
                    return subPkg;
            }
        }
        return null;
    }

    private Package findSubPackageByID(Package originPkg, String id) {
        if (originPkg.getId().equals(id))
            return originPkg;
        for (Package subPkg : originPkg.getNestedPackages()) {
            if (id.equalsIgnoreCase(subPkg.getId()))
                return subPkg;
            Package subP1 = findSubPackageByID(subPkg, id);
            if (subP1 != null)
                return subP1;
        }
        return null;
    }

    public Class findClassById(Architecture architecture, String idClass) {
        for (Class klass : architecture.getAllClasses())
            if (idClass.equalsIgnoreCase(klass.getId().trim()))
                return klass;

        for (Package p : architecture.getAllPackages())
            for (Class klass : p.getAllClasses())
                if (idClass.equalsIgnoreCase(klass.getId().trim()))
                    return klass;

        return null;
    }

    public Interface findInterfaceById(Architecture architecture, String idInterface) {
        for (Interface inter : architecture.getAllInterfaces())
            if (idInterface.equalsIgnoreCase(inter.getId().trim()))
                return inter;
        return null;
    }

    public Package findPackageOfClass(Architecture architecture, Class targetClass) {
        String packageName = UtilResources.extractPackageName(targetClass.getNamespace());
        return findPackageByName(architecture, packageName);
    }

    public Package findPackageOfElement(Architecture architecture, String id) {
        for (Class c1 : architecture.getAllClasses()) {
            if (c1.getId().equals(id))
                return null;
        }
        for (Interface inter : architecture.getAllInterfaces()) {
            if (inter.getId().equals(id))
                return null;
        }
        for (Package pkg : architecture.getAllPackages()) {
            for (Class clazz : pkg.getAllClasses()) {
                if (clazz.getId().equals(id))
                    return pkg;
            }
            for (Interface inter : pkg.getAllInterfaces()) {
                if (inter.getId().equals(id))
                    return null;
            }
        }
        return null;
    }

    public Element findElementById(Architecture architecture, String xmiId) {
        for (Class element : architecture.getAllClasses()) {
            Element element1 = findElementInPackage(xmiId, element);
            if (element1 != null) return element1;
        }
        for (Interface element : architecture.getAllInterfaces()) {
            if (element.getId().equals(xmiId))
                return element;
            for (Method m : element.getMethods()) {
                if (m.getId().equals(xmiId))
                    return m;
            }
        }

        for (Package p : architecture.getAllPackages()) {
            Element p1 = findElementByXmiIdInPackage(xmiId, p);
            if (p1 != null) return p1;
        }

        return null;
    }

    private Element findElementByXmiIdInPackage(String xmiId, Package p) {
        if (p.getId().equalsIgnoreCase(xmiId))
            return p;
        for (Class element : p.getAllClasses()) {
            Element element1 = findElementByXmiIdInPackage(xmiId, element);
            if (element1 != null) return element1;
        }
        for (Interface element : p.getAllInterfaces()) {
            if (element.getId().equals(xmiId))
                return element;
            for (Method m : element.getMethods()) {
                if (m.getId().equals(xmiId))
                    return m;
            }
        }
        Element e1 = findElementInSubPackageById(p, xmiId);
        if (e1 != null)
            return e1;
        return null;
    }

    private Element findElementByXmiIdInPackage(String xmiId, Class element) {
        if (element.getId().equals(xmiId))
            return element;
        for (Method m : element.getAllMethods()) {
            if (m.getId().equals(xmiId))
                return m;
        }
        for (Attribute m : element.getAllAttributes()) {
            if (m.getId().equals(xmiId))
                return m;
        }
        return null;
    }

    private Element findElementInPackage(String xmiId, Class element) {
        return findElementByXmiIdInPackage(xmiId, element);
    }

    private Element findElementInSubPackageById(Package pkg, String xmiId) {
        if (pkg.getId().equals(xmiId)) {
            return pkg;
        }
        for (Package p : pkg.getNestedPackages()) {
            Element p1 = findElementByXmiIdInPackage(xmiId, p);
            if (p1 != null) return p1;
        }
        return null;
    }

    private Element findMethodInSubPackageById(Package pkg, String xmiId) {
        for (Package p : pkg.getNestedPackages()) {
            Element m = findMethodByXmiIdInPackage(xmiId, p);
            if (m != null) return m;
        }
        return null;
    }

    private Element findMethodByXmiIdInPackage(String xmiId, Package p) {
        for (Class element : p.getAllClasses()) {
            for (Method m : element.getAllMethods()) {
                if (m.getId().equals(xmiId))
                    return m;
            }
        }
        for (Interface element : p.getAllInterfaces()) {
            for (Method m : element.getMethods()) {
                if (m.getId().equals(xmiId))
                    return m;
            }
        }
        Element mx = findMethodInSubPackageById(p, xmiId);
        if (mx != null)
            return mx;
        return null;
    }

    public Element findMethodById(Architecture architecture, String xmiId) {
        for (Class element : architecture.getAllClasses()) {
            for (Method m : element.getAllMethods()) {
                if (m.getId().equals(xmiId))
                    return m;
            }
        }
        for (Interface element : architecture.getAllInterfaces()) {
            for (Method m : element.getMethods()) {
                if (m.getId().equals(xmiId))
                    return m;
            }
        }
        for (Package p : architecture.getAllPackages()) {
            Element m = findMethodByXmiIdInPackage(xmiId, p);
            if (m != null) return m;
        }
        return null;
    }

    public Element findAttributeById(Architecture architecture, String xmiId) {
        for (Class element : architecture.getAllClasses()) {
            for (Attribute a : element.getAllAttributes()) {
                if (a.getId().equals(xmiId))
                    return a;
            }
        }
        for (Package p : architecture.getAllPackages()) {
            Element a = findAttributeByXmiIdInPackage(xmiId, p);
            if (a != null) return a;
        }
        return null;
    }

    private Element findAttributeByXmiIdInPackage(String xmiId, Package p) {
        for (Class element : p.getAllClasses()) {
            for (Attribute a : element.getAllAttributes()) {
                if (a.getId().equals(xmiId))
                    return a;
            }
        }
        return findAttributeInSubpackageById(p, xmiId);
    }

    private Element findAttributeInSubpackageById(Package pkg, String xmiId) {
        for (Package p : pkg.getNestedPackages()) {
            Element a = findAttributeByXmiIdInPackage(xmiId, p);
            if (a != null) return a;
        }
        return null;
    }
}
