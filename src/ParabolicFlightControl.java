import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.OptionalDouble;

public class ParabolicFlightControl {
    private NucleusElytra nucleusElytra;

    private Vec3d previousPosition;
    private double currentVelocity;

    private boolean isDescending;
    private boolean pullUp;
    private boolean pullDown;

    private ArrayList<Vec3d> positions;

    private Elytra elytra;

    private long tick;

    public ParabolicFlightControl(Elytra elytra, NucleusElytra nucleusElytra)
    {
        this.nucleusElytra = nucleusElytra;
        this.elytra = elytra;
        tick = 0;
        isDescending = true;
        positions = new ArrayList<>();
        previousPosition = null;
    }

    public void Tick()
    {
        // If client tick
        if (tick % 3 == 0)
        {
            ComputeVelocity();

            if (isDescending)
            {
                pullUp = false;
                pullDown = true;
                if (currentVelocity >= nucleusElytra.pullDownMaxVelocity)
                {
                    isDescending = false;
                    pullDown = false;
                    pullUp = true;
                }
            }
            else if (!isDescending)
            {
                pullUp = true;
                pullDown = false;
                if (currentVelocity <= nucleusElytra.pullUpMinVelocity)
                {
                    isDescending = true;
                    pullDown = true;
                    pullUp = false;
                }
            }

            elytra.Tick();

            positions.add(elytra.Position.Clone());
        }

        if (pullUp)
        {
            elytra.Pitch -= nucleusElytra.pullUpSpeed;

            if (elytra.Pitch <= nucleusElytra.pullUpAngle)
            {
                elytra.Pitch = nucleusElytra.pullUpAngle;
            }
        }

        if (pullDown)
        {
            elytra.Pitch += nucleusElytra.pullDownSpeed;

            if (elytra.Pitch >= nucleusElytra.pullDownAngle)
            {
                elytra.Pitch = nucleusElytra.pullDownAngle;
            }
        }

        tick++;
    }

    public void ApplyFitness()
    {
        nucleusElytra.setFitnessCalculator(new FitnessCalculator());

        float heightSum = 0;
        float heightNum = 0;

        boolean previousGoingDown = true;
        double previousY = positions.get(0).Y;
        double lowPoint = 0;
        double highPoint = 0;

        double amplitudeSum = 0;
        double amplitudeCount = 0;

        double altGainSum = 0;
        double altGainCount = 0;

        boolean atLeastOneCycle = false;
        for (Vec3d v : positions)
        {
            boolean goingDown;
            if (previousY > v.Y)
                goingDown = false;
            else
                goingDown = true;

            //System.out.println(v.Y);

            if (previousGoingDown && !goingDown) {
                //System.out.println("HighPoint");

                if (lowPoint != 0)
                {
                    double amplitude = v.Y - lowPoint;
                    //System.out.println(amplitude);

                    amplitudeSum += amplitude;
                    amplitudeCount ++;
                }

                if (highPoint != 0)
                {
                    double altitudeGain = v.Y - highPoint;
                    //System.out.println(altitudeGain);

                    altGainSum += altitudeGain;
                    altGainCount ++;
                    atLeastOneCycle = true;
                }

                highPoint = v.Y;
            }
            else if (!previousGoingDown && goingDown) {
                //System.out.println("LowPoint");

                lowPoint = v.Y;
            }


            previousY = v.Y;
            previousGoingDown = goingDown;
        }

        nucleusElytra.getFitnessCalculator().FitnessRaw = (float)(altGainSum / altGainCount);

        //if (atLeastOneCycle)

        //if (Float.isNaN(nucleusElytra.getFitnessCalculator().FitnessRaw))
        //    nucleusElytra.getFitnessCalculator().FitnessRaw = (float)positions.get(positions.size()-1).Y / 1000;
        //nucleusElytra.getFitnessCalculator().FitnessRaw = heightSum / heightNum;

        //nucleusElytra.getFitnessCalculator().FitnessRaw = (float)positions.get(positions.size()-1).Y;

        double maxAlt = 0;
        for (Vec3d p : positions)
        {
            if (p.Y > maxAlt) maxAlt = p.Y;
        }

        nucleusElytra.getFitnessCalculator().FitnessRaw = (float)maxAlt;
    }

    private void ComputeVelocity()
    {
        Vec3d newPosition = elytra.Position;

        if (previousPosition == null)
            previousPosition = newPosition.Clone();

        currentVelocity = Vec3d.Substract(newPosition, previousPosition).Length();

        previousPosition = newPosition.Clone();
    }

    private PrintWriter writer;
    public void OpenData(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
        writer = new PrintWriter(fileName, "UTF-8");
    }

    public void WriteData()
    {
        writer.println(tick + ";" + currentVelocity + ";" + elytra.Position.X + ";" + elytra.Position.Y + ";" + elytra.Position.Z + ";");
    }

    public void CloseData()
    {
        Fitness();

        writer.println("distanceTravelled : " + distanceTravelled);
        writer.println("averageHeight : " + averageHeight);

        writer.close();
    }

    public double distanceTravelled;
    public double averageHeight;
    public void Fitness()
    {
        positions.stream().mapToDouble(p -> p.Z).max();
        averageHeight = positions.stream().mapToDouble(p -> p.Y).average().getAsDouble();
    }
}
