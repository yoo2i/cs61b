package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove(){//使用test标签并且不要static
        AListNoResizing<Integer> First = new AListNoResizing<>();
        AListNoResizing<Integer> Second = new AListNoResizing<>();

        for(int i=4;i<=6;i++){
            First.addLast(i);
            Second.addLast(i);
        }

        for(int i=0;i<3;i++){
            assertEquals(First.removeLast(),Second.removeLast());
        }
    }

    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> M = new BuggyAList<>();

        int N = 500000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                M.addLast(randVal);
                //System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int size1 = M.size();
                //System.out.println("size: " + size);
                assertEquals(size,size1);
            } else if (operationNumber ==2) {
                //getLast
                if(L.size()==0) continue;
                int last = L.getLast();
                int last1 = M.getLast();
                //System.out.println("getLast: "+last);
                assertEquals(last,last1);
            } else if (operationNumber == 3) {
                //removeLast
                if(L.size()==0) continue;
                int last = L.removeLast();
                int last1 = M.removeLast();
                //System.out.println("removeLast: "+last);
                assertEquals(last,last1);
            }
        }
    }
}
