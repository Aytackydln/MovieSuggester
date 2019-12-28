package tr.edu.eskisehir.camishani.dataacquisition.cfiltering;

public interface SimilarityMeasurable {
    Iterable<?> getSimilarityFactors();

    int getMaxFactorId();
}
