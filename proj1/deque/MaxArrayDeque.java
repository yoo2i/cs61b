package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        comparator = c;
    }

    public T max() {
        int size = super.size();

        if (size == 0) {
            return null;
        } else {
            T answer = super.get(0);

            for (int i = 1; i <= size - 1; i++) {
                if (comparator.compare(answer, get(i)) < 0) {
                    answer = get(i);
                }
            }

            return answer;
        }
    }

    public T max(Comparator<T> c) {
        int size = super.size();

        if (size == 0) {
            return null;
        } else {
            T answer = super.get(0);

            for (int i = 1; i <= size - 1; i++) {
                if (c.compare(answer, get(i)) < 0) {
                    answer = get(i);
                }
            }

            return answer;
        }
    }
}
