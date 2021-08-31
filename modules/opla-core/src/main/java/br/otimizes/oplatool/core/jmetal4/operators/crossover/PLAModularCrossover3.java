package br.otimizes.oplatool.core.jmetal4.operators.crossover;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.relationship.AssociationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.RealizationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.operators.IOperator;
import br.otimizes.oplatool.core.jmetal4.util.PseudoRandom;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;


/**
 * @author Diego Fernandes da Silva
 * PLAModularCrossover third operator
 */

public class PLAModularCrossover3 extends PLAModularCrossover implements IOperator<Solution[]> {

	private static final Logger LOG = Logger.getLogger(PLAModularCrossover3.class);

	private static final long serialVersionUID = 1L;

	private int numberOfObjetive;
	private Double crossoverProbability_ = null;

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
			Architecture copyChosenP1;
			Architecture copyOtherP2;

			Random generator = new Random();
			if (generator.nextInt(2) == 0){
				copyChosenP1 = (Architecture) parent1.getDecisionVariables()[0].deepCopy();
				copyOtherP2 = (Architecture) parent2.getDecisionVariables()[0].deepCopy();
			}else{
				copyChosenP1 = (Architecture) parent2.getDecisionVariables()[0].deepCopy();
				copyOtherP2 = (Architecture) parent1.getDecisionVariables()[0].deepCopy();
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
			Architecture archP1 = copyChosenP1.deepClone();
			Architecture archP2 = copyOtherP2.deepClone();
			Architecture copyOriginalP1 = copyChosenP1.deepClone();
			Architecture copyOriginalP2 = copyOtherP2.deepClone();

			int oldcount = 0;
			int father;
			int posPackage;
			ArrayList<String> lstPack = new ArrayList<>();
			int nRelationship1;
			int nVariationPoint1;
			int nFeature1;
			int nRelationship2;
			int nVariationPoint2;
			int nFeature2;
			ArrayList<String> lstRelatedPackage = new ArrayList<>();
			ArrayList<String> lst2 = new ArrayList<>();
			ArrayList<String> lst3 = new ArrayList<>();


			while(true){
				if(copyChosenP1 == null && copyOtherP2 == null){
					break;
				}
				if(copyChosenP1 == null){
					if(copyOtherP2.getAllPackages().size() == 0){
						break;
					}else{
						oldcount = copyOtherP2.getAllPackages().size();
					}
				}
				if(copyOtherP2 == null){
					if(copyChosenP1.getAllPackages().size() == 0){
						break;
					}else{
						oldcount = copyChosenP1.getAllPackages().size();
					}
				}
				if(copyChosenP1.getAllPackages().size()+copyOtherP2.getAllPackages().size() == 0){
					break;
				}


				if(copyChosenP1 != null && copyOtherP2 != null) {
					if ((copyChosenP1.getAllPackages().size() + copyOtherP2.getAllPackages().size()) == oldcount) {

						break;
					} else {
						oldcount = (copyChosenP1.getAllPackages().size() + copyOtherP2.getAllPackages().size());
					}
				}

				father = generator.nextInt(2);

				if(copyChosenP1.getAllPackages().size()==0) {
					father = 1;
				}
				if(copyOtherP2.getAllPackages().size()==0) {
					father = 0;
				}

				Package selectedPackageParent1;
				Package selectedPackageParent2;
				if(father==0){
					ArrayList<Package> lstPackage = new ArrayList<>(copyChosenP1.getAllPackages());
					posPackage = generator.nextInt(lstPackage.size());
					selectedPackageParent1 = lstPackage.get(posPackage);
					selectedPackageParent2 = copyOtherP2.findPackageByName(selectedPackageParent1.getName());

				}
				else{
					ArrayList<Package> lstPackage = new ArrayList<>(copyOtherP2.getAllPackages());
					posPackage = generator.nextInt(lstPackage.size());
					selectedPackageParent2 = lstPackage.get(posPackage);
					selectedPackageParent1 = copyChosenP1.findPackageByName(selectedPackageParent2.getName());

				}

				if(selectedPackageParent1 == null || selectedPackageParent2 ==null){

					if(selectedPackageParent1 == null){

						lstPack.clear();
						for(Package pk2 : copyOtherP2.getAllPackages())
							lstPack.add(pk2.getId());
						addElements(child,lstPack,archP2);
						break;

					}else{
						lstPack.clear();
						for(Package pk2 : copyChosenP1.getAllPackages())
							lstPack.add(pk2.getId());
						addElements(child,lstPack,archP1);
						break;
					}
				}
				else {
					nRelationship1 = 0;
					nVariationPoint1 = 0;
					nFeature1 = 0;

					for (Class clazz : selectedPackageParent1.getAllClasses()) {
						nRelationship1 += clazz.getRelationships().size();
						if(clazz.isVariationPoint())
							nVariationPoint1 += 1;
						nFeature1 += clazz.getAllConcerns().size();
					}

					for (Interface ai : selectedPackageParent1.getAllInterfaces()) {
						nRelationship1 += ai.getRelationships().size();
						if(ai.isVariationPoint())
							nVariationPoint1 += 1;
						nFeature1 += ai.getAllConcerns().size();
					}


					nRelationship2 = 0;
					nVariationPoint2 = 0;
					nFeature2 = 0;


					for (Class clazz : selectedPackageParent2.getAllClasses()) {
						nRelationship2 += clazz.getRelationships().size();



						if(clazz.isVariationPoint())
							nVariationPoint2 += 1;
						nFeature2 += clazz.getAllConcerns().size();
					}

					for (Interface ai : selectedPackageParent2.getAllInterfaces()) {
						nRelationship2 += ai.getRelationships().size();
						if(ai.isVariationPoint())
							nVariationPoint2 += 1;
						nFeature2 += ai.getAllConcerns().size();
					}

					lstRelatedPackage .clear();
					lst2.clear();


					if(nVariationPoint1 == nVariationPoint2){
						if(nRelationship1 == nRelationship2){
							if(nFeature1 <= nFeature2){
								lstRelatedPackage = getRelatedPackages(copyChosenP1,selectedPackageParent1, lstRelatedPackage);
								lst2.addAll(lstRelatedPackage);
								addElements(child,lstRelatedPackage, archP1);
							}else{
								lstRelatedPackage = getRelatedPackages(copyOtherP2,selectedPackageParent2, lstRelatedPackage);
								lst2.addAll(lstRelatedPackage);
								addElements(child,lstRelatedPackage, archP2);
							}
						}else{
							if(nRelationship1 < nRelationship2){
								lstRelatedPackage = getRelatedPackages(copyOtherP2,selectedPackageParent2, lstRelatedPackage);
								lst2.addAll(lstRelatedPackage);
								addElements(child,lstRelatedPackage, archP2);
							}
							else{
								lstRelatedPackage = getRelatedPackages(copyChosenP1,selectedPackageParent1, lstRelatedPackage);
								lst2.addAll(lstRelatedPackage);
								addElements(child,lstRelatedPackage, archP1);
							}
						}
					}else{
						if(nVariationPoint1 < nVariationPoint2){
							lstRelatedPackage = getRelatedPackages(copyOtherP2,selectedPackageParent2, lstRelatedPackage);
							lst2.addAll(lstRelatedPackage);
							addElements(child,lstRelatedPackage, archP2);
						}
						else{
							lstRelatedPackage = getRelatedPackages(copyChosenP1,selectedPackageParent1, lstRelatedPackage);
							lst2.addAll(lstRelatedPackage);
							addElements(child,lstRelatedPackage, archP1);
						}
					}

					lst3.clear();
					lst3.addAll(lst2);
					if(father == 0){
						removeElements(copyOtherP2,lst3,copyChosenP1);
						removePackages(copyChosenP1,lst2);

					}
					else{
						removeElements(copyChosenP1,lst2,copyOtherP2);
						removePackages(copyOtherP2,lst3);
					}
				}
			}

			addElementsNotInPackage((Architecture) offspring.getDecisionVariables()[0],copyOriginalP1);
			addElementsNotInPackage((Architecture) offspring.getDecisionVariables()[0],copyOriginalP2);
			addElementsInPackage(child,(Architecture)parent1.getDecisionVariables()[0]);
			addElementsInPackage(child,(Architecture)parent2.getDecisionVariables()[0]);
			copyChosenP1.clearArchitecture();
			copyOtherP2.clearArchitecture();
			archP1.clearArchitecture();
			archP2.clearArchitecture();

			copyChosenP1 = null;
			copyOtherP2 = null;
			archP1 = null;
			archP2 = null;
			lstPack = null;
			lstRelatedPackage = null;
			lst2 = null;
			lst3 = null;

			CrossoverUtils.getInstance().restoreMissingElements2((Architecture) parent1.getDecisionVariables()[0], offspring);
			CrossoverUtils.getInstance().restoreMissingElements2((Architecture) parent2.getDecisionVariables()[0], offspring);

			for(Relationship r : copyOriginalP1.getRelationshipHolder().getAllRelationships()){
				((Architecture) offspring.getDecisionVariables()[0]).getRelationshipHolder().verifyAndAddRelationshipsChild2(r,((Architecture) offspring.getDecisionVariables()[0]));
			}
			for(Relationship r : copyOriginalP2.getRelationshipHolder().getAllRelationships()){
				((Architecture) offspring.getDecisionVariables()[0]).getRelationshipHolder().verifyAndAddRelationshipsChild2(r,((Architecture) offspring.getDecisionVariables()[0]));
			}

			for(Package pkg : ((Architecture) offspring.getDecisionVariables()[0]).getAllPackages()){
				pkg.setRelationshipHolder(((Architecture) offspring.getDecisionVariables()[0]).getRelationshipHolder());
			}
			for(Class c : ((Architecture) offspring.getDecisionVariables()[0]).getAllClasses()){
				c.setRelationshipHolder(((Architecture) offspring.getDecisionVariables()[0]).getRelationshipHolder());
			}
			for(Interface c : ((Architecture) offspring.getDecisionVariables()[0]).getAllInterfaces()){
				c.setRelationshipHolder(((Architecture) offspring.getDecisionVariables()[0]).getRelationshipHolder());
			}
			((Architecture) offspring.getDecisionVariables()[0]).matchRequiredAndImplementedInterface();

			copyOriginalP1.clearArchitecture();
			copyOriginalP2.clearArchitecture();

			CrossoverUtils.getInstance().removeDuplicateElements((Architecture) offspring.getDecisionVariables()[0]);

			((Architecture) parent1.getDecisionVariables()[0]).getClassesWithoutRelationship();

			((Architecture) parent2.getDecisionVariables()[0]).getClassesWithoutRelationship();

			ArrayList<String> semLig = child.getClassesWithoutRelationship();

			if(semLig.size() > 0){
				for(String id : semLig) {
					Class c1 = ((Architecture) parent1.getDecisionVariables()[0]).findClassById(id);
					if(c1 != null){
						for(Relationship r : c1.getRelationships()){
							if(r instanceof AssociationRelationship){
								child.getRelationshipHolder().forceAddAssociationRelationshipsChild(r, child);
								AssociationRelationship re1 = (AssociationRelationship)r;
							}
						}
					}else{
						c1 = ((Architecture) parent2.getDecisionVariables()[0]).findClassById(id);
						if(c1 != null){
							for(Relationship r : c1.getRelationships()){
								if(r instanceof AssociationRelationship){

									child.getRelationshipHolder().forceAddAssociationRelationshipsChild(r, child);
									AssociationRelationship re1 = (AssociationRelationship)r;
								}
							}
						}
					}
				}
			}

			semLig = child.getClassesWithoutRelationship();

			((Architecture) parent1.getDecisionVariables()[0]).getInterfacesWithoutRelationship();
			((Architecture) parent2.getDecisionVariables()[0]).getInterfacesWithoutRelationship();
			semLig = child.getInterfacesWithoutRelationship();

			if(semLig.size() > 0){
				for(String id : semLig) {
					Interface c1 = ((Architecture) parent1.getDecisionVariables()[0]).findInterfaceById(id);
					if(c1 != null){
						for(Relationship r : c1.getRelationships()){
							if(r instanceof RealizationRelationship)
								child.getRelationshipHolder().forceAddRealization(r,child);
						}
					}else{
						c1 = ((Architecture) parent2.getDecisionVariables()[0]).findInterfaceById(id);
						if(c1 != null){
							for(Relationship r : c1.getRelationships()){
								if(r instanceof RealizationRelationship)
									child.getRelationshipHolder().forceAddRealization(r,child);
							}
						}
					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Solution[] offspringReturn = new Solution[1];
		offspringReturn[0] = new Solution(offspring);
		return offspringReturn;

	}








}