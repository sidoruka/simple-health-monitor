from model.validation_config import ValidationConfig
from model.serializable import Serializable
from model.notifier import HM_FCMNotifier
from datetime import datetime

CURRENT_METRICS={}

class Metric(Serializable):
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
        metric_old = None
        if item.metric_id in CURRENT_METRICS:
            metric_old = CURRENT_METRICS[item.metric_id]

        CURRENT_METRICS[item.metric_id] = item

        HM_FCMNotifier.notify(metric_old, item)

    @classmethod
    def get_list(cls):
        return list(CURRENT_METRICS.values())
