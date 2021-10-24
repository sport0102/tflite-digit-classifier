package com.aiden.tflite.example

import android.content.res.AssetManager
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class Classifier(private val assetManager: AssetManager, private val modelName: String) {
    private lateinit var interpreter: Interpreter
    private var modelInputChannel = 0
    private var modelInputWidth = 0
    private var modelInputHeight = 0

    fun init() {
        val model = loadModelFile()
        model.order(ByteOrder.nativeOrder())
        interpreter = Interpreter(model)
        initModelShape()
    }

    private fun loadModelFile(): ByteBuffer {
        val assetFileDescriptor = assetManager.openFd(modelName)
        val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = fileInputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun initModelShape() {
        val inputTensor = interpreter.getInputTensor(0)
        val inputShape = inputTensor.shape()
        modelInputChannel = inputShape[0]
        modelInputWidth = inputShape[1]
        modelInputHeight = inputShape[2]
    }

    private fun resizeBitmap(bitmap: Bitmap) =
        Bitmap.createScaledBitmap(bitmap, modelInputWidth, modelInputHeight, false)

    private fun convertBitmapGrayByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(bitmap.byteCount)
        byteBuffer.order(ByteOrder.nativeOrder())

        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        pixels.forEach { pixel ->
            val r = pixel shr 16 and 0xFF
            val g = pixel shr 8 and 0xFF
            val b = pixel and 0xFF

            val avgPixelValue = (r + g + b) / 3.0f
            val normalizedPixelValue = avgPixelValue / 255.0f

            byteBuffer.putFloat(normalizedPixelValue)
        }
        return byteBuffer
    }

    companion object {
        const val DIGIT_CLASSIFIER = "saved_model.tflite"
    }
}