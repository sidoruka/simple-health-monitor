package com.epam.healthmonitor.android.task

import android.os.AsyncTask

class DistributedTask<Result>(val backgroundTask: () -> Result)
    : AsyncTask<Unit, Unit, Result>() {

    private var foregroundTask: ((Result) -> Unit)? = null

    override fun doInBackground(vararg params: Unit?): Result =
            backgroundTask()

    override fun onPostExecute(result: Result) {
        foregroundTask?.invoke(result)
    }

    infix fun andThen(foregroundTask: (Result) -> Unit) {
        this.foregroundTask = foregroundTask
        execute()
    }
}