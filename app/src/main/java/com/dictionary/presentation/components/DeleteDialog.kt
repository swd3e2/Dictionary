package com.dictionary.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun DeleteDialog(
    text: String,
    onClose: () -> Unit,
    onSuccess: () -> Unit,
) {
    Dialog(
        onDismissRequest = { onClose() },
        content = {
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .height(120.dp)
                    .width(300.dp)
                    .background(color = MaterialTheme.colors.surface)
                    .clip(RoundedCornerShape(30f))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(15.dp)
                ){
                    Text(text = text)
                    Spacer(Modifier.weight(1f))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Button(onClick = { onClose() }) {
                            Text(text = "Cancel")
                        }
                        Button(onClick = { onSuccess() }) {
                            Text(text = "Delete")
                        }
                    }
                }
            }
        }
    )
}