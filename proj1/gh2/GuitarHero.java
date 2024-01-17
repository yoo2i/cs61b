package gh2;
import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

/**
 * A client that uses the synthesizer package to replicate a plucked guitar string sound
 */
public class GuitarHero {
    public static final double CONCERT_A = 440.0;

    public static void main(String[] args) {
        /* create two guitar strings, for concert A and C */
        GuitarString string[] = new GuitarString[37];

        for (int i = 0; i < 37; i++) {
            double tmp = (i - 24) / 12.0;
            double ans = CONCERT_A * Math.pow(2, tmp);
            string[i] = new GuitarString(ans);
        }

        while (true) {
            String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = keyboard.indexOf(key);
                if (index == -1) {
                    continue;
                }
                string[index].pluck();
            }

            /* compute the superposition of samples */
            double sample = 0.0;
            for (int i = 0; i < 37; i++) {
                sample += string[i].sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (int i = 0; i < 37; i++) {
                string[i].tic();
            }
        }
    }
}

