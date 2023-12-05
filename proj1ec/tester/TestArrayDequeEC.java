package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {
    public String helper(int n, int m) {
        if(n == 0) {
            return "addFirst(" + m + ")\n";
        } else if (n == 1) {
            return "addLast(" + m + ")\n";
        } else if (n == 4) {
            return "removeFirst()\n";
        } else {
            return "removeLast()\n";
        }
    }
    @Test
    public void randomizedTest() {
        StudentArrayDeque<Integer> student = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solution = new ArrayDequeSolution<>();
        ArrayDequeSolution<Integer> action = new ArrayDequeSolution<>();
        ArrayDequeSolution<Integer> param = new ArrayDequeSolution<>();

        int n = 100000;
        for (int i = 0; i < n; i++) {
            int operationNumber = StdRandom.uniform(0, 6);

            if (operationNumber == 0) {
                //addFirst
                int randVal = StdRandom.uniform(0,100);
                student.addFirst(randVal);
                solution.addFirst(randVal);
                action.addLast(0);
                param.addLast(randVal);
            } else if (operationNumber == 1) {
                //addLast
                int randVal = StdRandom.uniform(0, 100);
                student.addLast(randVal);
                solution.addLast(randVal);
                action.addLast(1);
                param.addLast(randVal);
            } else if (operationNumber == 2) {
                //isEmpty
                boolean ans1 = student.isEmpty();
                boolean ans2 = solution.isEmpty();
                assertEquals(ans2, ans1);
            } else if (operationNumber == 3) {
                //size
                int ans1 = student.size();
                int ans2 = solution.size();
                assertEquals(ans2,ans1);
            } else if (operationNumber == 4) {
                //removeFirst
                boolean ans1 = student.isEmpty();
                boolean ans2 = solution.isEmpty();
                assertEquals(ans2, ans1);
                if (ans2 == false) {
                    action.addLast(4);
                    Integer studentAns = student.removeFirst();
                    Integer solutionAns = solution.removeFirst();
                    String message = "";
                    int k = 0;
                    for (int j = 0; j < action.size(); j++) {
                        int first = action.get(j);
                        if (first == 0 || first ==1) {
                            int second = param.get(k);
                            k += 1;
                            message += helper(first, second);
                        } else {
                            message += helper(first, 0);
                        }
                    }
                    assertEquals(message,solutionAns, studentAns);
                }
            } else if (operationNumber == 5) {
                //removeLast
                boolean ans1 = student.isEmpty();
                boolean ans2 = solution.isEmpty();
                assertEquals(ans2, ans1);
                if (ans2 == false) {
                    action.addLast(5);
                    Integer studentAns = student.removeLast();
                    Integer solutionAns = solution.removeLast();
                    String message = "";
                    int k = 0;
                    for (int j = 0; j < action.size(); j++) {
                        int first = action.get(j);
                        if (first == 0 || first ==1) {
                            int second = param.get(k);
                            k += 1;
                            message += helper(first, second);
                        } else {
                            message += helper(first, 0);
                        }
                    }
                    assertEquals(message,solutionAns,studentAns);
                }
            }
        }
    }
}
