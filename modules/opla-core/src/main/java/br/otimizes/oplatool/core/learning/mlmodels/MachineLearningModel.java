package br.otimizes.oplatool.core.learning.mlmodels;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.annotations.Type;
import weka.classifiers.AbstractClassifier;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type") @JsonSubTypes({
        @JsonSubTypes.Type(value = MLPLearningModel.class, name = "MLPLearningModel"),
        @JsonSubTypes.Type(value = KStarLearningModel.class, name = "KStarLearningModel"),
        @JsonSubTypes.Type(value = RandomForestLearningModel.class, name = "RandomForestLearningModel"),
        @JsonSubTypes.Type(value = RandomTreeLearningModel.class, name = "RandomTreeLearningModel"),
        @JsonSubTypes.Type(value = SVMLearningModel.class, name = "SVMLearningModel"),
        @JsonSubTypes.Type(value = VoteLearningModel.class, name = "VoteLearningModel"),
})
public class MachineLearningModel{

    public MachineLearningAlgorithms algorithm;

    @Override
    public String toString() {
        return "MachineLearningModel{" +
                "algorithm=" + algorithm +
                '}';
    }

    public MachineLearningModel() {

    }

    @JsonGetter
    public String getType() {
        return this.getClass().getName();
    }
}

