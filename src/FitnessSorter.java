import java.util.Comparator;

public class FitnessSorter implements Comparator<INucleus> {
    @Override
    public int compare(INucleus o1, INucleus o2) {
        return Float.compare(o2.getFitnessCalculator().FitnessRaw, o1.getFitnessCalculator().FitnessRaw);
    }
}
