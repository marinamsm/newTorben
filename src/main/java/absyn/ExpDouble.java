package absyn;

import env.Env;
import io.vavr.collection.Tree;
import parse.Loc;
import types.TypeDouble;
import types.Type;

public class ExpDouble extends Exp {
    public final double value;

    public ExpDouble(Loc loc, double value) {
        super(loc);
        this.value = value;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of(annotateType("ExpDouble: " + value));
    }

    @Override
    protected Type semantic_(Env env) {
        return TypeDouble.T;
    }
}
