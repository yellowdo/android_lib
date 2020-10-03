package com.yellowdo.library.ext

inline fun <T> List<T>.catchItem(
    firstCondition: () -> Boolean = { false },
    predicate: (T) -> Boolean,
    action: (T?) -> Unit
) {
    when {
        isEmpty() -> action(null)
        firstCondition() -> action(get(0))
        else -> run out@{
            forEachIndexed { _, item ->
                if (predicate(item)) {
                    action(item)
                    return@out
                }
            }
            action(get(0))
        }
    }
}