#ifndef C_SRC_TOKEN_H
#define C_SRC_TOKEN_H

#include <string>

namespace s21 {
class Token {
 public:
  enum class Type {
    NUMBER,
    NUMBER_X,
    OPERATOR,
    UNARY_OPERATOR,
    FUNCTION,
    OPEN_BRACKET,
    CLOSE_BRACKET
  };

  Token() = default;

  Token(Type type, std::string value, int priority);

  Token(Token &&other) = default;

  Token &operator=(Token &&other) = default;

  Token(const Token &other) = default;

  Token &operator=(const Token &other) = default;

  ~Token() = default;

  bool isNumber() const;

  bool isNumberX() const;

  bool isOperator() const;

  bool isUnaryOperator() const;

  bool isFunction() const;

  bool isOpenBracket() const;

  bool isCloseBracket() const;

  const std::string &getValue() const;

  int getPriority() const;

 private:
  Type type_;
  std::string value_;
  int priority_{};
};
}  // namespace s21
#endif  // C_SRC_TOKEN_H
