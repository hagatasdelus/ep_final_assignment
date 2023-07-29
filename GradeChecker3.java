import java.io.*;
import java.util.*;
// 必要な import 文を書いてください．
// 各メソッドの最後の行にあるコメントアウトは「メソッドの行数, そのメソッドで定義された変数の数」を示している。
// 最終課題 ステップ3
// https://ksuap.github.io/2023spring/lesson14/assignments/#one-ステップ1

public class GradeChecker3 {
    HashMap<String, String> exam;
    HashMap<String, String> assignments;
    HashMap<String, String> miniexam;
    Integer max = Integer.MIN_VALUE; // 0, 4

    void run(String[] args) throws IOException {
        this.initializeForExam(args[0]);
        this.initializeForAssign(args[1]);
        this.initializeForMiniExam(args[2]);
        this.gradeCheck(); // 4, 0
        // ファイルをから点数を読み解き，成績を算出してください．
    }

    void initializeForExam(String csvFile) throws IOException {
        this.exam = new HashMap<>();
        BufferedReader in = new BufferedReader(new FileReader(new File(csvFile)));
        String line;
        while ((line = in.readLine()) != null) {
            String[] array = line.split(",");
            exam.put(array[0], array[1]);
            if (this.max < Integer.valueOf(array[0]))
                this.max = Integer.valueOf(array[0]);
        }
        in.close(); // 10, 4
    }

    void initializeForAssign(String csvFile) throws IOException {
        this.assignments = new HashMap<>();
        BufferedReader in = new BufferedReader(new FileReader(new File(csvFile)));
        String line;
        Integer total = 0;
        while ((line = in.readLine()) != null) {
            String[] array = line.split(",");
            total = this.calcTotalAssignScore(array);
            assignments.put(array[0], total.toString());
        }
        in.close(); // 10, 5
    }

    Integer calcTotalAssignScore(String[] array) {
        Integer total = 0;
        for (Integer i = 1; i < array.length; i++) {
            if (!Objects.equals(array[i], ""))
                total += Integer.valueOf(array[i]);
        }
        return total; // 6, 2
    }

    void initializeForMiniExam(String csvFile) throws IOException {
        this.miniexam = new HashMap<>();
        BufferedReader in = new BufferedReader(new FileReader(new File(csvFile)));
        String line;
        while ((line = in.readLine()) != null) {
            String[] array = line.split(",");
            Integer count = this.calcTotalMiniExamScore(array);
            miniexam.put(array[0], count.toString());
        }
        in.close(); // 9, 5
    }

    Integer calcTotalMiniExamScore(String[] array) {
        Integer count = 0;
        for (Integer i = 1; i < array.length; i++) {
            if (!Objects.equals(array[i], ""))
                count++;
        }
        return count; // 6, 2
    }

    void gradeCheck() {
        for (Integer i = 1; i <= this.max; i++) {
            Double finalScore = Math.ceil(calcFinalScore(i.toString()));
            String grade = this.getGrade(finalScore, i);
            this.printGrades(i, finalScore, grade);
        }
        this.getStats(); // 6, 3
    }

    String getGrade(Double finalScore, Integer num) {
        String grade;
        if (!this.noExamCheck(num))
            grade = "K";
        else if (finalScore < 60.0)
            grade = "不可";
        else if (finalScore < 70.0)
            grade = "可";
        else if (finalScore < 80.0)
            grade = "良";
        else if (finalScore < 90.0)
            grade = "優";
        else
            grade = "秀";
        return grade;
    } // 14, 1

    void printGrades(Integer num, Double finalScore, String grade) {
        if (this.noExamCheck(num))
            System.out.printf("%d,%2.0f,%2.3f,%s,%s,%s%n", num, finalScore, Double.valueOf(exam.get(num.toString())),
                    assignments.getOrDefault(num.toString(), "0"), miniexam.getOrDefault(num.toString(), "0"), grade);
        else
            System.out.printf("%d,%2.0f,%s,%s,%s,%s%n", num, finalScore, "", assignments.get(num.toString()),
                    miniexam.getOrDefault(num.toString(), "0"), grade); // 2, 0
    } // 6, 0

