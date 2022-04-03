package com.dictionary.presentation.category_list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dictionary.domain.entity.Category
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.dictionary.R
import com.dictionary.presentation.category_list.CategoryListEvent
import com.dictionary.ui.theme.PrimaryTextColor
import com.dictionary.ui.theme.SecondaryTextColor

@Composable
fun CategoryListItem(
    category: Category,
    onEvent: (CategoryListEvent) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(0.dp, 5.dp)
            .clickable { onEvent(CategoryListEvent.OnCategoryClick(category.id!!)) },
        elevation = 0.dp,
        shape = RoundedCornerShape(30)
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
                        text = category.name,
                        style = MaterialTheme.typography.body1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 18.sp,
                        color = PrimaryTextColor
                    )
                    Text(
                        modifier = Modifier.padding(0.dp, 4.dp, 0.dp, 0.dp),
                        text = "3 terms",
                        style = MaterialTheme.typography.body1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 12.sp,
                        color = SecondaryTextColor
                    )
                }
            }
            Box(
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(
                    onClick = {
                        onEvent(CategoryListEvent.OnDeleteCategory(category.id!!))
                    }
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