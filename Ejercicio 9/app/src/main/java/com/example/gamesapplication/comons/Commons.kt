package com.example.gamesapplication.comons

import android.graphics.drawable.Drawable.ConstantState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun GenerateImage(modifier : Modifier, imageResId:Int,contentScale :ContentScale=ContentScale.None ){
    Image(
        painter = painterResource(id = imageResId),
        contentDescription = null,
        contentScale = contentScale,
        modifier = modifier
    )
}
@Composable
fun TextFieldWithPlaceHolder(text: String, value: String, onValueChange: (String) -> Unit,
                             visualTransformation: VisualTransformation = VisualTransformation.None) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.width(280.dp),
        visualTransformation = visualTransformation,
        placeholder = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = text,
                textAlign = TextAlign.Center
            )
        },
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
    )
}
@Composable
fun ButtonWithText(text: String, onClick : ()->Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier.width(160.dp)
    ) {
        Text(text)
    }
}