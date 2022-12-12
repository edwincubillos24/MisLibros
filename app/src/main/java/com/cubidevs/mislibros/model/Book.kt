package com.cubidevs.mislibros.model

import java.io.Serializable

data class Book(
    var id: String? = null,
    var name: String? = null,
    var author: String? = null,
    var pages: Int? = null,
    var genre: String? = null,
    var urlPhoto: String? = null
) : Serializable
