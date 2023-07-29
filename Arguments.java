import java.util.Objects;
// import java.util.ArrayList;;

public class Arguments {
    String record;
    String assignments;
    String miniexam;
    String output;
    Boolean scoreOrder = false; // true: score順 false: id:順

    void parse(String[] args) {
        for (Integer i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                i = parseOption(args, i);
            }
        }
        this.orderCheck(args);
    }

    Integer parseOption(String[] args, Integer i) {
        if (Objects.equals(args[i], "-record")) {
            i++;
            this.record = args[i];
        } else if (Objects.equals(args[i], "-assignments")) {
            i++;
            this.assignments = args[i];
        } else if (Objects.equals(args[i], "-miniexam")) {
            i++;
            this.miniexam = args[i];
        } else if (Objects.equals(args[i], "-output")) {
            i++;
            this.output = args[i];
        }
        return i;
    } // 14, 0

    /*
     * オプションの指定方法が間違っているケースは考えないので, -score -scoreなどと指定した場合は考えない。
     */
    void orderCheck(String[] args) {
        Integer specifiedCount = 0;
        for (Integer i = 0; i < args.length; i++) {
            if (Objects.equals(args[i], "-id")) {
                this.scoreOrder = false;
                specifiedCount++;
            }
            if (Objects.equals(args[i], "-score")) {
                this.scoreOrder = true;
                specifiedCount++;
            }
        }
        if (Objects.equals(specifiedCount, 2))
            this.scoreOrder = false;
    } // 13, 1

    // void orderCheck(String[] args, Integer i) {
    // if (Objects.equals(args[i], "-score"))
    // this.scoreOrder = true;
    // if (Objects.equals(args[i], "-id")) {
    // this.scoreOrder = false;
    // }
    // for (String arg: args) {
    // if (Objects.equals(arg, ""))
    // }
}
