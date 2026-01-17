package com.vs18.kyivstartvmini.ui.epg

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vs18.kyivstartvmini.data.model.dayLabel
import com.vs18.kyivstartvmini.data.model.formatTime
import com.vs18.kyivstartvmini.data.model.isNow
import com.vs18.kyivstartvmini.ui.progress.ProgramProgress
import com.vs18.kyivstartvmini.viewmodel.EpgViewModel

@Composable
fun EpgScreen(tvgId: String) {

    val viewModel: EpgViewModel = viewModel()
    val listState = rememberLazyListState()

    LaunchedEffect(tvgId) {
        viewModel.loadEpg(tvgId)
    }

    LaunchedEffect(viewModel.currentIndex) {
        viewModel.currentIndex?.let {
            listState.animateScrollToItem(it)
        }
    }

    if (viewModel.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.White)
        }
        return
    }

    if (viewModel.programs.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Для цього каналу немає EPG", color = Color.Gray)
        }
        return
    }

    val grouped = viewModel.programs.groupBy {
        dayLabel(it.startTime)
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        grouped.forEach { (day, items) ->

            stickyHeader {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF121212))
                        .padding(12.dp)
                ) {
                    Text(
                        day,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            items(items) { program ->

                val isCurrent = isNow(program.startTime, program.endTime)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (isCurrent) Color(0xFF1E88E5)
                            else Color.Transparent
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        "${formatTime(program.startTime)} - ${formatTime(program.endTime)}",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        program.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    if (program.description.isNotBlank()) {
                        Text(
                            program.description,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    if (isCurrent) {
                        Spacer(Modifier.height(8.dp))
                        ProgramProgress(
                            program.startTime,
                            program.endTime
                        )
                    }
                }
            }
        }
    }

}
