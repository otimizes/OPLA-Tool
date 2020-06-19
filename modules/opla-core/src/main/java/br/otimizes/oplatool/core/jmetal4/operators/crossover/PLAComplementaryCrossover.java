package br.otimizes.oplatool.core.jmetal4.operators.crossover;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.relationship.AssociationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.operators.IOperator;
import br.otimizes.oplatool.core.jmetal4.util.PseudoRandom;
import org.apache.log4j.Logger;

import java.util.*;

//import org.mockito.cglib.beans.BeanCopier.Generator;


/**
 * @author Diego Fernandes da Silva
 * Proposta de operador PLAComplementaryCrossover
 */

public class PLAComplementaryCrossover implements IOperator<Solution[]>{

	private static final Logger LOG = Logger.getLogger(PLAComplementaryCrossover.class);

	private static final long serialVersionUID = 1L;

	public Double crossoverProbability_ = null;

	public Solution[] execute(Map<String, Object> parameters, Solution[] parents, String scope) {
		if (parameters.get("probability") != null)
			crossoverProbability_ = (Double) parameters.get("probability");
		Solution father = parents[0];
		Solution mother = parents[1];

		if (PseudoRandom.randDouble() < crossoverProbability_) {
			return doCrossover(father, mother);
		}
		Solution[] offspring = new Solution[1];
		offspring[0] = new Solution(father);

		return offspring;
	}

