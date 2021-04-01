import graph.FormatPath;
import graph.Graph;

import java.io.*;
import java.util.List;

public class Main {
    private static final String input_source = "input\\";
    private static final String png_source = "output\\png\\";
    private static final String dot_source = "output\\dot\\";


    public static void readFile(String filename, Graph graph) {
        try {
            FileReader fr = new FileReader(FormatPath.formatPath(input_source) + filename);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null){
                String[] vertex = line.split(" ");
                for(String v: vertex) {
                    graph.addVertex(v);
                }
                for(int i = 1; i < vertex.length; i++) {
                    graph.addEdge(vertex[0], vertex[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writePngFile(String link_dot_file, String link_png_file) {
        Runtime rt = Runtime.getRuntime();
        try {
            Process pr = rt.exec("dot -Tpng " + FormatPath.formatPath(dot_source) + link_dot_file + " -o " + FormatPath.formatPath(png_source) + link_png_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ex1(Graph graph) {
        graph.writeDotFile(FormatPath.formatPath("ex1\\") + "ex1.dot");
        writePngFile(FormatPath.formatPath("ex1\\") + "ex1.dot", FormatPath.formatPath("ex1\\") + "ex1.png");
    }
    public static void ex2(Graph graph, String start, String end) {
        System.out.println(graph.findAllPaths(start, end));
    }
    public static void ex3(Graph graph, String start, String end) {
        File index_dot = new File(FormatPath.formatPath("output\\dot\\ex3"));
        File index_png = new File(FormatPath.formatPath("output\\png\\ex3"));
        String[] entries_dot = index_dot.list();
        String[] entries_png = index_png.list();
        for(String s: entries_dot) {
            File currentFile = new File(index_dot.getPath(),s);
            currentFile.delete();
        }
        for(String s: entries_png) {
            File currentFile = new File(index_png.getPath(),s);
            currentFile.delete();
        }


        for(List<String> l: graph.findAllPaths(start, end)) {
            String name = l.toString().replace(", ", "_");
            graph.writeDotFile(FormatPath.formatPath("ex3\\") + name + ".dot", l);
            writePngFile(FormatPath.formatPath("ex3\\") + name + ".dot", FormatPath.formatPath("ex3\\") + name + ".png");
        }
    }
    public static void main(String[] args){
        String start = "start";
        String end = "end";
        Graph graph = new Graph();
        readFile("input.txt", graph);
        ex1(graph);
        ex2(graph, start, end);
        ex3(graph, start, end);
//        System.out.println("Mac OS X".equals(System.getProperty("os.name")));
    }
}
