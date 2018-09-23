package absyn;

import env.Env;
import types.Type;

public enum TypeName {
   VOID {
      @Override
      public Type semantic(Env env) {
         return types.VOID.T;
      }
   },

   TypeBool {
      @Override
      public Type semantic(Env env) {
         return types.TypeBool.T;
      }
   },

   TypeInt {
      @Override
      public Type semantic(Env env) {
         return types.TypeInt.T;
      }
   },

   TypeDouble {
      @Override
      public Type semantic(Env env) {
         return types.TypeDouble.T;
      }
   },

   TypeString {
      @Override
      public Type semantic(Env env) {
         return types.TypeString.T;
      }
   };

   public abstract Type semantic(Env env);
}
