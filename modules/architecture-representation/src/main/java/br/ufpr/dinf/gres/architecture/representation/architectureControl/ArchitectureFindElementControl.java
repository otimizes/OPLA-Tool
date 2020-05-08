package br.ufpr.dinf.gres.architecture.representation.architectureControl;

import br.ufpr.dinf.gres.architecture.exceptions.ClassNotFound;
import br.ufpr.dinf.gres.architecture.helpers.UtilResources;
import br.ufpr.dinf.gres.architecture.representation.*;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Package;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class has the implementation of all the method from Architecture that is used to find an specific element from architecture
 *
 */
public class ArchitectureFindElementControl {
    public ArchitectureFindElementControl() {
    }

    private static final ArchitectureFindElementControl INSTANCE = new ArchitectureFindElementControl();

    public static ArchitectureFindElementControl getInstance() {
        return INSTANCE;
    }

    /**
     * find stereotype using its name
     * @param architecture
     * @param name
     * @return
     */
    public Concern findConcernByName(Architecture architecture, String name) {
        for (Concern c : architecture.getLstConcerns()) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    /**
     * finf type smarty using its id
     * @param architecture
     * @param id
     * @return
     */
    public TypeSmarty findTypeSMartyByID(Architecture architecture,String id) {
        for (TypeSmarty typeSmarty : architecture.getLstTypes()) {
            if (typeSmarty.getId().equals(id))
                return typeSmarty;
        }
        return null;
    }

    /**
     * search a type using its name from listTypesSMarty
     * if not found, return Object type
     * @param architecture
     * @param name
     * @return
     */
    public TypeSmarty findTypeSMartyByName(Architecture architecture,String name) {
        for (TypeSmarty typeSmarty : architecture.getLstTypes()) {
            if (typeSmarty.getName().equals(name))
                return typeSmarty;
        }
        return findObjectType(architecture);
    }

    /**
     * search a return type using its name from listTypesSMarty
     * return void type if not has name
     * if not found, return Object type
     * @param architecture
     * @param name
     * @return
     */
    public TypeSmarty findReturnTypeSMartyByName(Architecture architecture,String name) {
        if (name == null) {
            return findVoidType(architecture);
        }
        if (name.length() == 0) {
            return findVoidType(architecture);
        }
        for (TypeSmarty typeSmarty : architecture.getLstTypes()) {
            //System.out.println(typeSmarty.getName());
            if (typeSmarty.getName().equals(name))
                return typeSmarty;
        }
        return findObjectType(architecture);
    }

    /**
     * find an type Object in typesSMarty
     * @param architecture
     * @return
     */
    private TypeSmarty findObjectType(Architecture architecture) {
        for (TypeSmarty typeSmarty : architecture.getLstTypes()) {
            if (typeSmarty.getName().equals("Object"))
                return typeSmarty;
        }
        return null;
    }

    /**
     * find an type void in typesSMarty
     * @param architecture
     * @return
     */
    private TypeSmarty findVoidType(Architecture architecture) {
        for (TypeSmarty typeSmarty : architecture.getLstTypes()) {
            if (typeSmarty.getName().equals("void"))
                return typeSmarty;
        }
        return null;
    }

    /**
     * search an element by name in packages and call findElementByNameInSubPackage to search in subpackages
     * @param architecture
     * @param elementName
     * @return
     */
    public Element findElementByNameInPackageAndSubPackage(Architecture architecture,String elementName) {

        for (br.ufpr.dinf.gres.architecture.representation.Class clazz_ : architecture.getClasses()) {
            if (clazz_.getName().equals(elementName))
                return clazz_;
        }
        for (Interface inter : architecture.getInterfaces()) {
            if (inter.getName().equals(elementName))
                return inter;
        }

        for (br.ufpr.dinf.gres.architecture.representation.Package pkg : architecture.getAllPackages()) {
            for (br.ufpr.dinf.gres.architecture.representation.Class clazz_ : pkg.getAllClasses()) {
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
        }
        return null;
    }

    /**
     * find class and interface in subpackage using its name
     * @param pkg1
     * @param elementName
     * @return
     */
    private Element findElementByNameInSubPackage(br.ufpr.dinf.gres.architecture.representation.Package pkg1, String elementName) {
        for (Package pkg : pkg1.getNestedPackages()) {
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
        }
        return null;
    }

    /**
     * find element using its name and type
     * @param architecture - target architecture
     * @param name - name of element
     * @param type - type of element
     * @return
     */
    public Element findElement(Architecture architecture, String name, String type) {
        if (type.equalsIgnoreCase("class")) {
            for (Element element : architecture.getClasses()) {
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
            for (Element element : architecture.getInterfaces()) {
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
        List<Class> classesFound = new ArrayList<Class>();
        for (Class klass : architecture.getClasses())
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

    /**
     * find a element by name in architecture - call searchRecursivelyInPackage to search in subpackages
     * @param architecture
     * @param elementName
     * @return
     */
    public Element findElementByName(Architecture architecture, String elementName) {
        Element element = searchRecursivelyInPackage(architecture.getAllPackages(), elementName);
        if (element == null) {
            for (Class klass : architecture.getClasses())
                if (klass.getName().equals(elementName))
                    return klass;
            for (Interface inter : architecture.getInterfaces())
                if (inter.getName().equals(elementName))
                    return inter;
        }
        if (element == null)
            System.out.println("No element called: " + elementName + " found");
        return element;
    }

    /**
     * recursively search an element in subpackages using its name
     * @param packages
     * @param elementName
     * @return
     */
    private Element searchRecursivelyInPackage(Set<Package> packages, String elementName) {
        for (Package p : packages) {
            for (Element element : p.getElements()) {
                if (element.getName().equals(elementName))
                    return element;
                Element tempElement = searchRecursivelyInPackage(p.getNestedPackages(), elementName);
                if(tempElement != null)
                    return tempElement;
            }
            if (p.getName().equals(elementName))
                return p;
            searchRecursivelyInPackage(p.getNestedPackages(), elementName);
        }
        return null;
    }

    /**
     * find a interface using its name
     * @param architecture - target architecture
     * @param interfaceName - name of interface
     * @return
     */
    public Interface findInterfaceByName(Architecture architecture, String interfaceName) {
        for (Interface interfacee : architecture.getInterfaces())
            if (interfaceName.equalsIgnoreCase(interfacee.getName()))
                return interfacee;
        for (Package p : architecture.getAllPackages())
            for (Interface interfacee : p.getAllInterfaces())
                if (interfaceName.equalsIgnoreCase(interfacee.getName()))
                    return interfacee;

        return null;
    }

    /**
     * find package using its name - not recursive
     * @param architecture - target architecture
     * @param packageName - name of package that search
     * @return
     */
    public Package findPackageByName(Architecture architecture, String packageName) {
        for (Package pkg : architecture.getAllPackages())
            if (packageName.equalsIgnoreCase(pkg.getName()))
                return pkg;

        return null;
    }

    /**
     * find a package using its id (call findSubPackageByID to search in subpackages)
     * @param architecture - target architecture
     * @param id - id of package that search
     * @return
     */
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

    /**
     * recursively find an subpackage using its id
     * @param originPkg - target package that has subpackage
     * @param id - id of package that search
     * @return
     */
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

    /**
     * find an class using its id
     * @param architecture - target architecture
     * @param idClass - id of element used to find its parent package
     * @return parent package if exists else null
     */
    public Class findClassById(Architecture architecture, String idClass) throws ClassNotFound {
        for (Class klass : architecture.getClasses())
            if (idClass.equalsIgnoreCase(klass.getId().trim()))
                return klass;

        for (Package p : architecture.getAllPackages())
            for (Class klass : p.getAllClasses())
                if (idClass.equalsIgnoreCase(klass.getId().trim()))
                    return klass;

        throw new ClassNotFound("Class " + idClass + " can not found.\n");
    }

    /**
     * find an interface using its id
     * @param architecture - target architecture
     * @param idInterface - id of element used to find its parent package
     * @return parent package if exists else null
     */
    public Interface findInterfaceById(Architecture architecture, String idInterface) throws ClassNotFound {
        for (Interface inter : architecture.getInterfaces())
            if (idInterface.equalsIgnoreCase(inter.getId().trim()))
                return inter;
        throw new ClassNotFound("Class " + idInterface + " can not found.\n");
    }

    /**
     * find the parent package of an class
     * @param architecture - target architecture
     * @param targetClass - class of element used to find its parent package
     * @return parent package if exists else null
     */
    public Package findPackageOfClass(Architecture architecture, Class targetClass) {
        String packageName = UtilResources.extractPackageName(targetClass.getNamespace());
        return findPackageByName(architecture, packageName);
    }

    /**
     * find the parent package of an element
     * @param architecture - target architecture
     * @param id - id of element used to find its parent package
     * @return parent package if exists else null
     */
    public Package findPackageOfElement(Architecture architecture, String id) {
        for (Class c1 : architecture.getClasses()) {
            if (c1.getId().equals(id))
                return null;
        }
        for (Interface inter : architecture.getInterfaces()) {
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

    /**
     * find an element in architecture package and subpackage of an package (method findElementInSubpackageById)
     * @param architecture - parent package that has subpackages
     * @param xmiId - id of element to find
     * @return element, if find, else null
     */
    public Element findElementById(Architecture architecture, String xmiId) {
        for (Class element : architecture.getClasses()) {
            if (element.getId().equals(xmiId))
                return element;
            for(Method m : element.getAllMethods()){
                if(m.getId().equals(xmiId))
                    return m;
            }
            for(Attribute m : element.getAllAttributes()){
                if(m.getId().equals(xmiId))
                    return m;
            }
        }
        for (Interface element : architecture.getInterfaces()) {
            if (element.getId().equals(xmiId))
                return element;
            for(Method m : element.getMethods()){
                if(m.getId().equals(xmiId))
                    return m;
            }
        }

        for (Package p : architecture.getAllPackages()) {
            if (p.getId().equalsIgnoreCase(xmiId))
                return p;
            for (Class element : p.getAllClasses()) {
                if (element.getId().equals(xmiId))
                    return element;
                for(Method m : element.getAllMethods()){
                    if(m.getId().equals(xmiId))
                        return m;
                }
                for(Attribute m : element.getAllAttributes()){
                    if(m.getId().equals(xmiId))
                        return m;
                }
            }
            for (Interface element : p.getAllInterfaces()) {
                if (element.getId().equals(xmiId))
                    return element;
                for(Method m : element.getMethods()){
                    if(m.getId().equals(xmiId))
                        return m;
                }
            }
            Element e1 = findElementInSubPackageById(p, xmiId);
            if(e1 != null)
                return  e1;
        }

        return null;
    }

    /**
     * recursively find an element in subpackage of an package
     * @param pkg - parent package that has subpackages
     * @param xmiId - id of element to find
     * @return element, if find, else null
     */
    private Element findElementInSubPackageById(Package pkg, String xmiId) {
        if (pkg.getId().equals(xmiId)) {
            return pkg;
        }
        for (Package p : pkg.getNestedPackages()) {
            if (p.getId().equalsIgnoreCase(xmiId))
                return p;
            for (Class element : p.getAllClasses()) {
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
        }
        return null;
    }

    /**
     * recursively find an method in subpackage of an package
     * @param pkg - parent package that has subpackages
     * @param xmiId - id of element to find
     * @return element, if find, else null
     */
    private Element findMethodInSubPackageById(Package pkg, String xmiId) {
        for (Package p : pkg.getNestedPackages()) {
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
        }
        return null;
    }

    /**
     * find an method in architecture package and subpackage of an package ( call method findMethodInSubpackageById)
     * @param architecture - parent package that has subpackages
     * @param xmiId - id of element to find
     * @return element, if find, else null
     */
    public Element findMethodById(Architecture architecture, String xmiId) {
        for (Class element : architecture.getClasses()) {
            for (Method m : element.getAllMethods()) {
                if (m.getId().equals(xmiId))
                    return m;
            }
        }
        for (Interface element : architecture.getInterfaces()) {
            for (Method m : element.getMethods()) {
                if (m.getId().equals(xmiId))
                    return m;
            }
        }
        for (Package p : architecture.getAllPackages()) {
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
        }
        return null;
    }

    /**
     * find an attribute in architecture package and subpackage of an package (method findAttributeInSubpackageById)
     * @param architecture - parent package that has subpackages
     * @param xmiId - id of element to find
     * @return element, if find, else null
     */
    public Element findAttributeById(Architecture architecture, String xmiId) {
        for (Class element : architecture.getClasses()) {
            for (Attribute a : element.getAllAttributes()) {
                if (a.getId().equals(xmiId))
                    return a;
            }
        }
        for (Package p : architecture.getAllPackages()) {
            for (Class element : p.getAllClasses()) {
                for (Attribute a : element.getAllAttributes()) {
                    if (a.getId().equals(xmiId))
                        return a;
                }
            }
            Element e1 = findAttributeInSubpackageById(p, xmiId);
            if (e1 != null)
                return e1;
        }
        return null;
    }

    /**
     * recursively find an attribute in subpackage of an package
     * @param pkg - parent package that has subpackages
     * @param xmiId - id of element to find
     * @return element, if find, else null
     */
    private Element findAttributeInSubpackageById(Package pkg, String xmiId) {
        for (Package p : pkg.getNestedPackages()) {
            for (Class element : p.getAllClasses()) {
                for (Attribute a : element.getAllAttributes()) {
                    if (a.getId().equals(xmiId))
                        return a;
                }
            }
            Element e1 = findAttributeInSubpackageById(p, xmiId);
            if (e1 != null)
                return e1;
        }
        return null;
    }


}
