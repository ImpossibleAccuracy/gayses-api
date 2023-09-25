package com.gayses.tests.ext

import java.util.stream.Stream

fun <T> streamOf(vararg items: T): Stream<T> {
    return items.toList().stream()
}
