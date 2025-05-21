package com.nima.upquizz.network.models.errors

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

data class ValidationError(
    val detail: List<Detail>
){
    fun hasUsernameError(): Boolean{
        val hasError = detail.filter {
            it.loc[1] == "username"
        }
        return hasError.isNotEmpty()
    }

    @Composable
    fun usernameError(): (@Composable() () -> Unit)?{
        if (this.hasUsernameError()){
            val error = detail.filter {
                it.loc[1] == "username"
            }
            return { Text(error[0].msg) }
        }
        return null
    }

    fun hasDisplayNameError(): Boolean{
        val hasError = detail.filter {
            it.loc[1] == "display_name"
        }
        return hasError.isNotEmpty()
    }

    @Composable
    fun displayNameError(): (@Composable() () -> Unit)?{
        if (this.hasDisplayNameError()){
            val error = detail.filter {
                it.loc[1] == "display_name"
            }
            return { Text(error[0].msg) }
        }
        return null
    }

    fun hasAboutError(): Boolean{
        val hasError = detail.filter {
            it.loc[1] == "about"
        }
        return hasError.isNotEmpty()
    }

    @Composable
    fun aboutError(): (@Composable() () -> Unit)?{
        if (this.hasAboutError()){
            val error = detail.filter {
                it.loc[1] == "about"
            }
            return { Text(error[0].msg) }
        }
        return null
    }

    fun hasPasswordError(): Boolean{
        val hasError = detail.filter {
            it.loc[1] == "password"
        }
        return hasError.isNotEmpty()
    }

    @Composable
    fun passwordError(): (@Composable() () -> Unit)?{
        if (this.hasPasswordError()){
            val error = detail.filter {
                it.loc[1] == "password"
            }
            return { Text(error[0].msg) }
        }
        return null
    }
}