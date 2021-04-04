import graph.FormatPath;
import graph.Graph;

import java.io.*;
import java.util.List;

public class Main {
    private static final String input_source = "input\\";
    private static final String png_source = "output\\png\\";
    private static final String dot_source = "output\\dot\\";

    //đọc file dể thêm đỉnh và cạnh vào
    public static void readFile(String filename, Graph graph) {
        try {
            FileReader fr = new FileReader(FormatPath.formatPath(input_source) + filename);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                //đọc từng dòng ví dụ 1 2 3 -> line  = "1 2 3"
                //cắt string theo dấu  cách tạo thành 1 mảng string vertex
                String[] vertex = line.split(" ");
                for (String v : vertex) {
                    //chạy for cho mảng string vừa được tạo để add từng đỉnh vào
                    graph.addVertex(v);
                }
                for (int i = 1; i < vertex.length; i++) {
                    // thêm cạnh cho từng đỉnh
                    // ví dụ 1 2 3 thì cạnh sẽ được tạo là 1->2; 1->3
                    graph.addEdge(vertex[0], vertex[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // chuyển đổi file dot sang png : truyền 2 string là tên file dot và tên file pngthôi ( cái này dùng
    // thư viện
    public static void writePngFile(String link_dot_file, String link_png_file) {
        Runtime rt = Runtime.getRuntime();
        try {
            Process pr = rt.exec("dot -Tpng " + FormatPath.formatPath(dot_source) + link_dot_file + " -o " + FormatPath.formatPath(png_source) + link_png_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // bài 1 :  đó là truyền vào graph, sau đó viết file dot sau đ chuyển sang file png ó
    // tạo ra hình thôi


    public static void ex1(Graph graph) {
        graph.writeDotFile(FormatPath.formatPath("ex1\\") + "ex1.dot");
        writePngFile(FormatPath.formatPath("ex1\\") + "ex1.dot", FormatPath.formatPath("ex1\\") + "ex1.png");
    }
    // bài 2 này là truyền vào graph điểm bdau và kết thúc
    // tìm đường đi. nếu không có đường đi nào thì in ra not found
    // có thì in ra list đường đi
    public static void ex2(Graph graph, String start, String end) {
        if (graph.findAllPaths(start, end).size() == 0) {
            System.out.println("NOT FOUND!");
            return;
        }
        System.out.println(graph.findAllPaths(start, end));
    }
    // bài 3 là truyền vào graph, điểm đầu và điểm cuối
    public static void ex3(Graph graph, String start, String end) {
        File index_dot = new File(FormatPath.formatPath("output\\dot\\ex3"));
        File index_png = new File(FormatPath.formatPath("output\\png\\ex3"));
        // lấy ra list file dot và png ở lần chạy trước xong rồi xoá hết đi, khi chạy lần mới thì những
        // file lần mới sẽ sinh
        String[] entries_dot = index_dot.list();
        String[] entries_png = index_png.list();
        for (String s : entries_dot) {
            File currentFile = new File(index_dot.getPath(), s);
            // xoá file dot
            currentFile.delete();
        }
        for (String s : entries_png) {
            File currentFile = new File(index_png.getPath(), s);
            // xoá file png
            currentFile.delete();
        }

        // chạy for cho những đường tìm được từ điểm đầu đến điểm cuối

        for (List<String> l : graph.findAllPaths(start, end)) {
            // name là tên cho file dot và png theo định dạng đường đi của chúng .dot hoặc .png
            String name = l.toString().replace(", ", "_");
            // viết file dot nhưng để tạo màu cho đường đi tìm thấy ấy
            graph.writeDotFile(FormatPath.formatPath("ex3\\") + name + ".dot", l);
            // chuyển đổi file dot->png
            writePngFile(FormatPath.formatPath("ex3\\") + name + ".dot", FormatPath.formatPath("ex3\\") + name + ".png");
        }
    }

    public static void main(String[] args) {
        // trước khi chạy thì tải thư viện graphviz về :v
        String start = "start";
        String end = "end";
        // tạo graph
        Graph graph = new Graph();
        // đọc file đầu vào
        readFile("input.txt", graph);
        // làm câu 1, 2 ,3
        ex1(graph);
        ex2(graph, start, end);
        ex3(graph, start, end);

    }
}
