package com.fitwithai.common.res

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

sealed class AppString {

    data class Resource(
        @StringRes val resId: Int,
        val args: List<Any> = emptyList()
    ) : AppString()

    data class Plural(
        @PluralsRes val resId: Int,
        val quantity: Int,
        val args: List<Any> = emptyList()
    ) : AppString()

    data class Dynamic(
        val value: String
    ) : AppString()

    companion object {

        fun from(@StringRes resId: Int, vararg args: Any) =
            Resource(resId, args.toList())

        fun plural(@PluralsRes resId: Int, quantity: Int, vararg args: Any) =
            Plural(resId, quantity, args.toList())

        fun dynamic(value: String) =
            Dynamic(value)
    }
}