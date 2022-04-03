package com.dictionary.presentation.category_edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dictionary.ui.theme.PrimaryTextColor
import com.dictionary.ui.theme.SecondaryTextColor

@Composable
fun CustomTextField(
    label: String,
    value: MutableState<String>,
    onChange: (String) -> Unit,
    onEmpty: () -> Unit
) {
    Text(
        text = label,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 2.dp),
        textAlign = TextAlign.Start,
        color = PrimaryTextColor,
        fontWeight = FontWeight.Bold
    )
    TextField(
        modifier = Modifier
            .background(MaterialTheme.colors.surface)
            .padding(4.dp)
            .background(
                MaterialTheme.colors.surface,
                MaterialTheme.shapes.small,
            )
            .fillMaxWidth(),
        textStyle = LocalTextStyle.current.copy(
            fontSize = 20.sp,
            color = SecondaryTextColor
        ),
        singleLine = true,
        value = value.value,
        onValueChange = onChange,
    )

//    TextField(
//        modifier = Modifier
//            .background(MaterialTheme.colors.surface)
//            .padding(4.dp)
//            .height(23.dp)
//            .background(
//                MaterialTheme.colors.surface,
//                MaterialTheme.shapes.small,
//            )
//            .fillMaxWidth(),
//        value = value.value,
//        onValueChange = onChange,
//        singleLine = true,
//        cursorBrush = SolidColor(MaterialTheme.colors.primary),
//        textStyle = LocalTextStyle.current.copy(
//            fontSize = MaterialTheme.typography.body1.fontSize,
//            color = SecondaryTextColor
//        ),
//    )
}