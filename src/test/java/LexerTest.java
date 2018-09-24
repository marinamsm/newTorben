import error.CompilerError;
import io.vavr.collection.List;
import java_cup.runtime.Symbol;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Rule;
import org.junit.Test;
import parse.Lexer;
import parse.Terminals;

import java.io.IOException;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertArrayEquals;

public class LexerTest {

   private String run(String input) throws IOException {
      Lexer lexer = new Lexer(new StringReader(input), "unknown");
      Symbol token;
      StringBuilder builder = new StringBuilder();
      List<String> list = List.empty();
      do {
         token = lexer.next_token();
         builder.append(Terminals.dumpTerminal(token)).append('\n');
         list = list.append(Terminals.dumpTerminal(token));
      } while (token.sym != Terminals.EOF);
      return builder.toString();
      //return list;
   }

   private void trun(String input, String... output) throws IOException {
      StringBuilder builder = new StringBuilder();
      for (String x : output)
         builder.append(x).append('\n');
      softly.assertThat(run(input))
         .as("%s", input)
         .isEqualTo(builder.toString());
   }

   private void erun(String input, String message) throws IOException {
      softly.assertThatThrownBy(() -> run(input))
         .as("%s", input)
         .isInstanceOf(CompilerError.class)
         .hasMessage(message);
   }

   @Rule
   public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

