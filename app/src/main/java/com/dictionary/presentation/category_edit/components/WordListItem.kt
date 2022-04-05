package com.dictionary.presentation.category_edit.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dictionary.domain.entity.Word
import com.dictionary.presentation.category_edit.CategoryEditEvent
import com.dictionary.ui.theme.PrimaryTextColor
import com.dictionary.ui.theme.SecondaryTextColor

@Composable
fun WordListItem(
    word: Word,
    onEvent: (CategoryEditEvent) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(15.dp, 5.dp)
            .clip(shape = RoundedCornerShape(30))
            .clickable {
                onEvent(CategoryEditEvent.OnWordClick(word.id))
            },
        elevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp, 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .width(300.dp)
            ){
                Text(
                    text = word.term,
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                    color = PrimaryTextColor
                )
                Text(
                    text = word.definition,
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                    color = SecondaryTextColor,
                    fontSize = 12.sp
                )
            }
            IconButton(
                modifier = Modifier.padding(4.dp),
                onClick = {
                    onEvent(CategoryEditEvent.OnDeleteWord(word.id))
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    "Delete",
                )
            }
        }
    }
}