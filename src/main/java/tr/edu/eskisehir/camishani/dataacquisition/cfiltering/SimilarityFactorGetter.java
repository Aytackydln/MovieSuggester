package tr.edu.eskisehir.camishani.dataacquisition.cfiltering;

public interface SimilarityFactorGetter<F> {
    int getMeasureId(F object);

    int getMeasureValue(F object);
}
