//package jmetal.operators.crossover;
//
//import java.util.HashMap;
//
//import main.GenerateArchitecture;
//import mestrado.arquitetura.helpers.test.TestHelper;
//
//import org.junit.Test;
//
//import arquitetura.builders.ArchitectureBuilder;
//import arquitetura.representation.Architecture;
//import arquitetura.representation.Concern;
//
//public class PLACrossover2Test extends TestHelper {
//	
//	@Test
//	public void test1() throws Exception {
//		Architecture parent = givenAArchitecture("parent");
//		Architecture offspring = givenAArchitecture("offspring");
//		GenerateArchitecture g = new GenerateArchitecture();
//		
//		HashMap<String, Object> parameters = new HashMap<String, Object>() ;
//        parameters.put("probability", new Double(1)) ;
//        
//		PLACrossover2 crossover = new PLACrossover2(parameters);
//		
//		Concern feature = parent.getConcernByName("brickles");
//		
//		crossover.addElementsToOffspring(feature, offspring, parent, "allLevels");
//		g.generate(offspring, "crossoverTest");
//	}
//	
//	
//	@Test
//	public void test2() throws Exception {
//		Architecture parent = givenAArchitecture("parent2");
//		Architecture offspring = givenAArchitecture("offspring2");
//		GenerateArchitecture g = new GenerateArchitecture();
//		
//		HashMap<String, Object> parameters = new HashMap<String, Object>() ;
//        parameters.put("probability", new Double(1)) ;
//        
//		PLACrossover2 crossover = new PLACrossover2(parameters);
//		
//		Concern feature = parent.getConcernByName("brickles");
//		
//		crossover.addElementsToOffspring(feature, offspring, parent, "allLevels");
//		g.generate(offspring, "crossoverTest");
//	}
//	
//	@Test
//	public void test3() throws Exception {
//		Architecture parent = givenAArchitecture("parent3");
//		Architecture offspring = givenAArchitecture("offspring3");
//		GenerateArchitecture g = new GenerateArchitecture();
//		
//		HashMap<String, Object> parameters = new HashMap<String, Object>() ;
//        parameters.put("probability", new Double(1)) ;
//        
//		PLACrossover2 crossover = new PLACrossover2(parameters);
//		
//		Concern feature = parent.getConcernByName("brickles");
//		
//		crossover.addElementsToOffspring(feature, offspring, parent, "allLevels");
//		g.generate(offspring, "crossoverTest");
//	}
//	
//	@Test
//	public void test4() throws Exception {
//		Architecture parent = givenAArchitecture("parent4");
//		Architecture offspring = givenAArchitecture("offspring4");
//		GenerateArchitecture g = new GenerateArchitecture();
//		
//		HashMap<String, Object> parameters = new HashMap<String, Object>() ;
//        parameters.put("probability", new Double(1)) ;
//        
//		PLACrossover2 crossover = new PLACrossover2(parameters);
//		
//		Concern feature = parent.getConcernByName("brickles");
//		
//		crossover.addElementsToOffspring(feature, offspring, parent, "allLevels");
//		g.generate(offspring, "crossoverTest");
//	}
//	
//	@Test
//	public void test5() throws Exception {
//		Architecture parent = new ArchitectureBuilder().create("/Users/elf/Documents/workspaceModeling/crossover3/agm_copy2/agm_copy2.uml");
//		Architecture offspring = new ArchitectureBuilder().create("/Users/elf/Documents/workspaceModeling/agm_copy/agm_copy.uml");
//		GenerateArchitecture g = new GenerateArchitecture();
//		
//		HashMap<String, Object> parameters = new HashMap<String, Object>() ;
//        parameters.put("probability", new Double(1)) ;
//        
//		PLACrossover2 crossover = new PLACrossover2(parameters);
//		
//		Concern feature = parent.getConcernByName("ranking");
//		
//		crossover.obtainChild(feature, parent, offspring, "allLevels");
//		g.generate(offspring, "crossoverTest");
//	}
//
//}
