import click
import os
import json
from flask import Flask
from flask_restful import Api, Resource
from services.metric_service import MetricService
from services.device_service import DeviceService
from model.validation_config import ValidationConfig
from model.notifier import HM_FCMNotifier

@click.group()
@click.version_option(prog_name='Health Monitor API')
def cli():
    pass

@cli.command()
@click.option('-v', '--verbose', is_flag=True)
@click.option('-p', '--port', required=False, type=int, default=8080)
@click.option('-c', '--config', required=False, type=str)
def start(verbose, port, config):
    if config:
        ValidationConfig.init_from_file(config)
    app = Flask(__name__)
    api = Api(app)
    api.add_resource(MetricService, '/metrics')
    api.add_resource(DeviceService, '/devices')
    app.run(debug=verbose, port=port, host='0.0.0.0')