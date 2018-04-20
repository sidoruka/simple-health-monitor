package com.epam.healthmonitor.android

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.epam.healthmonitor.android.model.Parameter
import com.epam.healthmonitor.android.model.Service
import com.epam.healthmonitor.android.service.Client
import com.epam.healthmonitor.android.task.doAsync
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val servicesList = mutableListOf<Service>()
    private val servicesAdapter = ServicesAdapter(servicesList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipe_container.setOnRefreshListener {
            updateMetrics()
            swipe_container.isRefreshing = false
        }

        services_recycler_view.layoutManager = LinearLayoutManager(this)
        services_recycler_view.adapter = servicesAdapter

        schedule(30) { updateMetrics() }
    }

    private fun updateMetrics() {
        if (isEndpointDefined()) {
            doAsync {
                try {
                    Client.fromPreferences(this)
                        .fetchMetrics()
                        .groupBy { it.agentName }
                        .mapValues { (serviceName, metrics) ->
                            metrics
                                .map { Parameter(it.metricName, it.value, it.state, it.date) }
                                .sortedBy { it.name }
                                .let { Service(serviceName, it) }
                        }
                        .values
                        .sortedBy { it.name }
                        .also {
                            if (it.isNotEmpty()) {
                                servicesList.clear()
                                servicesList.addAll(it)
                            } else {
                                showSnackbar(services_recycler_view, "Metrics weren't received from the server")
                            }
                        }
                } catch (e: Throwable) {
                    Log.d("main_act", "Metrics wasn't received due to: $e")
                    showSnackbar(services_recycler_view, "Metrics weren't received from the server")
                }
            } andThen {
                servicesAdapter.notifyDataSetChanged()
            }
        } else {
            showSnackbar(services_recycler_view, "Please set endpoint in the settings")
        }
    }

    private fun isEndpointDefined(): Boolean {
        val key = getString(R.string.endpoint_key)
        val defaultValue = getString(R.string.endpoint_default_value)
        val value =
                PreferenceManager
                    .getDefaultSharedPreferences(this)
                    .getString(key, defaultValue)
        return value != defaultValue
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when (item.itemId) {
                R.id.action_settings -> {
                    startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}
