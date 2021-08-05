package br.otimizes.oplatool.core.jmetal4.operators.crossover;

import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.relationship.AssociationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.RealizationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.encodings.solutionType.ArchitectureSolutionType;
import br.otimizes.oplatool.core.jmetal4.operators.IOperator;
import br.otimizes.oplatool.core.jmetal4.problems.OPLA;
import br.otimizes.oplatool.core.jmetal4.util.PseudoRandom;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.mockito.cglib.beans.BeanCopier.Generator;


/**
 * @author Rafael, Janaina e João Messias
 * Proposta de operador PLAModularCrossover
 */

public class PLAFeatureDrivenCrossover implements IOperator<Solution[]> {

	private static final Logger LOG = Logger.getLogger(PLAFeatureDrivenCrossover.class);

	public static List<java.lang.Class<ArchitectureSolutionType>> VALID_TYPES = Arrays.asList(ArchitectureSolutionType.class);
	public Double crossoverProbability_ = null;
	public static String SCOPE_LEVEL = "allLevels";



	public Solution[] execute(Map<String, Object> parameters, Solution[] parents, String scope) {
		if (parameters.get("probability") != null)
			crossoverProbability_ = (Double) parameters.get("probability");
		Solution father = parents[0];
		Solution mother = parents[1];

		if (PseudoRandom.randDouble() < crossoverProbability_) {
			return doCrossover(father, mother);
		}
		Solution[] offspring = new Solution[2];
		offspring[0] = new Solution(father);
		offspring[1] = new Solution(mother);

		return offspring;
	}


