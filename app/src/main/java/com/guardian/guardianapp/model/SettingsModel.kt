package com.guardian.guardianapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SettingsModel(
    val msgSos: String
): Parcelable