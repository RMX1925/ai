package com.google.ai.sample.feature.askai

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
internal fun AskRoute() {
    Scaffold {paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Text("Text Route")
        }

    }
}

@Preview
@Composable
fun PreviewAskRoute() {
    AskRoute()
}