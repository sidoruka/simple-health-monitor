import click
import os
import json
from flask import Flask
from flask_restful import Api, Resource
from services.metric_service import MetricService
from model.validation_config import ValidationConfig

@click.group()
@click.version_option(prog_name='Health Monitor API')
def cli():
    pass

@cli.command()
@click.option('-v', '--verbose', is_flag=True)
@click.option('-p', '--port', required=False, type=int, default=8080)
@click.option('-c', '--config', required=False, type=str)
def start(verbose, port, config):
    ValidationConfig.init_from_file(config)
    app = Flask(__name__)
    api = Api(app)
    api.add_resource(MetricService, '/metrics')
    app.run(debug=verbose, port=port)