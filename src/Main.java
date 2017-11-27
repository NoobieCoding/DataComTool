import java.util.Vector;

/**
 * Created by tuterdust on 21/11/2560.
 */
public class Main {
    public static void main(String[] args) {
        NetworkInfo ni = new NetworkInfo();
        UI ui = new UI(ni);
        ni.addObserver(ui);
        ui.run();
    }
}
