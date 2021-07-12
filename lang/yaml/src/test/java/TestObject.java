public class TestObject {
    private final String string;
    private final int number;

    public TestObject(String string, int number) {
        this.string = string;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public String getString() {
        return string;
    }

    @Override
    public String toString() {
        return getString() + " and " + getNumber();
    }
}
