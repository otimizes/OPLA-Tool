package br.otimizes.oplatool.core.jmetal4.learning;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Categories.class)
@Categories.IncludeCategory({MachineLearningTests.class})
@Suite.SuiteClasses({SubjectiveAnalyzeAlgorithmTest.class, SubjectiveAnalyzeAlgorithmTestIII.class})
public class MachineLearningTestSuite {
}