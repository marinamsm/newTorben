package types;

public class TypeInt extends Type {

    public static final TypeInt T = new TypeInt();

    private TypeInt() {
    }

    @Override
    public String toString() {
        return "int";
    }
}
