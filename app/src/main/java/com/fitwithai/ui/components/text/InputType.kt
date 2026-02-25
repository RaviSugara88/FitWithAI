package com.fitwithai.ui.components.text

import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType

sealed class InputType(
    val keyboardType: KeyboardType,
    val capitalization: KeyboardCapitalization = KeyboardCapitalization.Companion.None
) {
    data object Text : InputType(
        keyboardType = KeyboardType.Companion.Text,
        capitalization = KeyboardCapitalization.Companion.Sentences
    )

    data object Email : InputType(
        keyboardType = KeyboardType.Companion.Email
    )

    data object Password : InputType(
        keyboardType = KeyboardType.Companion.Password
    )

    data object Number : InputType(
        keyboardType = KeyboardType.Companion.Number
    )

    data object Decimal : InputType(
        keyboardType = KeyboardType.Companion.Decimal
    )

    data object Phone : InputType(
        keyboardType = KeyboardType.Companion.Phone
    )

    data object Name : InputType(
        keyboardType = KeyboardType.Companion.Text,
        capitalization = KeyboardCapitalization.Companion.Words
    )
}