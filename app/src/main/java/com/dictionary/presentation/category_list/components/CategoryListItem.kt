package com.dictionary.presentation.category_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dictionary.domain.entity.CategoryWithWords
import com.dictionary.presentation.category_list.CategoryListEvent
import com.dictionary.ui.theme.PrimaryTextColor
import com.dictionary.ui.theme.SecondaryTextColor
import kotlin.math.roundToInt

@Composable
fun CategoryListItem(
    category: CategoryWithWords,
    onEvent: (CategoryListEvent) -> Unit
) {
    val countToRepeat = category.countWordsToRepeat()
    val countToLearn = category.countWordsToLearn()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp, 5.dp)
            .background(
                color = MaterialTheme.colors.surface,
                shape = MaterialTheme.shapes.medium
            )
            .clickable { onEvent(CategoryListEvent.OnCategoryClick(category.category.id)) },
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(15.dp, 15.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            if (category.category.image.isNotEmpty()) {
                AsyncImage(
                    model = category.category.image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(30)),
                )
            }
            Column(
                modifier = Modifier.padding(5.dp, 0.dp)
            ) {
                Row {
                    Column(
                        modifier = Modifier.padding(15.dp, 0.dp, 0.dp)
                    ) {
                        Text(
                            text = category.category.name,
                            style = MaterialTheme.typography.body1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 18.sp,
                            color = MaterialTheme.colors.onSurface
                        )
                        Text(
                            modifier = Modifier.padding(0.dp, 2.dp, 0.dp, 0.dp),
                            text = "${category.words.size} words",
                            style = MaterialTheme.typography.body1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 12.sp,
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ){
                        if (countToLearn > 0) {
                            Box(modifier = Modifier
                                .wrapContentHeight()
                                .wrapContentWidth()
                                .background(
                                    color = Color(0xFF3EAF20),
                                    shape = CircleShape
                                )
                            ) {
                                Text(
                                    modifier = Modifier.padding(8.dp, 5.dp),
                                    text = "$countToLearn",
                                    style = MaterialTheme.typography.body1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    color = Color.White
                                )
                            }
                        }
                        if (countToRepeat > 0) {
                            Spacer(modifier = Modifier.padding(2.dp))
                            Box(modifier = Modifier
                                .wrapContentHeight()
                                .wrapContentWidth()
                                .background(
                                    color = MaterialTheme.colors.primary,
                                    shape = CircleShape
                                )
                            ) {
                                Text(
                                    modifier = Modifier.padding(8.dp, 5.dp),
                                    text = "$countToRepeat",
                                    style = MaterialTheme.typography.body1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
