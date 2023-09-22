package com.gayses.api.exception

abstract class ServiceException : RuntimeException {
    constructor() : super()
    constructor(message: String?) : super(message)
}