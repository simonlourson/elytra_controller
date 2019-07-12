import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));


        boolean doGenetic = false;
        boolean doTestGenetic = true;
        boolean doFixedAngle = false;

        if (doGenetic) {
            GenerationElytra parent = new GenerationElytra(new Parameters());
            parent.ApplyFitness();

            for (int i = 0; i < 1000; i++) {
                GenerationElytra child = new GenerationElytra(parent);
                child.ApplyFitness();

                INucleus first = child.getIIndividuals().get(0);
                System.out.println(first.toString());

                parent = child;
            }
        }
        else if (doTestGenetic){


            NucleusElytra defaultNucleus = new NucleusElytra();
            defaultNucleus.pullUpAngle = -46.633514f;
            defaultNucleus.pullDownAngle = 37.19872f;
            defaultNucleus.pullUpMinVelocity = 1.9102669f;
            defaultNucleus.pullDownMaxVelocity = 2.3250866f;
            defaultNucleus.pullUpSpeed = 2.1605124f;
            defaultNucleus.pullDownSpeed = 0.20545267f;

            Elytra elytra = new Elytra();

            // 522,02 : 35.980534;-41.319027;2.2192945;1.3844357;2.0034056;0.1304855
            // 506,24 : 35.980534;-41.319027;2.2192945;1.3844357;2.0034056;0.1304855
            //          36.95915;-50.73443;2.136922;1.7259855;2.0569143;0.37636718
            // 11,96 : 38.06386;-53.735085;2.2800236;2.1795864;3.4034472;0.2429375
            // 605,41 : 37.19872;-46.633514;2.3250866;1.9102669;2.1605124;0.20545267

            //for (int j = 0; j < 500; j++) {


            elytra.ResetElytra();

            ParabolicFlightControl flightControl = new ParabolicFlightControl(elytra, defaultNucleus);

            PrintWriter writer = new PrintWriter("testGenetic.csv", "UTF-8");

            // 5 minutes
            for (int i = 0; i < 60 * 60 * 5; i++) {
                flightControl.Tick();

                if (i % 3 == 0)
                    flightControl.WriteData(writer);
            }
            writer.close();


            flightControl.ApplyFitness();
            System.out.println(defaultNucleus.toString());
            //}
        }
        else if (doFixedAngle)
        {
            PrintWriter writer = new PrintWriter("fixedAngles.csv", "UTF-8");
            for (float fixedAngle = -20; fixedAngle <= 20; fixedAngle += 5 )
            {
                NucleusElytra fixedAngleNucleus = new NucleusElytra();
                fixedAngleNucleus.fixedAngle = true;
                fixedAngleNucleus.fixedAngleValue = fixedAngle;

                Elytra elytra = new Elytra();
                elytra.ResetElytra();
                ParabolicFlightControl flightControl = new ParabolicFlightControl(elytra, fixedAngleNucleus);

                // 30 seconds
                for (int i = 0; i < 30 * 60 * 1; i++) {
                    flightControl.Tick();

                    if (i % 3 == 0)
                        flightControl.WriteData(writer);
                }

            }
            writer.close();
        }


    }
}
