package br.otimizes.oplatool.architecture.smarty;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.Method;
import br.otimizes.oplatool.architecture.representation.Package;

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
    public void Execute(Architecture architecture) {
        resizeClassAndInterface(architecture);
        rePosElementsInsideOfPackages(architecture);
        resizeExternalPackage(architecture);
        rePosExternalElements(architecture);
        getGlobalPosElements(architecture);
    }

    private void resizeClassAndInterface(Architecture architecture) {
        for (Class clazz : architecture.getClasses()) {
            clazz.setHeight("" + ((clazz.getAllMethods().size() + clazz.getOwnConcerns().size() + clazz.getAllAttributes().size()) * 20 + 200));
            int width = 400;
            for (Method m1 : clazz.getAllMethods()) {
                if (width < (400 + (100 * m1.getOwnConcerns().size()) + (100 * m1.getParameters().size()))) {
                    width = 400 + (100 * m1.getOwnConcerns().size()) + (100 * m1.getParameters().size());
                }
            }
            clazz.setWidth("" + width);
        }
        for (Interface clazz : architecture.getInterfaces()) {
            clazz.setHeight("" + ((clazz.getMethods().size() + clazz.getOwnConcerns().size()) * 20 + 200));
            int width = 400;
            for (Method m1 : clazz.getMethods()) {
                if (width < (400 + (100 * m1.getOwnConcerns().size()) + (100 * m1.getParameters().size()))) {
                    width = 400 + (100 * m1.getOwnConcerns().size()) + (100 * m1.getParameters().size());
                }
            }
            clazz.setWidth("" + width);
        }
        for (br.otimizes.oplatool.architecture.representation.Package pkg : architecture.getAllPackages()) {
            for (Class clazz : pkg.getAllClasses()) {
                clazz.setHeight("" + ((clazz.getAllMethods().size() + clazz.getOwnConcerns().size() + clazz.getAllAttributes().size()) * 20 + 100));
                int width = 400;
                for (Method m1 : clazz.getAllMethods()) {
                    if (width < (400 + (100 * m1.getOwnConcerns().size()) + (100 * m1.getParameters().size()))) {
                        width = 400 + (100 * m1.getOwnConcerns().size()) + (100 * m1.getParameters().size());
                    }
                }
                clazz.setWidth("" + width);
            }
            for (Interface clazz : pkg.getAllInterfaces()) {
                clazz.setHeight("" + ((clazz.getMethods().size() + clazz.getOwnConcerns().size()) * 20 + 100));
                int width = 400;
                for (Method m1 : clazz.getMethods()) {
                    if (width < (400 + (100 * m1.getOwnConcerns().size()) + (100 * m1.getParameters().size()))) {
                        width = 400 + (100 * m1.getOwnConcerns().size()) + (100 * m1.getParameters().size());
                    }
                }
                clazz.setWidth("" + width);
            }
            reSizeClassAndInterfaceInSubPackage(pkg);
        }
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
            for (Class clazz : subPkg.getAllClasses()) {
                clazz.setHeight("" + ((clazz.getAllMethods().size() + clazz.getOwnConcerns().size() + clazz.getAllAttributes().size()) * 20 + 100));
                int width = 400;
                for (Method m1 : clazz.getAllMethods()) {
                    if (width < (400 + (100 * m1.getOwnConcerns().size()) + (100 * m1.getParameters().size()))) {
                        width = 400 + (100 * m1.getOwnConcerns().size()) + (100 * m1.getParameters().size());
                    }
                }
                clazz.setWidth("" + width);
            }
            for (Interface inter : subPkg.getAllInterfaces()) {
                inter.setHeight("" + ((inter.getMethods().size() + inter.getOwnConcerns().size()) * 20 + 100));
                int width = 400;
                for (Method m1 : inter.getMethods()) {
                    if (width < (400 + (100 * m1.getOwnConcerns().size()) + (100 * m1.getParameters().size()))) {
                        width = 400 + (100 * m1.getOwnConcerns().size()) + (100 * m1.getParameters().size());
                    }
                }
                inter.setWidth("" + width);
            }
            reSizeClassAndInterfaceInSubPackage(subPkg);
        }
    }

    /**
     * Reposition of all elements inside packages
     * if package has a subpackage, first resize and repos all subpackages, after this, the other elements (class, interface)
     *
     * @param architecture - input architecture
     */
    private void rePosElementsInsideOfPackages(Architecture architecture) {
        int sizeC;
        int sizeI;
        int sizeP;
        int size;
        int div;
        int x;
        int y;
        int nexty;
        int posList;
        for (br.otimizes.oplatool.architecture.representation.Package pkg : architecture.getAllPackages()) {
            x = 40;
            y = 60;
            nexty = 60;
            posList = 0;
            sizeP = pkg.getNestedPackages().size();
            sizeC = pkg.getAllClasses().size();
            sizeI = pkg.getAllInterfaces().size();
            size = sizeP + sizeC + sizeI;
            resizeSubPackage(pkg);
            if (size == 1) {
                for (br.otimizes.oplatool.architecture.representation.Package pkx : pkg.getNestedPackages()) {
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
                for (br.otimizes.oplatool.architecture.representation.Package pkx : pkg.getNestedPackages()) {
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
            if (size > 2) {
                div = (int) Math.ceil(Math.sqrt(size));

                for (br.otimizes.oplatool.architecture.representation.Package subP : pkg.getNestedPackages()) {
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
        }
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
        } else {
            for (br.otimizes.oplatool.architecture.representation.Package pkg1 : pkg.getNestedPackages()) {
                resizeSubPackage(pkg1);
            }
            int x = 40;
            int y = 60;
            int nexty = 60;
            int posList = 0;

            int sizeP = pkg.getNestedPackages().size();
            int sizeC = pkg.getAllClasses().size();
            int sizeI = pkg.getAllInterfaces().size();
            int size = sizeP + sizeC + sizeI;

            if (size == 1) {
                for (br.otimizes.oplatool.architecture.representation.Package pkx : pkg.getNestedPackages()) {
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
                for (br.otimizes.oplatool.architecture.representation.Package pkx : pkg.getNestedPackages()) {
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
            if (size > 2) {
                int div = (int) Math.ceil(Math.sqrt(size));

                for (br.otimizes.oplatool.architecture.representation.Package subP : pkg.getNestedPackages()) {
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
            int height = 300;
            for (br.otimizes.oplatool.architecture.representation.Package pkx : pkg.getNestedPackages()) {
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
            for (br.otimizes.oplatool.architecture.representation.Package pkx : pkg.getNestedPackages()) {
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
            for (br.otimizes.oplatool.architecture.representation.Package clazz : pkg.getNestedPackages()) {
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

    /**
     * Reposition all outside elements (class and interface without package and the external packages)
     *
     * @param architecture - input architecture
     */
    private void rePosExternalElements(Architecture architecture) {
        int div;
        int x = 40;
        int y = 40;
        int nexty = 40;
        int posLista = 0;
        int size = architecture.getAllPackages().size();
        if (size == 1) {
            for (br.otimizes.oplatool.architecture.representation.Package clazz : architecture.getAllPackages()) {
                clazz.setPosX("" + x);
                clazz.setPosY("" + y);
                if (nexty < y + Integer.parseInt(clazz.getHeight())) {
                    nexty = y + Integer.parseInt(clazz.getHeight());
                }
            }
        }
        if (size == 2) {
            for (br.otimizes.oplatool.architecture.representation.Package clazz : architecture.getAllPackages()) {
                clazz.setPosX("" + x);
                clazz.setPosY("" + y);
                x = x + Integer.parseInt(clazz.getWidth()) + 40;
                if (nexty < y + Integer.parseInt(clazz.getHeight())) {
                    nexty = y + Integer.parseInt(clazz.getHeight());
                }
            }
        }
        if (size > 2) {
            div = (int) Math.ceil(Math.sqrt(size));
            for (br.otimizes.oplatool.architecture.representation.Package clazz : architecture.getAllPackages()) {
                if (posLista != 0) {
                    if ((posLista % div) == 0) {
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
                posLista++;
            }
        }
        nexty = setClassPositions(architecture, nexty);
        setInterfacePositions(architecture, nexty);
    }

    private int setClassPositions(Architecture architecture, int nexty) {
        int y;
        int x;
        if (architecture.getClasses().size() > 0) {
            y = nexty + 40;
            x = 40;
            for (Class clazz : architecture.getClasses()) {

                clazz.setPosX("" + x);
                clazz.setPosY("" + y);
                x = x + Integer.parseInt(clazz.getWidth()) + 40;
                if (nexty < y + Integer.parseInt(clazz.getHeight())) {
                    nexty = y + Integer.parseInt(clazz.getHeight());
                }
            }
        }
        return nexty;
    }

    private void setInterfacePositions(Architecture architecture, int nexty) {
        int y;
        int x;
        if (architecture.getInterfaces().size() > 0) {
            y = nexty + 40;
            x = 40;
            for (Interface inter : architecture.getInterfaces()) {
                inter.setPosX("" + x);
                inter.setPosY("" + y);
                x = x + Integer.parseInt(inter.getWidth()) + 40;
                if (nexty < y + Integer.parseInt(inter.getHeight())) {
                    nexty = y + Integer.parseInt(inter.getHeight());
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
        for (Class clazz : architecture.getClasses()) {
            clazz.setGlobalPosX(clazz.getPosX());
            clazz.setGlobalPosY(clazz.getPosY());
        }
        for (Interface inter : architecture.getInterfaces()) {
            inter.setGlobalPosX(inter.getPosX());
            inter.setGlobalPosY(inter.getPosY());
        }
        int pkgPosX = 0;
        int pkgPosY = 0;
        for (br.otimizes.oplatool.architecture.representation.Package pkg : architecture.getAllPackages()) {
            pkg.setGlobalPosX(pkg.getPosX());
            pkg.setGlobalPosY(pkg.getPosY());
            pkgPosX = Integer.parseInt(pkg.getPosX());
            pkgPosY = Integer.parseInt(pkg.getPosY());
            setPositionsInPackage(pkgPosX, pkgPosY, pkg);
            getGlobalPosElementsSubPackage(pkg);
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
