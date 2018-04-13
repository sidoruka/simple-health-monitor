from flask_restful import reqparse, Resource
from model.metric import Metric
from util.json_util import serialize_to_json

class MetricService(Resource):
    metric_parser = reqparse.RequestParser()
    metric_parser.add_argument('agent_name', type=str, required=True)
    metric_parser.add_argument('metric_name', type=str, required=True)
    metric_parser.add_argument('value', type=str, required=True)

    def get(self):
        return serialize_to_json(Metric.get_list())

    def put(self):
        args = self.metric_parser.parse_args()
        metric_item = Metric(args['agent_name'],
                            args['metric_name'],
                            args['value'])

        Metric.create_or_update(metric_item)
        return '', 201