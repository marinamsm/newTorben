package absyn;

import env.Env;
import io.vavr.collection.Tree;
import parse.Loc;
import types.TypeBool;
import types.TypeInt;
import types.TypeDouble;
import types.Type;

import static error.ErrorHelper.fatal;
import static error.ErrorHelper.typeMismatch;

public class ExpBin extends Exp {
   public enum Operator {
      PLUS, MINUS, TIMES, DIV, MOD, EXP,
      EQ, NE, GT, GE, LT, LE,
      AND, OR
   }

   public final Operator op;
   public final Exp left;
   public final Exp right;

   public ExpBin(Loc loc, Operator op, Exp left, Exp right) {
      super(loc);
      this.op = op;
      this.left = left;
      this.right = right;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of(annotateType("ExpBin: " + op), left.toTree(), right.toTree());
   }

   @Override
   protected Type semantic_(Env env) {
      left.semantic(env);
      right.semantic(env);
      switch (op) {
         case PLUS:
         case MINUS:
         case TIMES:
         case DIV:
         case MOD:
            if (! left.type.is(TypeInt.T))
               throw typeMismatch(left.loc, left.type, TypeInt.T);
            if (! right.type.is(TypeInt.T))
               throw typeMismatch(right.loc, right.type, TypeInt.T);
            return TypeInt.T;

         // COMPLETE THE MISSING BINARY OPERATORS
         case EXP:
            if (!(left.type.is(TypeInt.T))) {
                throw typeMismatch(left.loc, left.type, TypeInt.T);
            }
            if (! right.type.is(TypeInt.T))
               throw typeMismatch(right.loc, right.type, TypeInt.T);
            return TypeInt.T;
         default:
            throw fatal("incomplete semantic analisys of binary operation");
      }
   }
}
