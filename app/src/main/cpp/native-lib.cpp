#include <jni.h>
#include <string>
#include <math.h>

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_example_g_1ultron_calculator_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {

    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


extern "C"
JNIEXPORT jdouble

JNICALL
Java_com_example_g_1ultron_calculator_MainActivity_math(JNIEnv *env, jobject instance,
                                                        jdouble radian, jstring func) {



    if ( strcmp((const char *) func, "cos"))
        return cos(radian);
    else
        if (strcmp((const char *) func, "sin"))
            return sin(radian);
        else
            if (strcmp((const char *) func, "tan"))
                return tan(radian);
            else
                return -1;

}