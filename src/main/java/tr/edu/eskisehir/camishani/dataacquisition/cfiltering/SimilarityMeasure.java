package tr.edu.eskisehir.camishani.dataacquisition.cfiltering;

public interface SimilarityMeasure {
    double similarity(SimilarityFactorGetter<?> similarityFactorGetter, SimilarityMeasurable value1, SimilarityMeasurable values2);
}
