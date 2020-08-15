/**
 * 2020/8/15 18:08
 *
 * @author owen pan
 */
public class MainTest {
    public static void main(String[] args) {
        String path = new MainTest().getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        System.out.println(path);
    }
}
