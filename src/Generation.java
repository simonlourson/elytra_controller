import java.util.ArrayList;

public class Generation {
    private int generationId;
    int getGenerationId() {
        return generationId;
    }

    private ArrayList<INucleus> individuals;
    ArrayList<INucleus> getIIndividuals() {
        return individuals;
    }

    public Parameters Parameters;

    public Generation()
    {
        Parameters = new Parameters();

        individuals = new ArrayList<INucleus>();

        // Descendant classes should create Individuals here
    }

    public Generation(Parameters parameters)
    {
        Parameters = parameters;

        individuals = new ArrayList<INucleus>();

        // Descendant classes should create Individuals here
    }

    public Generation(Generation parents) throws Exception
    {
        generationId = parents.generationId + 1;
        Parameters = parents.Parameters;
        individuals = new ArrayList<INucleus>();

        for (int i = 0; i < parents.individuals.size(); i++)
        {
            // Keep the first x best
            if (i < Parameters.GA_KEEP_BEST)
            {

                parents.individuals.sort(new FitnessSorter());

                individuals.add(parents.individuals.get(i).Clone());

            }
            else {

                INucleus parent1 = GetBreeder(parents.individuals);

                if (Parameters.GA_CROSSOVER) {
                    INucleus parent2 = null;
                    while (parent2 == null || parent2.equals(parent1))
                        parent2 = GetBreeder(parents.individuals);

                    individuals.add(parent1.BreedWith(parent2));
                } else {
                    INucleus clone = parent1.Clone();
                    clone.Mutate();
                    individuals.add(clone);
                }
            }
        }
    }

    public void Mutate(Parameters parameters)
    {
        for (INucleus i : individuals)
            i.Mutate();
    }

    private INucleus GetBreeder(ArrayList<INucleus> candidates) throws Exception {
        for (INucleus nucleus:candidates) {
            nucleus.getFitnessCalculator().ComputeFitnessRaw();
        }

        candidates.sort(new FitnessSorter());

        if (Parameters.Random.nextDouble() < Parameters.GA_RANDOM_BREEDERS)
            return candidates.get(Parameters.Random.nextInt(candidates.size()));


        if (Parameters.Random.nextDouble() < Parameters.GA_NEW_INDIVIDUAL)
        {
            INucleus random = candidates.get(Parameters.Random.nextInt(candidates.size())).Clone();
            random.RandomizeAll();
            return random;
        }



        // Roulette wheel selection

        // Apply rank
        if (Parameters.GA_SELECTION_TYPE == SelectionType.ROULETTE_RANK_BEZIER ||
            Parameters.GA_SELECTION_TYPE == SelectionType.ROULETTE_RANK_LINEAR ||
            Parameters.GA_SELECTION_TYPE == SelectionType.ROULETTE_RANK_TRUE_BEZIER)
        {
            int rank = 0;
            for (INucleus n :candidates) {
                n.getFitnessCalculator().Rank = rank;
                rank++;
            }
        }

        float x1 = 0, y1 = 0, x2 = 0, y2 = 0, al = 0, bl = 0;
        if (Parameters.GA_SELECTION_TYPE == SelectionType.ROULETTE_RANK_LINEAR)
        {
            // Compute a and b (linear transformation)
            x1 = 0.0f;
            y1 = Parameters.GA_RANK_MAX;
            x2 = candidates.size() - 1;
            y2 = Parameters.GA_RANK_MIN;
            al = (y2 - y1) / (x2 - x1);
            bl = y1 - al * x1;
        }

        Vector2 a = Vector2.Zero, b = Vector2.Zero, c = Vector2.Zero;
        if (Parameters.GA_SELECTION_TYPE == SelectionType.ROULETTE_RANK_BEZIER)
        {
            // Compute Bezier curve
            a = new Vector2(0, Parameters.GA_RANK_MAX);
            b = new Vector2(0, Parameters.GA_RANK_MIN);
            c = new Vector2(0, Parameters.GA_RANK_MIN);
        }


        float totalFitness = 0;
        for (INucleus nucleus : candidates)
        {
            // No transformation
            if (Parameters.GA_SELECTION_TYPE == SelectionType.ROULETTE_RAW)
                nucleus.getFitnessCalculator().FitnessSmoothed = nucleus.getFitnessCalculator().FitnessRaw;

            // Linear transformation
            if (Parameters.GA_SELECTION_TYPE == SelectionType.ROULETTE_RANK_LINEAR)
                nucleus.getFitnessCalculator().FitnessSmoothed = nucleus.getFitnessCalculator().Rank * al + bl;

            // TODO : Translate Bezier transformation
            if (Parameters.GA_SELECTION_TYPE == SelectionType.ROULETTE_RANK_BEZIER)
            {
                //float t = nucleus.FitnessCalculator.Rank / candidates.Count;
                //Vector2 result = a * (1 - t) * (1 - t) + 2 * b * (1 - t) * t + c * t * t;
                //nucleus.FitnessCalculator.FitnessSmoothed = result.Y;
            }

            // TODO : Translate True bezier
            if (Parameters.GA_SELECTION_TYPE == SelectionType.ROULETTE_RANK_TRUE_BEZIER)
            {
                //nucleus.FitnessCalculator.FitnessSmoothed = bezierCurve.First(p => p.X >= nucleus.FitnessCalculator.Rank).Y;
            }


            totalFitness += nucleus.getFitnessCalculator().FitnessSmoothed;
        }

        float r = (float)Parameters.Random.nextDouble() * totalFitness;
        float nextStep = 0;
        for (INucleus parent : candidates)
        {
            nextStep += parent.getFitnessCalculator().FitnessSmoothed;
            if (r < nextStep)
                return parent;
        }

        throw  new Exception("No breeder found");
    }

    // TODO : Xml Serialization

    public void ApplyFitness()
    {

    }

    public Generation Clone()
    {
        Generation returnValue = new Generation(Parameters);

        for (INucleus n : individuals) {
            INucleus nClone = n.Clone();
            returnValue.individuals.add(nClone);
        }

        return returnValue;
    }


    public Generation CreateDescendants() throws Exception {
        return new Generation(this);
    }


    public boolean IsEvaluated()
    {
        return individuals.stream().mapToDouble(n -> n.getFitnessCalculator().FitnessRaw).max().getAsDouble() > 0;
    }

}

