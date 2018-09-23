package types;

public class TypeBool extends Type{

    public static final TypeBool T = new TypeBool();

    private TypeBool() {

    }

    @Override
    public String toString() {
        return "bool";
    }
}


