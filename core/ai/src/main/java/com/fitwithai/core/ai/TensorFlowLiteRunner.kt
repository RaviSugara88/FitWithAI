package com.fitwithai.core.ai

import org.tensorflow.lite.Interpreter

class TensorFlowLiteRunner(
    private val interpreter: Interpreter,
) {
    fun <Input, Output> run(input: Input, output: Output) {
        interpreter.run(input, output)
    }
}
