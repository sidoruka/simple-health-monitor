package com.epam.healthmonitor.android

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.ContextCompat.getColor
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.epam.healthmonitor.android.model.Parameter
import com.epam.healthmonitor.android.model.Service
import java.text.SimpleDateFormat
import java.util.*

class ServicesAdapter(private var services: List<Service>)
    : RecyclerView.Adapter<ServicesAdapter.ViewHolder>() {

    private val percentagesSuffix = ".used.percent"
    private val fontSize = 18F
    private val mutedFontSize = 11F
    private val dateFormatter = SimpleDateFormat("MMM d, HH:mm", Locale.US)

    class ViewHolder(val rootView: View) : RecyclerView.ViewHolder(rootView) {
        val cardView = rootView.findViewById<CardView>(R.id.service_card)!!
        val name = cardView.findViewById<TextView>(R.id.service_name)!!
        val status = cardView.findViewById<TextView>(R.id.service_status)!!
        val parameters = cardView.findViewById<TableLayout>(R.id.parameters_table_view)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.service_layout, parent, false)
                .let { ViewHolder(it) }

    override fun getItemCount(): Int = services.size

    override fun onBindViewHolder(serviceView: ViewHolder, position: Int) {
        val context = serviceView.rootView.context

        services
            .getOrNull(position)
            ?.also { service ->
                val status = retrieveServiceStatus(service)
                serviceView.name.text = service.name
                serviceView.status.text = status
                serviceView.status.setTextColor(getColor(context, retrieveStatusColorCode(status)))
                serviceView.parameters.removeAllViews()
                service.parameters.forEach { parameter ->
                    serviceView.parameters.addView(parameterRow(context, parameter))
                    serviceView.parameters.addView(parameterDateRow(context, parameter))
                }
            }
    }

    private fun retrieveServiceStatus(service: Service) =
            if (service.parameters.any { it.state == "FAIL" }) "FAIL" else "OK"

    private fun parameterRow(context: Context, parameter: Parameter): TableRow =
            TableRow(context).also {
                it.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
                it.addView(parameterTitle(context, parameter, fontSize))
                it.addView(parameterValue(context, parameter, fontSize))
                it.addView(parameterStatus(context, parameter, fontSize))
            }

    private fun parameterTitle(context: Context, parameter: Parameter, fontSize: Float): TextView =
            TextView(context).also {
                it.text = parameter.name.let {
                    if (it.endsWith(percentagesSuffix))
                        it.dropLast(percentagesSuffix.length)
                    else it
                }
                it.textSize = fontSize
                it.setAllCaps(true)
                it.typeface = Typeface.DEFAULT_BOLD
            }

    private fun parameterValue(context: Context, parameter: Parameter, fontSize: Float): TextView =
            TextView(context).also {
                it.text = parameter.value.let {
                    if (parameter.name.endsWith(percentagesSuffix))
                        it.toDouble().toPercentages()
                    else it
                }
                it.textSize = fontSize
                it.textAlignment = View.TEXT_ALIGNMENT_CENTER
            }

    private fun parameterStatus(context: Context, parameter: Parameter, fontSize: Float): TextView =
            TextView(context).also {
                it.text = parameter.state
                it.textSize = fontSize
                it.setTextColor(getColor(context, retrieveStatusColorCode(parameter.state)))
            }

    private fun parameterDateRow(context: Context, parameter: Parameter): TableRow =
            TableRow(context).also {
                it.addView(TextView(context).also {
                    it.text = dateFormatter.format(parameter.date)
                    it.textSize = mutedFontSize
                    it.setTextColor(getColor(context, R.color.mutedText))
                })
            }

    private fun retrieveStatusColorCode(status: String): Int =
            if (status == "OK") R.color.okStatus else R.color.failStatus
}
