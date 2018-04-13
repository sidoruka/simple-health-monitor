FROM ubuntu:16.04

ENV LC_ALL=C.UTF-8 \
    LANG=C.UTF-8

RUN apt update && \
    apt install -y  curl \
                    git \
                    python3 \
                    python3-pip

ADD health-api /opt/health-api

RUN cd /opt/health-api && \
    pip3 install . -I

EXPOSE 8888

CMD hm-api start -p 8888 -c /opt/health-api/sample_config.json