package com.aiden.tflite.example

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aiden.tflite.example.databinding.ActivityDrawBinding

class DrawActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDrawBinding.inflate(layoutInflater, null, false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initCanvas()
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
}