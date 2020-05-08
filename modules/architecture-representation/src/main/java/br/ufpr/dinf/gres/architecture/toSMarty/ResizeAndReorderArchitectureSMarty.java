package br.ufpr.dinf.gres.architecture.toSMarty;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Interface;
import br.ufpr.dinf.gres.architecture.representation.Method;
import br.ufpr.dinf.gres.architecture.representation.Package;

public class ResizeAndReorderArchitectureSMarty {

    public ResizeAndReorderArchitectureSMarty(Architecture architecture) {
        resizeClassAndInterface(architecture);
        rePosElementsInsideOfPackages(architecture);
        resizeExternalPackage(architecture);
        rePosExternalElements(architecture);
        getGlobalPosElements(architecture);
    }

    private void resizeClassAndInterface(Architecture architecture) {

        // class without package
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
        // interface without package
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
        // packages
        for (Package pkg : architecture.getAllPackages()) {
            // class in package
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
            // interface in package
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
            // subpackages
            reSizeClassAndInterfaceInSubPackage(pkg);
        }
    }

    private void reSizeClassAndInterfaceInSubPackage(Package pkg1) {

        // subpackage
        for (Package pkg : pkg1.getNestedPackages()) {
            // class of subpackage
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
            // interface of subpackage
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
            // subpackage of subpackage
            reSizeClassAndInterfaceInSubPackage(pkg);
        }
    }

