package br.otimizes.oplatool.core.jmetal4.operators.crossover;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.relationship.DependencyRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.GeneralizationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.RealizationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.encodings.solutionType.ArchitectureSolutionType;
import br.otimizes.oplatool.core.jmetal4.operators.IOperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @author Diego Fernandes da Silva
 * Proposta de operador PLAModularCrossover
 */

public class PLAModularCrossover  {

	public static List<java.lang.Class<ArchitectureSolutionType>> VALID_TYPES = Arrays.asList(ArchitectureSolutionType.class);
	public Double crossoverProbability_ = null;
	public static String SCOPE_LEVEL = "allLevels";

	public ArrayList<String> getRelatedPackages(Architecture parent, Package pkg, ArrayList<String> lstRelatedPackage){
		lstRelatedPackage = new ArrayList<>();
		lstRelatedPackage.add(pkg.getId());

		Package pgn;

		for(Class clazz : pkg.getAllClasses()) {

			for (Relationship r : clazz.getRelationships()) {
				if (r instanceof GeneralizationRelationship) {
					GeneralizationRelationship gn = (GeneralizationRelationship) r;
					if (gn.getChild().equals(clazz)) {
						if (gn.getParent() instanceof Class) {
							pgn = parent.findPackageOfClass((Class) gn.getParent());
							if(pgn != null) {
								if (!pgn.getId().equals(pkg.getId())) {
									if (!lstRelatedPackage.contains(pgn.getId()))
										lstRelatedPackage.add(pgn.getId());
								}
							}
						}
						if (gn.getParent() instanceof Interface) {
							pgn = parent.findPackageOfInterface((Interface) gn.getParent());
							if(pgn != null) {
								if (!pgn.getId().equals(pkg.getId())) {
									if (!lstRelatedPackage.contains(pgn.getId()))
										lstRelatedPackage.add(pgn.getId());
								}
							}
						}
					}
				}
				if (r instanceof RealizationRelationship) {

					RealizationRelationship gn = (RealizationRelationship) r;
					if (gn.getClient().equals(clazz)) {
						if (gn.getSupplier() instanceof Class) {
							pgn = parent.findPackageOfClass((Class) gn.getSupplier());
							if(pgn != null) {
								if (!pgn.getId().equals(pkg.getId())) {
									if (!lstRelatedPackage.contains(pgn.getId()))
										lstRelatedPackage.add(pgn.getId());
								}
							}
						}
						if (gn.getSupplier() instanceof Interface) {
							pgn = parent.findPackageOfInterface((Interface) gn.getSupplier());
							if(pgn != null) {
								if (!pgn.getId().equals(pkg.getId())) {
									if (!lstRelatedPackage.contains(pgn.getId()))
										lstRelatedPackage.add(pgn.getId());
								}
							}
						}
					}

				}
				if (r instanceof DependencyRelationship) {
					DependencyRelationship gn = (DependencyRelationship) r;
					if (gn.getClient().equals(clazz)) {
						if (gn.getSupplier() instanceof Class) {
							pgn = parent.findPackageOfClass((Class) gn.getSupplier());
							if(pgn != null) {
								if (!pgn.getId().equals(pkg.getId())) {
									if (!lstRelatedPackage.contains(pgn.getId()))
										lstRelatedPackage.add(pgn.getId());
								}
							}
						}
						if (gn.getSupplier() instanceof Interface) {
							pgn = parent.findPackageOfInterface((Interface) gn.getSupplier());
							if(pgn != null) {
								if (!pgn.getId().equals(pkg.getId())) {
									if (!lstRelatedPackage.contains(pgn.getId()))
										lstRelatedPackage.add(pgn.getId());
								}
							}
						}
					}
				}
			}
		}

		for(Interface clazz : pkg.getAllInterfaces()) {

			for (Relationship r : clazz.getRelationships()) {
				if (r instanceof GeneralizationRelationship) {
					GeneralizationRelationship gn = (GeneralizationRelationship) r;
					if (gn.getChild().equals(clazz)) {
						if (gn.getParent() instanceof Class) {
							pgn = parent.findPackageOfClass((Class) gn.getParent());
							if(pgn != null) {
								if (!pgn.getId().equals(pkg.getId())) {
									if (!lstRelatedPackage.contains(pgn.getId()))
										lstRelatedPackage.add(pgn.getId());
								}
							}
						}
						if (gn.getParent() instanceof Interface) {
							pgn = parent.findPackageOfInterface((Interface) gn.getParent());
							if(pgn != null) {
								if (!pgn.getId().equals(pkg.getId())) {
									if (!lstRelatedPackage.contains(pgn.getId()))
										lstRelatedPackage.add(pgn.getId());
								}
							}
						}
					}
				}
				if (r instanceof RealizationRelationship) {

					RealizationRelationship gn = (RealizationRelationship) r;
					if (gn.getClient().equals(clazz)) {
						if (gn.getSupplier() instanceof Class) {
							pgn = parent.findPackageOfClass((Class) gn.getSupplier());
							if(pgn!=null) {
								if (!pgn.getId().equals(pkg.getId())) {
									if (!lstRelatedPackage.contains(pgn.getId()))
										lstRelatedPackage.add(pgn.getId());
								}
							}
						}
						if (gn.getSupplier() instanceof Interface) {
							pgn = parent.findPackageOfInterface((Interface) gn.getSupplier());
							if (pgn != null) {
								if (!pgn.getId().equals(pkg.getId())) {
									if (!lstRelatedPackage.contains(pgn.getId()))
										lstRelatedPackage.add(pgn.getId());
								}
							}
						}
					}

				}
				if (r instanceof DependencyRelationship) {
					DependencyRelationship gn = (DependencyRelationship) r;
					if (gn.getClient().equals(clazz)) {
						if (gn.getSupplier() instanceof Class) {
							pgn = parent.findPackageOfClass((Class) gn.getSupplier());
							if(pgn!=null) {
								if (!pgn.getId().equals(pkg.getId())) {
									if (!lstRelatedPackage.contains(pgn.getId()))
										lstRelatedPackage.add(pgn.getId());
								}
							}
						}
						if (gn.getSupplier() instanceof Interface) {
							pgn = parent.findPackageOfInterface((Interface) gn.getSupplier());
							if(pgn!=null) {
								if (!pgn.getId().equals(pkg.getId())) {
									if (!lstRelatedPackage.contains(pgn.getId()))
										lstRelatedPackage.add(pgn.getId());
								}
							}
						}
					}
				}
			}
		}
		return  lstRelatedPackage;
	}

