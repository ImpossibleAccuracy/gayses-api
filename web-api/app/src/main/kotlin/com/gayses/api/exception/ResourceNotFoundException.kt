package com.gayses.api.exception

class ResourceNotFoundException : ServiceException {
    constructor() : super()
    constructor(message: String?) : super(message)
}