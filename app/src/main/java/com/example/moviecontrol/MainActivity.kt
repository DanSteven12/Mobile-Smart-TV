package com.example.moviecontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    background = Color(0xFF0B0F19),
                    surface = Color(0xFF161F30),
                    primary = Color(0xFF6366F1),
                    onPrimary = Color.White,
                    onBackground = Color(0xFFF8FAFC),
                    onSurface = Color(0xFFE2E8F0)
                )
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0B0F19)
                ) {
                    MovieControlApp()
                }
            }
        }
    }
}

// Modelo local para las opciones del catálogo
data class MovieOption(
    val titulo: String,
    val genero: String,
    val descripcion: String,
    val icon: String
)

@Composable
fun MovieControlApp() {
    val repository = remember { FirebaseRepository() }
    var movie by remember { mutableStateOf(Movie(titulo = "Cargando...")) }

    // Catálogo de películas disponibles
    val catalogo = remember {
        listOf(
            MovieOption(
                titulo = "Avengers",
                genero = "Acción",
                descripcion = "Los Vengadores se reúnen para salvar el mundo.",
                icon = "🦸‍♂️"
            ),
            MovieOption(
                titulo = "Spider-Man",
                genero = "Acción",
                descripcion = "Un joven superhéroe con habilidades arácnidas.",
                icon = "🕷️"
            ),
            MovieOption(
                titulo = "Toy Story",
                genero = "Animación",
                descripcion = "Las aventuras secretas de un grupo de juguetes.",
                icon = "🤠"
            )
        )
    }

    LaunchedEffect(Unit) {
        repository.leerPelicula { peliculaDescargada ->
            if (peliculaDescargada != null) {
                movie = peliculaDescargada
            } else {
                movie = Movie(titulo = "Error de conexión")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F172A), Color(0xFF070A12))
                )
            )
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // --- ENCABEZADO ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "🎬 MovieControl",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "Control Remoto Inteligente",
                    fontSize = 13.sp,
                    color = Color(0xFF94A3B8)
                )
            }

            Surface(
                color = Color(0xFF1E293B),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color(0xFF334155))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF10B981))
                    )
                    Text(
                        text = "En Línea",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFCBD5E1)
                    )
                }
            }
        }

        // --- TARJETA PRINCIPAL: EN PANTALLA ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            border = BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.6f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF2E3D59),
                                Color(0xFF1E293B)
                            ),
                            radius = 900f
                        )
                    )
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "EN PANTALLA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp,
                        color = Color(0xFF818CF8)
                    )
                }

                Text(
                    text = if (movie.titulo.isNotBlank()) movie.titulo else "Sin título",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                if (movie.genero.isNotBlank()) {
                    Surface(
                        color = Color(0xFF312E81).copy(alpha = 0.6f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFF6366F1).copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = movie.genero.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFA5B4FC),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Text(
                    text = if (movie.descripcion.isNotBlank()) movie.descripcion else "Selecciona una película para ver su descripción...",
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = Color(0xFF94A3B8)
                )

                HorizontalDivider(color = Color(0xFF334155).copy(alpha = 0.5f), thickness = 1.dp)

                val (colorEstado, fondoEstado, textoEstado) = when (movie.estado.lowercase()) {
                    "reproduciendo" -> Triple(Color(0xFF10B981), Color(0xFF064E3B), "▶ REPRODUCIENDO")
                    "pausado" -> Triple(Color(0xFFF59E0B), Color(0xFF78350F), "⏸ PAUSADO")
                    "detenido" -> Triple(Color(0xFFEF4444), Color(0xFF7F1D1D), "⏹ DETENIDO")
                    else -> Triple(Color(0xFF94A3B8), Color(0xFF1E293B), movie.estado.ifBlank { "SIN ESTADO" }.uppercase())
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Estado:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF64748B)
                    )
                    Surface(
                        color = fondoEstado.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, colorEstado.copy(alpha = 0.8f))
                    ) {
                        Text(
                            text = textoEstado,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorEstado,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // --- CONTROLES DE REPRODUCCIÓN ---
        Text(
            text = "Controles",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE2E8F0)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Botón Reproducir
            Button(
                onClick = { repository.actualizarPelicula(mapOf("estado" to "reproduciendo")) },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (movie.estado.equals("reproduciendo", true)) Color(0xFF10B981) else Color(0xFF1E293B)
                ),
                border = BorderStroke(
                    1.dp,
                    if (movie.estado.equals("reproduciendo", true)) Color(0xFF34D399) else Color(0xFF334155)
                )
            ) {
                Text(
                    text = "▶ Play",
                    fontWeight = FontWeight.Bold,
                    color = if (movie.estado.equals("reproduciendo", true)) Color.White else Color(0xFF10B981)
                )
            }

            // Botón Pausar
            Button(
                onClick = { repository.actualizarPelicula(mapOf("estado" to "pausado")) },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (movie.estado.equals("pausado", true)) Color(0xFFF59E0B) else Color(0xFF1E293B)
                ),
                border = BorderStroke(
                    1.dp,
                    if (movie.estado.equals("pausado", true)) Color(0xFFFBBF24) else Color(0xFF334155)
                )
            ) {
                Text(
                    text = "⏸ Pausa",
                    fontWeight = FontWeight.Bold,
                    color = if (movie.estado.equals("pausado", true)) Color.White else Color(0xFFF59E0B)
                )
            }

            // Botón Detener
            Button(
                onClick = { repository.actualizarPelicula(mapOf("estado" to "detenido")) },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (movie.estado.equals("detenido", true)) Color(0xFFEF4444) else Color(0xFF1E293B)
                ),
                border = BorderStroke(
                    1.dp,
                    if (movie.estado.equals("detenido", true)) Color(0xFFF87171) else Color(0xFF334155)
                )
            ) {
                Text(
                    text = "⏹ Stop",
                    fontWeight = FontWeight.Bold,
                    color = if (movie.estado.equals("detenido", true)) Color.White else Color(0xFFEF4444)
                )
            }
        }

        // --- CATÁLOGO DE PELÍCULAS ---
        Text(
            text = "Seleccionar Película",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE2E8F0)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            catalogo.forEach { item ->
                val isSelected = movie.titulo.equals(item.titulo, ignoreCase = true)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            repository.actualizarPelicula(
                                mapOf(
                                    "titulo" to item.titulo,
                                    "genero" to item.genero,
                                    "descripcion" to item.descripcion
                                )
                            )
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF1E293B) else Color(0xFF131B2A)
                    ),
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) Color(0xFF6366F1) else Color(0xFF1E293B)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(46.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) Color(0xFF4F46E5).copy(alpha = 0.2f) else Color(0xFF1E293B)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = item.icon, fontSize = 22.sp)
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.titulo,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else Color(0xFFCBD5E1)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.genero,
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }

                        if (isSelected) {
                            Surface(
                                color = Color(0xFF4F46E5),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "ACTIVA",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}