   @Test
   public void lexerTest1() throws IOException {
       trun("bool f(string s) =\ns = \"pedro\" || true", "1:1-1:5 TypeBool\n1:6-1:7 ID(f)\n1:7-1:8 LPAREN\n1:8-1:14 TypeString\n1:15-1:16 ID(s)\n1:16-1:17 RPAREN\n1:18-1:19 EQ\n2:1-2:2 ID(s)\n2:3-2:4 EQ\n2:5-2:12 LITSTRING(pedro)\n2:13-2:15 OR\n2:16-2:20 LITBOOL(true)\n2:20-2:20 EOF");
       trun("int main() =\n" +
               "let a = 873 in\n" +
               "let b = a ^ 3 in\n" +
               "(a + b)/2", "1:1-1:4 TypeInt\n1:5-1:9 ID(main)\n1:9-1:10 LPAREN\n1:10-1:11 RPAREN\n1:12-1:13 EQ\n2:1-2:4 LET\n2:5-2:6 ID(a)\n2:7-2:8 EQ\n2:9-2:12 LITINT(873)\n2:13-2:15 IN\n3:1-3:4 LET\n3:5-3:6 ID(b)\n3:7-3:8 EQ\n3:9-3:10 ID(a)\n3:11-3:12 EXP\n3:13-3:14 LITINT(3)\n3:15-3:17 IN\n4:1-4:2 LPAREN\n4:2-4:3 ID(a)\n4:4-4:5 PLUS\n4:6-4:7 ID(b)\n4:7-4:8 RPAREN\n4:8-4:9 DIV\n4:9-4:10 LITINT(2)\n4:10-4:10 EOF");
       trun(" int main() =\n" +
               "# program execution starts here\n" +
               "let PesoPessoa = 45 in\n" +
               "print_int(PesoPessoa + 2)", "1:2-1:5 TypeInt\n1:6-1:10 ID(main)\n1:10-1:11 LPAREN\n1:11-1:12 RPAREN\n1:13-1:14 EQ\n2:1-2:32 LCOMMENT\n3:1-3:4 LET\n3:5-3:15 ID(PesoPessoa)\n3:16-3:17 EQ\n3:18-3:20 LITINT(45)\n3:21-3:23 IN\n4:1-4:10 ID(print_int)\n4:10-4:11 LPAREN\n4:11-4:21 ID(PesoPessoa)\n4:22-4:23 PLUS\n4:24-4:25 LITINT(2)\n4:25-4:26 RPAREN\n4:26-4:26 EOF");
       erun("int main() =\n" +
               "print(-2342,\n" +
               "56.7,\n" +
               "\"Letra \\064.\",\n" +
               "\"Escape \\k inválida\",\n" +
               "\"aspas\\\"internas\\\" e \\\\ barra\",\n" +
               "\"ouro \\n preto\",\n" +
               "\"bom dia)", "5:9-5:11 lexical error: invalid escape arguments in string literal");
      // whitespaces
      trun("    \t\n\n\n\t\r\n\r\n  ", "6:3-6:3 EOF");

      // comments
      trun("# a line comment\n", "1:1-1:17 LCOMMENT\n2:1-2:1 EOF");
      trun("# a line comment", "1:1-1:17 LCOMMENT\n1:17-1:17 EOF");
      trun("{# a block comment #}", "1:1-1:22 BCOMMENT\n1:22-1:22 EOF");
      trun("{# a\nmultiline\ncomment #}", "3:11-3:11 EOF");
      trun("{# begin ### end #}", "1:1-1:20 BCOMMENT\n1:20-1:20 EOF");
      trun("{# begin #### end #}", "1:1-1:21 BCOMMENT\n1:21-1:21 EOF");
      trun("{# begin ####}", "1:1-1:15 BCOMMENT\n1:15-1:15 EOF");
      trun("{# begin #####}", "1:1-1:16 BCOMMENT\n1:16-1:16 EOF");
      trun("{# outer {# inner #} outer #}", "1:1-1:30 BCOMMENT\n1:30-1:30 EOF");

      // punctuation
      trun(":=", "1:1-1:3 ASSIGN", "1:3-1:3 EOF");
      trun("=", "1:1-1:2 EQ", "1:2-1:2 EOF");
      trun("(", "1:1-1:2 LPAREN", "1:2-1:2 EOF");
      trun(")", "1:1-1:2 RPAREN", "1:2-1:2 EOF");
      trun(",", "1:1-1:2 COMMA", "1:2-1:2 EOF");
      trun("2^2", "1:1-1:2 LITINT(2)\n1:2-1:3 EXP\n1:3-1:4 LITINT(2)\n1:4-1:4 EOF");

      // operators
      trun("+", "1:1-1:2 PLUS", "1:2-1:2 EOF");
      trun("-", "1:1-1:2 MINUS", "1:2-1:2 EOF");
      trun("*", "1:1-1:2 TIMES", "1:2-1:2 EOF");
      trun("/", "1:1-1:2 DIV", "1:2-1:2 EOF");
      trun("%", "1:1-1:2 MOD", "1:2-1:2 EOF");
      trun("<>", "1:1-1:3 NE", "1:3-1:3 EOF");
      trun("<", "1:1-1:2 LT", "1:2-1:2 EOF");
      trun("<=", "1:1-1:3 LE", "1:3-1:3 EOF");
      trun(">", "1:1-1:2 GT", "1:2-1:2 EOF");
      trun("&&", "1:1-1:3 AND", "1:3-1:3 EOF");
      trun("||", "1:1-1:3 OR", "1:3-1:3 EOF");

      // boolean literals
      trun("true", "1:1-1:5 LITBOOL(true)", "1:5-1:5 EOF");
      trun("false", "1:1-1:6 LITBOOL(false)", "1:6-1:6 EOF");

      // integer literals
      trun("26342", "1:1-1:6 LITINT(26342)", "1:6-1:6 EOF");
      trun("0", "1:1-1:2 LITINT(0)", "1:2-1:2 EOF");
      //trun("+75"    , "1:1-1:4 LITINT(75)"    , "1:4-1:4 EOF");
      //trun("-75"    , "1:1-1:4 LITINT(-75)"   , "1:4-1:4 EOF");

      // string literals
      trun("\"A\"", "1:1-1:4 LITSTRING(A)", "1:4-1:4 EOF");
      trun("\"b\"", "1:1-1:4 LITSTRING(b)", "1:4-1:4 EOF");
      trun("\"*\"", "1:1-1:4 LITSTRING(*)", "1:4-1:4 EOF");
      trun("\" \"", "1:1-1:4 LITSTRING( )", "1:4-1:4 EOF");
      trun("\"\t\"", "1:1-1:4 LITSTRING(\t)", "1:4-1:4 EOF");
      trun("\"\\b\"", "1:1-1:5 LITSTRING(\b)", "1:5-1:5 EOF");
      trun("\"\\t\"", "1:1-1:5 LITSTRING(\t)", "1:5-1:5 EOF");
      trun("\"\\n\"", "1:1-1:5 LITSTRING(\n)", "1:5-1:5 EOF");
      trun("\"\\r\"", "1:1-1:5 LITSTRING(\r)", "1:5-1:5 EOF");
      trun("\"\\f\"", "1:1-1:5 LITSTRING(\f)", "1:5-1:5 EOF");
      trun("\"\\\"\"", "1:1-1:5 LITSTRING(\")", "1:5-1:5 EOF");
      trun("\"\\065\"", "1:1-1:7 LITSTRING(A)", "1:7-1:7 EOF");
      erun("\"\\x\"", "1:2-1:4 lexical error: invalid escape arguments in string literal");
      trun("\"ABC\"", "1:1-1:6 LITSTRING(ABC)", "1:6-1:6 EOF");
      erun("\"\n\"", "1:2-1:3 lexical error: invalid newline in string literal");

      // keywords
      trun("bool", "1:1-1:5 TypeBool", "1:5-1:5 EOF");
      trun("int", "1:1-1:4 TypeInt", "1:4-1:4 EOF");
      trun("string", "1:1-1:7 TypeString\n1:7-1:7 EOF");
      trun("if", "1:1-1:3 IF", "1:3-1:3 EOF");
      trun("then", "1:1-1:5 THEN", "1:5-1:5 EOF");
      trun("else", "1:1-1:5 ELSE", "1:5-1:5 EOF");
      trun("while", "1:1-1:6 WHILE", "1:6-1:6 EOF");
      trun("do", "1:1-1:3 DO", "1:3-1:3 EOF");
      trun("let", "1:1-1:4 LET", "1:4-1:4 EOF");
      trun("in", "1:1-1:3 IN", "1:3-1:3 EOF");

      // identifiers
      trun("nome", "1:1-1:5 ID(nome)", "1:5-1:5 EOF");
      trun("camelCase", "1:1-1:10 ID(camelCase)", "1:10-1:10 EOF");
      trun("with_underscore", "1:1-1:16 ID(with_underscore)", "1:16-1:16 EOF");
      trun("A1b2C33", "1:1-1:8 ID(A1b2C33)", "1:8-1:8 EOF");
      trun("set+", "1:1-1:4 ID(set)", "1:4-1:5 PLUS", "1:5-1:5 EOF");
      trun("45let", "1:1-1:3 LITINT(45)", "1:3-1:6 LET", "1:6-1:6 EOF");
      erun("_invalid", "1:1-1:2 lexical error: invalid character '_'");
   }

}
