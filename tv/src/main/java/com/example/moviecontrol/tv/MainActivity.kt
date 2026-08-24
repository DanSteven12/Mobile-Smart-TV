package com.example.moviecontrol.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.SurfaceDefaults
import androidx.tv.material3.Text

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    colors = SurfaceDefaults.colors(
                        containerColor = Color(0xFF070A12)
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
    var movie by remember { mutableStateOf(Movie(titulo = "Cargando...", estado = "detenido")) }

    LaunchedEffect(Unit) {
        repository.leerPelicula { peliculaDescargada ->
            if (peliculaDescargada != null) {
                movie = peliculaDescargada
            }
        }
    }

    // Colores e iconos dinámicos según el estado de reproducción
    val (statusColor, statusBg, statusText, statusIcon) = when (movie.estado.lowercase()) {
        "reproduciendo" -> Quadruple(Color(0xFF10B981), Color(0xFF064E3B), "REPRODUCIENDO", "▶")
        "pausado" -> Quadruple(Color(0xFFF59E0B), Color(0xFF78350F), "EN PAUSA", "⏸")
        "detenido" -> Quadruple(Color(0xFFEF4444), Color(0xFF7F1D1D), "DETENIDO", "⏹")
        else -> Quadruple(Color(0xFF94A3B8), Color(0xFF1E293B), movie.estado.ifBlank { "SIN ESTADO" }.uppercase(), "•")
    }

    // Icono temático dinámico según la película
    val movieIcon = when (movie.titulo.lowercase()) {
        "avengers" -> "🦸‍♂️"
        "spider-man" -> "🕷️"
        "toy story" -> "🤠"
        "badman" -> "🦇"
        "caperuza" -> "❤️"
        else -> "🎬"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        statusColor.copy(alpha = 0.18f),
                        Color(0xFF0F172A),
                        Color(0xFF070A12)
                    ),
                    radius = 1400f
                )
            )
            .padding(horizontal = 48.dp, vertical = 32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // --- ENCABEZADO SUPERIOR ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "🎬 MOVIECONTROL",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF6366F1).copy(alpha = 0.25f))
                            .border(1.dp, Color(0xFF6366F1), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "SMART TV",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFA5B4FC)
                        )
                    }
                }

                // Indicador de conexión en tiempo real
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF1E293B))
                        .border(1.dp, Color(0xFF334155), RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981))
                        )
                        Text(
                            text = "FIRESTORE SYNC",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFCBD5E1)
                        )
                    }
                }
            }

            // --- CONTENIDO PRINCIPAL (2 PANELES 16:9) ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Panel Izquierdo: Información y Sinopsis de la Película
                Box(
                    modifier = Modifier
                        .weight(1.3f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(26.dp))
                        .background(Color(0xFF1E293B).copy(alpha = 0.75f))
                        .border(1.5.dp, Color(0xFF334155).copy(alpha = 0.8f), RoundedCornerShape(26.dp))
                        .padding(26.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Poster / Insignia visual grande
                        Box(
                            modifier = Modifier
                                .size(130.dp)
                                .clip(RoundedCornerShape(22.dp))
                                .background(Color(0xFF0F172A))
                                .border(1.5.dp, Color(0xFF6366F1).copy(alpha = 0.6f), RoundedCornerShape(22.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = movieIcon,
                                fontSize = 56.sp
                            )
                        }

                        // Textos de la película
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (movie.genero.isNotBlank()) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF312E81).copy(alpha = 0.6f))
                                        .border(1.dp, Color(0xFF6366F1).copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = movie.genero.uppercase(),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFA5B4FC)
                                    )
                                }
                            }

                            Text(
                                text = if (movie.titulo.isNotBlank()) movie.titulo.uppercase() else "SIN TÍTULO",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                lineHeight = 40.sp
                            )

                            Text(
                                text = if (movie.descripcion.isNotBlank()) movie.descripcion else "Esperando selección desde el control móvil...",
                                fontSize = 17.sp,
                                lineHeight = 24.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                }

                // Panel Derecho: HUD de Estado de Reproducción
                Box(
                    modifier = Modifier
                        .weight(0.85f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(26.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    statusBg.copy(alpha = 0.35f),
                                    Color(0xFF1E293B).copy(alpha = 0.9f)
                                )
                            )
                        )
                        .border(1.5.dp, statusColor.copy(alpha = 0.6f), RoundedCornerShape(26.dp))
                        .padding(24.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            text = "ESTADO ACTUAL",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp,
                            color = Color(0xFF94A3B8)
                        )

                        // Icono circular brillante
                        Box(
                            modifier = Modifier
                                .size(86.dp)
                                .clip(CircleShape)
                                .background(statusBg.copy(alpha = 0.6f))
                                .border(2.dp, statusColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = statusIcon,
                                fontSize = 36.sp,
                                color = statusColor,
                                fontWeight = FontWeight.Black
                            )
                        }

                        // Píldora de estado grande
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(statusBg.copy(alpha = 0.7f))
                                .border(1.5.dp, statusColor, RoundedCornerShape(16.dp))
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "$statusIcon $statusText",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = statusColor
                            )
                        }
                    }
                }
            }

            // --- PIE DE PANTALLA ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📱 Controla la reproducción y selecciona películas en tiempo real desde tu móvil",
                    fontSize = 13.sp,
                    color = Color(0xFF64748B),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
