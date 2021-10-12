package com.example.weatherdemo

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProgressBar(
    isDisplay : Boolean,
){
    if(isDisplay){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(50.dp),
            horizontalArrangement = Arrangement.Center
        ){
            CircularProgressIndicator(
                progress = 0.8f,
                color = MaterialTheme.colors.primary,
                strokeWidth = 2.dp
            )
        }
    }
}