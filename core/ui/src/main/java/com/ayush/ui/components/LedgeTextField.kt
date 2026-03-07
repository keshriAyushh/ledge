package com.ayush.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ayush.ui.theme.BgCard
import com.ayush.ui.theme.BgCard2
import com.ayush.ui.theme.BorderSubtle
import com.ayush.ui.theme.Gold
import com.ayush.ui.theme.LedgeRadius
import com.ayush.ui.theme.LedgeTextStyle
import com.ayush.ui.theme.RedDim
import com.ayush.ui.theme.SemanticRed
import com.ayush.ui.theme.TextMuted
import com.ayush.ui.theme.TextMuted2
import com.ayush.ui.theme.TextPrimary

@Composable
fun LedgeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor by animateColorAsState(
        targetValue = when {
            isError -> SemanticRed
            isFocused -> Gold
            else -> BorderSubtle
        },
        animationSpec = tween(200),
        label = "borderColor",
    )

    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                style = LedgeTextStyle.Caption.copy(
                    color = if (isError) SemanticRed else TextMuted2,
                    letterSpacing = 0.8.sp,
                ),
                modifier = Modifier.padding(bottom = 6.dp),
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    style = LedgeTextStyle.Body.copy(color = TextMuted),
                )
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError,
            singleLine = singleLine,
            enabled = enabled,
            interactionSource = interactionSource,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            textStyle = LedgeTextStyle.Body.copy(color = TextPrimary),
            shape = RoundedCornerShape(LedgeRadius.medium),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = BgCard2,
                unfocusedContainerColor = BgCard,
                disabledContainerColor = BgCard,
                errorContainerColor = RedDim,
                focusedBorderColor = Gold,
                unfocusedBorderColor = BorderSubtle,
                errorBorderColor = SemanticRed,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                cursorColor = Gold,
                focusedLeadingIconColor = Gold,
                unfocusedLeadingIconColor = TextMuted,
                focusedTrailingIconColor = Gold,
                unfocusedTrailingIconColor = TextMuted,
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        if (isError && !errorMessage.isNullOrEmpty()) {
            LedgeErrorText(
                message = errorMessage,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp),
            )
        }
    }
}

@Composable
fun LedgeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier,
    placeholder: String,
    isError: Boolean,
    errorMessage: String?,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    trailingIcon: @Composable (() -> Unit)?,
    visualTransformation: VisualTransformation,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                style = LedgeTextStyle.Caption.copy(
                    color = if (isError) SemanticRed else TextMuted2,
                    letterSpacing = 0.8.sp,
                ),
                modifier = Modifier.padding(bottom = 6.dp),
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    style = LedgeTextStyle.Body.copy(color = TextMuted),
                )
            },
            trailingIcon = trailingIcon,
            isError = isError,
            singleLine = true,
            interactionSource = interactionSource,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            textStyle = LedgeTextStyle.Body.copy(color = TextPrimary),
            shape = RoundedCornerShape(LedgeRadius.medium),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = BgCard2,
                unfocusedContainerColor = BgCard,
                errorContainerColor = RedDim,
                focusedBorderColor = Gold,
                unfocusedBorderColor = BorderSubtle,
                errorBorderColor = SemanticRed,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                cursorColor = Gold,
                focusedTrailingIconColor = Gold,
                unfocusedTrailingIconColor = TextMuted,
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        if (isError && !errorMessage.isNullOrEmpty()) {
            LedgeErrorText(
                message = errorMessage,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp),
            )
        }
    }
}