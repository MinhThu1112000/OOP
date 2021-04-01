package graph;

public class FormatPath {
    public static String formatPath(String input_path) {
        if (System.getProperty("os.name").startsWith("Mac")) {
            return input_path.replace("\\", "/");
        } else {
            return input_path;
        }
    }
}
