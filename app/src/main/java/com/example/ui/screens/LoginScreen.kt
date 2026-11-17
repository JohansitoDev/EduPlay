package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DuolingoButton
import com.example.ui.theme.TextDark

@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isRegisterMode by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7)), // Duolingo light background
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Mascot Icon Circle
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE5FDCC)), // Light green
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🦁", fontSize = 48.sp)
            }

            Text(
                text = if (isRegisterMode) "¡Crea tu Cuenta!" else "¡Aprende con EduPlay!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = TextDark,
                textAlign = TextAlign.Center
            )

            Text(
                text = if (isRegisterMode) "Regístrate para guardar tu progreso." else "Ingresa o entra como invitado.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF777777),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    if (errorMessage.isNotEmpty()) errorMessage = ""
                },
                label = { Text(if (isRegisterMode) "Elige tu nombre de usuario" else "Tu Nombre o Usuario") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF58CC02),
                    unfocusedBorderColor = Color(0xFFE5E5E5),
                    focusedLabelColor = Color(0xFF58CC02),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña o PIN") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1CB0F6),
                    unfocusedBorderColor = Color(0xFFE5E5E5),
                    focusedLabelColor = Color(0xFF1CB0F6),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color(0xFFFF4B4B),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Primary Button: Login or Register
            DuolingoButton(
                text = if (isRegisterMode) "¡CREAR CUENTA!" else "¡INICIAR SESIÓN!",
                onClick = {
                    if (username.isBlank()) {
                        errorMessage = "¡Por favor escribe un nombre!"
                    } else {
                        onLogin(username, password)
                    }
                },
                containerColor = Color(0xFF58CC02),
                shadowColor = Color(0xFF46A302)
            )

            // Guest Login Button
            DuolingoButton(
                text = "ENTRAR COMO INVITADO",
                onClick = {
                    onLogin("Invitado", "")
                },
                containerColor = Color(0xFF1CB0F6), // Duolingo blue
                shadowColor = Color(0xFF008CC4)
            )

            // Toggle between Login and Register
            Text(
                text = if (isRegisterMode) "¿Ya tienes cuenta? Inicia sesión" else "¿No tienes cuenta? Regístrate aquí",
                color = Color(0xFF1CB0F6),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable { isRegisterMode = !isRegisterMode }
                    .padding(8.dp)
            )
        }
    }
}
