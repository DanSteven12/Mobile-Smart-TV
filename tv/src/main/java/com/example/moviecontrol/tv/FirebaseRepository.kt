package com.example.moviecontrol.tv

import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepository {
    private val db = FirebaseFirestore.getInstance()
    private val docRef = db.document("control/pelicula_actual")

    // Solo necesitamos escuchar los cambios en la TV
    fun leerPelicula(onUpdate: (Movie?) -> Unit) {
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                onUpdate(null)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val movie = snapshot.toObject(Movie::class.java)
                onUpdate(movie)
            }
        }
    }
}
