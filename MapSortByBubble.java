import java.util.*;

// もっといいソートアルゴリズムもあるが、一番簡単なのでこれを採用
public class MapSortByBubble {
    void sortMapBubbleSort(ArrayList<Map.Entry<String, Double>> list) {
        for (Integer i = 1; i <= list.size() - 1; i++) {
            for (Integer j = list.size() - 1; j >= i; j--) {
                this.compareAndSwap(j, list);
            }
        }
    }

    void compareAndSwap(Integer j, ArrayList<Map.Entry<String, Double>> list) { // 入れ替えてソート
        if (list.get(j).getValue() < list.get(j - 1).getValue()) {
            Collections.swap(list, j, j - 1);
        }
    }
}
