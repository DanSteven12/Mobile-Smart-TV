package com.example.moviecontrol

import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepository {
    private val db = FirebaseFirestore.getInstance()
    private val docRef = db.document("control/pelicula_actual")

    // Lee el documento en tiempo real y avisa si hay cambios
    fun leerPelicula(onUpdate: (Movie?) -> Unit) {
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                onUpdate(null)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                // Firebase convierte los datos directamente al modelo Movie
                val movie = snapshot.toObject(Movie::class.java)
                onUpdate(movie)
            }
        }
    }

    // Actualiza campos específicos en la nube (ej: solo el estado, o solo el titulo)
    fun actualizarPelicula(datosAModificar: Map<String, Any>) {
        docRef.update(datosAModificar)
    }
}
