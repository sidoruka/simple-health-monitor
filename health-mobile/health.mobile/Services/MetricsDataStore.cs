using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Threading.Tasks;
using Newtonsoft.Json;
using health.mobile.Models;
using Plugin.Connectivity;

namespace health.mobile.Services
{
    public class MetricsDataStore
    {

        HttpClient httpClient;
        IEnumerable<Agent> items;

        public MetricsDataStore()
        {
            httpClient = new HttpClient();
            httpClient.BaseAddress = new Uri($"https://pipe.opensource.epam.com");
        }

        public async Task<IEnumerable<Agent>> GetItemsAsync(bool forceRefresh = false)
        {
            if (forceRefresh && CrossConnectivity.Current.IsConnected)
            {
                var json = await httpClient.GetStringAsync($"metrics");
                json = json.Trim();
                if (json.StartsWith("\""))
                {
                    json = json.Substring(1);
                }
                if (json.EndsWith("\""))
                {
                    json = json.Substring(0, json.Length - 1);
                }
                json = json.Replace("\\\\\\", "\\");
                json = json.Replace("\\\"", "\"");
                var metrics = await Task.Run(() => JsonConvert.DeserializeObject<IEnumerable<Metric>>(json));
                var agents = new List<Agent>();
                foreach (Metric metric in metrics)
                {
                    Agent agent = agents.Find(a => a.Name == metric.AgentName);
                    if (agent == null)
                    {
                        agent = new Agent();
                        agent.Name = metric.AgentName;
                        agents.Add(agent);
                    }
                    agent.Add(metric);
                }
                items = agents;
            }

            return items;
        }
    }
}
