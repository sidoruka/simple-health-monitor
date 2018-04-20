package com.epam.healthmonitor.android

fun schedule(timeoutInSeconds: Int, task: () -> Unit) {
    val timeoutInMillis = timeoutInSeconds * 1000;
    Thread {
        while (true) {
            task()
            Thread.sleep(timeoutInMillis.toLong())
        }
    }.start()
}