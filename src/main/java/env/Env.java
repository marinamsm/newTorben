package env;

import io.vavr.collection.List;
import types.*;

public class Env {

  public Table<Type> tenv;
  public Table<Entry> venv;

  public Env() {
    tenv = new Table<Type>();
    put(tenv, "void", VOID.T);
    put(tenv, "int", TypeInt.T);

    venv = new Table<Entry>();
     put(venv, "minint", new VarEntry(TypeInt.T));
     put(venv, "maxint", new VarEntry(TypeInt.T));
     put(venv, "printint", new FunEntry(List.of(TypeInt.T), VOID.T));
     put(venv, "not", new FunEntry(List.of(TypeBool.T), TypeBool.T));
     put(venv, "substring", new FunEntry(List.of(TypeString.T, TypeInt.T, TypeInt.T), TypeString.T));
  }

  @Override
  public String toString() {
    return "Env{" +
           "tenv=" + tenv +
           ", venv=" + venv +
           '}';
  }

  private static <E> void put(Table<E> table, String name, E value) {
    table.put(name.intern(), value);
  }

}
