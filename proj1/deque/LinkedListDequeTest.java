package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {
        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

		assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
		lld1.addFirst("front");

		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

		lld1.addLast("middle");
		assertEquals(2, lld1.size());

		lld1.addLast("back");
		assertEquals(3, lld1.size());

		System.out.println("Printing out deque: ");
		lld1.printDeque();

    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		// should be empty
		assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty
		assertFalse("lld1 should contain 1 item", lld1.isEmpty());

		lld1.removeFirst();
		// should be empty
		assertTrue("lld1 should be empty after removal", lld1.isEmpty());

    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);

    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {


        LinkedListDeque<String>  lld1 = new LinkedListDeque<String>();
        LinkedListDeque<Double>  lld2 = new LinkedListDeque<Double>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();

    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());


    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }
    }
    
    @Test
    public void randomizedTest() {
        //随机测试该怎么写我不会喵，这么写只会报错
        //改完了喵，把remove这种有返回值的且可能返回null的接收变量类型改成Integer就好啦，int不可以是null但是Integer可以喵
        LinkedListDeque<Integer> L = new LinkedListDeque<>();
        ArrayDeque<Integer> M = new ArrayDeque<>();
        
        int N = 1000000;
        for(int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 8);
            if(operationNumber == 0){
                //addFirst
                int randVal = StdRandom.uniform(0,100);
                L.addFirst(randVal);
                M.addFirst(randVal);
            } else if (operationNumber == 1){
                //addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                M.addLast(randVal);
            } else if (operationNumber == 2) {
                //isEmpty
                boolean ans1 = L.isEmpty();
                boolean ans2 = M.isEmpty();
                assertEquals(ans1,ans2);
            } else if (operationNumber == 3) {
                //size
                int ans1 = L.size();
                int ans2 = M.size();
                assertEquals(ans1,ans2);
            } else if (operationNumber == 4) {
                //removeFirst
                Integer ans1 = L.removeFirst();
                Integer ans2 = M.removeFirst();
                assertEquals(ans1,ans2);
            } else if (operationNumber == 5) {
                //removeLast
                Integer ans1 = L.removeLast();
                Integer ans2 = M.removeLast();
                assertEquals(ans1, ans2);
            } else if (operationNumber == 6) {
                //get
                int tmp1 = StdRandom.uniform(0,2);
                int tmp2 = StdRandom.uniform(0,100);
                Integer ans1;
                Integer ans2 = M.get(tmp2);
                if (tmp1 == 0) {
                    ans1 = L.getRecursive(tmp2);
                } else {
                    ans1 = L.get(tmp2);
                }
                assertEquals(ans1, ans2);
            } else if (operationNumber == 7) {
                int tmp = StdRandom.uniform(0, 2);

                if (tmp == 0) {
                    assertTrue(L.equals(M));
                } else {
                    assertTrue(M.equals(L));
                }
            }
        }
    }

    @Test
    public void testEqual() {
        LinkedListDeque<Integer> tmp1 = new LinkedListDeque<>();
        ArrayDeque<Integer> tmp2 = new ArrayDeque<>();


        assertTrue(tmp1.equals(tmp2));
    }
}
