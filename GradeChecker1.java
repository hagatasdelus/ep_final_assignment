import java.io.*;
import java.util.*;
// 必要な import 文を書いてください．
// 各メソッドの最後の行にあるコメントアウトは「メソッドの行数, そのメソッドで定義された変数の数」を示している。
// 最終課題 ステップ1 
// https://ksuap.github.io/2023spring/lesson14/assignments/#one-ステップ1

public class GradeChecker1 {
    HashMap<String, String> grades; // 1

    void run(String[] args) throws IOException {
        this.initialize(args[0]);
        // ファイルをから点数を読み解き，成績を算出してください．
    } // 1, 0

    void initialize(String csvFile) throws IOException {
        this.grades = new HashMap<>();
        BufferedReader in = new BufferedReader(new FileReader(new File(csvFile)));
        String line;
        Integer max = Integer.MIN_VALUE;
        while ((line = in.readLine()) != null) {
            String[] array = line.split(",");
            grades.put(array[0], array[1]);
            if (max < Integer.valueOf(array[0]))
                max = Integer.valueOf(array[0]);
        }
        in.close();
        this.gradeCheck(max);// 12, 5
    }

    void gradeCheck(Integer max) {
        for (Integer i = 1; i <= max; i++) {
            Double score = Double.valueOf(grades.getOrDefault(String.valueOf(i), "0"));
            String grade;
            if (Objects.equals(score, 0.0))
                grade = "K";
            else if (score < 60.0)
                grade = "不可";
            else if (score < 70.0)
                grade = "可";
            else if (score < 80.0)
                grade = "良";
            else if (score < 90.0)
                grade = "優";
            else
                grade = "秀";
            System.out.printf("%d,%.3f,%s%n", i, score, grade);
        } // 17, 3
    }

    // mainメソッドは省略
    public static void main(String[] args) throws IOException {
        GradeChecker1 app = new GradeChecker1();
        app.run(args);
    }
}

// 完成したら，このファイルをコピーして，GradeChecker2.java を作成してください．
// その際，クラス名，mainメソッドの中の書き換えを忘れないように注意しましょう．
// これ以降も同様に完成後，次の練習問題のためにコピーしてください．
