import java.util.ArrayList;

public interface IGeneration {
    ArrayList<INucleus> getIIndividuals();

    int getGenerationId();
    float getDebug1();


    IGeneration CreateDescendants();

    void ResetEvaluation();

    IGeneration Clone();

    boolean IsEvaluated();
}