	public Solution[] doCrossover(Solution parent1, Solution parent2) {

		Solution offspring = new Solution(parent1);
		Solution sol = null;

		try {

			Architecture father;
			Architecture mother;
			Random random = new Random();

			if (random.nextInt(2) == 0){
				father = (Architecture) parent1.getDecisionVariables()[0].deepCopy();
				mother = (Architecture) parent2.getDecisionVariables()[0].deepCopy();
			}else{
				father = (Architecture) parent2.getDecisionVariables()[0].deepCopy();
				mother = (Architecture) parent1.getDecisionVariables()[0].deepCopy();
			}

			Architecture child = (Architecture) offspring.getDecisionVariables()[0];
			for(Class c1 : child.getAllClasses()){
				child.removeClassByID(c1.getId());
			}
			for(Interface i1 : child.getAllInterfaces()){
				child.removeInterfaceByID(i1.getId());
			}
			child.removeAllPackages();
			child.getRelationshipHolder().clearLists();
			Architecture copyFather = father.deepClone();
			Architecture copyMother = mother.deepClone();
			int pos = 0;

			if(father.getClasses().size() > 0){
				int class_crossover_point = random.nextInt(father.getClasses().size());
				pos = 0;
				for(Class class_ : father.getClasses()){
					if(pos == class_crossover_point){
						break;
					}
					child.addExternalClass(class_);
					pos++;
				}
			}

			if(father.getInterfaces().size() > 0){
				int interface_crossover_point = random.nextInt(father.getInterfaces().size());
				pos = 0;
				for(Interface interface_ : father.getInterfaces()){
					if(pos == interface_crossover_point){
						break;
					}
					child.addExternalInterface(interface_);
					pos++;
				}
			}
			if(father.getAllPackages().size() > 0){
				int package_crossover_point = random.nextInt(father.getAllPackages().size());
				pos = 0;
				for(Package pkg : father.getAllPackages()){
					if(pos == package_crossover_point){
						break;
					}
					child.addPackage(pkg);
					pos++;
				}
			}

			if(mother.getClasses().size() > 0){
				for(Class class_ : mother.getClasses()){
					if(child.findClassById(class_.getId())==null){
						child.addExternalClass(class_);
					}
				}
			}
			if(mother.getInterfaces().size() > 0){
				for(Interface interface_ : mother.getInterfaces()){
					if(child.findInterfaceById(interface_.getId())==null){
						child.addExternalInterface(interface_);
					}
				}
			}
			if(mother.getAllPackages().size() > 0){
				for(Package pkg : mother.getAllPackages()){
					Package pkg_child = child.findPackageByID(pkg.getId());
					if(pkg_child==null){
						ArrayList<String> rem_i = new ArrayList<>();
						for(Interface int_ : pkg.getAllInterfaces()){
							if(child.findInterfaceById(int_.getId())!=null){
								rem_i.add(int_.getId());
							}
						}
						for(String s : rem_i){
							pkg.removeInterfaceByID(s);
						}
						rem_i = new ArrayList<>();
						for(Class int_ : pkg.getAllClasses()){
							if(child.findClassById(int_.getId())!=null){
								rem_i.add(int_.getId());
							}
						}
						for(String s : rem_i){
							pkg.removeClassByID(s);
						}
						if(pkg.getAllClasses().size()+pkg.getAllInterfaces().size() > 0) {
							child.addPackage(pkg);
						}
					}else{
						for(Interface int_ : pkg.getAllInterfaces()){
							if(child.findInterfaceById(int_.getId())==null){
								pkg_child.addExternalInterface(int_);
							}
						}
						for(Class int_ : pkg.getAllClasses()){
							if(child.findClassById(int_.getId())==null){
								pkg_child.addExternalClass(int_);
							}
						}
					}
				}
			}

			CrossoverUtils.getInstance().restoreMissingElements2(copyMother, offspring);
			CrossoverUtils.getInstance().restoreMissingElements2(copyFather, offspring);
			Architecture archP1 = ((Architecture)parent1.getDecisionVariables()[0]).deepClone();
			Architecture archP2 = ((Architecture)parent2.getDecisionVariables()[0]).deepClone();

			for(Relationship r : archP1.getRelationshipHolder().getAllRelationships()){
				child.getRelationshipHolder().verifyAndAddRelationshipsChild2(r,child);
			}
			for(Relationship r : archP2.getRelationshipHolder().getAllRelationships()){
				child.getRelationshipHolder().verifyAndAddRelationshipsChild2(r,child);
			}
			for(Class c : child.getClasses()){
				c.setRelationshipHolder(child.getRelationshipHolder());
			}
			for(Interface c : child.getInterfaces()){
				c.setRelationshipHolder(child.getRelationshipHolder());
			}
			for(Package pkg : child.getAllPackages()){
				for(Class c : pkg.getAllClasses()){
					c.setRelationshipHolder(child.getRelationshipHolder());
				}
				for(Interface c : pkg.getAllInterfaces()){
					c.setRelationshipHolder(child.getRelationshipHolder());
				}
			}

			archP1.clearArchitecture();
			archP2.clearArchitecture();
			archP1 = null;
			archP2 = null;
			((Architecture) offspring.getDecisionVariables()[0]).matchRequiredAndImplementedInterface();
			father.clearArchitecture();
			mother.clearArchitecture();
			copyFather.clearArchitecture();
			copyMother.clearArchitecture();
			father = null;
			mother = null;
			CrossoverUtils.getInstance().removeDuplicateElements((Architecture) offspring.getDecisionVariables()[0]);
			((Architecture) parent1.getDecisionVariables()[0]).verifyClassWithoutRelationship();
			((Architecture) parent2.getDecisionVariables()[0]).verifyClassWithoutRelationship();
			ArrayList<String> semLig = child.verifyClassWithoutRelationship();

			if(semLig.size() > 0){
				for(String id : semLig) {

					Class c1 = ((Architecture) parent1.getDecisionVariables()[0]).findClassById(id);
					if(c1 != null){

						for(Relationship r : c1.getRelationships()){

							child.getRelationshipHolder().forceAddAssociationRelationshipsChild(r, child);
							if(r instanceof AssociationRelationship){
								AssociationRelationship re1 = (AssociationRelationship)r;

							}
						}
					}else{
						c1 = ((Architecture) parent2.getDecisionVariables()[0]).findClassById(id);
						if(c1 != null){

							for(Relationship r : c1.getRelationships()){
								child.getRelationshipHolder().forceAddAssociationRelationshipsChild(r, child);

								if(r instanceof AssociationRelationship){

									AssociationRelationship re1 = (AssociationRelationship)r;
								}
							}
						}
					}
				}
			}

			semLig = child.verifyClassWithoutRelationship();
			((Architecture) parent1.getDecisionVariables()[0]).verifyInterfaceWithoutRelationship();
			((Architecture) parent2.getDecisionVariables()[0]).verifyInterfaceWithoutRelationship();
			semLig = child.verifyInterfaceWithoutRelationship();

			if(semLig.size() > 0){
				for(String id : semLig) {

					Interface c1 = ((Architecture) parent1.getDecisionVariables()[0]).findInterfaceById(id);
					if(c1 != null){
						for(Relationship r : c1.getRelationships()){

							child.getRelationshipHolder().forceAddRealization(r,child);
						}
					}else{
						c1 = ((Architecture) parent2.getDecisionVariables()[0]).findInterfaceById(id);
						if(c1 != null){

							for(Relationship r : c1.getRelationships()){

								child.getRelationshipHolder().forceAddRealization(r,child);
							}
						}
					}
				}
			}
			semLig = child.verifyInterfaceWithoutRelationship();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Solution[] offspringReturn = new Solution[1];
		offspringReturn[0] = new Solution(offspring);
		return offspringReturn;
	}

}