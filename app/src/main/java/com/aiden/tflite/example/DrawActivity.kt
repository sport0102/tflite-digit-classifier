package com.aiden.tflite.example

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aiden.tflite.example.databinding.ActivityDrawBinding
import java.io.IOException

class DrawActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDrawBinding.inflate(layoutInflater, null, false) }
    private lateinit var classifier: Classifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initCanvas()
        initClassifier()
    }

    private fun initCanvas() {
        binding.run {
            drawView.run {
                setStrokeWidth(100.0f)
                setBackgroundColor(Color.BLACK)
                setColor(Color.WHITE)
            }
            btnClassify.setOnClickListener {
                val image = drawView.getBitmap()
            }
            btnClear.setOnClickListener {
                drawView.clearCanvas()
            }
        }
    }

    private fun initClassifier() {
        classifier = Classifier(assets, Classifier.DIGIT_CLASSIFIER)
        try {
            classifier.init()
        } catch (exception: IOException) {
            Toast.makeText(this@DrawActivity, "classifier IOException", Toast.LENGTH_SHORT).show()
        }
    }
}