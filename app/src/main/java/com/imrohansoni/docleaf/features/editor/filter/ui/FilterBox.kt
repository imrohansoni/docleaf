package com.imrohansoni.docleaf.features.editor.filter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.imrohansoni.docleaf.features.editor.filter.model.FilterType

@Composable
fun FilterBox(
    modifier: Modifier = Modifier,
    filterType: FilterType,
    isSelected: Boolean = false,
    onClick: (FilterType) -> Unit
) {
    Column {
        Box(
            Modifier
                .size(50.dp)
                .background(Color.Red, RoundedCornerShape(12.dp))
                .border(width = 2.dp, if (isSelected) Color.White else Color.Transparent, RoundedCornerShape(12.dp))
                .clickable {
                    onClick.invoke(filterType)
                }
        ) {

        }
        BasicText(text = filterType.displayName)
    }
}