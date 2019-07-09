public interface INucleus
{
    FitnessCalculator getFitnessCalculator();

    Parameters getParameters();

    INucleus BreedWith(INucleus otherParent);

    INucleus Clone();

    void Mutate();

    void RandomizeAll();

}
