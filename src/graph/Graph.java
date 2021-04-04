package graph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Graph {
    private final String dot_source = "output\\dot\\";
    //map là 1 danh sách gồm key và value ( key : đỉnh, value : 1 danh sách đỉnh mà từ key nối
    // từng đỉnh trong danh sách)
    //ví dụ 1->2 1->3 thì key : 1 còn value là 2, 3
    private Map<Vertex, List<Vertex>> adjVertices;

    public Graph() {
        this.adjVertices = new HashMap<>();
    }

    public Map<Vertex, List<Vertex>> getAdjVertices() {
        return this.adjVertices;
    }

    public void addVertex(String label) {
        //putIfAbsent : thêm đỉnh và thêm những đỉnh liền kề với nó tạo thành vector kề
        // ví dụ 1->2 1->3 thì 1 là tên đỉnh còn 2,3 là đỉnh liền
        this.adjVertices.putIfAbsent(new Vertex(label), new ArrayList<>());
    }
    // xoá đỉnh
    public void removeVertex(String label) {
        Vertex v = new Vertex(label);
        //xoá những đỉnh liền kề với nó
        this.adjVertices.values().forEach(e -> e.remove(v));
        //  xong mới được xoá chính nó
        this.adjVertices.remove(new Vertex(label));
    }
    //thêm cạnh từ label1 -> label2 truyền vào tên của 2 đỉnh
    public void addEdge(String label1, String label2) {
        // khởi tạo 2 đối tượng đỉnh truyền vào tên đỉnh
        Vertex v1 = new Vertex(label1);
        Vertex v2 = new Vertex(label2);
        //lấy ra key v1 xong add v2 vào value của v1
        this.adjVertices.get(v1).add(v2);
    }
    //xoá cạnh
    public void removeEdge(String label1, String label2) {
        Vertex v1 = new Vertex(label1);
        Vertex v2 = new Vertex(label2);
        //lấy ra value của v1 và v2 : những đỉnh mà v1, v2 liên kết với
        List<Vertex> eV1 = this.adjVertices.get(v1);
        List<Vertex> eV2 = this.adjVertices.get(v2);
        // nếu ev1 mà khác null nếu tồn tại đỉnh v2 trong value của v1 thì xoá v2
        if (eV1 != null)
            eV1.remove(v2);
        // nếu ev2 mà khác null nếu tồn tại đỉnh v1 trong value của v2 thì xoá v1 đi
        if (eV2 != null)
            eV2.remove(v1);
    }

    List<Vertex> getAdjVertices(String label) {
        return this.adjVertices.get(new Vertex(label));
    }
    // duyệt theo chiều sâu để lấy tất cả các đỉnh ( k cần đọc vì không dùng :v )
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
    // duyệt theo chiều rộng để lấy tâts car các đỉnh ( không cần đọc cũng được :v vì không dùng )
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

    // viết file dot( file dot là file có thể dùng câu lệnh trong terminal hoặc command line
    // để chuyển sang hình  ảnh câu lênh vd : dot -Tpng ex1.dot -o ex1.png )
    //formatPath.formatPath : chỉ là 1 hàm ở trong class FormatPath để format đường link để có thể chạy ở cả 2 hệ điều hành là macos và windows
    public void writeDotFile(String dotfile) {
        try {
            // cái này là tạo file dot

            FileWriter myWriter = new FileWriter(FormatPath.formatPath(this.dot_source) + dotfile);
            myWriter.write("digraph myGraph {\n");
            // chạy vòng lặp của map tạo ở trên ( chạy từng key và value của chúng)
            for (Map.Entry<Vertex, List<Vertex>> entry : this.getAdjVertices().entrySet()) {
                // nếu mảng value của key rỗng thì thôi
                // còn nếu mảng value của key > 0  thì chạy for cho từng mảng value của từng  key
                // viết vào file theo định dạng là key đến từng value xong xuống dòng ví dụ key là 1 và mảng
                // value là 2, 3 thì viết vào file sẽ là
                // 1 -> 2
                // 1 -> 3
                if (entry.getValue().size() > 0) {
                    for (Vertex v : entry.getValue()) {
                        myWriter.write("\t" + entry.getKey().getLabel() + " -> ");
                        myWriter.write(v.getLabel());
                        myWriter.write("\n");
                    }
                }
            }
            // kết thúc là dấu  }
            myWriter.write("}");
            // đóng file
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // viết file dot nhưng mà file dot này là để dùng cho câu 3 tô màu cho đường đi
    // tìm được từ đỉnhkia này đến đỉnh
    // truyền vào tên file dot và 1 list đường đi từ đỉnh này đến đỉnh kia ví dụ tìm được đường đi từ 1 đến 4
    // là [1, 2, 4]  thì list<String> path = {1, 2, 4}

    public void writeDotFile(String dotfile, List<String> path) {
        //Pair là 1 đối tượng được tạo ra bao gồm 2 thuộc tính là string1 và string2
        // Tạo 1 list Pair để phân chia từng đoạn đường đi
        // ví dụ 1 đoạn đường từ 1 đến 4 thì là 1 đến 2 đến 4 thì chia làm 2 đoạn là 1 đến 2 và 2 đến 4
        // thì listPath sẽ bao gồm 2 đối tượng Pair .
        // đối tượng pair đầu tiên là bao gồm str1 là 1, str2 là 2
        // đối tượng Pair 2 là bao gồm str1 là 2, str2 là 4
        // chạy vòng lặp for cho path và lấy 2 đỉnh liên tiếp nhau( tạo thành đối tượng pair) rồi add vào listpath
        List<Pair> listpath = new ArrayList<>();
        for (int i = 0; i < path.size() - 1; i++) {
            listpath.add(new Pair(path.get(i), path.get(i + 1)));
        }
        // viết vào file dot
        try {
            FileWriter myWriter = new FileWriter(FormatPath.formatPath(dot_source) + dotfile);
            myWriter.write("digraph myGraph {\n");
            // chạy vòng lặp của map ở trên ( chạy từng key ứng với từng value của chúng)
            //
            for (Map.Entry<Vertex, List<Vertex>> entry : this.getAdjVertices().entrySet()) {
                // nếu mảng value  > 0 thì chạy for cho mảng value
                // viết vào file dot theo định dạng key ->  từng value
                if (entry.getValue().size() > 0) {
                    for (Vertex v : entry.getValue()) {
                        myWriter.write("\t" + entry.getKey().getLabel() + " -> ");
                        myWriter.write(v.getLabel());
                        // ví dụ đường đi từ 1, 2, 4 thì listpath là  (1,2) && ( 2,4)
                        // kiểm tra xem listPath vừa tạo ở trên có chứa cái nào bao gồm ( key , 1 phần tử ở trong mảng value hay không)
                        // nếu có thì viết thêm vào dòng bên cạnh của key -> value là [color = red] thì nó sẽ tô màu đường
                        // đi ấy là màu đỏ
                        //sau đó xoá luôn cái đối tượng pair tồn tại ở trong listPath ấy đi luôn để tránh phải duyệt nhiều lần tốn tgian :
                        if (listpath.contains(new Pair(entry.getKey().getLabel(), v.getLabel()))) {
                            myWriter.write(" [color = red]");
                            listpath.remove(new Pair(entry.getKey().getLabel(), v.getLabel()));
                        }
                        // xuống dòng

                        myWriter.write("\n");
                    }
                }
            }
            // đóng ngoặc và đóng file
            myWriter.write("}");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // đối tượng này dùng để làm câu tìm đường đi ở bài 3. không cần hiểu long bảo nó phụ trách phần này

    class NodeAllPath {
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
        while (!stack.isEmpty()) {
            NodeAllPath node_popped = stack.pop();
            for (Vertex v : this.getAdjVertices(node_popped.label)) {
                if (!node_popped.visited.contains(v.getLabel())) {
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
