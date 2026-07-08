package com.pdm0126.medpal.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.pdm0126.medpal.R

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
){
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = colorResource(R.color.beige),
        unfocusedTextColor = colorResource(R.color.beige),
        focusedLabelColor = colorResource(R.color.beige),
        unfocusedLabelColor = colorResource(R.color.beige),
        focusedBorderColor = colorResource(R.color.beige),
        unfocusedBorderColor = colorResource(R.color.beige)
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        modifier = Modifier.Companion.fillMaxWidth(),
        singleLine = true,
        colors =  textFieldColors
    )
}