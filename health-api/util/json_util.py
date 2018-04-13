import json

def obj_dict(obj):
    return obj.__dict__

def serialize_to_json(item):
    return json.dumps(item, default=obj_dict)