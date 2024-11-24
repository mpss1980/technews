package br.com.alura.technewsupdated.repositories

open class Resource<T>(
    val data: T?,
    val error: String? = null
)

class ResourceSuccess<T>(data: T) : Resource<T>(data)

class ResourceError<T>(error: String) : Resource<T>(null, error)