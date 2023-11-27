package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        AList<Integer> N = new AList<>();
        AList<Double> Time = new AList<>();

        for(int i=1000;i<=128000;i=i*2){
            AList<Integer> tmp = new AList<>();
            Stopwatch sw = new Stopwatch();

            for(int j=0;j<=i;j++){
                tmp.addLast(j);
            }

            double time = sw.elapsedTime();
            N.addLast(i);
            Time.addLast(time);
        }

        printTimingTable(N,Time,N);
    }
}
