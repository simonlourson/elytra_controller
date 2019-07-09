import java.util.Random;

public class Parameters {

    public boolean GA_GAUSSIAN_MUTATION;
    public float GA_NEW_INDIVIDUAL;
    public float GA_RANDOM_BREEDERS;
    public float GA_MUTATION_RATE;
    public float GA_RANK_MIN;
    public float GA_RANK_MAX;
    public float GA_BEZIER_RATE;
    public boolean GA_CROSSOVER_AB;
    public int GA_NB_FRAMES;
    public int GA_NB_GENERATIONS;
    public int GA_KEEP_BEST;
    public int GA_NB_INDIVIDUALS;
    public boolean GA_CROSSOVER;
    public float GA_BLEND;
    public SelectionType GA_SELECTION_TYPE;

    public boolean PERF_MULTITHREAD_UPDATE;

    public java.util.Random Random;

    public Parameters()
    {

        GA_GAUSSIAN_MUTATION = false;
        GA_NEW_INDIVIDUAL = 0.1f;
        GA_RANDOM_BREEDERS = 0.1f;
        GA_MUTATION_RATE = 0.1f;
        GA_RANK_MIN = 1;
        GA_RANK_MAX = 10;
        GA_CROSSOVER_AB = false;
        GA_CROSSOVER = true;
        GA_KEEP_BEST = 1;
        GA_BLEND = 0.5f;


        GA_NB_FRAMES = 60*60*10;
        GA_NB_GENERATIONS = 500;
        GA_NB_INDIVIDUALS = 1000;

        PERF_MULTITHREAD_UPDATE = false;
        GA_BEZIER_RATE = 0.5f;
        GA_SELECTION_TYPE = SelectionType.ROULETTE_RANK_LINEAR;

        Random = new Random();
    }
}
