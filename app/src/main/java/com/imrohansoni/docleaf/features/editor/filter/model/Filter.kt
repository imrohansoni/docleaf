package com.imrohansoni.docleaf.features.editor.filter.model

// This class represents all the types of filter that are available in the app
enum class FilterType(val displayName : String){
    NONE("None"),
    MAGIC("Magic"),
    GRAYSCALE("Grayscale"),
    SEPIA("Sepia"),
    VINTAGE("Vintage"),
}

// Filter state will hold the current state of the filter
data class FilterState(
    val type : FilterType,
    val intensity : Float
)