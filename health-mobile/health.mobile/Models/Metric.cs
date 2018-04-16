using System;
using Newtonsoft.Json;

namespace health.mobile.Models
{
    public class Metric
    {
        [JsonProperty(PropertyName = "agent_name")]
        public string AgentName { get; set; }
        [JsonProperty(PropertyName = "metric_name")]
        public string Name { get; set; }
        [JsonProperty(PropertyName = "value")]
        public string Value { get; set; }
        [JsonProperty(PropertyName = "state")]
        public string State { get; set; }
        [JsonProperty(PropertyName = "date")]
        public DateTime Date { get; set; }
        [JsonIgnore]
        public string DateString {
            get {
                return Date.ToString("dd.MM.yyyy HH:mm:ss");
            }
        }

        public Metric()
        {
        }
    }
}