	public Solution[] doCrossover(Solution parent1, Solution parent2)
	{

		Solution[] offspring = new Solution[2];
		offspring[0] = new Solution(parent1);
		offspring[1] = new Solution(parent2);

		try {

			List<Concern> concernsArchitecture = new ArrayList<Concern>(((Architecture) offspring[0].getDecisionVariables()[0]).getAllConcerns());

			Concern feature = randomObject(concernsArchitecture);

			Architecture p1 = (Architecture) parent1.getDecisionVariables()[0].deepCopy();;
			Architecture p2 = (Architecture) parent2.getDecisionVariables()[0].deepCopy();



			Architecture child1 = (Architecture) offspring[0].getDecisionVariables()[0];
			Architecture child2 = (Architecture) offspring[0].getDecisionVariables()[0];


			ArrayList<Class> class_p1 = getClassAssociatedWithFeature(p1,feature);
			ArrayList<Interface> interface_p1 = getInterfaceAssociatedWithFeature(p1,feature);
			ArrayList<Method> method_p1 = getMethodAssociatedWithFeature(p1,feature);
			ArrayList<Attribute> attribute_p1 = getAttributeAssociatedWithFeature(p1,feature);
			ArrayList<Method> operation_p1 = getOperationAssociatedWithFeature(p1,feature);

			ArrayList<Class> class_p2 = getClassAssociatedWithFeature(p2,feature);
			ArrayList<Interface> interface_p2 = getInterfaceAssociatedWithFeature(p2,feature);
			ArrayList<Method> method_p2 = getMethodAssociatedWithFeature(p2,feature);
			ArrayList<Attribute> attribute_p2 = getAttributeAssociatedWithFeature(p2,feature);
			ArrayList<Method> operation_p2 = getOperationAssociatedWithFeature(p2,feature);

			removeClassRealizingFeature(child1,class_p1);
			removeInterfaceRealizingFeature(child1,interface_p1);
			removeMethodRealizingFeature(child1,method_p1);
			removeAttributeRealizingFeature(child1,attribute_p1);
			removeOperationRealizingFeature(child1,operation_p1);


			removeClassRealizingFeature(child2,class_p2);
			removeInterfaceRealizingFeature(child2,interface_p2);
			removeMethodRealizingFeature(child2,method_p2);
			removeAttributeRealizingFeature(child2,attribute_p2);
			removeOperationRealizingFeature(child2,operation_p2);




			addClassRealizingFeature(child1,class_p2, p2);
			addInterfaceRealizingFeature(child1,interface_p2, p2);
			addMethodRealizingFeature(child1,method_p2, p2);
			addAttributeRealizingFeature(child1,attribute_p2, p2);
			addOperationRealizingFeature(child1,operation_p2, p2);


			addClassRealizingFeature(child2,class_p1, p1);
			addInterfaceRealizingFeature(child2,interface_p1, p1);
			addMethodRealizingFeature(child2,method_p1, p1);
			addAttributeRealizingFeature(child2,attribute_p1, p1);
			addOperationRealizingFeature(child2,operation_p1, p1);


			Architecture archP1 = ((Architecture)parent1.getDecisionVariables()[0]).deepClone();
			Architecture archP2 = ((Architecture)parent2.getDecisionVariables()[0]).deepClone();

			for(Relationship r : archP1.getRelationshipHolder().getAllRelationships()){
				child1.getRelationshipHolder().verifyAndAddRelationshipsChild2(r,child1);
			}
			for(Relationship r : archP2.getRelationshipHolder().getAllRelationships()){
				child1.getRelationshipHolder().verifyAndAddRelationshipsChild2(r,child1);
			}

			for(Package pkg : child1.getAllPackages()){
				pkg.setRelationshipHolder(child1.getRelationshipHolder());
			}
			for(Class c : child1.getAllClasses()){
				c.setRelationshipHolder(child1.getRelationshipHolder());
			}
			for(Interface c : child1.getAllInterfaces()){
				c.setRelationshipHolder(child1.getRelationshipHolder());
			}
			child1.matchRequiredAndImplementedInterface();


			for(Relationship r : archP1.getRelationshipHolder().getAllRelationships()){
				child2.getRelationshipHolder().verifyAndAddRelationshipsChild2(r,child2);
			}
			for(Relationship r : archP2.getRelationshipHolder().getAllRelationships()){
				child2.getRelationshipHolder().verifyAndAddRelationshipsChild2(r,child2);
			}

			for(Package pkg : child2.getAllPackages()){
				pkg.setRelationshipHolder(child2.getRelationshipHolder());
			}
			for(Class c : child2.getAllClasses()){
				c.setRelationshipHolder(child2.getRelationshipHolder());
			}
			for(Interface c : child2.getAllInterfaces()){
				c.setRelationshipHolder(child2.getRelationshipHolder());
			}
			child2.matchRequiredAndImplementedInterface();

			archP1.clearArchitecture();
			archP2.clearArchitecture();

			ArrayList<String> semLig = child1.getClassesWithoutRelationship();

			if(semLig.size() > 0){
				for(String id : semLig) {
					Class c11 = ((Architecture) parent1.getDecisionVariables()[0]).findClassById(id);
					if(c11 != null){
						for(Relationship r : c11.getRelationships()){

							if(r instanceof AssociationRelationship){
								child1.getRelationshipHolder().forceAddAssociationRelationshipsChild(r, child1);
							}
						}
					}else{
						c11 = ((Architecture) parent2.getDecisionVariables()[0]).findClassById(id);
						if(c11 != null){
							for(Relationship r : c11.getRelationships()){

								if(r instanceof AssociationRelationship){

									child1.getRelationshipHolder().forceAddAssociationRelationshipsChild(r, child1);

								}
							}
						}
					}
				}
			}



			semLig = child1.getInterfacesWithoutRelationship();


			if(semLig.size() > 0){
				for(String id : semLig) {

					Interface c11 = ((Architecture) parent1.getDecisionVariables()[0]).findInterfaceById(id);
					if(c11 != null){

						for(Relationship r : c11.getRelationships()){

							if(r instanceof RealizationRelationship)
								child1.getRelationshipHolder().forceAddRealization(r,child1);
						}
					}else{
						c11 = ((Architecture) parent2.getDecisionVariables()[0]).findInterfaceById(id);
						if(c11 != null){
							for(Relationship r : c11.getRelationships()){
								if(r instanceof RealizationRelationship)
									child1.getRelationshipHolder().forceAddRealization(r,child1);
							}
						}
					}
				}
			}



			semLig = child2.getClassesWithoutRelationship();

			if(semLig.size() > 0){
				for(String id : semLig) {
					Class c11 = ((Architecture) parent1.getDecisionVariables()[0]).findClassById(id);
					if(c11 != null){
						for(Relationship r : c11.getRelationships()){

							if(r instanceof AssociationRelationship){
								child2.getRelationshipHolder().forceAddAssociationRelationshipsChild(r, child2);
								AssociationRelationship re1 = (AssociationRelationship)r;
							}
						}
					}else{
						c11 = ((Architecture) parent2.getDecisionVariables()[0]).findClassById(id);
						if(c11 != null){
							for(Relationship r : c11.getRelationships()){
								if(r instanceof AssociationRelationship){
									child2.getRelationshipHolder().forceAddAssociationRelationshipsChild(r, child2);

									AssociationRelationship re1 = (AssociationRelationship)r;
								}
							}
						}
					}
				}
			}


			semLig = child2.getInterfacesWithoutRelationship();

			if(semLig.size() > 0){
				for(String id : semLig) {
					Interface c11 = ((Architecture) parent1.getDecisionVariables()[0]).findInterfaceById(id);
					if(c11 != null){
						for(Relationship r : c11.getRelationships()){
							if(r instanceof RealizationRelationship)
								child2.getRelationshipHolder().forceAddRealization(r,child2);
						}
					}else{
						c11 = ((Architecture) parent2.getDecisionVariables()[0]).findInterfaceById(id);
						if(c11 != null){
							for(Relationship r : c11.getRelationships()){
								if(r instanceof RealizationRelationship)
									child2.getRelationshipHolder().forceAddRealization(r,child2);
							}
						}
					}
				}
			}


			if (!(isValidSolution((Architecture) offspring[0].getDecisionVariables()[0]))) {
				offspring[0] = new Solution(parent1);
				OPLA.contDiscardedSolutions_++;
			}
			if (!(isValidSolution((Architecture) offspring[1].getDecisionVariables()[0]))) {
				offspring[1] = new Solution(parent2);
				OPLA.contDiscardedSolutions_++;
			}


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return offspring;

	}






	public <T> T randomObject(List<T> allObjects) throws JMException {
		int numObjects = allObjects.size();
		int key;
		T object;
		if (numObjects == 0) {
			object = null;
		} else {
			key = PseudoRandom.randInt(0, numObjects - 1);
			object = allObjects.get(key);
		}
		return object;
	}

	public ArrayList<Element> getAllElementsAssociatedWithFeature(Architecture architecture, Concern feature){
		ArrayList<Element> elementsAssociatedWithFeature = new ArrayList<>();

		for(Class clazz_ : architecture.getAllClasses()){
			if(clazz_.getOwnConcerns().size()==1 && clazz_.getOwnConcerns().contains(feature)){
				elementsAssociatedWithFeature.add(clazz_);
			}else {
				for (Method method : clazz_.getAllMethods()) {
					if (method.getOwnConcerns().size() == 1 && method.getOwnConcerns().contains(feature)) {
						elementsAssociatedWithFeature.add(method);
					}
				}
				for (Attribute attribute : clazz_.getAllAttributes()) {
					if (attribute.getOwnConcerns().size() == 1 && attribute.getOwnConcerns().contains(feature)) {
						elementsAssociatedWithFeature.add(attribute);
					}
				}
			}
		}
		for(Interface clazz_ : architecture.getAllInterfaces()){
			if(clazz_.getOwnConcerns().size()==1 && clazz_.getOwnConcerns().contains(feature)){
				elementsAssociatedWithFeature.add(clazz_);
			}else {
				for (Method method : clazz_.getMethods()) {
					if (method.getOwnConcerns().size() == 1 && method.getOwnConcerns().contains(feature)) {
						elementsAssociatedWithFeature.add(method);
					}
				}
			}
		}

		return  elementsAssociatedWithFeature;
	}

	public ArrayList<Class> getClassAssociatedWithFeature(Architecture architecture, Concern feature){
		ArrayList<Class> elementsAssociatedWithFeature = new ArrayList<>();
		for(Class clazz_ : architecture.getAllClasses()){
			if(clazz_.getOwnConcerns().size()==1 && clazz_.getOwnConcerns().contains(feature)){
				elementsAssociatedWithFeature.add(clazz_);
			}
		}
		return  elementsAssociatedWithFeature;
	}

	public ArrayList<Attribute> getAttributeAssociatedWithFeature(Architecture architecture, Concern feature){
		ArrayList<Attribute> elementsAssociatedWithFeature = new ArrayList<>();
		for(Class clazz_ : architecture.getAllClasses()) {
			for (Attribute attribute : clazz_.getAllAttributes()) {
				if (attribute.getOwnConcerns().size() == 1 && attribute.getOwnConcerns().contains(feature)) {
					elementsAssociatedWithFeature.add(attribute);
				}
			}
		}
		return  elementsAssociatedWithFeature;
	}

	public ArrayList<Method> getMethodAssociatedWithFeature(Architecture architecture, Concern feature){
		ArrayList<Method> elementsAssociatedWithFeature = new ArrayList<>();
		for(Class clazz_ : architecture.getAllClasses()) {
			for (Method method : clazz_.getAllMethods()) {
				if (method.getOwnConcerns().size() == 1 && method.getOwnConcerns().contains(feature)) {
					elementsAssociatedWithFeature.add(method);
				}
			}
		}
		return  elementsAssociatedWithFeature;
	}

	public ArrayList<Interface> getInterfaceAssociatedWithFeature(Architecture architecture, Concern feature){
		ArrayList<Interface> elementsAssociatedWithFeature = new ArrayList<>();
		for(Interface clazz_ : architecture.getAllInterfaces()){
			if(clazz_.getOwnConcerns().size()==1 && clazz_.getOwnConcerns().contains(feature)){
				elementsAssociatedWithFeature.add(clazz_);
			}
		}
		return  elementsAssociatedWithFeature;
	}

	public ArrayList<Method> getOperationAssociatedWithFeature(Architecture architecture, Concern feature){
		ArrayList<Method> elementsAssociatedWithFeature = new ArrayList<>();
		for(Interface clazz_ : architecture.getAllInterfaces()) {
			for (Method method : clazz_.getMethods()) {
				if (method.getOwnConcerns().size() == 1 && method.getOwnConcerns().contains(feature)) {
					elementsAssociatedWithFeature.add(method);
				}
			}
		}
		return  elementsAssociatedWithFeature;
	}

	public void removeElementsRealizingFeature(Architecture child, ArrayList<Element> elements){
		for(Element element : elements){
			if(element instanceof Class){
				child.removeClassByID(element.getId());
			}
			if(element instanceof Interface){
				child.removeInterfaceByID(element.getId());
			}
			if(element instanceof Method){
				child.removeMethodByID(element.getId());
			}
			if(element instanceof Attribute){
				child.removeAttributeByID(element.getId());
			}
		}
	}

	public void removeClassRealizingFeature(Architecture child, ArrayList<Class> elements){
		for(Class element : elements){
			child.removeClassByID(element.getId());
		}
	}

	public void removeInterfaceRealizingFeature(Architecture child, ArrayList<Interface> elements){
		for(Interface element : elements){
			child.removeInterfaceByID(element.getId());
		}
	}

	public void removeMethodRealizingFeature(Architecture child, ArrayList<Method> elements){
		for(Method element : elements){
			child.removeMethodOfClassByID(element.getId());
		}
	}

	public void removeAttributeRealizingFeature(Architecture child, ArrayList<Attribute> elements){
		for(Attribute element : elements){
			child.removeAttributeByID(element.getId());
		}
	}

	public void removeOperationRealizingFeature(Architecture child, ArrayList<Method> elements){
		for(Method element : elements){
			child.removeOperationOfInterfaceByID(element.getId());
		}
	}

	public void addElementsRealizingFeature(Architecture child, ArrayList<Element> elements, Architecture parent){
		for(Element element : elements){
			if(element instanceof Class){
				Class clazz_ = (Class) element;
				Package pkg_parent = parent.findPackageOfElementID(clazz_.getId());
				if(pkg_parent == null){
					child.addExternalClass(clazz_);
				}else{
					Package pkg_child = child.findPackageByID(pkg_parent.getId());
					if(pkg_child == null){
						pkg_child = child.createPackage(pkg_parent.getName(),pkg_parent.getId());
					}
					child.addClassOrInterface(clazz_,pkg_child);
				}
			}
			if(element instanceof Interface){
				Interface interface_ = (Interface)element;
				Package pkg_parent = parent.findPackageOfElementID(interface_.getId());
				if(pkg_parent == null){
					child.addExternalInterface(interface_);
				}else{
					Package pkg_child = child.findPackageByID(pkg_parent.getId());
					if(pkg_child == null){
						pkg_child = child.createPackage(pkg_parent.getName(),pkg_parent.getId());
					}
					child.addClassOrInterface(interface_,pkg_child);
				}
			}
			if(element instanceof Method){
				Method method = (Method)element;
				Element elementParent = parent.findClassOrInterfaceOfMethodByID(method.getId());
				Element elementChild = child.findElementById(elementParent.getId());
				if(elementChild== null){
					if(elementParent instanceof Class){
						Class parentClass = (Class)elementParent;

						Class newClass = new Class(child.getRelationshipHolder(), parentClass.getName(), parentClass.getVariant(), parentClass.isAbstract(),parentClass.getNamespace(),parentClass.getId());
						newClass.addExternalMethod(method);

						Package pkg_parent = parent.findPackageOfElementID(newClass.getId());
						if(pkg_parent == null){
							child.addExternalClass(newClass);
						}else{
							Package pkg_child = child.findPackageByID(pkg_parent.getId());
							if(pkg_child == null){
								pkg_child = child.createPackage(pkg_parent.getName(),pkg_parent.getId());
							}
							child.addClassOrInterface(newClass,pkg_child);
						}
					}
					if(elementParent instanceof Interface){
						Interface parentClass = (Interface) elementParent;

						Interface newInterface = new Interface(child.getRelationshipHolder(), parentClass.getName(), parentClass.getVariant(), parentClass.getNamespace(),parentClass.getId());
						newInterface.addExternalMethod(method);

						Package pkg_parent = parent.findPackageOfElementID(newInterface.getId());
						if(pkg_parent == null){
							child.addExternalInterface(newInterface);
						}else{
							Package pkg_child = child.findPackageByID(pkg_parent.getId());
							if(pkg_child == null){
								pkg_child = child.createPackage(pkg_parent.getName(),pkg_parent.getId());
							}
							child.addClassOrInterface(newInterface,pkg_child);
						}
					}
				}else{
					if(elementChild instanceof Class){
						((Class)elementChild).addExternalMethod(method);
					}
					if(elementChild instanceof Interface){
						((Interface)elementChild).addExternalMethod(method);
					}
				}


			}
			if(element instanceof Attribute){
				Attribute attribute = (Attribute)element;

				Element elementParent = parent.findClassOfAttributeByID(attribute.getId());
				Element elementChild = child.findElementById(elementParent.getId());
				if(elementChild== null){
					if(elementParent instanceof Class){
						Class parentClass = (Class)elementParent;

						Class newClass = new Class(child.getRelationshipHolder(), parentClass.getName(), parentClass.getVariant(), parentClass.isAbstract(),parentClass.getNamespace(),parentClass.getId());
						newClass.addExternalAttribute(attribute);

						Package pkg_parent = parent.findPackageOfElementID(newClass.getId());
						if(pkg_parent == null){
							child.addExternalClass(newClass);
						}else{
							Package pkg_child = child.findPackageByID(pkg_parent.getId());
							if(pkg_child == null){
								pkg_child = child.createPackage(pkg_parent.getName(),pkg_parent.getId());
							}
							child.addClassOrInterface(newClass,pkg_child);
						}
					}
				}else{
					((Class)elementChild).addExternalAttribute(attribute);

				}
			}
		}
	}

	public void addClassRealizingFeature(Architecture child, ArrayList<Class> elements, Architecture parent) {
		for (Class clazz_ : elements) {
			for(Class cx : child.getAllClasses()){
				for(Attribute a : cx.getAllAttributes()){
					clazz_.removeAttributeByID(a.getId());
				}
				for(Method a : cx.getAllMethods()){
					clazz_.removeMethodByID(a.getId());
				}
			}


			Package pkg_parent = parent.findPackageOfElementID(clazz_.getId());
			if (pkg_parent == null) {
				child.addExternalClass(clazz_);
			} else {
				Package pkg_child = child.findPackageByID(pkg_parent.getId());
				if (pkg_child == null) {
					pkg_child = child.createPackage(pkg_parent.getName(), pkg_parent.getId());
				}
				child.addClassOrInterface(clazz_, pkg_child);
			}

		}
	}

	public void addInterfaceRealizingFeature(Architecture child, ArrayList<Interface> elements, Architecture parent) {
		for (Interface interface_ : elements) {
			for(Interface cx : child.getAllInterfaces()){
				for(Method a : cx.getMethods()){
					interface_.removeMethodByID(a.getId());
				}
			}


			Package pkg_parent = parent.findPackageOfElementID(interface_.getId());
			if (pkg_parent == null) {
				child.addExternalInterface(interface_);
			} else {
				Package pkg_child = child.findPackageByID(pkg_parent.getId());
				if (pkg_child == null) {
					pkg_child = child.createPackage(pkg_parent.getName(), pkg_parent.getId());
				}
				child.addClassOrInterface(interface_, pkg_child);
			}

		}
	}

	public void addMethodRealizingFeature(Architecture child, ArrayList<Method> elements, Architecture parent) {
		for (Method method : elements) {

			boolean exist = false;
			for(Class cx : child.getAllClasses()){

				for(Method a : cx.getAllMethods()){
					if(method.getId().equals(a.getId()))
						exist = true;
				}
			}
			if(exist)
				continue;

			Class parentClass = parent.findClassOfMethodByID(method.getId());
			if(parentClass != null) {
				Class elementChild = child.findClassById(parentClass.getId());
				if (elementChild == null) {

					Class newClass = new Class(child.getRelationshipHolder(), parentClass.getName(), parentClass.getVariant(), parentClass.isAbstract(), parentClass.getNamespace(), parentClass.getId());
					newClass.addExternalMethod(method);
					Package pkg_parent = parent.findPackageOfElementID(newClass.getId());
					if (pkg_parent == null) {
						child.addExternalClass(newClass);
					} else {
						Package pkg_child = child.findPackageByID(pkg_parent.getId());
						if (pkg_child == null) {
							pkg_child = child.createPackage(pkg_parent.getName(), pkg_parent.getId());
						}
						child.addClassOrInterface(newClass, pkg_child);
					}
				} else {
					elementChild.addExternalMethod(method);
				}
			}
		}
	}



	public void addAttributeRealizingFeature(Architecture child, ArrayList<Attribute> elements, Architecture parent) {
		for (Attribute attribute : elements) {

			boolean exist = false;
			for(Class cx : child.getAllClasses()){

				for(Attribute a : cx.getAllAttributes()){
					if(attribute.getId().equals(a.getId()))
						exist = true;
				}
			}
			if(exist)
				continue;;


			Class parentClass = parent.findClassOfAttributeByID(attribute.getId());
			Class elementChild = child.findClassById(parentClass.getId());
			if (elementChild == null) {

				Class newClass = new Class(child.getRelationshipHolder(), parentClass.getName(), parentClass.getVariant(), parentClass.isAbstract(), parentClass.getNamespace(), parentClass.getId());
				newClass.addExternalAttribute(attribute);

				Package pkg_parent = parent.findPackageOfElementID(newClass.getId());
				if (pkg_parent == null) {
					child.addExternalClass(newClass);
				} else {
					Package pkg_child = child.findPackageByID(pkg_parent.getId());
					if (pkg_child == null) {
						pkg_child = child.createPackage(pkg_parent.getName(), pkg_parent.getId());
					}
					child.addClassOrInterface(newClass, pkg_child);
				}

			} else {
				elementChild.addExternalAttribute(attribute);
			}
		}
	}

	public void addOperationRealizingFeature(Architecture child, ArrayList<Method> elements, Architecture parent) {
		for (Method method : elements) {

			boolean exist = false;
			for(Interface cx : child.getAllInterfaces()){

				for(Method a : cx.getMethods()){
					if(method.getId().equals(a.getId()))
						exist = true;
				}
			}
			if(exist)
				continue;

			Interface parentClass = parent.findInterfaceOfOperationByID(method.getId());
			Interface elementChild = child.findInterfaceById(parentClass.getId());
			if (elementChild == null) {

				Interface newClass = new Interface(child.getRelationshipHolder(), parentClass.getName(), parentClass.getVariant(), parentClass.getNamespace(), parentClass.getId());
				newClass.addExternalMethod(method);
				Package pkg_parent = parent.findPackageOfElementID(newClass.getId());
				if (pkg_parent == null) {
					child.addExternalInterface(newClass);
				} else {
					Package pkg_child = child.findPackageByID(pkg_parent.getId());
					if (pkg_child == null) {
						pkg_child = child.createPackage(pkg_parent.getName(), pkg_parent.getId());
					}
					child.addClassOrInterface(newClass, pkg_child);
				}
			} else {
				elementChild.addExternalMethod(method);
			}
		}
	}

	// Thelma - Dez2013 m��todo adicionado
	// verify if the architecture contains a valid PLA design, i.e., if there is not any interface without relationships in the architecture.
	private boolean isValidSolution(Architecture solution) {
		boolean isValid = true;

		final List<Interface> allInterfaces = new ArrayList<Interface>(solution.getAllInterfaces());
		if (!allInterfaces.isEmpty()) {
			for (Interface itf : allInterfaces) {
				if ((itf.getImplementors().isEmpty()) && (itf.getDependents().isEmpty()) && (!itf.getMethods().isEmpty())) {
					return false;
				}
			}
		}
		return isValid;
	}

}