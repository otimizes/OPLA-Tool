//package jmetal.operators.crossover;
//
//
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Set;
//import java.util.logging.Level;
//
//import jmetal4.core.Solution;
//import jmetal4.encodings.solutionType.ArchitectureSolutionType;
//import jmetal4.problems.OPLA;
//import jmetal4.util.Configuration;
//import jmetal4.util.JMException;
//import jmetal4.util.PseudoRandom;
//import arquitetura.exceptions.ClassNotFound;
//import arquitetura.exceptions.ConcernNotFoundException;
//import arquitetura.exceptions.InterfaceNotFound;
//import arquitetura.exceptions.NotFoundException;
//import arquitetura.exceptions.PackageNotFound;
//import arquitetura.representation.Architecture;
//import arquitetura.representation.Attribute;
//import arquitetura.representation.Class;
//import arquitetura.representation.Concern;
//import arquitetura.representation.Element;
//import arquitetura.representation.Interface;
//import arquitetura.representation.Method;
//import arquitetura.representation.Package;
//import arquitetura.representation.RelationshipHolder;
//import arquitetura.representation.Variability;
//import arquitetura.representation.VariationPoint;
//import arquitetura.representation.relationship.DependencyRelationship;
//import arquitetura.representation.relationship.GeneralizationRelationship;
//import arquitetura.representation.relationship.Relationship;
//
//
//public class PLACrossover extends Crossover {
//		
//	private static final long serialVersionUID = -51015356906090226L;
//	
//	/**
//	 * ARCHITECTURE_SOLUTION represents class jmetal4.encodings.solutionType.ArchitectureSolutionType
//	 */
//	private Double crossoverProbability_ = null;
//		
//			
//	/**
//	 * Valid solution types to apply this operator 
//	 */
//	private static List VALID_TYPES = Arrays.asList(ArchitectureSolutionType.class) ;
//
//	  	
//	public PLACrossover(HashMap<String, Object> parameters) {
//		super(parameters) ;
//			
//	  	if (parameters.get("probability") != null)
//	  		crossoverProbability_ = (Double) getParameter("probability");
//	}
//
//				        
//    public Solution[] doCrossover(double probability, Solution parent1, Solution parent2) throws JMException, CloneNotSupportedException, ClassNotFound, PackageNotFound, NotFoundException, ConcernNotFoundException {
//
//    	//use "oneLevel" para não verificar a presença de interesses nos atributos e métodos
//    	String scopeLevel = "allLevels"; 
//        Solution[] offspring = new Solution[2];
//        
//        Solution[] crossFeature = this.crossoverFeatures(crossoverProbability_, parent1, parent2, scopeLevel);
//        offspring[0] = crossFeature[0];
//        offspring[1] = crossFeature[1];
//     
//        return offspring;
//    }
//    
//	     
//    public Solution[] crossoverFeatures(double probability, Solution parent1, Solution parent2, String scope) throws JMException, CloneNotSupportedException, ClassNotFound, PackageNotFound, NotFoundException, ConcernNotFoundException {
//
//    	// STEP 0: Create two offsprings
//        Solution[] offspring = new Solution[2];
//        offspring[0] = new Solution(parent1);
//        offspring[1] = new Solution(parent2);
//               
//        try {
//            if (parent1.getDecisionVariables()[0].getVariableType() == java.lang.Class.forName(Architecture.ARCHITECTURE_TYPE)) {
//               if (PseudoRandom.randDouble() < probability) {
//              
//                    // STEP 1: Get feature to crossover
//                    List<Concern> concernsArchitecture = new ArrayList<Concern> (((Architecture) offspring[0].getDecisionVariables()[0]).getAllConcerns());
//                    //Concern feature = randomObject(concernsArchitecture); //EDIPO descomentar somente para testes
//                    Concern feature  =  getConcernTesteApenas(concernsArchitecture);
//
//                    // STEP 2: Obtain the first child
//                    obtainChild(feature, (Architecture) offspring[1].getDecisionVariables()[0], (Architecture) offspring[0].getDecisionVariables()[0], scope);
//                    
//                    //  STEP 3: Obtain the second child
//                    obtainChild(feature, (Architecture) offspring[0].getDecisionVariables()[0], (Architecture) offspring[1].getDecisionVariables()[0], scope);
//               }
//            }
//            else {
//            	Configuration.logger_.log(Level.SEVERE, "PLACrossover.doCrossover: "+ "invalid type{0}", parent1.getDecisionVariables()[0].getVariableType());
//            	java.lang.Class<String> cls = java.lang.String.class;
//            	String name = cls.getName();
//            	throw new JMException("Exception in " + name + ".doCrossover()");
//            }
//        } catch (ClassNotFoundException e) {
//        	e.printStackTrace();            
//        }
//
//        return offspring;
//    }
//	    
//	  //--------------------------------------------------------------------------
//	    
//
//		/**
//		 * Executes the operation
//		 * @param object An object containing an array of two solutions 
//		 * @return An object containing an array with the offSprings
//		 * @throws JMException 
//	     * @throws NotFoundException 
//	     * @throws PackageNotFound 
//	     * @throws ClassNotFound 
//	     * @throws CloneNotSupportedException 
//	     * @throws ConcernNotFoundException 
//		 */
//		public Object execute(Object object) throws JMException, CloneNotSupportedException, ClassNotFound, PackageNotFound, NotFoundException, ConcernNotFoundException {
//			Solution [] parents = (Solution []) object;
//			//Double crossoverProbability ;
//			
//			if (!(VALID_TYPES.contains(parents[0].getType().getClass())  && VALID_TYPES.contains(parents[1].getType().getClass())) ) {
//				Configuration.logger_.severe("PLACrossover.execute: the solutions " + "are not of the right type. The type should be 'Permutation', but " + parents[0].getType() + " and " + parents[1].getType() + " are obtained");
//			} 
//
//			crossoverProbability_ = (Double)getParameter("probability");
//			
//			if (parents.length < 2)
//			{
//				Configuration.logger_.severe("PLACrossover.execute: operator needs two " +	"parents");
//				java.lang.Class<String> cls = java.lang.String.class;
//				String name = cls.getName(); 
//				throw new JMException("Exception in " + name + ".execute()") ;      
//			}
//
//			Solution [] offspring = doCrossover(crossoverProbability_, parents[0], parents[1]); 
//
//				
//			return offspring; 
//		} // execute
//		
//		
//		
//	    
//	    private void obtainChild(Concern feature, Architecture parent, Architecture offspring, String scope) throws CloneNotSupportedException, ClassNotFound, PackageNotFound, NotFoundException, ConcernNotFoundException{
//	    	Architecture auxParent = parent.deepClone();
//	    	//eliminar os elementos arquiteturais que realizam feature em offspring
//	    	if (removeOffspringArchitecturalElementsRealizingFeature(feature, offspring, scope)){
//	    		//adicionar em offspring os elementos arquiteturais que realizam feature em parent
//	    		addParentArchitecturalElementsRealizingFeature(feature, offspring, auxParent,scope);	
//	    		updateVariabilitiesOffspring(offspring);
//	    	}
//	    	
//	    }
//
//    private void addClassesRealizingFeatureToOffspring (Concern feature, Package comp, Architecture offspring, Architecture parent, String scope) throws ConcernNotFoundException, PackageNotFound, NotFoundException, ClassNotFound, CloneNotSupportedException {
//    	Package newComp = null;
//    	Interface newItf;
//    	List<Class> allClasses = new ArrayList<Class> (comp.getAllClasses());
//		if (!allClasses.isEmpty()) {
//			Iterator<Class> iteratorClasses = allClasses.iterator();
//			while (iteratorClasses.hasNext()){
//            	Class classComp = iteratorClasses.next();
//            	if (comp.getAllClasses().contains(classComp)){
//            		if (classComp.containsConcern(feature) && classComp.getOwnConcerns().size() == 1){
//	            		newComp = offspring.findPackageByName(comp.getName());
//	        			if (newComp == null)
//	    					try {
//	    						newComp = offspring.createPackage(comp.getName());
//	    					} catch (Exception e) {
//	    						e.printStackTrace();
//	    						newItf = offspring.createInterface("Interface"+ OPLA.contInt_++);
//	    						newItf.addConcern(feature.getName());
//	    						offspring.addImplementedInterface(newItf, newComp);
//	    					}	        		 	
//	        			
//	            		if (!searchForGeneralizations(classComp)){
//	            			CrossoverOperations.addClassToOffspring(classComp, newComp, offspring);
//	            		} else {
//	            			if (this.isHierarchyInASameComponent(classComp,parent))
//	            				CrossoverOperations.moveHierarchyToSameComponent(classComp, newComp, comp, offspring, parent, feature);
//	            			//TODO resolver problema com a heranca entre diferentes componentes
//	            			//else moveHierarchyToComponent(classComp, newComp, comp, offspring, parent, feature);
//	            		}
//	            	}	
//		    		else{
//	    				if ((scope=="allLevels") && (!searchForGeneralizations(classComp))){
//	    					CrossoverOperations.addAttributesRealizingFeatureToOffspring(feature, classComp, comp, offspring);
//	    					addMethodsRealizingFeatureToOffspring(feature, classComp, comp, offspring, parent);
//	    				}
//		    		}	            		
//            	}
//			}
//		}
//    }
//	    
//    private void addClassesToOffspring (Concern feature, Package comp, Package newComp, Architecture offspring, Architecture parent) throws ClassNotFound, CloneNotSupportedException, PackageNotFound {
//    	
//    	List<Class> allClasses = new ArrayList<Class> (comp.getAllClasses());
//		if (!allClasses.isEmpty()) {
//			Iterator<Class> iteratorClasses = allClasses.iterator();
//			while (iteratorClasses.hasNext()){
//            	Class classComp= iteratorClasses.next();
//            	if (comp.getAllClasses().contains(classComp)){
//            		if (!searchForGeneralizations(classComp)){
//            			CrossoverOperations.addClassToOffspring(classComp, newComp, offspring);
//	        		}
//	        		else {	
//	        			if (this.isHierarchyInASameComponent(classComp,parent))
//	        				CrossoverOperations.moveHierarchyToSameComponent(classComp, newComp, comp, offspring, parent, feature);
//	        			//else moveHierarchyToComponent(classComp, newComp, comp, offspring, parent, feature);
//	        			//TODO else qdo a hierarquia inclui diferentes componentes
//	        		}
//            	}	            	        	
//            }
//		} 
//    }
//
//    private void addInterfacesRealizingFeatureToOffspring (Concern feature, Package comp, Architecture offspring, Architecture parent) throws PackageNotFound, NotFoundException, InterfaceNotFound {
//    	Package newComp;
//    	List<Interface> allInterfaces = new ArrayList<Interface> (comp.getImplementedInterfaces());
//		if (!allInterfaces.isEmpty()) {
//			Iterator<Interface> iteratorInterfaces = allInterfaces.iterator();
//			while (iteratorInterfaces.hasNext()){
//				Interface interfaceComp = iteratorInterfaces.next();
//            	if (interfaceComp.containsConcern(feature) && interfaceComp.getOwnConcerns().size() == 1){
//        			newComp = offspring.findPackageByName(comp.getName());
//        			if (newComp == null)
//						try {
//							newComp = offspring.createPackage(comp.getName());
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//        			updateInterfaceDependencies(interfaceComp, offspring, parent);
//        			offspring.addExternalInterface(interfaceComp);
//					offspring.addImplementedInterface(interfaceComp, newComp);
//        			
//            	}else{
//            		addOperationsRealizingFeatureToOffspring(feature, interfaceComp, comp, offspring, parent);
//            	}
//			}
//		}
//    }
//	 
//    private void addParentArchitecturalElementsRealizingFeature(Concern feature, Architecture offspring, Architecture parent, String scope) throws ClassNotFound, CloneNotSupportedException, PackageNotFound, NotFoundException, ConcernNotFoundException{
//    	Package newComp;
//    	List<Package> allParentComponents = new ArrayList<Package> (parent.getAllPackages());
//    	
//    	if(!allParentComponents.isEmpty()){
//			for (Package comp : allParentComponents){
//				newComp = null;
//            	if (comp.containsConcern(feature) && comp.getOwnConcerns().size() == 1){
//            		newComp = offspring.findPackageByName(comp.getName());
//            		if (newComp == null){
//            			try {		            			
//							newComp = offspring.createPackage(comp.getName());
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//            		}	            		
//            	//	CrossoverOperations.addInterfacesToOffspring(feature, comp, newComp, offspring);
//            		addClassesToOffspring (feature, comp, newComp, offspring, parent);
//            	}
//            	else{
//            		try {
//						addInterfacesRealizingFeatureToOffspring (feature, comp, offspring, parent);
//					} catch (InterfaceNotFound e) {
//						e.printStackTrace();
//					}
//            		addClassesRealizingFeatureToOffspring(feature, comp, offspring, parent, scope);
//            	}	            	
//            }
//		}
//    }	       
//	    
//	    
//    private void addOperationsRealizingFeatureToOffspring(Concern feature, Interface interfaceComp, Package comp, Architecture offspring, Architecture parent) throws PackageNotFound, InterfaceNotFound{
//    	
//    	Interface targetInterface =  offspring.findInterfaceByName(interfaceComp.getName());
//    	List<Method> allOperations = new ArrayList<Method> (interfaceComp.getOperations());
//		if (!allOperations.isEmpty()) {
//			Iterator<Method> iteratorOperations = allOperations.iterator();
//			while (iteratorOperations.hasNext()){
//				Method operation= iteratorOperations.next();
//            	if (operation.containsConcern(feature) && operation.getOwnConcerns().size()==1){
//            		if (targetInterface == null){
//            			Package newComp = offspring.findPackageByName(comp.getName());
//            			if (newComp == null) {
//            				try {
//								newComp=offspring.createPackage(comp.getName());
//								newComp.addConcern(feature.getName());
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//            			}
//            			try {
//							targetInterface=offspring.createInterface(interfaceComp.getName());
//							targetInterface.addConcern(feature.getName());
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//            		}
//            		interfaceComp.moveOperationToInterface(operation, targetInterface);
//            	}
//			}
//		}
//    }     
//	    
//    private void addMethodsRealizingFeatureToOffspring(Concern feature, Class classComp, Package comp, Architecture offspring, Architecture parent) throws PackageNotFound, ClassNotFound{
//    	
//    	Class targetClass =  offspring.findClassByName(classComp.getName()).get(0);
//    	List<Method> allMethods = new ArrayList<Method> (classComp.getAllMethods());
//		if (!allMethods.isEmpty()) {
//			Iterator<Method> iteratorMethods = allMethods.iterator();
//			while (iteratorMethods.hasNext()){
//				Method method= iteratorMethods.next();
//            	if (method.containsConcern(feature) && method.getOwnConcerns().size()==1){
//            			if (targetClass==null){
//            				Package newComp = offspring.findPackageByName(comp.getName());
//	            			if (newComp==null) {
//	            				try {
//									newComp=offspring.createPackage(comp.getName());
//									newComp.addConcern(feature.getName());
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//	            			}
//	            			try {
//								targetClass=newComp.createClass(classComp.getName(), false);
//								targetClass.addConcern(feature.getName());
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//	            		}
//	            		classComp.moveMethodToClass(method, targetClass);
//            	}
//			}
//		}
//    }
//	   
//    private Package getClassComponent(Class class_, Architecture architecture){
//    	Package sourceComp=null;
//    	Collection<Package> allComponents =  architecture.getAllPackages();
//    	for (Package comp: allComponents){
//    		if (comp.getAllClasses().contains(class_)){
//    			return comp;
//    		}
//    	}
//    	return sourceComp;
//    }
//	    
//	/*
//	 * método para identificar as subclasses da classe pai na hierarquia de herança
//	 * 
//	 */
//    private Set<Element> getChildren(Element cls){
//  	  GeneralizationRelationship g = getGeneralizationForClass(cls);
//  	  return g.getAllChildrenForGeneralClass();
//    }
//	
//	private GeneralizationRelationship getGeneralizationForClass(Element cls) {
//  	  for (Relationship relationship: cls.getRelationships()){
//    	if (relationship instanceof GeneralizationRelationship){
//    		GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
//    		if (generalization.getParent().equals(cls))
//    			return (GeneralizationRelationship) relationship;
//    	}
//  	  }
//  	  return null;
//	}
//
//	private void moveChildrenAndRelationshipsToComponent(Element parent, Package sourceComp, Package targetComp, Architecture offspring, Architecture parentArch) throws PackageNotFound, ClassNotFound, CloneNotSupportedException{
//	  	
//		Collection<Element> children = getChildren(parent);
//		for (Element child: children){
//			moveChildrenAndRelationshipsToComponent(child, sourceComp, targetComp, offspring, parentArch);		
//		}
//		if (sourceComp.getAllClasses().contains(parent)){
//			//sourceComp.moveClassToComponent(parent, targetComp);
//			if (sourceComp.getName()!=targetComp.getName()){
//				targetComp = offspring.findPackageByName(sourceComp.getName());
//				if (targetComp==null){
//					try {
//						targetComp=offspring.createPackage(sourceComp.getName());
//						for (Concern feature:sourceComp.getOwnConcerns())
//							targetComp.addConcern(feature.getName());
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//			CrossoverOperations.addClassToOffspring(parent,targetComp, offspring);
//			//sourceComp.copyClassToComponent(parent, targetComp);
//			//this.updateClassRelationships(parent, targetComp, sourceComp, offspring, parentArch);
//		} else{
//			for (Package auxComp: parentArch.getAllPackages()){
//				if (auxComp.getAllClasses().contains(parent)){
//					sourceComp = auxComp;
//					if (sourceComp.getName()!=targetComp.getName()){
//						targetComp = offspring.findPackageByName(sourceComp.getName());
//						if (targetComp==null){
//							try {
//								targetComp=offspring.createPackage(sourceComp.getName());
//								for (Concern feature:sourceComp.getOwnConcerns())
//									targetComp.addConcern(feature.getName());
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}
//					}
//					
//					CrossoverOperations.addClassToOffspring(parent, targetComp, offspring);
//					//sourceComp.moveClassToComponent(parent, targetComp);
//					//this.updateClassRelationships(parent, targetComp, sourceComp, offspring, parentArch);	
//					break;
//				}
//			}
//		}
//	}
//					
//	public <T> T randomObject(List<T> allObjects)  throws JMException   {
//	    int numObjects= allObjects.size(); 
//	    int key;
//	    T object;
//	    if (numObjects == 0) {
//	    	object = null;
//	    } else{
//	    	key = PseudoRandom.randInt(0, numObjects-1); 
//	    	object = allObjects.get(key);
//	    }
//	    return object;      	    	
//	}
//	
//  private void removeAttributesOfClassRealizingFeature(Class cls, Concern feature){
//    	List<Attribute> attributesClassComp = new ArrayList<Attribute>(cls.getAllAttributes());
//		if (!attributesClassComp.isEmpty()){
//			Iterator<Attribute> iteratorAttributes = attributesClassComp.iterator();
//			while (iteratorAttributes.hasNext()){
//            	Attribute attribute = iteratorAttributes.next();
//				if (attribute.containsConcern(feature) && attribute.getOwnConcerns().size()==1){
//					//só elimina se a classe não fizer parte de uma hierarquia
//					if (!searchForGeneralizations(cls))
//						cls.removeAttribute(attribute);
//				}
//			}
//		}
//    }
//				    	   
//				    				    
//    //metodo para remover os filhos de uma classe
//	private void removeChildrenOfComponent(Element parent, Package comp, Architecture architecture){
//		
//		Collection<Element> children = getChildren(parent);
//		for (Element child: children){
//			removeChildrenOfComponent(child, comp, architecture);
//		}
//		if (comp.getAllClasses().contains(parent)){
//			RelationshipHolder.removeRelatedRelationships(parent);
//			//removeClassRelationships(parent, architecture);
//			comp.removeClass(parent);							
//		} else{				
//			for (Package auxComp: architecture.getAllPackages()){
//				if (auxComp.getAllClasses().contains(parent)){
//					RelationshipHolder.removeRelatedRelationships(parent);
//					//removeClassRelationships(parent,architecture);
//					auxComp.removeClass(parent);
//					break;
//				}
//			}
//		}
//	}
//				    
//	private void removeClassesComponent(Package comp, Architecture offspring, String scope){
//    	List<Class> allClasses = new ArrayList<Class> (comp.getAllClasses());
//		if (!allClasses.isEmpty()) {
//			Iterator<Class> iteratorClasses = allClasses.iterator();
//			while (iteratorClasses.hasNext()){
//            	Class classComp= iteratorClasses.next();
//            	if (comp.getAllClasses().contains(classComp)){
//            		//se n�o estiver numa hierarquia elimina os relacionamentos e a classe
//            		if (!searchForGeneralizations(classComp)){
//            			//this.removeClassRelationships(classComp,offspring);
//            			comp.removeClass(classComp);		    	            			
//            		} else{ // tem que eliminar a hierarquia toda
//            			removeHierarchyOfComponent(classComp,comp,offspring);
//            		}		
//    			}
//            }
//		}
//    }
//
//    private void removeClassesComponentRealizingFeature(Package comp, Concern feature, Architecture offspring, String scope){
//    	List<Class> allClasses = new ArrayList<Class> (comp.getAllClasses());
//    	
//		if (!allClasses.isEmpty()) {
//			Iterator<Class> iteratorClasses = allClasses.iterator();
//			while (iteratorClasses.hasNext()){
//            	Class classComp= iteratorClasses.next();
//            	if (comp.getAllClasses().contains(classComp)){
//            		if ((classComp.containsConcern(feature)) && (classComp.getOwnConcerns().size() == 1)){
//    					//se não estiver numa hierarquia elimina os relacionamentos e a classe
//    	            		if (!searchForGeneralizations(classComp)){
//    	            		//	this.removeClassRelationships(classComp,offspring);
//    	            			comp.removeClass(classComp);				    	            			
//    	            		} else{ // tem que eliminar a hierarquia toda
//    	            			removeHierarchyOfComponent(classComp,comp,offspring);
//    	            		}       		
//    					}else{
//    					if (scope=="allLevels"){
//    						removeAttributesOfClassRealizingFeature(classComp, feature);
//    						removeMethodsOfClassRealizingFeature(classComp, feature);
//						}
//    				}
//            	}	            						
//			}
//		}
//    }
//				    
//				    
//    private void removeHierarchyOfComponent(Class cls, Package comp, Architecture architecture){
//		Class parent = cls;
//		
//		while (CrossoverOperations.isChild(parent)){
//			parent = CrossoverOperations.getParent(parent);				
//		}
//		removeChildrenOfComponent(parent, comp, architecture);			
//	}
//				    
//	private void removeInterfacesComponentRealizingFeature(Package comp, Concern feature, Architecture offspring) throws NotFoundException{
//		List<Interface> allInterfaces = new ArrayList<Interface> (comp.getImplementedInterfaces());
//		
//		if (!allInterfaces.isEmpty()) {
//			Iterator<Interface> iteratorInterfaces = allInterfaces.iterator();
//	        while (iteratorInterfaces.hasNext()){
//	        	Interface interfaceComp = iteratorInterfaces.next();
//				if (interfaceComp.containsConcern(feature) && interfaceComp.getOwnConcerns().size() == 1)
//					offspring.removeInterface(interfaceComp);
//				else
//					removeOperationsOfInterfaceRealizingFeature(interfaceComp, feature);						
//			}
//		}
//	}
//	    
//    private void removeMethodsOfClassRealizingFeature(Class cls, Concern feature){
//    	List<Method> methodsClassComp = new ArrayList<Method>(cls.getAllMethods());
//		if (!methodsClassComp.isEmpty()){
//			Iterator<Method> iteratorMethods = methodsClassComp.iterator();
//			while (iteratorMethods.hasNext()){
//            	Method method = iteratorMethods.next();
//				if (method.containsConcern(feature) && method.getOwnConcerns().size()==1){
//					//só elimina se a classe não fizer parte de uma hierarquia
//					if (!searchForGeneralizations(cls))
//						cls.removeMethod(method);
//				}
//			}	
//		}
//    }
//	
//	private boolean removeOffspringArchitecturalElementsRealizingFeature(Concern feature, Architecture offspring, String scope) throws PackageNotFound, NotFoundException{
//		boolean ok = true;
//		List<Package> allComponents = new ArrayList<Package> (offspring.getAllPackages());
//		if(!allComponents.isEmpty()){
//			
//			for (Package comp : offspring.getAllPackages() ){
//				if ((comp.containsConcern(feature)) && (comp.getOwnConcerns().size() == 1)  && (this.thereIsHierarchyInDifferentComponents(comp, offspring)))
//					return false;
//				else{
//					List<Class> allClasses = new ArrayList<Class> (comp.getAllClasses());
//					for (Class cls: allClasses){
//						if ((cls.containsConcern(feature)) && (cls.getOwnConcerns().size() == 1) && (this.isHierarchyInASameComponent(cls, offspring)))
//							return false;
//					}
//				}
//			}
//			
//			Iterator<Package> iteratorComponents = allComponents.iterator();
//	        while (iteratorComponents.hasNext()){
//	        	Package comp = iteratorComponents.next();
//	        	if (comp.containsConcern(feature) && comp.getOwnConcerns().size() == 1){
//	        		List<Interface> allInterfacesComp = new ArrayList<Interface> (comp.getImplementedInterfaces());
//	        		if (!allInterfacesComp.isEmpty()) {
//						Iterator<Interface> iteratorInterfaces = allInterfacesComp.iterator();
//			            while (iteratorInterfaces.hasNext()){
//			            	Interface interfaceComp = iteratorInterfaces.next();
//			            	offspring.removeInterface(interfaceComp);	
//						}
//	        		}
//	        		this.removeClassesComponent(comp, offspring, scope);
//	        		//TODO não deveria remover o componente se ele tem classes em hierarquia... ver como resolver esta quest�o
//	        		// removeComponentRelationships(comp, offspring);
//	        		offspring.removePackage(comp);
//	        		
//				}
//				else{					
//					this.removeInterfacesComponentRealizingFeature(comp, feature, offspring);
//					this.removeClassesComponentRealizingFeature(comp, feature, offspring, scope);		
//				}
//	        }	
//		}
//		return ok;
//	}
//				       
//   private void removeOperationsOfInterfaceRealizingFeature(Interface interfaceComp, Concern feature){
//    	List<Method> operationsInterfaceComp = new ArrayList<Method> (interfaceComp.getOperations());
//		if (!operationsInterfaceComp.isEmpty()){
//			Iterator<Method> iteratorOperations = operationsInterfaceComp.iterator();
//			while (iteratorOperations.hasNext()){
//				Method operation = iteratorOperations.next();
//            	if (operation.containsConcern(feature) && operation.getOwnConcerns().size() == 1)
//					interfaceComp.removeOperation(operation);
//			}
//		}
//    }
//			   
//    //m�todo para verificar se algum dos relacionamentos recebidos � generaliza��o
//    private boolean searchForGeneralizations(Class cls){
//    	
//    	Collection<Relationship> relationships = cls.getRelationships();
//    	for (Relationship relationship: relationships){
//	    	if (relationship instanceof GeneralizationRelationship){
//	    		GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
//	    		if (generalization.getChild().equals(cls) || generalization.getParent().equals(cls))
//	    		return true;
//	    	}
//	    }
//    	return false;
//    }
//    
//	private void updateInterfaceDependencies(Interface interface_, Architecture offspring, Architecture parent) throws NotFoundException, PackageNotFound{
//		
//		Collection<Relationship> relationships = parent.getAllRelationships();
//		for (Relationship relationship:relationships){
//			if (relationship instanceof DependencyRelationship){
//				DependencyRelationship dependency = (DependencyRelationship) relationship;
//				Interface itf = dependency.getInterfaceOfDependency();
//				if (itf.equals(interface_)) {
//					Package targetComp = offspring.findPackageByName(dependency.getPackageOfDependency().getName());
//					if (targetComp!=null){
//						offspring.addExternalInterface(interface_);
//						offspring.addRequiredInterface(interface_, targetComp);
//					}
//				}
//			} 
//		}
//	}
//				
//	private void updateVariabilitiesOffspring(Architecture offspring){ 
//		for (Variability variability: offspring.getAllVariabilities()){	    		
//			VariationPoint variationPoint = variability.getVariationPoint();
//			Element elementVP = variationPoint.getVariationPointElement();
//			if (!(offspring.findElementByName(elementVP.getName(), "class").equals(elementVP))){
//				variationPoint.replaceVariationPointElement(offspring.findElementByName(elementVP.getName(), "class"));
//			}
//		}
//	}
//	
//	private boolean isHierarchyInASameComponent (Class class_, Architecture architecture) throws PackageNotFound{
//		boolean sameComponent = true;
//		Class parent = class_;
//		Package componentOfClass = architecture.findPackageOfClass(class_);
//		Package componentOfParent = componentOfClass;
//		while (CrossoverOperations.isChild(parent)){
//			parent = CrossoverOperations.getParent(parent);	
//			componentOfParent = architecture.findPackageOfClass(parent);
//			if (!(componentOfClass.equals(componentOfParent))){
//				sameComponent = false;
//				return false;
//			}
//		}
//		return sameComponent;
//	}
//	
//	private boolean thereIsHierarchyInDifferentComponents (Package comp, Architecture architecture) throws PackageNotFound{
//		boolean sameComponent = false;
//		for (Class class_: comp.getAllClasses()){
//			if (this.searchForGeneralizations(class_))
//				if (!(this.isHierarchyInASameComponent(class_,architecture))){
//					sameComponent = true;
//					return true;
//				}
//		}			
//		return sameComponent;
//	}
//	
//	
//    private Concern getConcernTesteApenas(List<Concern> concernsArchitecture) {
//		for(Concern c : concernsArchitecture)
//			if (c.getName().equals("bowling"))
//				return c;
//		return null;
//	}
//}
