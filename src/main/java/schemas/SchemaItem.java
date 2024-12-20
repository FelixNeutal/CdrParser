package schemas;

public class SchemaItem {
    private String key;
    private int value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SchemaItem{" +
                "key='" + key + '\'' +
                ", value=" + value +
                '}';
    }
}
