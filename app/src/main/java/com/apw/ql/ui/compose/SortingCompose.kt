package com.apw.ql.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apw.ql.data.model.SortDialogItem

@Composable
fun SortingItem(
    modifier: Modifier = Modifier,
    item: SortDialogItem,
    onSelected: () -> Unit
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = item.checked, onClick = onSelected)
        Text(
            text = item.label,
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        )
    }
}

@Composable
fun SortingGroup(
    modifier: Modifier = Modifier,
    list: List<SortDialogItem>,
    onSelected: (item: SortDialogItem) -> Unit
) {
    MaterialTheme {
        Surface(modifier) {
            Column {
                list.forEach {
                    SortingItem(item = it) {
                        onSelected(it)
                    }
                }
            }
        }
    }
}

private fun fakeSortDialogItems() = List(3) {
    SortDialogItem(it,"Best matches", false)
}

@Preview
@Composable
fun PreviewSortingGroupDialog() {
    SortingGroup(list = fakeSortDialogItems(), onSelected = {})
}