package com.example.moviecontrol.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    colors = androidx.tv.material3.SurfaceDefaults.colors(
                        containerColor = Color(0xFF1E1E1E) // Fondo oscuro para TV
                    )
                ) {
                    TvControlApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvControlApp() {
    val repository = remember { FirebaseRepository() }
    var movie by remember { mutableStateOf(Movie(titulo = "Cargando conexión con Firestore...")) }

    LaunchedEffect(Unit) {
        repository.leerPelicula { peliculaDescargada ->
            if (peliculaDescargada != null) {
                movie = peliculaDescargada
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🎬 MOVIECONTROL",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.LightGray
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = movie.titulo.uppercase(),
            fontSize = 64.sp,
            fontWeight = FontWeight.Black,
            color = Color.White
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = movie.genero.uppercase(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF9E9E9E)
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = movie.descripcion,
            fontSize = 28.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFFE0E0E0),
            modifier = Modifier.padding(horizontal = 100.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        val estadoFormateado = when(movie.estado.lowercase()) {
            "reproduciendo" -> "▶ REPRODUCIENDO"
            "pausado" -> "⏸ PAUSADO"
            "detenido" -> "⏹ DETENIDO"
            else -> movie.estado.uppercase()
        }

        val colorEstado = when(movie.estado.lowercase()) {
            "reproduciendo" -> Color(0xFF4CAF50)
            "pausado" -> Color(0xFFFF9800)
            "detenido" -> Color(0xFFF44336)
            else -> Color.Gray
        }

        Text(
            text = estadoFormateado,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = colorEstado
        )
    }
}
