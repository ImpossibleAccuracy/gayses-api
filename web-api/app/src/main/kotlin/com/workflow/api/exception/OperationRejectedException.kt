package com.workflow.api.exception

class OperationRejectedException : ServiceException {
    constructor() : super()
    constructor(message: String?) : super(message)
}