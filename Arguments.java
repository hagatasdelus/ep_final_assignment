import java.util.Objects;
import java.util.ArrayList;;

public class Arguments {
    String record;
    String assignments;
    String miniexam;
    String output;
    Boolean scoreOrder = false; // true: score順 false: id:順

    void parse(String[] args) {
        ArrayList<String> arguments = new ArrayList<>();
        for (Integer i = 0; i < args.length; i++) {
            if (!args[i].startsWith("-")) {
                arguments.add(args[i]);
            } else {
                i = parseOption(args, i);
            }
        }
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
        this.orderCheck(args, i); // 固定させたいけど、後々から指定できるconstみたいなのがあるか調べる
        return i;
    } // 18, 0

    void orderCheck(String[] args, Integer i) {
        Boolean hasId = false;
        if (Objects.equals(args[i], "-score") && !hasId)
            this.scoreOrder = true;
        if (Objects.equals(args[i], "-id")) {
            this.scoreOrder = false;
            hasId = true;
        }
        // this.scoreOrder = false;
    }
}