	public void addElementsNotInPackage(Architecture child, Architecture parentOriginal){

		Architecture parent = parentOriginal.deepClone();

		for(Class c1 : parent.getClasses()) {
			Class clazz = child.findClassById(c1.getId());

			if(clazz == null) {

				c1.setRelationshipHolder(child.getRelationshipHolder());
				child.addExternalClass(c1);


			}
		}
		for(Interface c1 : parent.getInterfaces()) {

			Interface inter = child.findInterfaceById(c1.getId());

			if(inter == null) {

				c1.setRelationshipHolder(child.getRelationshipHolder());
				child.addExternalInterface(c1);

			}
		}
		parent.clearArchitecture();
	}

	public void addElementsInPackage(Architecture child, Architecture parentOriginal){

		Architecture parent = parentOriginal.deepClone();

		for(Package pkg : parent.getAllPackages()){
			ArrayList<String> lstRemove = new ArrayList<>();
			for(Class clazz : pkg.getAllClasses()){
				if(child.findClassById(clazz.getId())!= null){
					lstRemove.add(clazz.getId());
				}
			}
			for(String remove : lstRemove){
				pkg.removeClassByID(remove);
			}
			lstRemove.clear();
			for(Interface clazz : pkg.getAllInterfaces()){
				if(child.findInterfaceById(clazz.getId())!= null){
					lstRemove.add(clazz.getId());
				}
			}
			for(String remove : lstRemove){
				pkg.removeInterfaceByID(remove);
			}
			if(pkg.getAllClasses().size() + pkg.getAllInterfaces().size() > 0){
				Package pkgChild = child.findPackageByID(pkg.getId());
				if(pkgChild==null){
					pkg.setRelationshipHolder(child.getRelationshipHolder());
					for(Class clazz : pkg.getAllClasses()){
						clazz.setRelationshipHolder(child.getRelationshipHolder());
					}
					for(Interface clazz : pkg.getAllInterfaces()){
						clazz.setRelationshipHolder(child.getRelationshipHolder());
					}
					child.addPackage(pkg);
				}else{
					for(Class clazz : pkg.getAllClasses()){
						clazz.setRelationshipHolder(child.getRelationshipHolder());
						child.addClassOrInterface(clazz,pkgChild);
					}
					for(Interface clazz : pkg.getAllInterfaces()){
						clazz.setRelationshipHolder(child.getRelationshipHolder());
						child.addClassOrInterface(clazz,pkgChild);
					}
				}
			}
		}

		parent.clearArchitecture();
	}

