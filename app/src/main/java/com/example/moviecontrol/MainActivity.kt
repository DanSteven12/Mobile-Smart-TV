package com.example.moviecontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MovieControlApp()
                }
            }
        }
    }
}

@Composable
fun MovieControlApp() {
    // 1. Instanciar el Repositorio
    val repository = remember { FirebaseRepository() }

    // 2. Estado que guarda la película actual proveniente de Firebase
    var movie by remember { mutableStateOf(Movie(titulo = "Cargando...")) }

    // 3. Leer de Firebase al iniciar la app
    LaunchedEffect(Unit) {
        repository.leerPelicula { peliculaDescargada ->
            if (peliculaDescargada != null) {
                movie = peliculaDescargada
            } else {
                movie = Movie(titulo = "Error de conexión")
            }
        }
    }

    // 4. Construir la Interfaz de Usuario
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "🎬 MovieControl",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Selecciona una película", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

        // Botones de películas (Etapa 9: envían datos completos a Firebase)
        Button(
            onClick = {
                repository.actualizarPelicula(
                    mapOf(
                        "titulo" to "Avengers",
                        "genero" to "Acción",
                        "descripcion" to "Los Vengadores se reúnen para salvar el mundo."
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Avengers")
        }

        Button(
            onClick = {
                repository.actualizarPelicula(
                    mapOf(
                        "titulo" to "Spider-Man",
                        "genero" to "Acción",
                        "descripcion" to "Un joven superhéroe con habilidades arácnidas."
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Spider-Man")
        }

        Button(
            onClick = {
                repository.actualizarPelicula(
                    mapOf(
                        "titulo" to "Toy Story",
                        "genero" to "Animación",
                        "descripcion" to "Las aventuras secretas de un grupo de juguetes."
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Toy Story")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar datos actuales
        Text(text = "Película:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(text = movie.titulo, fontSize = 18.sp, color = Color.DarkGray)

        Text(text = "Género:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(text = movie.genero, fontSize = 18.sp, color = Color.DarkGray)

        Text(text = "Descripción:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(text = movie.descripcion, fontSize = 18.sp, color = Color.DarkGray)

        Spacer(modifier = Modifier.height(16.dp))

        // Botones de control (Etapa 9: envían solo el estado a Firebase)
        Button(
            onClick = { repository.actualizarPelicula(mapOf("estado" to "reproduciendo")) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("▶ Reproducir")
        }

        Button(
            onClick = { repository.actualizarPelicula(mapOf("estado" to "pausado")) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
        ) {
            Text("⏸ Pausar")
        }

        Button(
            onClick = { repository.actualizarPelicula(mapOf("estado" to "detenido")) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
        ) {
            Text("⏹ Detener")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Estado:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(
            text = movie.estado.uppercase(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = when(movie.estado.lowercase()) {
                "reproduciendo" -> Color(0xFF4CAF50)
                "pausado" -> Color(0xFFFF9800)
                "detenido" -> Color(0xFFF44336)
                else -> Color.DarkGray
            }
        )
    }
}