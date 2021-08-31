package br.otimizes.oplatool.architecture.smarty;

import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.*;

/**
 * Resize and reorder all elements of architecture
 * this class ensures that none of elements overlap other in the interface
 */
public class ResizeAndReorderArchitectureSMarty {

    public ResizeAndReorderArchitectureSMarty() {
    }

    private static final ResizeAndReorderArchitectureSMarty INSTANCE = new ResizeAndReorderArchitectureSMarty();

    public static ResizeAndReorderArchitectureSMarty getInstance() {
        return INSTANCE;
    }

    /**
     * Resize and reorder all elements of architecture
     * this class ensures that none of elements overlap other in the interface
     * first, resize all class and interface (includes elements inside of subpackages)
     * reposition elements inside of subpackages (class, interface and subpackage
     * - use recursive method to subpackage until not has package in subpackage)
     * resize all of most external packages
     * reposition all external elements (class and interface without package and packages most external)
     * recalculate the global position of all architectural elements
     *
     * @param architecture - input architecture
     */
    public void execute(Architecture architecture) {
        resizeClassAndInterface(architecture);
        rePosElementsInsideOfPackages(architecture);
        resizeExternalPackage(architecture);
        rePosExternalElements(architecture);
        getGlobalPosElements(architecture);
    }

    private void resizeClassAndInterface(Architecture architecture) {
        for (Class clazz : architecture.getClasses()) {
            clazz.setHeight("" + ((clazz.getAllMethods().size() + clazz.getOwnConcerns().size() + clazz.getAllAttributes().size()) * 20 + 200));
            setWidthOnClass(clazz);
        }
        for (Interface clazz : architecture.getInterfaces()) {
            clazz.setHeight("" + ((clazz.getMethods().size() + clazz.getOwnConcerns().size()) * 20 + 200));
            setWidthOnInterface(clazz);
        }
        for (br.otimizes.oplatool.architecture.representation.Package pkg : architecture.getAllPackages()) {
            setClassesSize(pkg);
        }
    }

    private void setWidthOnInterface(Interface clazz) {
        int width = 400;
        for (Method method : clazz.getMethods()) {
            if (width < (400 + (100 * method.getOwnConcerns().size()) + (100 * method.getParameters().size()))) {
                width = 400 + (100 * method.getOwnConcerns().size()) + (100 * method.getParameters().size());
            }
        }
        clazz.setWidth("" + width);
    }

    private void setWidthOnClass(Class clazz) {
        int width = 400;
        for (Method method : clazz.getAllMethods()) {
            if (width < (400 + (100 * method.getOwnConcerns().size()) + (100 * method.getParameters().size()))) {
                width = 400 + (100 * method.getOwnConcerns().size()) + (100 * method.getParameters().size());
            }
        }
        clazz.setWidth("" + width);
    }

    /**
     * Resize and reorder all elements of architecture
     * recursive class to verify all subpackages of an package
     * this class ensures that none of elements overlap other in the interface
     * first, resize all class and interface inside of subpackages
     *
     * @param pkg - package that has elements
     */
    private void reSizeClassAndInterfaceInSubPackage(br.otimizes.oplatool.architecture.representation.Package pkg) {
        for (br.otimizes.oplatool.architecture.representation.Package subPkg : pkg.getNestedPackages()) {
            setClassesSize(subPkg);
        }
    }

    private void setClassesSize(Package subPkg) {
        for (Class clazz : subPkg.getAllClasses()) {
            clazz.setHeight("" + ((clazz.getAllMethods().size() + clazz.getOwnConcerns().size() + clazz.getAllAttributes().size()) * 20 + 100));
            setWidthOnClass(clazz);
        }
        for (Interface inter : subPkg.getAllInterfaces()) {
            inter.setHeight("" + ((inter.getMethods().size() + inter.getOwnConcerns().size()) * 20 + 100));
            setWidthOnInterface(inter);
        }
        reSizeClassAndInterfaceInSubPackage(subPkg);
    }