    private void rePosElementsInsideOfPackages(Architecture architecture) {
        int sizeC;
        int sizeI;
        int sizeP;

        int size;
        int div;
        int x;
        int y;
        int nexty;
        int posLista;


        for (Package pkg : architecture.getAllPackages()) {
            x = 40;
            y = 60;
            nexty = 60;
            posLista = 0;

            sizeP = pkg.getNestedPackages().size();
            sizeC = pkg.getAllClasses().size();
            sizeI = pkg.getAllInterfaces().size();

            size = sizeP + sizeC + sizeI;

            // resize subpackage before repos elements in the package because the subpackage size matter
            resizeSubPackage(pkg);

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
            if (size > 2) {
                div = (int) Math.ceil(Math.sqrt(size));

                for (Package subP : pkg.getNestedPackages()) {
                    if (posLista != 0) {
                        if ((posLista % div) == 0) {
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
                    posLista++;
                }

                for (Class clazz : pkg.getAllClasses()) {
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
                for (Interface clazz : pkg.getAllInterfaces()) {
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
        }
    }

    // resize the subpackage weight and height
    private void resizeSubPackage(Package pkg) {
        // if subpackage not has subpackage, use the size of interface and class to get the size of subpackage
        // for it, use the position of element + width ou height to know the max position that all elements in subpackage use
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
            // if has subpackage, use recursive method until has not subpackage
            for (Package pkg1 : pkg.getNestedPackages()) {
                resizeSubPackage(pkg1);
            }
            // verify mas position using class, interface and subpackage
            // repos elements in sub package and after, resize the subpackage
            int x = 40;
            int y = 60;
            int nexty = 60;
            int posLista = 0;

            int sizeP = pkg.getNestedPackages().size();
            int sizeC = pkg.getAllClasses().size();
            int sizeI = pkg.getAllInterfaces().size();
            int size = sizeP + sizeC + sizeI;

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
            if (size > 2) {
                int div = (int) Math.ceil(Math.sqrt(size));

                for (Package subP : pkg.getNestedPackages()) {
                    if (posLista != 0) {
                        if ((posLista % div) == 0) {
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
                    posLista++;
                }

                for (Class clazz : pkg.getAllClasses()) {
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
                for (Interface clazz : pkg.getAllInterfaces()) {
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


            int height = 300;
            for (Package pkx : pkg.getNestedPackages()) {
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
            for (Package pkx : pkg.getNestedPackages()) {
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

    private void resizeExternalPackage(Architecture architecture) {
        for (Package pkg : architecture.getAllPackages()) {
            int height = 300;
            for (Package clazz : pkg.nestedPackages) {
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
            for (Package clazz : pkg.getNestedPackages()) {
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

    private void rePosExternalElements(Architecture architecture) {
        int sizeC;
        int sizeI;

        int size;
        int div;
        int x;
        int y;
        int nexty;
        int posLista;

        x = 40;
        y = 40;
        nexty = 40;
        posLista = 0;

        size = architecture.getAllPackages().size();

        if (size == 1) {
            for (Package clazz : architecture.getAllPackages()) {
                //clazz.setPosX("500");
                //clazz.setPosY("500");
                clazz.setPosX("" + x);
                clazz.setPosY("" + y);
                if (nexty < y + Integer.parseInt(clazz.getHeight())) {
                    nexty = y + Integer.parseInt(clazz.getHeight());
                }
            }
        }
        if (size == 2) {
            for (Package clazz : architecture.getAllPackages()) {
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
            for (Package clazz : architecture.getAllPackages()) {
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
        if (architecture.getInterfaces().size() > 0) {
            y = nexty + 40;
            x = 40;
            for (Interface clazz : architecture.getInterfaces()) {

                clazz.setPosX("" + x);
                clazz.setPosY("" + y);
                x = x + Integer.parseInt(clazz.getWidth()) + 40;
                if (nexty < y + Integer.parseInt(clazz.getHeight())) {
                    nexty = y + Integer.parseInt(clazz.getHeight());
                }
            }
        }
    }

    private void getGlobalPosElements(Architecture architecture) {
        // atualizar tamanho de classe e interface:

        for (Class clazz : architecture.getClasses()) {

            clazz.setGlobalPosX(clazz.getPosX());
            clazz.setGlobalPosY(clazz.getPosY());
        }
        for (Interface clazz : architecture.getInterfaces()) {
            clazz.setGlobalPosX(clazz.getPosX());
            clazz.setGlobalPosY(clazz.getPosY());

        }
        int pkgPosX = 0;
        int pkgPosY = 0;
        for (Package pkg : architecture.getAllPackages()) {
            pkg.setGlobalPosX(pkg.getPosX());
            pkg.setGlobalPosY(pkg.getPosY());

            pkgPosX = Integer.parseInt(pkg.getPosX());
            pkgPosY = Integer.parseInt(pkg.getPosY());

            for (Class clazz : pkg.getAllClasses()) {
                clazz.setGlobalPosX("" + (Integer.parseInt(clazz.getPosX()) + pkgPosX));
                clazz.setGlobalPosY("" + (Integer.parseInt(clazz.getPosY()) + pkgPosY));
            }
            for (Interface clazz : pkg.getAllInterfaces()) {
                clazz.setGlobalPosX("" + (Integer.parseInt(clazz.getPosX()) + pkgPosX));
                clazz.setGlobalPosY("" + (Integer.parseInt(clazz.getPosY()) + pkgPosY));
            }
            getGlobalPosElementsSubPackage(pkg);
        }
    }

    private void getGlobalPosElementsSubPackage(Package pkg1) {
        // atualizar tamanho de classe e interface:


        int pkgPosX = 0;
        int pkgPosY = 0;
        for (Package pkg : pkg1.getNestedPackages()) {
            pkgPosX = Integer.parseInt(pkg1.getPosX());
            pkgPosY = Integer.parseInt(pkg1.getPosY());

            pkg.setGlobalPosX("" + (Integer.parseInt(pkg.getPosX()) + pkgPosX));
            pkg.setGlobalPosY("" + (Integer.parseInt(pkg.getPosY()) + pkgPosY));

            pkgPosX = Integer.parseInt(pkg.getPosX()) + Integer.parseInt(pkg1.getPosX());
            pkgPosY = Integer.parseInt(pkg.getPosY()) + Integer.parseInt(pkg1.getPosY());

            for (Class clazz : pkg.getAllClasses()) {
                clazz.setGlobalPosX("" + (Integer.parseInt(clazz.getPosX()) + pkgPosX));
                clazz.setGlobalPosY("" + (Integer.parseInt(clazz.getPosY()) + pkgPosY));
            }
            for (Interface clazz : pkg.getAllInterfaces()) {
                clazz.setGlobalPosX("" + (Integer.parseInt(clazz.getPosX()) + pkgPosX));
                clazz.setGlobalPosY("" + (Integer.parseInt(clazz.getPosY()) + pkgPosY));
            }
        }
    }

}
