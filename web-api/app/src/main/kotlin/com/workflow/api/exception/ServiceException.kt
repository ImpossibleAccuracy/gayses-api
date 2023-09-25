package com.workflow.api.exception

abstract class ServiceException : RuntimeException {
    constructor() : super()
    constructor(message: String?) : super(message)
}