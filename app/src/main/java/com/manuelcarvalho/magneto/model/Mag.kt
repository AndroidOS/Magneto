package com.manuelcarvalho.magneto.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Mag(
    val reading: Double
) {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}