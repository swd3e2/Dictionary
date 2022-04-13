package com.dictionary.presentation.category_list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.dictionary.R
import com.dictionary.domain.entity.CategoryWithWords
import com.dictionary.presentation.category_list.CategoryListEvent
import com.dictionary.presentation.common.DisabledInteractionSource
import com.dictionary.ui.theme.PrimaryTextColor
import com.dictionary.ui.theme.SecondaryTextColor

@Composable
fun CategoryListItem(
    category: CategoryWithWords,
    onEvent: (CategoryListEvent) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(15.dp, 5.dp)
            .clickable { onEvent(CategoryListEvent.OnCategoryClick(category.category.id)) },
        elevation = 0.dp
    ) {
        Row(
            Modifier
                .padding(15.dp, 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row{
                Image(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(R.drawable.category_test_image),
                    contentDescription = "Contact profile picture",
                )
                Column(
                    modifier = Modifier.padding(10.dp, 0.dp)
                ){
                    Text(
                        text = category.category.name,
                        style = MaterialTheme.typography.body1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 18.sp,
                        color = PrimaryTextColor
                    )
                    Text(
                        modifier = Modifier.padding(0.dp, 2.dp, 0.dp, 0.dp),
                        text = "${category.words.size} words\n${category.countWordsToRepeat()} to repeat \n${category.countWordsToLearn()} to learn",
                        style = MaterialTheme.typography.body1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 12.sp,
                        color = SecondaryTextColor
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        onEvent(CategoryListEvent.OnShowDeleteDialog(category))
                    },
                    interactionSource = remember { DisabledInteractionSource() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        "Delete",
                        modifier = Modifier.size(20.dp),
                    )
                }
            }

        }
    }
}