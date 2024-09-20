#ifndef C_SRC_CORE_H
#define C_SRC_CORE_H

#include <unordered_map>
#include <vector>

#include "token.h"

namespace s21 {
class Core {
 public:
  Core() = default;

  Core(Core &&expression) = default;

  Core &operator=(Core &&other) = default;

  Core(const Core &expression) = default;

  Core &operator=(const Core &other) = default;

  double calculate(const char *infixExpression, const char *x_value);

  std::vector<double> calculateGraph(const char *infixExpression,
                                     const double *borders_values);

  void validateExpression(const std::vector<Token> &tokens);

  std::vector<Token> infixToPostfix(const std::vector<Token> &tokens);

  double evaluatePostfix(const std::vector<Token> &postfix, double x_value);

 private:
  std::vector<Token> tokenize(const std::string &expression);

  int getOperatorPriority(char ch);

  bool isInteger(double value);

  std::string expression_;
};
}  // namespace s21

#endif  // C_SRC_CORE_H
