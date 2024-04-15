package com.yan.source.exception

import java.lang.RuntimeException

class ExistException(private val msg:String):RuntimeException(msg)