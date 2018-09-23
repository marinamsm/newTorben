package main;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import error.CompilerError;
import java_cup.runtime.ComplexSymbolFactory;
import parse.Lexer;
import parse.Parser;
import parse.Terminals;

// main
public class Driver {

   public static void main(String[] args) {
      Reader input = null;
      final String name;
      try {
          System.out.println("MAIN");
         // set the input (source code) to be compiled
         if (args.length == 0) {
             System.out.println("ENTRADA");
            name = "unknown";
            input = new InputStreamReader(System.in);
         }
         else {
            name = args[0];
            input = new FileReader(name);
         }
         lexicalAnalysis(name, input);
      }
      catch (CompilerError e) {
         System.out.println(e.getMessage());
         System.exit(3);
      }
      catch (IOException e) {
         System.out.println(e.getMessage());
         System.exit(2);
      }
      catch (Exception e) {
         System.out.println(e.getMessage());
         e.printStackTrace();
         System.exit(3);
      }
      finally {
         // closes the input file
         if (input instanceof FileReader)
            try {
               input.close();
            }
            catch (IOException e) {
               System.out.println(e.getMessage());
               System.exit(4);
            }
      }
   }

   private static void lexicalAnalysis(String name, Reader input) throws IOException {
       System.out.println("AEEEEEEEEEEEEEEEEEEE");
      final Lexer lexer = new Lexer(input, name);
      ComplexSymbolFactory.ComplexSymbol tok;
      do {
         tok = (ComplexSymbolFactory.ComplexSymbol) lexer.next_token();
         System.out.println(Terminals.dumpTerminal(tok));
      } while (tok.sym != Terminals.EOF);
   }
}
