package com.epam.healthmonitor.android.model;

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date

class Metric(@JsonProperty(value = "agent_name")
             val agentName: String,
             @JsonProperty(value = "date")
             val date: Date,
             @JsonProperty(value = "metric_name")
             val metricName: String,
             @JsonProperty(value = "state")
             val state: String,
             @JsonProperty(value = "value")
             val value: String
)