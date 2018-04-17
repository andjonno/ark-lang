package org.arklang.lang;

public enum TokenType {

  /*
  Syntax tokens
    */
  LEFT_PAREN, RIGHT_PAREN,
  LEFT_BRACE, RIGHT_BRACE,
  LEFT_BRACKET, RIGHT_BRACKET,
  COLON, PIPE, COMMA, DOT

  ,

  /*
  Operator tokens
   */
  RIGHT_ARROW, LEFT_ARROW, EQUAL, EQUAL_EQUAL, BANG, BANG_EQUAL,
  GREATER, GREATER_EQUAL, LESS, LESS_EQUAL, PERCENT, STAR,
  STAR_STAR, PLUS, PLUS_PLUS, MINUS, MINUS_MINUS, SLASH

  ,

  /*
  Keyword tokens
   */
  LET, IF, ELSE, ARG_POS, SEND, WHILE, BREAK, TRUE, FALSE, AND, OR, NIL

  ,

  /*
  Built-ins & Literals Types
   */
  ID, INT, DOUBLE, CHAR, STRING, BOOL, LIST, LAMBDA, IDENTIFIER, DICT

  ,

  EOF, NEW_LINE

}
