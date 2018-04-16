from model.serializable import Serializable

CURRENT_DEVICES={}

class Device(Serializable):
    def __init__(self, device_id, device_name):
        self.device_id = device_id
        self.device_name = device_name
    
    @classmethod
    def create_or_update(cls, item):
        CURRENT_DEVICES[item.device_id] = item

    @classmethod
    def get_list(cls):
        return list(CURRENT_DEVICES.values())
