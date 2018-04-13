HEALTH_API_URL=$1
HEALTH_SERVICE_NAME=$2

if [ -z $HEALTH_API_URL ]; then
    echo "[ERROR] Health API URL is not set. Exiting"
    exit 1
fi

if [ -z $HEALTH_SERVICE_NAME ]; then
    echo "[ERROR] Health service name is not set. Exiting"
    exit 1
fi

# CPU
#####
HEALTH_USED_CPU_PERCENT_NAME='cpu.used.percent'
HEALTH_USED_CPU_PERCENT_VALUE=$(cat <(grep 'cpu ' /proc/stat) <(sleep 1 && grep 'cpu ' /proc/stat) | awk -v RS="" '{print ($13-$2+$15-$4)*100/($13-$2+$15-$4+$16-$5)}')

curl -X PUT \
  "$HEALTH_API_URL" \
  -H 'content-type: application/json' \
  -d "{\"agent_name\":\"$HEALTH_SERVICE_NAME\", \"metric_name\":\"$HEALTH_USED_CPU_PERCENT_NAME\", \"value\": \"$HEALTH_USED_CPU_PERCENT_VALUE\"}"

# MEM
#####
HEALTH_USED_MEM_PERCENT_NAME='mem.used.percent'
HEALTH_USED_MEM_PERCENT_VALUE=$(free | grep Mem | awk '{print $3/$2 * 100.0}')

curl -X PUT \
  "$HEALTH_API_URL" \
  -H 'content-type: application/json' \
  -d "{\"agent_name\":\"$HEALTH_SERVICE_NAME\", \"metric_name\":\"$HEALTH_USED_MEM_PERCENT_NAME\", \"value\": \"$HEALTH_USED_MEM_PERCENT_VALUE\"}"

