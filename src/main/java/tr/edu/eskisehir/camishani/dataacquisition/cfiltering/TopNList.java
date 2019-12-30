package tr.edu.eskisehir.camishani.dataacquisition.cfiltering;

import org.springframework.data.util.Pair;

import java.util.Comparator;
import java.util.TreeSet;

public class TopNList<K extends SimilarityMeasurable> {

    private final TreeSet<Pair<K, Double>> neighbors;
    private final K center;
    private final int capacity;
    private final SimilarityMeasure similarityMeasure;
    private final SimilarityFactorGetter<?> similarityFactorGetter;

    public TopNList(K center, int capacity, SimilarityMeasure similarityMeasure, SimilarityFactorGetter<?> similarityFactorGetter, Comparator<Pair<K, Double>> comparator) {
        this.center = center;
        this.capacity = capacity;
        this.similarityMeasure = similarityMeasure;
        this.similarityFactorGetter = similarityFactorGetter;
        neighbors = new TreeSet<>(comparator);
    }

    public void add(K element) {
        double distance = similarityMeasure.similarity(similarityFactorGetter, center, element);
        if (neighbors.size() < capacity) {
            neighbors.add(Pair.of(element, distance));
        } else {
            Pair<K, Double> lastNeighborPair = neighbors.last();
            if (distance < lastNeighborPair.getSecond()) {
                neighbors.remove(lastNeighborPair);
                neighbors.add(Pair.of(element, distance));
            }
        }
    }

    public Iterable<Pair<K, Double>> getPairs() {
        return neighbors;
    }
}
