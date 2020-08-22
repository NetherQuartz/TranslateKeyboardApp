package ru.vlarkin.translatekeyboardapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var clipboardManager: ClipboardManager? = null
    private var clipData: ClipData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val keysRuUp = resources.getString(R.string.ru_up)
        val keysRuLow = resources.getString(R.string.ru_low)
        val keysEnUp = resources.getString(R.string.en_up)
        val keysEnLow = resources.getString(R.string.en_low)
        val keysRuUpperRow = resources.getString(R.string.ru_upperRow)
        val keysEnUpperRow = resources.getString(R.string.en_upperRow)

        val keysEn = keysEnLow + keysEnUp + keysEnUpperRow
        val keysRu = keysRuLow + keysRuUp + keysRuUpperRow

        val input = findViewById<EditText>(R.id.input)
        val output = findViewById<EditText>(R.id.output)

        Log.d("keys", keysRuUp)
        Log.d("keys", keysRuLow)
        Log.d("keys", keysEnUp)
        Log.d("keys", keysEnLow)

        input.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(text: Editable?) {
                if (text == null || input.text.isEmpty()) {
                    output.setText("")
                    output.hint = ""
                    return
                }

                var layout: Layouts =
                    Layouts.Any

                for (c in text) {
                    layout = when {
                        c in keysEn && c in keysRu -> Layouts.Any
                        c in keysEn -> Layouts.En
                        c in keysRu -> Layouts.Ru
                        else -> Layouts.Any
                    }
                    if (layout != Layouts.Any) break
                }

                Log.d("Layout", "$layout $text")

                when (layout) {
                    Layouts.Any -> {
                        output.setText("")
                        output.hint = resources.getString(R.string.ambiguous_text)
                    }
                    Layouts.En -> {
                        var s = ""
                        for (c in text) {
                            val i = keysEn.indexOfFirst { it == c}
                            s += if (i != -1) {
                                keysRu[i]
                            } else {
                                c
                            }
                        }
                        output.setText(s)
                    }
                    Layouts.Ru -> {
                        var s = ""
                        for (c in text) {
                            val i = keysRu.indexOfFirst { it == c }
                            s += if (i != -1) {
                                keysEn[i]
                            } else {
                                c
                            }
                        }
                        output.setText(s)
                    }
                }
            }

            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        val copy = findViewById<ImageButton>(R.id.copy)
        val paste = findViewById<ImageButton>(R.id.paste)

        clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?

        copy.setOnClickListener {
            clipData = ClipData.newPlainText("TranslatedText", output.text)
            if (clipData == null) return@setOnClickListener
            clipboardManager?.setPrimaryClip(clipData!!)
            Toast.makeText(this, resources.getString(R.string.copied), Toast.LENGTH_SHORT).show()
        }

        paste.setOnClickListener {
            val text = clipboardManager?.primaryClip?.getItemAt(0)?.text ?: return@setOnClickListener
            input.setTextKeepState(text)
        }
    }
}

enum class Layouts { Any, Ru, En }