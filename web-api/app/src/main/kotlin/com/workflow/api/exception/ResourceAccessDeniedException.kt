package com.workflow.api.exception

class ResourceAccessDeniedException : ServiceException {
    constructor() : super()
    constructor(message: String?) : super(message)
}