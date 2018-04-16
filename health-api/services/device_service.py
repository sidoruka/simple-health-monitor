from flask_restful import reqparse, Resource
from flask import jsonify
from model.device import Device

class DeviceService(Resource):
    device_parser = reqparse.RequestParser()
    device_parser.add_argument('device_id', type=str, required=True)
    device_parser.add_argument('device_name', type=str, required=True)

    def get(self):
        return jsonify([m.serialize() for m in Device.get_list()])

    def put(self):
        args = self.device_parser.parse_args()
        device_item = Device(args['device_id'],
                            args['device_name'])

        Device.create_or_update(device_item)
        return device_item.device_id, 201