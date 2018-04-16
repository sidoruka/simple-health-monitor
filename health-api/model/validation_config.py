import json
import os
import click

class ValidationConfig():
    INSTANCE = None

    def __init__(self):
        self.validators = {}

    @classmethod
    def init_from_file(cls, config_path):
        if not os.path.exists(config_path):
            raise Exception('Configuration file {} not found'.format(config_path))

        config = ValidationConfig()
        with open(config_path, 'r') as config_file_stream:
            config_data = json.load(config_file_stream)
            for config_item in config_data:
                if 'metric_id' not in config_item:
                    click.echo('metric_id not set, skipping config item')
                    continue
                if 'constraint' not in config_item:
                    click.echo('constraint not set, skipping config item')
                    continue
                if 'constraint_type' not in config_item:
                    config_item['constraint_type'] = 'less'
        
                config.validators[config_item['metric_id']] = config_item
        cls.INSTANCE = config

                
    @classmethod
    def validate(cls, metric_id, value):
        if cls.INSTANCE and (len(cls.INSTANCE.validators) == 0 or metric_id not in cls.INSTANCE.validators):
            return 'UNKNOWN'

        config = cls.INSTANCE.validators[metric_id]
        if config['constraint_type'] == 'eq':
            return 'OK' if str(value) == str(config['constraint']) else 'FAIL'
        elif config['constraint_type'] == 'less':
            return 'OK' if float(value) < float(config['constraint']) else 'FAIL'
        else:
            return 'UNKNOWN'

        
