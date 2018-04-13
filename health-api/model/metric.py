from model.validation_config import ValidationConfig
from datetime import datetime

CURRENT_METRICS={}

class Metric():
    def __init__(self, agent_name, metric_name, value):
        self.agent_name = agent_name
        self.metric_name = metric_name
        self.value = value
        self.state = ValidationConfig.validate(self.metric_id, value)
        self.date = datetime.now().isoformat()
    
    @property
    def metric_id(self):
        return '{}.{}'.format(self.agent_name, self.metric_name)

    @classmethod
    def create_or_update(cls, item):
        CURRENT_METRICS[item.metric_id] = item

    @classmethod
    def get_list(cls):
        return list(CURRENT_METRICS.values())