	public void addElements(Architecture child, ArrayList<String> id_packages, Architecture parentOriginal){

		Architecture parent = parentOriginal.deepClone();
		Package pkg;
		Package pkgChild;
		Class classChild;
		ArrayList<String> lstClassRemove = new ArrayList<>();
		ArrayList<String> lstInterfaceRemove = new ArrayList<>();
		Interface interfaceChild;

		for(String id : id_packages) {

			pkg = parent.findPackageByID(id);

			if(pkg != null) {
				pkgChild = child.findPackageByID(pkg.getId());

				if (pkgChild == null) {
					lstClassRemove.clear();
					for(Class c : pkg.getAllClasses()){
						classChild = child.findClassById(c.getId());
						if(classChild != null) {
							lstClassRemove.add(c.getId());
						}

					}
					for(String id0 : lstClassRemove) {
						pkg.removeClassByID(id0);
					}

					lstInterfaceRemove.clear();
					for(Interface c : pkg.getAllInterfaces()){
						interfaceChild = child.findInterfaceById(c.getId());
						if(interfaceChild != null)
							lstInterfaceRemove.add(c.getId());

					}
					if(lstInterfaceRemove.size() > 0){
						for(String id1 : lstInterfaceRemove){
							pkg.removeInterfaceByID(id1);
						}
					}

					for (Class clazz : pkg.getAllClasses()) {
						clazz.setRelationshipHolder(child.getRelationshipHolder());
					}
					for (Interface inter : pkg.getAllInterfaces()) {
						inter.setRelationshipHolder(child.getRelationshipHolder());
					}

					child.addPackage(pkg);

				} else {

					for (Class clazz : pkg.getAllClasses()) {
						classChild = child.findClassById(clazz.getId());
						if(classChild == null) {

							clazz.setRelationshipHolder(child.getRelationshipHolder());
							child.addClassOrInterface(clazz, pkgChild);
						}
					}
					for (Interface inter : pkg.getAllInterfaces()) {
						interfaceChild = child.findInterfaceById(inter.getId());
						if(interfaceChild == null) {
							inter.setRelationshipHolder(child.getRelationshipHolder());
							child.addClassOrInterface(inter, pkgChild);
						}
					}
				}
				pkg.setRelationshipHolder(child.getRelationshipHolder());
			}else{

			}
		}
		parent.clearArchitecture();
		pkg = null;
		pkgChild = null;
		classChild = null;
		lstClassRemove = null;
		lstInterfaceRemove = null;
		interfaceChild = null;
	}


	public void removeElements(Architecture parent, ArrayList<String> id_packages, Architecture selectedParent){


		if(parent.getAllPackages().size()==0)
			return;
		ArrayList<String> lstElement = new ArrayList<>();
		Package pkg1;

		for(String id : id_packages) {
			pkg1 = selectedParent.findPackageByID(id);
			if(pkg1 != null) {
				for (Class clazz : pkg1.getAllClasses()) {
					lstElement.add(clazz.getId());

				}
				for (Interface inter : pkg1.getAllInterfaces()) {
					lstElement.add(inter.getId());
				}
			}

		}
		for(String id : lstElement){
			parent.removeClassByID(id);
			parent.removeInterfaceByID(id);
		}


		ArrayList<String> lstPkg = new ArrayList<>();
		for(Package pkg : parent.getAllPackages()){
			if(pkg.getAllClasses().size()+pkg.getAllInterfaces().size()==0){
				lstPkg.add(pkg.getId());
			}

		}

		for(String id : lstPkg){
			parent.removePackageByID(id);

		}
		pkg1 = null;
		lstElement = null;
		lstPkg = null;
	}


	public void removePackages(Architecture parent, ArrayList<String> id_packages){
		if(parent.getAllPackages().size()==0)
			return;
		for(String pk : id_packages){
			parent.removePackageByID(pk);
		}
	}

	// verify if the architecture contains a valid PLA design, i.e., if there is not any interface without relationships in the architecture.
	private boolean isValidSolution(Architecture solution) {
		boolean isValid = true;

		final List<Interface> allInterfaces = new ArrayList<Interface>(solution.getAllInterfaces());
		if (!allInterfaces.isEmpty()) {
			for (Interface itf : allInterfaces) {
				if ((itf.getImplementors().isEmpty()) && (itf.getDependents().isEmpty()) && (!itf.getOperations().isEmpty())) {
					return false;
				}
			}
		}

		return isValid;
	}

}