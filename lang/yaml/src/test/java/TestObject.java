import java.util.List;
import java.util.Objects;

public class TestObject {
    private final String string;
    private final int number;
    private final List<TestObject> list;

    public TestObject(String string, int number, List<TestObject> list) {
        this.string = string;
        this.number = number;
        this.list = list;
    }

    public int getNumber() {
        return number;
    }

    public String getString() {
        return string;
    }

    @Override
    public String toString() {
        return getString() + " and " + getNumber() + " and " + list;
    }

    public List<TestObject> getList() {
        return list;
    }
}
