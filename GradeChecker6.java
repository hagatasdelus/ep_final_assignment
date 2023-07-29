import java.io.*;
import java.util.*;
// 必要な import 文を書いてください．
// 最終課題 ステップ6
// 各メソッドの最後の行にあるコメントアウトは「メソッドの行数, そのメソッドで定義された変数の数」を示している。
// https://ksuap.github.io/2023spring/lesson14/assignments/#one-ステップ1

public class GradeChecker6 {
    HashMap<String, String> exam;
    HashMap<String, String> assignments;
    HashMap<String, String> miniexam;
    Integer max = Integer.MIN_VALUE;
    Arguments arguments = new Arguments(); // 0, 5

    void run(String[] args) throws IOException {
        HashMap<String, Double> scoreOrder = null;
        arguments.parse(args);
        if (arguments.record != null)
            this.initializeForExam(arguments.record);
        if (arguments.assignments != null)
            this.initializeForAssign(arguments.assignments);
        if (arguments.miniexam != null)
            this.initializeForMiniExam(arguments.miniexam);
        if (arguments.scoreOrder)
            scoreOrder = this.initializeForScore();

        this.gradeCheck(scoreOrder); // 9, 0
        // ファイルをから点数を読み解き，成績を算出してください．
    }

    HashMap<String, Double> initializeForScore() {
        HashMap<String, Double> scoreOrder = new HashMap<>();
        for (Integer i = 1; i <= this.max; i++) {
            Double finalScore = this.calcFinalScore(Double.valueOf(exam.getOrDefault(i.toString(), "0.0")),
                    i.toString());
            scoreOrder.put(i.toString(), finalScore);
        }
        return scoreOrder;
    } // 7, 3

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
        return count; // 5, 2
    }

    void gradeCheck(HashMap<String, Double> scoreOrder) throws IOException {
        PrintWriter out;
        if (arguments.output != null)
            out = new PrintWriter(new FileWriter(arguments.output));
        else
            out = null;
        if (!arguments.scoreOrder)
            this.printGradesByIdOrder(out); // nullはあまり使いたくないけどコード
        else
            this.printGradesByScoreOrder(out, scoreOrder);
        this.getStats(out);
        if (out != null)
            out.close(); // 12, 1
    }

    Double calcFinalScoreFromAll(String id) {
        Double score = 0.0;
        if (arguments.record != null)
            score += 70 * Double.valueOf(this.exam.getOrDefault(id, "0.0")) / 100.0;
        if (arguments.assignments != null)
            score += 25.0 * Double.valueOf(this.assignments.getOrDefault(id, "0.0")) / 60.0;
        if (arguments.miniexam != null)
            score += 5.0 * Double.valueOf(this.miniexam.getOrDefault(id, "0.0")) / 14.0;
        return score; // 8, 1;
    }

    Double calcFinalScore(Double examScore, String num) {
        if (examScore >= 80.0) {
            if (examScore < calcFinalScoreFromAll(num)) {
                return Math.ceil(calcFinalScoreFromAll(num));
            }
            return Math.ceil(Double.valueOf(exam.get(num)));
        } else {
            return Math.ceil(calcFinalScoreFromAll(num));
        } // 8, 0
    }

    String getGrade(Double finalScore, Integer num) {
        String grade;
        if (!this.noExamCheck(num))
            grade = "K";
        else if (finalScore < 60.0 && (arguments.miniexam == null
                || Integer.valueOf(miniexam.getOrDefault(num.toString(), "0")) <= 7))
            grade = "\u203B";
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
        return grade; // 17, 1
    }

    String valueIsExistForIdOrder(Integer num, Double finalScore, String grade) {
        String format = String.format("%d,%2.0f", num, finalScore);
        if (arguments.record != null) {
            if (!this.noExamCheck(num))
                format = format + String.format(",");
            else
                format = format + String.format(",%2.3f", Double.valueOf(exam.getOrDefault(num.toString(), "0.000")));
        } else
            format = format + String.format(",%2.3f", 0.000);
        if (arguments.assignments != null)
            format = format + String.format(",%s", assignments.getOrDefault(num.toString(), "0"));
        else
            format = format + String.format(",%s", "0");
        if (arguments.miniexam != null)
            format = format + String.format(",%s", miniexam.getOrDefault(num.toString(), "0"));
        else
            format = format + String.format(",%s", "0");
        format = format + String.format(",%s%n", grade);
        return format; // 18, 1
    }

    String valueIsExistForScoreOrder(Integer num, String grade) {
        String format = "";
        if (arguments.record != null) {
            if (!this.noExamCheck(num))
                format = format + String.format(",");
            else
                format = format + String.format(",%2.3f", Double.valueOf(exam.getOrDefault(num.toString(), "0.000")));
        } else
            format = format + String.format(",%2.3f", 0.000);
        if (arguments.assignments != null)
            format = format + String.format(",%s", assignments.getOrDefault(num.toString(), "0"));
        else
            format = format + String.format(",%s", "0");
        if (arguments.miniexam != null)
            format = format + String.format(",%s", miniexam.getOrDefault(num.toString(), "0"));
        else
            format = format + String.format(",%s", "0");
        format = format + String.format(",%s%n", grade);
        return format; // 18, 1
    }

    void printGradesByIdOrder(PrintWriter out) throws IOException {
        for (Integer i = 1; i <= this.max; i++) {
            Double finalScore = this.calcFinalScore(Double.valueOf(exam.getOrDefault(i.toString(), "0.0")),
                    i.toString());
            String grade = this.getGrade(finalScore, i);
            String format = this.valueIsExistForIdOrder(i, finalScore, grade);
            if (out == null)
                System.out.printf("%s", format);
            else
                out.printf("%s", format);
        } // 10, 4
    }

    void printGradesByScoreOrder(PrintWriter out, HashMap<String, Double> mapScoreOrder) throws IOException {
        ArrayList<Map.Entry<String, Double>> sortedMapList = new ArrayList<>(mapScoreOrder.entrySet());
        MapSortByBubble sorter = new MapSortByBubble();
        sorter.sortMapBubbleSort(sortedMapList);
        for (Map.Entry<String, Double> entry : sortedMapList) {
            String grade = this.getGrade(entry.getValue(), Integer.valueOf(entry.getKey()));
            String format = this.valueIsExistForScoreOrder(Integer.valueOf(entry.getKey()), grade);
            if (out == null)
                System.out.printf("%s,%2.0f%s", entry.getKey(), entry.getValue(), format);
            else
                out.printf("%s,%2.0f%s", entry.getKey(), entry.getValue(), format);
        } // 11, 5
    }

    void getStats(PrintWriter out) {
        this.printAverage(out);
        this.printMax(out);
        this.printMin(out);
        this.statsInfo(out); // 4, 0
    }

    Double getMaxWithAndWithoutFails(Boolean without) {
        Double max = 0.0;
        for (Integer i = 1; i <= this.max; i++) {
            Double finalScore = this.calcFinalScore(Double.valueOf(exam.getOrDefault(i.toString(), "0.0")),
                    i.toString());
            if (without && (!this.noExamCheck(i) || finalScore < 60.0))
                continue;
            if (finalScore > max)
                max = finalScore; // 最大値
        }
        return max; // 10, 3
    }

    Double getMinWithAndWithoutFails(Boolean without) {
        Double min = 100.0;
        for (Integer i = 1; i <= this.max; i++) {
            Double finalScore = this.calcFinalScore(Double.valueOf(exam.getOrDefault(i.toString(), "0.0")),
                    i.toString());
            if (without && (!this.noExamCheck(i) || finalScore < 60.0)) // 試験を受けていない人
                continue;
            if (finalScore < min)
                min = finalScore;
        }
        return min; // 10, 3
    }

    Double getAveWithAndWithoutFails(Boolean without) {
        Double sum = 0.0;
        Integer count = 0;
        for (Integer i = 1; i <= this.max; i++) {
            Double finalScore = this.calcFinalScore(Double.valueOf(exam.getOrDefault(i.toString(), "0.0")),
                    i.toString());
            if (without && (!this.noExamCheck(i) || finalScore < 60.0)) // (!this.noExamCheck(i) || )
                continue;
            sum += finalScore;
            count++;
        }
        return sum / count; // 11, 4
    }

    void printMax(PrintWriter out) {
        Double maxWithFails = this.getMaxWithAndWithoutFails(false);
        Double minWithoutFails = this.getMaxWithAndWithoutFails(true);
        if (out != null)
            out.printf("Max: %.3f (%.3f)%n", maxWithFails, minWithoutFails);
        else
            System.out.printf("Max: %.3f (%.3f)%n", maxWithFails, minWithoutFails);
    } // 6, 2

    void printMin(PrintWriter out) {
        Double minWithFails = this.getMinWithAndWithoutFails(false);
        Double minWithoutFails = this.getMinWithAndWithoutFails(true);
        if (out != null)
            out.printf("Min: %.3f (%.3f)%n", minWithFails, minWithoutFails);
        else
            System.out.printf("Min: %.3f (%.3f)%n", minWithFails, minWithoutFails);
    } // 6, 2

    void printAverage(PrintWriter out) {
        Double aveWithFails = this.getAveWithAndWithoutFails(false);
        Double aveWithoutFails = this.getAveWithAndWithoutFails(true);
        if (out != null)
            out.printf("Avg: %.3f (%.3f)%n", aveWithFails, aveWithoutFails);
        else
            System.out.printf("Avg: %.3f (%.3f)%n", aveWithFails, aveWithoutFails);
    } // 6, 2

    Boolean noExamCheck(Integer num) {
        if (Objects.equals(exam.get(num.toString()), null)) // null
            return false;
        else
            return true; // 4, 0
    }

    void statsInfo(PrintWriter out) {
        LinkedHashMap<String, Integer> statsMap = this.setup();
        Integer passedCount = 0;
        for (Integer i = 1; i <= this.max; i++) {
            Double finalScore = this.calcFinalScore(Double.valueOf(exam.getOrDefault(i.toString(), "0.0")),
                    i.toString());
            statsMap.put(getGrade(finalScore, i), statsMap.get(getGrade(finalScore, i)) + 1);
            if (finalScore >= 60)
                passedCount++;
        }
        this.printStatsInfo(statsMap, out, passedCount); // 7, 3
    }

    void printStatsInfo(LinkedHashMap<String, Integer> map, PrintWriter out, Integer count) {
        if (out != null)
            out.printf("単位取得率: %.3f%%%n", Double.valueOf(count) / Double.valueOf(this.max) * 100.0);
        else
            System.out.printf("単位取得率: %.3f%%%n", Double.valueOf(count) / Double.valueOf(this.max) * 100.0);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            this.printNumAndCreditRate(entry, out, count);
        } // 6, 1
    }

    String getStatsFormat(Map.Entry<String, Integer> entry, Integer count) {
        String format = String.format("%s:   %2d   %.3f%% ", entry.getKey(), entry.getValue(),
                Double.valueOf(entry.getValue()) / Double.valueOf(this.max) * 100.0);
        if (Objects.equals(entry.getKey(), "秀") || Objects.equals(entry.getKey(), "優")
                || Objects.equals(entry.getKey(), "良") || Objects.equals(entry.getKey(), "可")) {
            format += String.format("(%.3f%%)", Double.valueOf(entry.getValue()) / Double.valueOf(count) * 100.0);
        }
        format += String.format("%n");
        return format;
    }

    void printNumAndCreditRate(Map.Entry<String, Integer> entry, PrintWriter out, Integer count) {
        String format = this.getStatsFormat(entry, count);
        if (out != null) {
            out.printf("%s", format);
        } else {
            System.out.printf("%s", format);
        }
    }

    LinkedHashMap<String, Integer> setup() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("秀", 0);
        map.put("優", 0);
        map.put("良", 0);
        map.put("可", 0);
        map.put("不可", 0);
        map.put("K", 0);
        map.put("\u203B", 0);
        return map; // 9, 1
    }

    // mainメソッドは省略
    public static void main(String[] args) throws IOException {
        GradeChecker6 app = new GradeChecker6();
        app.run(args);
    }
}

// 完成したら，このファイルをコピーして，GradeChecker2.java を作成してください．
// その際，クラス名，mainメソッドの中の書き換えを忘れないように注意しましょう．
// これ以降も同様に完成後，次の練習問題のためにコピーしてください．