    void getStats() {
        this.printAverage();
        this.printMax();
        this.printMin();
        this.statsInfo();
    } // 4 ,0

    Double getMaxWithAndWithoutFails(Boolean without) {
        Double max = 0.0;
        for (Integer i = 1; i <= this.max; i++) {
            Double finalScore = Math.ceil(calcFinalScore(i.toString()));
            if (without && (!this.noExamCheck(i) || finalScore < 60.0))
                continue;
            if (finalScore > max)
                max = finalScore; // 最大値
        }
        return max;
    } // 9, 3

    Double getMinWithAndWithoutFails(Boolean without) {
        Double min = 100.0;
        for (Integer i = 1; i <= this.max; i++) {
            Double finalScore = Math.ceil(calcFinalScore(i.toString())); // 最終成績
            if (without && (!this.noExamCheck(i) || finalScore < 60.0)) // 試験を受けていない人
                continue;
            if (finalScore < min)
                min = finalScore;
        }
        return min; // 9, 3
    }

    Double getAveWithAndWithoutFails(Boolean without) {
        Double sum = 0.0;
        Integer count = 0;
        for (Integer i = 1; i <= this.max; i++) {
            Double finalScore = Math.ceil(calcFinalScore(i.toString())); // 最終成績
            if (without && (!this.noExamCheck(i) || finalScore < 60.0)) // (!this.noExamCheck(i) || )
                continue;
            sum += finalScore;
            count++;
        }
        return sum / count;
    } // 10, 4

    void printMax() {
        Double maxWithFails = this.getMaxWithAndWithoutFails(false);
        Double minWithoutFails = this.getMaxWithAndWithoutFails(true);
        System.out.printf("Max: %.3f (%.3f)%n", maxWithFails, minWithoutFails);
    }// 3, 2

    void printMin() {
        Double minWithFails = this.getMinWithAndWithoutFails(false);
        Double minWithoutFails = this.getMinWithAndWithoutFails(true);
        System.out.printf("Min: %.3f (%.3f)%n", minWithFails, minWithoutFails);
    }// 3, 2

    void printAverage() {
        Double aveWithFails = this.getAveWithAndWithoutFails(false);
        Double aveWithoutFails = this.getAveWithAndWithoutFails(true);
        System.out.printf("Avg: %.3f (%.3f)%n", aveWithFails, aveWithoutFails);
    }// 3, 2

    Boolean noExamCheck(Integer num) {
        if (Objects.equals(exam.get(num.toString()), null))
            return false;
        else
            return true; // 4, 0
    }

    Double calcFinalScore(String id) {
        Double score = 70.0 * Double.valueOf(exam.getOrDefault(id, "0.0")) / 100.0
                + 25.0 * Double.valueOf(assignments.get(id)) / 60.0
                + 5.0 * Double.valueOf(miniexam.getOrDefault(id, "0.0")) / 14.0;
        return score; // 4, 1;
    }

    void statsInfo() {
        LinkedHashMap<String, Integer> statsMap = this.setup();
        for (Integer i = 1; i <= this.max; i++) {
            Double finalScore = Math.ceil(calcFinalScore(i.toString()));
            statsMap.put(getGrade(finalScore, i), statsMap.get(getGrade(finalScore, i)) + 1);
        }
        this.printStatsInfo(statsMap);
        // 6, 3
    }

    void printStatsInfo(LinkedHashMap<String, Integer> map) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.printf("%s:   %2d%n", entry.getKey(), entry.getValue());
        }
    } // 3, 1

    LinkedHashMap<String, Integer> setup() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("秀", 0);
        map.put("優", 0);
        map.put("良", 0);
        map.put("可", 0);
        map.put("不可", 0);
        map.put("K", 0);
        return map;
    } // 8, 1

    // mainメソッドは省略
    public static void main(String[] args) throws IOException {
        GradeChecker3 app = new GradeChecker3();
        app.run(args);
    }
}

// 完成したら，このファイルをコピーして，GradeChecker2.java を作成してください．
// その際，クラス名，mainメソッドの中の書き換えを忘れないように注意しましょう．
// これ以降も同様に完成後，次の練習問題のためにコピーしてください．
