package absyn;

import env.Env;
import io.vavr.collection.Tree;
import parse.Loc;
import types.TypeBool;
import types.Type;
import types.VOID;

public class ExpBool extends Exp {
   public final boolean value;

   public ExpBool(Loc loc, boolean value) {
      super(loc);
      this.value = value;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of(annotateType("ExpBool: " + value));
   }

   @Override
   protected Type semantic_(Env env) {
      return TypeBool.T;
   }

}
