package types;

public class TypeDouble extends Type {

    public static final TypeDouble T = new TypeDouble();

    private TypeDouble() {
    }

    @Override
    public String toString() {
        return "double";
    }
}
