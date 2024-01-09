/** Class that prints the Collatz sequence starting from a given number.
 *  @author yoo2i
 */
public class Collatz {

    /** Correct implementation of nextNumber! */
    public static int nextNumber(int n) {
        if(n%2==1){
            return n*3+1;
        }
        else{
            return n/2;
        }
    }

    public static void main(String[] args) {
        int n = 5;
        System.out.print(n + " ");
        while (n != 1) {
            n = nextNumber(n);
            System.out.print(n + " ");
        }
        System.out.println();
    }
}

