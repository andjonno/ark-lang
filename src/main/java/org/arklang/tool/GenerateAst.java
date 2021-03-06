package org.arklang.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.err.println("Usage: generate_ast <output directory>");
      System.exit(1);
    }
    String outputDir = args[0];
    defineAst(outputDir, "Expr", Arrays.asList(
        "Assign   : Token name, Expr value",
        "Operation: Token token, Expr target, List<Expr> arguments",
        "Binary   : Token operator, Expr left, Expr right",
        "Unary    : Token operator, Expr right",
        "Literal  : Object value",
        "Variable : Token name",
        "Ternary  : Expr condition, Expr expr1, Expr expr2",
        "Lambda   : Token name, List<Token> parameters, List<Stmt> body",
        "Array    : Token bracket, List<Expr> items",
        "Str      : Token token, String str",
        "Char     : Token token, Character c",
        "IndexGet : Expr indexee, Token token, Expr index",
        "IndexSet : Expr indexee, Token token, Expr index, Expr value",
        "Range    : Expr lower, Expr upper, Token token, boolean closed"
        ));
    defineAst(outputDir, "Stmt", Arrays.asList(
        "Block      : List<Stmt> statements",
        "Expression : Expr expression",
        "If         : Expr condition, Stmt thenBranch, Stmt elseBranch",
        "While      : Expr condition, Stmt body",
        "ForIn      : Token token, Token itemIterator, Token indexIterator, Expr enumerable, Stmt body",
        "Print      : Expr expression",
        "Send       : Token keyword, Expr value",
        "Let        : List<Token> names, List<Expr> initializers",
        "Break      : Token keyword"
    ));
    System.out.println("Done writing to " + outputDir);
  }

  private static void defineAst(
          String outputDir, String baseName, List<String> types)
          throws IOException {
    String path = outputDir + "/" + baseName + ".java";
    PrintWriter writer = new PrintWriter(path, "UTF-8");

    writer.println("package org.arklang.lang;");
    writer.println("");
    writer.println("import java.util.List;");
    writer.println("");
    writer.println("abstract class " + baseName + " {");

    defineVisitor(writer, baseName, types);

    // The AST classes.
    for (String type : types) {
      String className = type.split(":")[0].trim();
      String fields = type.split(":")[1].trim();
      defineType(writer, baseName, className, fields);
    }

    // The base accept() method.
    writer.println("");
    writer.println("  abstract <R> R accept(Visitor<R> visitor);");

    writer.println("}");
    writer.close();
  }

  private static void defineType(
          PrintWriter writer, String baseName,
          String className, String fieldList) {
    writer.println("  static class " + className + " extends " +
            baseName + " {");

    // Constructor.
    writer.println("    " + className + "(" + fieldList + ") {");

    // Store parameters in fields.
    String[] fields = fieldList.split(", ");
    for (String field : fields) {
      String name = field.split(" ")[1];
      writer.println("      this." + name + " = " + name + ";");
    }

    writer.println("    }");

    // Visitor pattern.
    writer.println();
    writer.println("    <R> R accept(Visitor<R> visitor) {");
    writer.println("      return visitor.visit" + className + baseName + "(this);");
    writer.println("    }");

    // Fields.
    writer.println();
    for (String field : fields) {
      writer.println("    final " + field + ";");
    }

    writer.println("  }");
  }

  private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
    writer.println("  interface Visitor<R> {");

    for (String type: types) {
      String typeName = type.split(":")[0].trim();
      writer.println("    R visit" + typeName + baseName + "(" +
              typeName + " " + baseName.toLowerCase() + ");");
    }

    writer.println("  }");
  }
}
