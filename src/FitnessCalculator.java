public class FitnessCalculator {

    public float FitnessRaw;
    public float FitnessSmoothed;
    public float Rank;

    public FitnessCalculator() {
        ResetEvaluation();
    }

    public void ComputeFitnessRaw() {

    }

    public void ResetEvaluation() {
        FitnessRaw = 0;
        FitnessSmoothed = 0;
    }
}
