package com.example.mindspace.ml

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class MLModelHelper(context: Context) {

    private val interpreter: Interpreter

    init {
        val model = loadModelFile(context, "mental_health_model_new.tflite")
        interpreter = Interpreter(model)
    }

    private fun loadModelFile(context: Context, modelName: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun runInference(input: FloatArray): String {
        val output = Array(1) { FloatArray(4) } // Adjust based on actual output shape
        interpreter.run(input, output)

        // Determine the index with the highest value
        val maxIndex = output[0].indices.maxByOrNull { output[0][it] } ?: -1

        // Translate the max index to a descriptive string
        val result = when (maxIndex) {
            0 -> "Mild"
            1 -> "Moderate"
            2 -> "Severe"
            3 -> "Critical"
            else -> "Unknown"
        }

        return "Prediction: $result"
    }
}
