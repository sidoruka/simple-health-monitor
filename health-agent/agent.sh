function log_metric {
  local METRIC_NAME=$1
  local METRIC_VALUE=$2

  curl -X PUT \
    "$HEALTH_API_URL" \
    -H 'content-type: application/json' \
    -d "{\"agent_name\":\"$HEALTH_SERVICE_NAME\", \"metric_name\":\"$METRIC_NAME\", \"value\": \"$METRIC_VALUE\"}" \
    --connect-timeout 5 \
    --max-time 5 \
    -k
}

HEALTH_API_URL=$1
HEALTH_SERVICE_NAME=$2
HEALTH_PING_CONFIG=$3

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

log_metric "$HEALTH_USED_CPU_PERCENT_NAME" "$HEALTH_USED_CPU_PERCENT_VALUE"

# MEM
#####
HEALTH_USED_MEM_PERCENT_NAME='mem.used.percent'
HEALTH_USED_MEM_PERCENT_VALUE=$(free | grep Mem | awk '{print $3/$2 * 100.0}')

log_metric "$HEALTH_USED_MEM_PERCENT_NAME" "$HEALTH_USED_MEM_PERCENT_VALUE"


# DISK
#####
HEALTH_USED_ROOT_FS_PERCENT_NAME='fs.root.used.percent'
HEALTH_USED_ROOT_FS_PERCENT_VALUE=$(df / | awk 'END{print $5}' | sed 's/%//')

log_metric "$HEALTH_USED_ROOT_FS_PERCENT_NAME" "$HEALTH_USED_ROOT_FS_PERCENT_VALUE"

if [ -z $HEALTH_PING_CONFIG ]; then
  echo "Ping config not set, skipping pings"
else
  while IFS='' read -r line || [[ -n "$line" ]]; do
      options=($line)
      _PING_METRIC_NAME=${options[0]}
      _PING_URL=${options[1]}
      _PING_TIMEOUT=${options[2]}
      _PING_METHOD=${options[3]}

      

      if [ -z $_PING_TIMEOUT ]; then
        _PING_TIMEOUT=2
      fi

      if [ -z $_PING_METHOD ]; then
        _PING_METHOD="GET"
      fi

      if [ -z "$HEALTH_METRIC_AUTH" ]; then
        BEARER_HEADER=
      else
        BEARER_HEADER="Authorization: Bearer ${HEALTH_METRIC_AUTH}"
      fi

      _PING_HTTP_CODE=$(curl -X $_PING_METHOD \
                        "$_PING_URL" \
                        --connect-timeout $_PING_TIMEOUT \
                        --max-time $_PING_TIMEOUT \
			-H "${BEARER_HEADER}" \
                        -s \
			-k \
                        -o /dev/null \
                        -w "%{http_code}")

      _PING_EXIT_CODE=$?

      if [ "$_PING_EXIT_CODE" != 0 ]; then 
        _PING_HTTP_CODE="TIMEOUT_OR_GENERIC_ERROR"
      fi 

      log_metric "$_PING_METRIC_NAME" "$_PING_HTTP_CODE"
      
  done < "$HEALTH_PING_CONFIG"
fi
