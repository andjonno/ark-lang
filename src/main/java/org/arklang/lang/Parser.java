package org.arklang.lang;

import java.util.ArrayList;
import java.util.List;

import static org.arklang.lang.TokenType.*;

public class Parser {

  private static class ParseError extends RuntimeException {}
  private final List<Token> tokens;
  private int current = 0;

  Parser(List<Token> tokens) {
    this.tokens = tokens;
  }

  List<Stmt> parse() {
    List<Stmt> expressions = new ArrayList<>();

    try {
      while (!isAtEnd()) {
        expressions.add(declaration());
      }
    } catch (ParseError e) {
      return null;
    }

    return expressions;
  }

  private Stmt declaration() {
    if (match(LET)) return varDeclaration();

    return statement();
  }

  private Stmt statement() {

    if (match(IF)) return ifStatement();
    if (match(PRINT)) return printStatement();
    if (match(LEFT_BRACE)) return block();

    return expressionStmt();
  }

  private Stmt expressionStmt() {
    return new Stmt.Expression(assignment());
  }

  private Expr assignment() {
    Expr expr = expression();

    if (match(EQUAL)) {
      Token equals = previous();
      Expr value = assignment();
      if (expr instanceof Expr.Variable) {
        return new Expr.Assign(((Expr.Variable) expr).name, value);
      }

      error(equals, "Invalid assignment target.");
    }

    return expr;
  }

  private Expr expression() {
    while (match(LEFT_PAREN)) {
      Expr expr = binary();
      consume(RIGHT_PAREN, "Expect ')' after expression.");
      return expr;
    }
    return unary();
  }

  private Expr binary() {
    while (match(OR, AND, BANG_EQUAL, EQUAL_EQUAL, GREATER, GREATER_EQUAL,
        LESS, LESS_EQUAL, MINUS, PLUS, SLASH, STAR, STAR_STAR, PERCENT,
        AMPERSAND, CARET, LEFT_SHIFT, RIGHT_SHIFT, U_RIGHT_SHIFT, PIPE)) {
      Token operator = previous();
      Expr left = expression();
      Expr right = expression();
      return new Expr.Binary(operator, left, right);
    }
    return operation();
  }

  private Expr unary() {
    if (match(BANG, MINUS, TILDE)) {
      Token operator = previous();
      Expr right = unary();
      return new Expr.Unary(operator, right);
    }

    return primary();
  }

  private Expr operation() {
    if (match(IDENTIFIER)) {
      Token name = previous();
      List<Expr> arguments = new ArrayList<>();
      while (!check(RIGHT_PAREN)) {
        arguments.add(argument());
      }
      return new Expr.Operation(name, arguments);
    }

    return primary();
  }

  private Expr argument() {
    if (check(LAMBDA)) {
      // parse lambda expr
      // return lambda();
    }
    return expression();
  }

  private Expr primary() {
    if (match(FALSE)) return new Expr.Literal(false);
    if (match(TRUE)) return new Expr.Literal(true);
    if (match(NIL)) return new Expr.Literal(null);

    if (match(INT, DOUBLE, STRING, CHAR)) {
      return new Expr.Literal(previous().literal);
    }

    if (match(IDENTIFIER)) {
      return new Expr.Variable(previous());
    }

    throw error(peek(), "Expect expression.");
  }

  private Stmt varDeclaration() {
    Token name = consume(IDENTIFIER, "Expect variable name.");

    Expr initializer = null;
    if (match(EQUAL)) {
      initializer = expression();
    }

    return new Stmt.Let(name, initializer);
  }

  /*
  Statement Functions
   */
  private Stmt ifStatement() {
    Expr condition = expression();
    Stmt thenBranch = statement();
    Stmt elseBranch = null;
    if (match(ELSE)) {
      elseBranch = statement();
    }
    return new Stmt.If(condition, thenBranch, elseBranch);
  }

  private Stmt printStatement() {
    Expr expr = expression();
    return new Stmt.Print(expr);
  }

  private Stmt block() {
    List<Stmt> statements = new ArrayList<>();

    while (!check(RIGHT_BRACE)) {
      statements.add(declaration());
    }

    consume(RIGHT_BRACE, "Expect '}' after block.");
    return new Stmt.Block(statements);
  }

  /*
  Helper methods for Parser.
   */

  private boolean isAtEnd() {
    return peek().type == EOF;
  }

  private Token peek() {
    return tokens.get(current);
  }

  private Token previous() {
    return tokens.get(current - 1);
  }

  private boolean match(TokenType... types) {
    for (TokenType type : types) {
      if (check(type)) {
        advance();
        return true;
      }
    }

    return false;
  }

  private boolean check(TokenType tokenType) {
    if (isAtEnd()) return false;
    return peek().type == tokenType;
  }

  private Token advance() {
    if (!isAtEnd()) current++;
    return previous();
  }

  private Token consume(TokenType type, String message) {
    if (check(type)) return advance();

    throw error(peek(), message);
  }

  private ParseError error(Token token, String message) {
    Ark.error(token, message);
    return new ParseError();
  }
}
