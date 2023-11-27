package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
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
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        AList<Integer> ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> ops = new AList<>();

        for(int n = 1000; n<=128000;n=n*2){
            SLList<Integer> a = new SLList<>();
            for(int i=1;i<=n;i++){
                a.addLast(i);
            }

            Stopwatch sw = new Stopwatch();

            for(int j=1;j<=10000;j++){
                a.getLast();
            }

            double time = sw.elapsedTime();

            ns.addLast(n);
            times.addLast(time);
            ops.addLast(10000);
        }

        printTimingTable(ns,times,ops);
    }

}
