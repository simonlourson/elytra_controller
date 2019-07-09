import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

public class GenerationElytra extends Generation{

    public ArrayList<NucleusElytra> getIndividualsElytra() {
        throw new NotImplementedException();
    }

    public GenerationElytra(Parameters parameters)
    {
        super(parameters);

        for (int i = 0; i < parameters.GA_NB_INDIVIDUALS; i++)
        {
            NucleusElytra n = new NucleusElytra(parameters);
            n.RandomizeAll();

            getIIndividuals().add(n);
        }
    }

    public GenerationElytra(GenerationElytra parents) throws Exception {
        super(parents);
    }

    public GenerationElytra()
    {

    }

    public void ApplyFitness()
    {

        Elytra elytra = new Elytra();

        for (INucleus n : getIIndividuals())
        {
            NucleusElytra nElytra = (NucleusElytra)n;

            elytra.ResetElytra();
            ParabolicFlightControl flightControl = new ParabolicFlightControl(elytra, nElytra);

            // 5 minutes
            for (int i = 0; i < 60 * 60 * 5; i++)
            {
                flightControl.Tick();
            }

            flightControl.ApplyFitness();
        }

        getIIndividuals().sort(new FitnessSorter());

    }
}
