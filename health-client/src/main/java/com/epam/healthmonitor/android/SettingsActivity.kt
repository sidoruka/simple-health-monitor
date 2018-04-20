package com.epam.healthmonitor.android

import android.os.Bundle
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.preference.Preference
import android.util.Log
import android.util.Patterns
import com.epam.healthmonitor.android.task.registerClient

class SettingsActivity : AppCompatPreferenceActivity() {

    companion object {
        const val TAG = "Settings activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        fragmentManager
            .beginTransaction()
            .replace(android.R.id.content, MainPreferenceFragment())
            .commit()
    }

    class MainPreferenceFragment : PreferenceFragment() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preferences)

            findPreference(getString(R.string.endpoint_key)).also { endpoint ->
                endpoint.onPreferenceChangeListener = EndpointPreferenceChangeListener(this)
                endpoint.summary =
                        PreferenceManager
                            .getDefaultSharedPreferences(endpoint.context)
                            .getString(endpoint.key, getString(R.string.endpoint_default_value))
            }
        }
    }

    class EndpointPreferenceChangeListener(private val preferenceFragment: PreferenceFragment)
        : Preference.OnPreferenceChangeListener {

        override fun onPreferenceChange(preference: Preference,
                                        newValue: Any
        ): Boolean {
            val endpoint = retrieveEndpointUrl(newValue.toString())
            val valid = Patterns.WEB_URL.matcher(endpoint).matches()
            if (valid) {
                preference.summary = endpoint

                PreferenceManager
                    .getDefaultSharedPreferences(preference.context)
                    .getString(preference.context.getString(R.string.firebase_token_key), null)
                    ?.also { firebaseToken ->
                        registerClient(
                                endpoint,
                                firebaseToken,
                                firebaseTopic = preference.context.getString(R.string.firebase_topic)
                        ) andThen { registered ->
                            preferenceFragment.view?.also { view ->
                                if (registered) {
                                    showSnackbar(view, "Client was registered and subscribed")
                                } else {
                                    showSnackbar(view, "Client was not registered")
                                }
                            }
                        }
                    }
                        ?: Log.d(TAG, "There is no firebase token to register client")
            } else {
                preferenceFragment.view?.also { view ->
                    showSnackbar(view, "Given endpoint is not a valid url")
                }
            }
            return valid
        }

        private fun retrieveEndpointUrl(newValue: String): String =
                if (newValue.endsWith("/")) newValue.trim()
                else newValue.trim().plus("/")

    }

}
