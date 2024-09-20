#include "token.h"

#include <utility>

namespace s21 {
Token::Token(Type type, std::string value, int priority)
    : type_(type), value_(std::move(value)), priority_(priority) {}

bool Token::isNumber() const { return type_ == Type::NUMBER; }

bool Token::isNumberX() const { return type_ == Type::NUMBER_X; }

bool Token::isOperator() const { return type_ == Type::OPERATOR; }

bool Token::isUnaryOperator() const { return type_ == Type::UNARY_OPERATOR; }

bool Token::isFunction() const { return type_ == Type::FUNCTION; }

bool Token::isOpenBracket() const { return type_ == Type::OPEN_BRACKET; }

bool Token::isCloseBracket() const { return type_ == Type::CLOSE_BRACKET; }

const std::string &Token::getValue() const { return value_; }

int Token::getPriority() const { return priority_; }
}  // namespace s21
