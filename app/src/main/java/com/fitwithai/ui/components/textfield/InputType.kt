package com.fitwithai.ui.components.textfield

import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType

sealed class InputType(
    val keyboardType: KeyboardType,
    val capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
) {
    data object Text : InputType(
        keyboardType = KeyboardType.Text,
        capitalization = KeyboardCapitalization.Sentences,
    )

    data object Email : InputType(
        keyboardType = KeyboardType.Email,
    )

    data object Password : InputType(
        keyboardType = KeyboardType.Password,
    )

    data object Number : InputType(
        keyboardType = KeyboardType.Number,
    )

    data object Decimal : InputType(
        keyboardType = KeyboardType.Decimal,
    )

    data object Phone : InputType(
        keyboardType = KeyboardType.Phone,
    )

    data object Name : InputType(
        keyboardType = KeyboardType.Text,
        capitalization = KeyboardCapitalization.Words,
    )
}