    /**
     * Reposition of all elements inside packages
     * if package has a subpackage, first resize and repos all subpackages, after this, the other elements (class, interface)
     *
     * @param architecture - input architecture
     */
    private void rePosElementsInsideOfPackages(Architecture architecture) {
        for (br.otimizes.oplatool.architecture.representation.Package pkg : architecture.getAllPackages()) {
            int x = 40;
            int y = 60;
            int nextY = 60;
            int posList = 0;
            int sizeP = pkg.getNestedPackages().size();
            int sizeC = pkg.getAllClasses().size();
            int sizeI = pkg.getAllInterfaces().size();
            int size = sizeP + sizeC + sizeI;
            resizeSubPackage(pkg);
            x = getX(size, x, y, pkg);
            if (size > 2) {
                int div = (int) Math.ceil(Math.sqrt(size));
                setElementPositions(div, x, y, nextY, posList, pkg);
            }
        }
    }

    private void setElementPositions(int div, int x, int y, int nexty, int posList, Package pkg) {
        for (Package subP : pkg.getNestedPackages()) {
            if (posList != 0) {
                if ((posList % div) == 0) {
                    x = 40;
                    y = nexty + 40;
                }
            }
            subP.setPosX("" + x);
            subP.setPosY("" + y);
            x = x + Integer.parseInt(subP.getWidth()) + 40;
            if (nexty < y + Integer.parseInt(subP.getHeight())) {
                nexty = y + Integer.parseInt(subP.getHeight());
            }
            posList++;
        }

        for (Class clazz : pkg.getAllClasses()) {
            if (posList != 0) {
                if ((posList % div) == 0) {
                    x = 40;
                    y = nexty + 40;
                }
            }
            clazz.setPosX("" + x);
            clazz.setPosY("" + y);
            x = x + Integer.parseInt(clazz.getWidth()) + 40;
            if (nexty < y + Integer.parseInt(clazz.getHeight())) {
                nexty = y + Integer.parseInt(clazz.getHeight());
            }
            posList++;
        }

        for (Interface clazz : pkg.getAllInterfaces()) {
            if (posList != 0) {
                if ((posList % div) == 0) {
                    x = 40;
                    y = nexty + 40;
                }
            }
            clazz.setPosX("" + x);
            clazz.setPosY("" + y);
            x = x + Integer.parseInt(clazz.getWidth()) + 40;
            if (nexty < y + Integer.parseInt(clazz.getHeight())) {
                nexty = y + Integer.parseInt(clazz.getHeight());
            }
            posList++;
        }
    }

