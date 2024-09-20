#include "calculator_model.h"

#include <iostream>
#include <stdexcept>

#include "core.h"

JNIEXPORT jdouble JNICALL
Java_edu_school21_SmartCalc_model_CalculatorModel_evaluateExpression(
    JNIEnv *env, jobject obj, jstring expression, jstring x_value) {
  const char *native_expression = env->GetStringUTFChars(expression, nullptr);
  const char *native_x_value = env->GetStringUTFChars(x_value, nullptr);

  double result = 0.0;
  try {
    s21::Core core;
    result = core.calculate(native_expression, native_x_value);
  } catch (std::invalid_argument ex) {
    jclass exceptionClass = env->FindClass("java/lang/Exception");
    if (exceptionClass != nullptr) {
      env->ThrowNew(exceptionClass, ex.what());
    }
  }

  env->ReleaseStringUTFChars(expression, native_expression);
  env->ReleaseStringUTFChars(x_value, native_x_value);

  return result;
}

JNIEXPORT jdoubleArray JNICALL
Java_edu_school21_SmartCalc_model_CalculatorModel_buildAGraph(
    JNIEnv *env, jobject obj, jstring expression, jdoubleArray borders) {
  const char *c_expression = env->GetStringUTFChars(expression, nullptr);
  jsize borders_length = env->GetArrayLength(borders);
  std::vector<double> borders_values(borders_length);
  env->GetDoubleArrayRegion(borders, 0, borders_length, borders_values.data());

  std::vector<double> data;
  try {
    s21::Core core;
    data = core.calculateGraph(c_expression, borders_values.data());
  } catch (std::invalid_argument ex) {
    jclass exceptionClass = env->FindClass("java/lang/Exception");
    if (exceptionClass != nullptr) {
      env->ThrowNew(exceptionClass, ex.what());
    }
  }
  env->ReleaseStringUTFChars(expression, c_expression);

  jdoubleArray result = env->NewDoubleArray(data.size());
  env->SetDoubleArrayRegion(result, 0, data.size(), data.data());
  return result;
}