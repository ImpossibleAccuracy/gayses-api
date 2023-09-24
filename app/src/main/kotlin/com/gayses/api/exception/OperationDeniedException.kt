package com.gayses.api.exception

class OperationDeniedException : ServiceException {
    constructor() : super()
    constructor(message: String?) : super(message)
}