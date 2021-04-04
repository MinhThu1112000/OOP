package graph;

public class FormatPath {
    // đây là format đường dẫn để chạy được cả 2 hệ điều hành macos và windows
    // ở trong window thì đường dẫn là  "ex1\\abc\\xyz" nhưng trong macos thì là "ex1/abc/xyz"
    // nên ktra hệ điều hành . nếu là windows thì để nguyên còn macos thì replace \\ thành /
    public static String formatPath(String input_path) {
        if (System.getProperty("os.name").startsWith("Mac")) {
            return input_path.replace("\\", "/");
        } else {
            return input_path;
        }
    }
}
