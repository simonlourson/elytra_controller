public class NucleusElytra implements INucleus {
    public float pullDownAngle;
    public float pullUpAngle;
    public float pullDownMaxVelocity;
    public float pullUpMinVelocity;
    public float pullUpSpeed;
    public float pullDownSpeed;

    public boolean fixedAngle;
    public float fixedAngleValue;

    private FitnessCalculator fitnessCalculator;

    private Parameters parameters;

    public NucleusElytra(Parameters parameters)
    {
        this.parameters = parameters;
    }

    public NucleusElytra()
    {
    }

    public Parameters getParameters()
    {
        return parameters;
    }

    public FitnessCalculator getFitnessCalculator()
    {
        if (fitnessCalculator == null) fitnessCalculator = new FitnessCalculator();
        return fitnessCalculator;
    }

    public void setFitnessCalculator(FitnessCalculator f)
    {
        fitnessCalculator = f;
    }

    public INucleus BreedWith(INucleus otherParent)
    {
        NucleusElytra returnValue = new NucleusElytra(parameters);

        NucleusElytra a = this;
        NucleusElytra b = (NucleusElytra)otherParent;

        returnValue.pullDownAngle = MixGenes(a.pullDownAngle, b.pullDownAngle);
        returnValue.pullUpAngle = MixGenes(a.pullUpAngle, b.pullUpAngle);
        returnValue.pullDownMaxVelocity = MixGenes(a.pullDownMaxVelocity, b.pullDownMaxVelocity);
        returnValue.pullUpMinVelocity = MixGenes(a.pullUpMinVelocity, b.pullUpMinVelocity);
        returnValue.pullUpSpeed = MixGenes(a.pullUpSpeed, b.pullUpSpeed);
        returnValue.pullDownSpeed = MixGenes(a.pullDownSpeed, b.pullDownSpeed);

        return returnValue;
    }

    private float MixGenes(float a, float b)
    {
        if (parameters.Random.nextDouble() < parameters.GA_BLEND)
            return (a + b) / 2;
        else if (parameters.Random.nextDouble() < 0.5)
            return a;
        else
            return b;
    }

    public INucleus Clone()
    {
        NucleusElytra returnValue = new NucleusElytra(parameters);
        returnValue.pullDownAngle = pullDownAngle;
        returnValue.pullUpAngle = pullUpAngle;
        returnValue.pullDownMaxVelocity = pullDownMaxVelocity;
        returnValue.pullUpMinVelocity = pullUpMinVelocity;
        returnValue.pullUpSpeed = pullUpSpeed;
        returnValue.pullDownSpeed = pullDownSpeed;

        return returnValue;
    }

    @Override
    public String toString()
    {
        String s = String.format("%.2f", getFitnessCalculator().FitnessRaw)+" : "+pullDownAngle+";"+pullUpAngle+";"+pullDownMaxVelocity+";"+pullUpMinVelocity+";"+ pullUpSpeed+";"+pullDownSpeed;
        return s;
    }

    public void Mutate()
    {
        pullDownAngle = MutateGene(pullDownAngle, 8);
        pullUpAngle = MutateGene(pullUpAngle, 8);
        pullDownMaxVelocity = MutateGene(pullDownMaxVelocity, 2);
        pullUpMinVelocity = MutateGene(pullUpMinVelocity, 2);
        pullUpSpeed = MutateGene(pullUpSpeed, 1);
        pullDownSpeed = MutateGene(pullDownSpeed, 1);
    }

    private float MutateGene(float a, float range)
    {
        if (parameters.Random.nextDouble() < parameters.GA_MUTATION_RATE)
        {
            return a + (float) parameters.Random.nextGaussian() * range;
        }
        else
            return a;
    }

    public void RandomizeAll()
    {
        pullDownAngle = (float) parameters.Random.nextDouble() * 90;
        pullUpAngle = (float) parameters.Random.nextDouble() * -90;

        while (pullDownMaxVelocity <= pullUpMinVelocity)
        {
            pullDownMaxVelocity = (float) parameters.Random.nextDouble() * 4;
            pullUpMinVelocity = (float) parameters.Random.nextDouble() * 4;
        }

        pullUpSpeed = (float) parameters.Random.nextDouble() * 4;
        pullDownSpeed = (float) parameters.Random.nextDouble() * 4;
    }
}
