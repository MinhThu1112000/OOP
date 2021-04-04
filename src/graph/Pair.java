package graph;

import java.util.Objects;
// tạo đối tượng pair
public class Pair {
    String str1;
    String str2;

    public Pair(String str1, String str2) {
        this.str1 = str1;
        this.str2 = str2;
    }

    public String getStr1() {
        return str1;
    }

    public String getStr2() {
        return str2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return Objects.equals(str1, pair.str1) && Objects.equals(str2, pair.str2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(str1, str2);
    }
}
