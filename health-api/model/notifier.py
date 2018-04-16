from pyfcm import FCMNotification

class HM_FCMNotifier():
    
    @classmethod
    def notify(cls, api_key):

        push_service = FCMNotification(api_key=api_key)

        registration_ids = ["id1", "id2"]
        message_title = "Uber update"
        message_body = "Hope you're having fun this weekend, don't forget to check today's news"
        result = push_service.notify_multiple_devices(registration_ids=registration_ids, message_title=message_title, message_body=message_body)

        print (result)