#include "core.h"

#include <bits/stdc++.h>

#include <cmath>
#include <stack>
#include <stdexcept>

namespace s21 {

double Core::calculate(const char *infixExpression, const char *x_value) {
  std::vector<Token> tokens = tokenize(infixExpression);
  validateExpression(tokens);
  std::vector<Token> postfix = infixToPostfix(tokens);
  bool x_value_present = false;
  for (const Token &token : postfix) {
    if (token.isNumberX()) {
      x_value_present = true;
      break;
    }
  }
  double double_x_value = x_value_present ? std::stod(x_value) : 0.;
  return evaluatePostfix(postfix, double_x_value);
}

std::vector<double> Core::calculateGraph(const char *infixExpression,
                                         const double *borders_values) {
  for (int i = 0; i != 4; ++i) {
    if (borders_values[i] < -1000000 || borders_values[i] > 1000000) {
      throw std::invalid_argument("Value too high");
    }
  }

  if (borders_values[0] > borders_values[1]) {
    throw std::invalid_argument(
        "minX cannot exceed the value in the field maxX");
  } else if (borders_values[2] > borders_values[3]) {
    throw std::invalid_argument(
        "minY cannot exceed the value in the field maxY");
  }

  std::vector<Token> tokens = tokenize(infixExpression);
  validateExpression(tokens);
  std::vector<Token> postfix = infixToPostfix(tokens);

  std::vector<double> result;

  double minX = borders_values[0];
  double maxX = borders_values[1];
  double step = (maxX - minX) / 1000.0;

  result.reserve(static_cast<size_t>((maxX - minX) / step) * 2);

  for (double x = minX; x <= maxX; x += step) {
    double y = evaluatePostfix(postfix, x);
    if (y >= borders_values[2] && y <= borders_values[3]) {
      result.push_back(x);
      result.push_back(y);
    }
  }

  return result;
}

int Core::getOperatorPriority(char ch) {
  switch (ch) {
    case '+':
    case '-':
      return 1;
    case '*':
    case '/':
    case 'm':
      return 2;
    case '^':
      return 4;
    default:
      return 0;
  }
}

std::vector<Token> Core::tokenize(const std::string &expression) {
  static const std::vector<std::string> literals = {
      "cos", "sin", "tan", "acos", "asin", "atan", "sqrt", "ln", "log"};
  std::vector<Token> tokens;
  for (size_t i = 0; i != expression.length(); ++i) {
    while (i != expression.length() && std::isspace(expression[i])) {
      ++i;
    }
    if (i >= expression.length()) break;
    if (std::isdigit(expression[i]) || expression[i] == ',') {
      std::string number;
      number += expression[i];
      while (i + 1 != expression.length() &&
             (std::isdigit(expression[i + 1]) || expression[i + 1] == ',')) {
        number += expression[++i];
      }
      if (expression[i + 1] == 'e' || expression[i + 1] == 'E') {
        number += expression[++i];
        if (i + 1 != expression.length() &&
            getOperatorPriority(expression[i + 1]) == 1)
          number += expression[++i];
        while (i + 1 != expression.length() &&
               std::isdigit(expression[i + 1])) {
          number += expression[++i];
        }
      }
      tokens.emplace_back(Token::Type::NUMBER, number,
                          getOperatorPriority(expression[i]));
    } else if (expression[i] == '(') {
      tokens.emplace_back(Token::Type::OPEN_BRACKET, "(",
                          getOperatorPriority(expression[i]));
    } else if (expression[i] == ')') {
      tokens.emplace_back(Token::Type::CLOSE_BRACKET, ")",
                          getOperatorPriority(expression[i]));
    } else if (getOperatorPriority(expression[i])) {
      std::string op;
      op.push_back(expression[i]);
      if (getOperatorPriority(expression[i]) == 1 &&
          (i == 0 || expression[i - 1] == '(')) {
        tokens.emplace_back(Token::Type::UNARY_OPERATOR, op, 3);
      } else {
        tokens.emplace_back(Token::Type::OPERATOR, op,
                            getOperatorPriority(expression[i]));
        if (expression[i] == 'm') {
          if (i + 2 < expression.length() && expression[i + 1] == 'o' &&
              expression[i + 2] == 'd') {
            i += 2;
          } else {
            throw std::invalid_argument("Incorrect expression");
          }
        }
      }
    } else if (expression[i] == 'x') {
      tokens.emplace_back(Token::Type::NUMBER_X, "x",
                          getOperatorPriority(expression[i]));
    } else {
      std::string func;
      func += expression[i];
      while (i + 1 != expression.length() && std::isalpha(expression[i + 1])) {
        func += expression[++i];
      }
      if (std::find(literals.begin(), literals.end(), func) != literals.end()) {
        tokens.emplace_back(Token::Type::FUNCTION, func, 5);
      } else {
        throw std::invalid_argument("Unknown function or token");
      }
    }
  }
  return tokens;
}

void Core::validateExpression(const std::vector<Token> &tokens) {
  int brackets_open = 0;
  bool lastWasOperator = true;
  for (int i = 0; i != tokens.size(); ++i) {
    if (tokens[i].isNumber() || tokens[i].isNumberX()) {
      lastWasOperator = false;
      if (i != 0 && (tokens[i - 1].isNumber() || tokens[i - 1].isNumberX())) {
        throw std::invalid_argument("Two numbers in a row");
      }
    } else if (tokens[i].isOpenBracket()) {
      if (i + 1 != tokens.size() && tokens[i + 1].isCloseBracket()) {
        throw std::invalid_argument("Incorrect initialization of parentheses");
      }
      ++brackets_open;
      lastWasOperator = true;
    } else if (tokens[i].isCloseBracket()) {
      --brackets_open;
      if (brackets_open < 0)
        throw std::invalid_argument("Unmatched closing bracket");
      lastWasOperator = false;
    } else if (tokens[i].isOperator()) {
      if (lastWasOperator) throw std::invalid_argument("Unexpected operator");
      lastWasOperator = true;
    }
  }
  if (tokens.empty()) throw std::invalid_argument("Malformed expression");
  if (lastWasOperator)
    throw std::invalid_argument("Expression cannot end with an operator");
  if (brackets_open != 0)
    throw std::invalid_argument("Unmatched opening bracket");
}

std::vector<Token> Core::infixToPostfix(const std::vector<Token> &tokens) {
  std::vector<Token> output;
  std::stack<Token> operators;

  for (const auto &token : tokens) {
    if (token.isNumber() || token.isNumberX()) {
      output.push_back(token);
    } else if (token.isOpenBracket() || token.isFunction()) {
      operators.push(token);
    } else if (token.isCloseBracket()) {
      while (!operators.empty() && !operators.top().isOpenBracket()) {
        output.push_back(operators.top());
        operators.pop();
      }
      if (!operators.empty()) operators.pop();
    } else if (token.isOperator() || token.isUnaryOperator()) {
      while (!operators.empty() &&
             operators.top().getPriority() >= token.getPriority() &&
             ((token.getPriority() != 4 &&
               token.getPriority() <= operators.top().getPriority()) ||
              (token.getPriority() == 4 &&
               token.getPriority() < operators.top().getPriority()))) {
        output.push_back(operators.top());
        operators.pop();
      }
      operators.push(token);
    }
  }
  while (!operators.empty()) {
    output.push_back(operators.top());
    operators.pop();
  }
  return output;
}

double Core::evaluatePostfix(const std::vector<Token> &postfix,
                             double x_value) {
  std::stack<double> stack;
  for (const auto &token : postfix) {
    if (token.isNumber()) {
      stack.push(std::stod(token.getValue()));
    } else if (token.isNumberX()) {
      stack.push(x_value);
    } else if (token.isOperator() || token.isUnaryOperator()) {
      double rhs = stack.top();
      stack.pop();
      double lhs = 0.;
      if (!token.isUnaryOperator()) {
        lhs = stack.top();
        stack.pop();
      }
      switch (token.getValue()[0]) {
        case '+':
          stack.push(lhs + rhs);
          break;
        case '-':
          stack.push(lhs - rhs);
          break;
        case '*':
          stack.push(lhs * rhs);
          break;
        case '/':
          stack.push(lhs / rhs);
          break;
        case '^':
          stack.push(std::pow(lhs, rhs));
          break;
        case 'm':
          if (isInteger(lhs) && isInteger(rhs)) {
            long long_lhs = static_cast<long>(lhs);
            long long_rhs = static_cast<long>(rhs);
            if (long_lhs < 0) long_lhs = long_rhs + long_lhs;
            stack.push(static_cast<double>(long_lhs % long_rhs));
          } else {
            throw std::invalid_argument(
                "Modulus division is only defined for integers");
          }
          break;
      }
    } else if (token.isFunction()) {
      double value = stack.top();
      stack.pop();
      const std::string &func = token.getValue();
      if (func == "cos")
        stack.push(std::cos(value));
      else if (func == "sin")
        stack.push(std::sin(value));
      else if (func == "tan")
        stack.push(std::tan(value));
      else if (func == "acos")
        stack.push(std::acos(value));
      else if (func == "asin")
        stack.push(std::asin(value));
      else if (func == "atan")
        stack.push(std::atan(value));
      else if (func == "sqrt")
        stack.push(std::sqrt(value));
      else if (func == "ln")
        stack.push(std::log(value));
      else if (func == "log")
        stack.push(std::log10(value));
    }
  }

  return stack.top();
}

bool Core::isInteger(double value) {
  return std::fabs(value - std::round(value)) <
         std::numeric_limits<double>::epsilon();
}
}  // namespace s21
