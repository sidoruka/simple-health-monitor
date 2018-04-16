using System;
using System.Collections.Generic;

namespace health.mobile.Models
{
    public class Agent : List<Metric>
    {
        public string Name { get; set; }
        public Agent()
        {
        }
    }
}
