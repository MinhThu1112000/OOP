package graph;

import java.util.Objects;

public class Vertex {
    // label : thuộc tính: tên của đỉnh
    private String label;
    Vertex(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    //override equals của lớp object ( so sánh 2 đỉnh có giống nhau ko )
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return Objects.equals(label, vertex.label);
    }
    // override hashCode của lớp Object
    @Override
    public int hashCode() {
        return Objects.hash(label);
    }
    // override ToString của lớp Object
    @Override
    public String toString() {
        return "graph.Vertex{" +
                "label='" + label + '\'' +
                '}';
    }
}
