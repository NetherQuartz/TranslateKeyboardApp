package com.example.translatekeyboardapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val keysRuUp = resources.getString(R.string.ru_up)
        val keysRuLow = resources.getString(R.string.ru_low)
        val keysEnUp = resources.getString(R.string.en_up)
        val keysEnLow = resources.getString(R.string.en_low)

        val input = findViewById<EditText>(R.id.input)
        val output = findViewById<EditText>(R.id.output)

        Log.d("keys", keysRuUp)
        Log.d("keys", keysRuLow)
        Log.d("keys", keysEnUp)
        Log.d("keys", keysEnLow)

        input.addTextChangedListener(object : TextWatcher {

            private var layout: Layouts = Layouts.Any

            override fun afterTextChanged(text: Editable?) {
                if (text == null) return

                for (c in text) {
                    layout = when {
                        (c in keysEnLow || c in keysEnUp) && (c in keysRuLow || c in keysRuUp) -> Layouts.Any
                        c in keysEnLow || c in keysEnUp -> Layouts.En
                        c in keysRuLow || c in keysRuUp -> Layouts.Ru
                        else -> Layouts.Any
                    }
                    if (layout != Layouts.Any) break
                }

                Log.d("Layout", "$layout $text")

                output.setText(when (layout) {
                    Layouts.Any -> resources.getString(R.string.ambiguous_text)
                    Layouts.En -> {
                        var s = ""
                        for (c in text) {
                            val i = (keysEnLow + keysEnUp).indexOfFirst { it == c}
                            s += if (i != -1) {
                                (keysRuLow + keysRuUp)[i]
                            } else {
                                c
                            }
                        }
                        s
                    }
                    Layouts.Ru -> {
                        var s = ""
                        for (c in text) {
                            val i = (keysRuLow + keysRuUp).indexOfFirst { it == c }
                            s += if (i != -1) {
                                (keysEnLow + keysEnUp)[i]
                            } else {
                                c
                            }
                        }
                        s
                    }
                })
            }

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

    }
}

enum class Layouts { Any, Ru, En }