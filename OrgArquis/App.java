import java.io.File;
import java.io.IOException;

public class App {
    public static void main(String args[]) throws IOException {
        File file = new File("in.mips");
        Processor pro = new Processor(file);
        pro.process();
    }
}