    private int getX(int size, int x, int y, Package pkg) {
        if (size == 1) {
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
        if (size == 2) {
            for (Package pkx : pkg.getNestedPackages()) {
                pkx.setPosX("" + x);
                pkx.setPosY("" + y);
                x = x + Integer.parseInt(pkx.getWidth()) + 40;
            }
            for (Class clazz : pkg.getAllClasses()) {
                clazz.setPosX("" + x);
                clazz.setPosY("" + y);
                x = x + Integer.parseInt(clazz.getWidth()) + 40;
            }
            for (Interface clazz : pkg.getAllInterfaces()) {
                clazz.setPosX("" + x);
                clazz.setPosY("" + y);
                x = x + Integer.parseInt(clazz.getWidth()) + 40;
            }
        }
        return x;
    }


    /**
     * Resize recursively all subpackages in the package
     * if subpackage not has subpackage, use the size of interface and class to get the size of subpackage
     * for it, use the position of element + width ou height to know the max position that all elements in subpackage use
     * if has subpackage, resize the subpackage
     *
     * @param pkg - the package that has subpackage
     */
    private void resizeSubPackage(br.otimizes.oplatool.architecture.representation.Package pkg) {
        if (pkg.getNestedPackages().size() == 0) {
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
            setWidthInElements(pkg, width);
        } else {
            for (br.otimizes.oplatool.architecture.representation.Package otherPackage : pkg.getNestedPackages()) {
                resizeSubPackage(otherPackage);
            }
            int x = 40;
            int y = 60;
            int nextY = 60;
            int posList = 0;

            int sizeP = pkg.getNestedPackages().size();
            int sizeC = pkg.getAllClasses().size();
            int sizeI = pkg.getAllInterfaces().size();
            int size = sizeP + sizeC + sizeI;

            x = getX(size, x, y, pkg);
            if (size > 2) {
                int div = (int) Math.ceil(Math.sqrt(size));

                setElementPositions(div, x, y, nextY, posList, pkg);
            }
            int height = 300;
            for (br.otimizes.oplatool.architecture.representation.Package pkx : pkg.getNestedPackages()) {
                if (height < (Integer.parseInt(pkx.getPosY()) + Integer.parseInt(pkx.getHeight()) + 20)) {
                    height = (Integer.parseInt(pkx.getPosY()) + Integer.parseInt(pkx.getHeight()) + 20);
                }
            }
            setHeightInElementsOnPackage(pkg, height);
        }
    }

    private void setWidthInElements(Package pkg, int width) {
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

    private void setHeightInElementsOnPackage(Package pkg, int height) {
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
        setWidthInElements(pkg, width);
    }


    /**
     * Resize all most external package (architecture.packages)
     *
     * @param architecture - input architecture
     */
    private void resizeExternalPackage(Architecture architecture) {
        for (br.otimizes.oplatool.architecture.representation.Package pkg : architecture.getAllPackages()) {
            int height = 300;
            for (br.otimizes.oplatool.architecture.representation.Package clazz : pkg.nestedPackages) {
                if (height < (Integer.parseInt(clazz.getPosY()) + Integer.parseInt(clazz.getHeight()) + 20)) {
                    height = (Integer.parseInt(clazz.getPosY()) + Integer.parseInt(clazz.getHeight()) + 20);
                }
            }
            setHeightInElementsOnPackage(pkg, height);
        }
    }

    /**
     * Reposition all outside elements (class and interface without package and the external packages)
     *
     * @param architecture - input architecture
     */
    private void rePosExternalElements(Architecture architecture) {
        int div;
        int x = 40;
        int y = 40;
        int nextY = 40;
        int positionOnList = 0;
        int size = architecture.getAllPackages().size();
        if (size == 1) {
            for (br.otimizes.oplatool.architecture.representation.Package packageToSetPos : architecture.getAllPackages()) {
                packageToSetPos.setPosX("" + x);
                packageToSetPos.setPosY("" + y);
                if (nextY < y + Integer.parseInt(packageToSetPos.getHeight())) {
                    nextY = y + Integer.parseInt(packageToSetPos.getHeight());
                }
            }
        }
        if (size == 2) {
            for (br.otimizes.oplatool.architecture.representation.Package classToSetPos : architecture.getAllPackages()) {
                classToSetPos.setPosX("" + x);
                classToSetPos.setPosY("" + y);
                x = x + Integer.parseInt(classToSetPos.getWidth()) + 40;
                if (nextY < y + Integer.parseInt(classToSetPos.getHeight())) {
                    nextY = y + Integer.parseInt(classToSetPos.getHeight());
                }
            }
        }
        if (size > 2) {
            div = (int) Math.ceil(Math.sqrt(size));
            for (br.otimizes.oplatool.architecture.representation.Package packageToSetPos : architecture.getAllPackages()) {
                if (positionOnList != 0) {
                    if ((positionOnList % div) == 0) {
                        x = 40;
                        y = nextY + 40;
                    }
                }
                packageToSetPos.setPosX("" + x);
                packageToSetPos.setPosY("" + y);
                x = x + Integer.parseInt(packageToSetPos.getWidth()) + 40;
                if (nextY < y + Integer.parseInt(packageToSetPos.getHeight())) {
                    nextY = y + Integer.parseInt(packageToSetPos.getHeight());
                }
                positionOnList++;
            }
        }
        nextY = setClassPositions(architecture, nextY);
        setInterfacePositions(architecture, nextY);
    }

    private int setClassPositions(Architecture architecture, int nextY) {
        if (architecture.getClasses().size() > 0) {
            int y = nextY + 40;
            int x = 40;
            for (Class clazz : architecture.getClasses()) {
                clazz.setPosX("" + x);
                clazz.setPosY("" + y);
                x = x + Integer.parseInt(clazz.getWidth()) + 40;
                if (nextY < y + Integer.parseInt(clazz.getHeight())) {
                    nextY = y + Integer.parseInt(clazz.getHeight());
                }
            }
        }
        return nextY;
    }

    private void setInterfacePositions(Architecture architecture, int nextY) {
        if (architecture.getInterfaces().size() > 0) {
            int y = nextY + 40;
            int x = 40;
            for (Interface inter : architecture.getInterfaces()) {
                inter.setPosX("" + x);
                inter.setPosY("" + y);
                x = x + Integer.parseInt(inter.getWidth()) + 40;
                if (nextY < y + Integer.parseInt(inter.getHeight())) {
                    nextY = y + Integer.parseInt(inter.getHeight());
                }
            }
        }
    }

    /**
     * verify global position x and y of all elements in architecture
     *
     * @param architecture - input architecture
     */
    private void getGlobalPosElements(Architecture architecture) {
        for (Class classToSetPos : architecture.getClasses()) {
            classToSetPos.setGlobalPosX(classToSetPos.getPosX());
            classToSetPos.setGlobalPosY(classToSetPos.getPosY());
        }
        for (Interface interfaceToSetPos : architecture.getInterfaces()) {
            interfaceToSetPos.setGlobalPosX(interfaceToSetPos.getPosX());
            interfaceToSetPos.setGlobalPosY(interfaceToSetPos.getPosY());
        }
        for (br.otimizes.oplatool.architecture.representation.Package packageToSetPos : architecture.getAllPackages()) {
            packageToSetPos.setGlobalPosX(packageToSetPos.getPosX());
            packageToSetPos.setGlobalPosY(packageToSetPos.getPosY());
            int pkgPosX = Integer.parseInt(packageToSetPos.getPosX());
            int pkgPosY = Integer.parseInt(packageToSetPos.getPosY());
            setPositionsInPackage(pkgPosX, pkgPosY, packageToSetPos);
            getGlobalPosElementsSubPackage(packageToSetPos);
        }
    }

    /**
     * verify global position x and y of all elements in subpackages
     *
     * @param pkg - package that has subpackage
     */
    private void getGlobalPosElementsSubPackage(br.otimizes.oplatool.architecture.representation.Package pkg) {
        int pkgPosX;
        int pkgPosY;
        for (Package subPkg : pkg.getNestedPackages()) {
            pkgPosX = Integer.parseInt(pkg.getPosX());
            pkgPosY = Integer.parseInt(pkg.getPosY());
            subPkg.setGlobalPosX("" + (Integer.parseInt(subPkg.getPosX()) + pkgPosX));
            subPkg.setGlobalPosY("" + (Integer.parseInt(subPkg.getPosY()) + pkgPosY));
            pkgPosX = Integer.parseInt(subPkg.getPosX()) + Integer.parseInt(pkg.getPosX());
            pkgPosY = Integer.parseInt(subPkg.getPosY()) + Integer.parseInt(pkg.getPosY());
            setPositionsInPackage(pkgPosX, pkgPosY, subPkg);
        }
    }

    private void setPositionsInPackage(int pkgPosX, int pkgPosY, Package subPkg) {
        for (Class clazz : subPkg.getAllClasses()) {
            clazz.setGlobalPosX("" + (Integer.parseInt(clazz.getPosX()) + pkgPosX));
            clazz.setGlobalPosY("" + (Integer.parseInt(clazz.getPosY()) + pkgPosY));
        }
        for (Interface inter : subPkg.getAllInterfaces()) {
            inter.setGlobalPosX("" + (Integer.parseInt(inter.getPosX()) + pkgPosX));
            inter.setGlobalPosY("" + (Integer.parseInt(inter.getPosY()) + pkgPosY));
        }
    }

}
