package com.fitwithai.ui.components.text

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.*
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,

    inputType: InputType = InputType.Text,

    readOnly: Boolean = false,
    enabled: Boolean = true,
    clickable: Boolean = false,
    onClick: (() -> Unit)? = null,

    isError: Boolean = false,
    errorMessage: String? = null,

    singleLine: Boolean = true,
    maxLines: Int = 1,

    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,

    imeAction: ImeAction = ImeAction.Done,
    onImeAction: (() -> Unit)? = null,

    outlined: Boolean = true
) {

    var passwordVisible by remember { mutableStateOf(false) }

    val keyboardOptions = KeyboardOptions(
        keyboardType = inputType.keyboardType,
        capitalization = inputType.capitalization,
        imeAction = imeAction
    )

    val keyboardActions = KeyboardActions(
        onDone = { onImeAction?.invoke() },
        onNext = { onImeAction?.invoke() }
    )

    val visualTransformation = when (inputType) {
        is InputType.Password ->
            if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation()
        else -> VisualTransformation.None
    }

    val trailing = when {
        inputType is InputType.Password -> {
            @androidx.compose.runtime.Composable {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff,
                        contentDescription = "Toggle Password"
                    )
                }
            }
        }

        trailingIcon != null -> {
            { Icon(trailingIcon, contentDescription = null) }
        }

        else -> null
    }

    val interactionSource = remember { MutableInteractionSource() }

    val clickModifier = if (clickable && readOnly) {
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = null
        ) {
            onClick?.invoke()
        }
    } else Modifier

    if (outlined) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .then(clickModifier),
            label = label?.let { { Text(it) } },
            placeholder = placeholder?.let { { Text(it) } },
            readOnly = readOnly,
            enabled = enabled,
            isError = isError,
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            leadingIcon = leadingIcon?.let { { Icon(it, null) } },
            trailingIcon = trailing
        )
    } else {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .then(clickModifier),
            label = label?.let { { Text(it) } },
            placeholder = placeholder?.let { { Text(it) } },
            readOnly = readOnly,
            enabled = enabled,
            isError = isError,
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            leadingIcon = leadingIcon?.let { { Icon(it, null) } },
            trailingIcon = trailing
        )
    }

    if (isError && errorMessage != null) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelSmall
        )
    }
}