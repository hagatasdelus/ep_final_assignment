import java.util.*;

// アルゴリズムとデータ構造10より
// もっといいソートアルゴリズムもあるが、これが一番簡単
public class MapSortByBubble {
    void sortMapBubbleSort(ArrayList<Map.Entry<String, Double>> list) {
        for (Integer i = 0; i < list.size() - 1; i++) {
            for (Integer j = 0; j < list.size() - i - 1; j++) {
                this.compareAndSwap(j, list);
            }
        }
    }

    void compareAndSwap(Integer j, ArrayList<Map.Entry<String, Double>> list) { // 入れ替えてソート
        if (list.get(j).getValue() > list.get(j + 1).getValue()) {
            Collections.swap(list, j, j + 1);
        }
    }
}
