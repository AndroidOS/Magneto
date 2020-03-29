package com.manuelcarvalho.magneto.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Mag(
    val reading: Double,
    val date: String
) {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}