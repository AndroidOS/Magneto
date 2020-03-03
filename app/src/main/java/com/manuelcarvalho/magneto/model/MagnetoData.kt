package com.manuelcarvalho.magneto.model


data class Imo
    (
    val iaga_code: String?,
    val name: String?,
    val coordinates: List<Double>?
)

data class Intermagnet
    (
    val imo: Imo?,
    val reported_orientation: String?,
    val sensor_orientation: String?,
    val data_type: String?,
    val sampling_period: Int?,
    val digital_sampling_rate: Double?
)

data class Metadata
    (
    val intermagnet: Intermagnet?,
    val status: Int?,
    val generated: String?,
    val url: String?,
    val api: String?
)

data class Metadata2
    (
    val element: String?,
    val network: String?,
    val station: String?,
    val channel: String?,
    val location: String?,
    val flag: String?
)

data class Value
    (
    val id: String?,
    val metadata: Metadata2?,
    val values: List<Double>?
)

data class MagnetoData
    (
    val type: String?,
    val metadata: Metadata?,
    val times: List<String>?,
    val values: List<Value>?
)
