package com.example.translatekeyboardapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast

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
            override fun afterTextChanged(text: Editable?) {
                var layout: Layouts = Layouts.Any
                for (c in text.toString()) {
                    layout = when {
                        (c in keysEnLow || c in keysEnUp) && (c in keysRuLow || c in keysRuUp) -> Layouts.Any
                        c in keysEnLow || c in keysEnUp -> Layouts.En
                        c in keysRuLow || c in keysRuUp -> Layouts.Ru
                        else -> Layouts.None
                    }
                    if (layout != Layouts.None && layout != Layouts.Any) break
                }

                Toast.makeText(this@MainActivity, layout.toString(), Toast.LENGTH_SHORT).show()

                output.setText(text.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })

    }
}

enum class Layouts {
    Any, None, Ru, En
}