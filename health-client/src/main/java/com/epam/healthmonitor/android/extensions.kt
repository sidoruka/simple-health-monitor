package com.epam.healthmonitor.android

fun Double.toPercentages(): String = "${toInt()} %"

fun Long.toMegabytes(): String = "${this / 1024} Mb"