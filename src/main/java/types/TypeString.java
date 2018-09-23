package types;

public class TypeString extends Type {

    public static final TypeString T = new TypeString();

    private TypeString() {
    }

    @Override
    public String toString() {
        return "string";
    }
}
