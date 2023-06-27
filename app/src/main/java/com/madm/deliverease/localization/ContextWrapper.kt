package com.madm.deliverease.localization

import android.content.Context
import android.os.LocaleList
import java.util.*


class ContextWrapper(base: Context) : android.content.ContextWrapper(base) {
    companion object {
        fun wrap(context: Context, newLocale: Locale?): ContextWrapper {
            // retrieve app's resources and configuration
            val resources = context.resources
            val configuration = resources.configuration
            val localeList = LocaleList(newLocale)

            // sets up the app's locales and locale
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
            configuration.setLocale(newLocale)

            // override the existing configuration
            val ctx = context.createConfigurationContext(configuration)

            // provide the ContextWrapper to the caller
            return ContextWrapper(ctx)
        }
    }
}
