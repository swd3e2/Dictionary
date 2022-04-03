package com.dictionary.presentation.category_edit.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dictionary.domain.entity.Word
import com.dictionary.presentation.category_edit.CategoryEditEvent

@Composable
fun WordListItem(
    word: Word,
    onEvent: (CategoryEditEvent) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(0.dp, 5.dp)
            .border(1.dp, MaterialTheme.colors.secondary)
            .clickable {
                onEvent(CategoryEditEvent.OnWordClick(word.id))
            },
        elevation = 5.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                Modifier.padding(10.dp).width(IntrinsicSize.Min)
            ) {
                Text(
                    text = word.term,
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                )
                Divider(modifier = Modifier.fillMaxWidth().height(1.dp))
                Text(
                    text = word.definition,
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Button(
                modifier = Modifier.padding(10.dp),
                onClick = {
                    onEvent(CategoryEditEvent.OnDeleteWord(word.id))
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    "Delete",
                    modifier = Modifier.padding(3.dp)
                )
            }
        }
    }
}