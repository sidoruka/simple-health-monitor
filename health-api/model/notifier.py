import os
from pyfcm import FCMNotification
from model.device import Device

NOTIFICATION_SUBJECT_TEMPLATE="{agent}"
NOTIFICATION_BODY_TEMPLATE="{metric} changed from {old_state} to {new_state}"

class HM_FCMNotifier():
    
    @classmethod
    def notify(cls, metric_old, metric_new):
        if not metric_old or not metric_new or metric_new.state == metric_old.state:
            return

        api_key = os.getenv("HM_FRC_API_KEY")

        push_service = FCMNotification(api_key=api_key)

        registration_ids = [d.device_id for d in Device.get_list()]
        if len(registration_ids) == 0:
            return

        message_title = NOTIFICATION_SUBJECT_TEMPLATE.format(agent=metric_new.agent_name)
        message_body = NOTIFICATION_BODY_TEMPLATE.format(metric=metric_new.metric_name, 
                                                         old_state=metric_old.state, 
                                                         new_state=metric_new.state)
        result = push_service.notify_multiple_devices(registration_ids=registration_ids, message_title=message_title, message_body=message_body)

        print (result)