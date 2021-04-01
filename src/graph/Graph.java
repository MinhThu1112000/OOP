package graph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Graph {
    private final String dot_source = "output\\dot\\";
    private Map<Vertex, List<Vertex>> adjVertices;

    public Graph() {
        this.adjVertices = new HashMap<>();
    }

    public Map<Vertex, List<Vertex>> getAdjVertices() {
        return this.adjVertices;
    }

    public void addVertex(String label) {
        this.adjVertices.putIfAbsent(new Vertex(label), new ArrayList<>());
    }

    public void removeVertex(String label) {
        Vertex v = new Vertex(label);
        this.adjVertices.values().forEach(e -> e.remove(v));
        this.adjVertices.remove(new Vertex(label));
    }

    public void addEdge(String label1, String label2) {
        Vertex v1 = new Vertex(label1);
        Vertex v2 = new Vertex(label2);
        this.adjVertices.get(v1).add(v2);
    }

    public void removeEdge(String label1, String label2) {
        Vertex v1 = new Vertex(label1);
        Vertex v2 = new Vertex(label2);
        List<Vertex> eV1 = this.adjVertices.get(v1);
        List<Vertex> eV2 = this.adjVertices.get(v2);
        if (eV1 != null)
            eV1.remove(v2);
        if (eV2 != null)
            eV2.remove(v1);
    }

    List<Vertex> getAdjVertices(String label) {
        return this.adjVertices.get(new Vertex(label));
    }

    public static Set<String> depthFirstTraversal(Graph graph, String root) {
        Set<String> visited = new LinkedHashSet<String>();
        Stack<String> stack = new Stack<String>();
        stack.push(root);
        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                visited.add(vertex);
                for (Vertex v : graph.getAdjVertices(vertex)) {
                    stack.push(v.getLabel());
                }
            }
        }
        return visited;
    }

    public static Set<String> breadthFirstTraversal(Graph graph, String root) {
        Set<String> visited = new LinkedHashSet<String>();
        Queue<String> queue = new LinkedList<String>();
        queue.add(root);
        visited.add(root);
        while (!queue.isEmpty()) {
            String vertex = queue.poll();
            for (Vertex v : graph.getAdjVertices(vertex)) {
                if (!visited.contains(v.getLabel())) {
                    visited.add(v.getLabel());
                    queue.add(v.getLabel());
                }
            }
        }
        return visited;
    }

    public void writeDotFile(String dotfile) {
        File myObj = new File(FormatPath.formatPath(this.dot_source) + dotfile);
        try {
            myObj.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter(FormatPath.formatPath(this.dot_source) + dotfile);
            myWriter.write("digraph myGraph {\n");
            for (Map.Entry<Vertex, List<Vertex>> entry : this.getAdjVertices().entrySet()) {
                if (entry.getValue().size() > 0) {
                    for (Vertex v : entry.getValue()) {
                        myWriter.write("\t" + entry.getKey().getLabel() + " -> ");
                        myWriter.write(v.getLabel());
                        myWriter.write("\n");
                    }
                }
            }
            myWriter.write("}");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeDotFile(String dotfile, List<String> path) {
        List<Pair> listpath = new ArrayList<>();
        for (int i = 0; i < path.size() - 1; i++) {
            listpath.add(new Pair(path.get(i), path.get(i + 1)));
        }
        File myObj = new File(FormatPath.formatPath(dot_source)+ dotfile);
        try {
            myObj.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter(FormatPath.formatPath(dot_source) + dotfile);
            myWriter.write("digraph myGraph {\n");
            for (Map.Entry<Vertex, List<Vertex>> entry : this.getAdjVertices().entrySet()) {
                if (entry.getValue().size() > 0) {
                    for (Vertex v : entry.getValue()) {
                        myWriter.write("\t" + entry.getKey().getLabel() + " -> ");
                        myWriter.write(v.getLabel());
                        if (listpath.contains(new Pair(entry.getKey().getLabel(), v.getLabel()))) {
                            myWriter.write(" [color = red]");
                            listpath.remove(new Pair(entry.getKey().getLabel(), v.getLabel()));
                        }
                        myWriter.write("\n");
                    }
                }
            }
            myWriter.write("}");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class NodeAllPath{
        private String label;
        private List<String> visited;

        public NodeAllPath(String label, List<String> visited) {
            this.label = label;
            this.visited = visited;
        }

        @Override
        public String toString() {
            return "NodeAllPath{" +
                    "label='" + label + '\'' +
                    ", visited=" + visited +
                    '}';
        }
    }

    public List<List<String>> findAllPaths(String start, String end) {
        List<List<String>> result = new ArrayList<>();
        Stack<NodeAllPath> stack = new Stack<NodeAllPath>();
        stack.push(new NodeAllPath(start, Arrays.asList(start)));
        while(!stack.isEmpty()) {
            NodeAllPath node_popped = stack.pop();
            for (Vertex v: this.getAdjVertices(node_popped.label)) {
                if(!node_popped.visited.contains(v.getLabel())) {
                    List<String> res_path = new ArrayList<>(node_popped.visited);
                    res_path.add(v.getLabel());
                    if (v.getLabel().equals(end)) {
                        result.add(res_path);
                    } else {
                        stack.push(new NodeAllPath(v.getLabel(), res_path));
                    }
                }
            }
        }
        return result;
    }
}
