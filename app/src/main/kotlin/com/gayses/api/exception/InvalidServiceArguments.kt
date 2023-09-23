package com.gayses.api.exception

class InvalidServiceArguments : ServiceException {
    constructor() : super()
    constructor(message: String?) : super(message)
}