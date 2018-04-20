package com.epam.healthmonitor.android.task

fun <Result> doAsync(backgroundTask: () -> Result)
        : DistributedTask<Result> =
        DistributedTask(backgroundTask)