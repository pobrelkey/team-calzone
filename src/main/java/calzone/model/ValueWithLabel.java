package calzone.model;

public class ValueWithLabel {
    private String label;
    private Object value;

    public ValueWithLabel(String label, Object value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public Object getValue() {
        return value;
    }
}
