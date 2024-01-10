package deque;

import java.util.Comparator;

public class MaxArrayDeque<Item> extends ArrayDeque<Item> {
    private Comparator<Item> comparator;

    public MaxArrayDeque(Comparator<Item> c){
        super();
        comparator = c;
    }

    public Item max() {
        int size = super.size();

        if (size == 0) {
            return null;
        } else {
            Item answer = super.get(0);

            for (int i = 1; i <= size - 1; i++) {
                if (comparator.compare(answer, get(i)) > 0) {
                    answer = get(i);
                }
            }

            return answer;
        }
    }

    public Item max(Comparator<Item> c) {
        int size = super.size();

        if (size == 0) {
            return null;
        } else {
            Item answer = super.get(0);

            for (int i = 1; i <= size - 1; i++) {
                if (c.compare(answer, get(i)) > 0) {
                    answer = get(i);
                }
            }

            return answer;
        }
    }
}
