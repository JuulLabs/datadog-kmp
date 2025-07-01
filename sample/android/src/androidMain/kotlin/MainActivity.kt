package com.juul.datadog.sample.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.datadog.kmp.Datadog
import com.juul.datadog.sample.info
import com.juul.datadog.sample.testException

public class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(Modifier.background(color = MaterialTheme.colors.background)) {
                var text by remember { mutableStateOf("Hello, world!") }

                Row {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("Message") },
                    )
                    Button(
                        onClick = {
                            println("data init? ${Datadog.isInitialized()}")
                            info(text)
                        },
                    ) {
                        Text("Info")
                    }
                }

                Row {
                    var exceptionText by remember { mutableStateOf("Ouch!") }
                    OutlinedTextField(
                        value = exceptionText,
                        onValueChange = { exceptionText = it },
                        label = { Text("Message") },
                    )
                    Button(
                        onClick = {
                            testException(text, exceptionText)
                        },
                    ) {
                        Text("Exception")
                    }
                }
            }
        }
    }
}
