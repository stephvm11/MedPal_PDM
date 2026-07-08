package com.pdm0126.medpal.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm0126.medpal.R
import org.w3c.dom.Text

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory),
){
    var isLoginMode by rememberSaveable { mutableStateOf(true) }

    var loginEmail by rememberSaveable {mutableStateOf("") }
    var loginPassword by rememberSaveable {mutableStateOf("") }

    var registerFirstName by rememberSaveable { mutableStateOf("") }
    var registerLastName by rememberSaveable { mutableStateOf("") }
    var registerEmail by rememberSaveable { mutableStateOf("") }
    var registerPassword by rememberSaveable { mutableStateOf("") }
    var registerConfirmPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()


    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = colorResource(R.color.beige),
        unfocusedTextColor = colorResource(R.color.beige),
        focusedLabelColor = colorResource(R.color.beige),
        unfocusedLabelColor = colorResource(R.color.beige),
        focusedBorderColor = colorResource(R.color.beige),
        unfocusedBorderColor = colorResource(R.color.beige)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.midnight_green))
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(20.dp))

            Image(
                painter = painterResource(id = R.drawable.medpal_logo),
                contentDescription = "Logo de MedPal",
                modifier = Modifier
                    .size(150.dp)
            )
            Text(
                text = if (isLoginMode) "Iniciar Sesion" else "Regístrate",
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                color = colorResource(R.color.beige),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))

            if (isLoginMode) {
                OutlinedTextField(
                    value = loginEmail,
                    onValueChange = { loginEmail = it },
                    label = { Text("Email") },
                    colors = textFieldColors,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = loginPassword,
                    onValueChange = { loginPassword = it },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    colors = textFieldColors,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = registerFirstName,
                        onValueChange = { registerFirstName = it },
                        label = { Text("Nombre") },
                        colors = textFieldColors,
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(12.dp))

                    OutlinedTextField(
                        value = registerLastName,
                        onValueChange = { registerLastName = it },
                        label = { Text("Apellido") },
                        colors = textFieldColors,
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = registerEmail,
                    onValueChange = { registerEmail = it },
                    label = { Text("Email") },
                    colors = textFieldColors,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = registerPassword,
                    onValueChange = { registerPassword = it },
                    label = { Text("Contraseña") },
                    colors = textFieldColors,
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image =
                            if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = "Ver contraseña")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = registerConfirmPassword,
                    onValueChange = { registerConfirmPassword = it },
                    label = { Text("Confirmar contraseña") },
                    colors = textFieldColors,
                    singleLine = true,
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image =
                            if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(imageVector = image, contentDescription = "Ver contraseña")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (error != null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (successMessage != null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = successMessage!!,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    if (isLoginMode) {
                        viewModel.login(loginEmail, loginPassword)
                    } else {
                        if (registerPassword != registerConfirmPassword) {
                            viewModel.setError("Las contraseñas no coinciden")
                            return@Button
                        }
                        viewModel.register(
                            firstName = registerFirstName,
                            lastName = registerLastName,
                            email = registerEmail,
                            password = registerPassword
                        )
                    }
                },
                enabled = !isLoading && (
                        if (isLoginMode) {
                            loginEmail.isNotBlank() && loginPassword.isNotBlank()
                        } else {
                            registerFirstName.isNotBlank() &&
                                    registerLastName.isNotBlank() &&
                                    registerEmail.isNotBlank() &&
                                    registerPassword.isNotBlank() &&
                                    registerConfirmPassword.isNotBlank()
                        }
                        ),
                shape = CircleShape,
                modifier = Modifier.width(160.dp)
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.rosy_brown),
                    contentColor = colorResource(R.color.beige))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = colorResource(R.color.beige ))
                } else {
                    Text(text = if (isLoginMode) "Ingresar" else "Registrar", color = colorResource(R.color.beige))
                }
            }

            Spacer(Modifier.height(24.dp))


            if (isLoginMode) {
                val annotatedLoginString = buildAnnotatedString {
                    append("O ")
                    pushStringAnnotation(tag = "SWITCH", annotation = "register")
                    withStyle(
                        style = SpanStyle(
                            color = colorResource(R.color.beige),
                            textDecoration = TextDecoration.Underline,
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append("registrate")
                    }
                    pop()
                    append(" si no tienes una cuenta.")
                }

                ClickableText(
                    text = annotatedLoginString,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 15.sp,
                        color = colorResource(R.color.rosy_brown),
                        textAlign = TextAlign.Center
                    ),
                    onClick = { offset ->
                        annotatedLoginString.getStringAnnotations(
                            tag = "SWITCH",
                            start = offset,
                            end = offset
                        )
                            .firstOrNull()?.let {
                                isLoginMode = false
                                viewModel.clearMessages()
                            }
                    }
                )
            } else {
                val annotatedRegisterString = buildAnnotatedString {
                    append("¿Ya tienes cuenta? ")
                    pushStringAnnotation(tag = "SWITCH", annotation = "login")
                    withStyle(
                        style = SpanStyle(
                            color = colorResource(R.color.beige),
                            textDecoration = TextDecoration.Underline,
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append("Inicia sesión")
                    }
                    pop()
                }

                ClickableText(
                    text = annotatedRegisterString,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 15.sp,
                        color = colorResource(R.color.rosy_brown),
                        textAlign = TextAlign.Center
                    ),
                    onClick = { offset ->
                        annotatedRegisterString.getStringAnnotations(
                            tag = "SWITCH",
                            start = offset,
                            end = offset
                        )
                            .firstOrNull()?.let {
                                isLoginMode = true
                                viewModel.clearMessages()
                            }
                    }
                )
            }
        }
    }
}
