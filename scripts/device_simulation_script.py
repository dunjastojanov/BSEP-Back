import sys
import json
import requests
import threading
import time
import keyboard

from datetime import datetime

DEVICE_TYPES = [
    "LIGHT_SWITCH",
    "CAMERA",
    "DOOR_BELL",
    "DOOR_LOCK",
    "LIGHT",
    "THERMOSTAT",
    "AC",
    "BLINDS",
    "REFRIGERATOR",
    "SHOWER",
    "SMART_PLUG",
    "ALARM_SYSTEM",
    "OTHER"
]

MESSAGE_TYPES = {
    0: "INFO",
    1: "WARN",
    2: "ERROR",
    3: "ALARM"
}

MESSAGE_ENDPOINT = "http://localhost:8080/api/device/message"
MESSAGE_ENDPOINT_HTTPS = "https://localhost:8080/api/device/message"

def valueChanged(unit: str, value: int):
    return f"{unit} changed. Value: {value}."

def deviceOn(name, type):
    return f"{name} ({type}) switched on."

def deviceOff(name, type):
    return f"{name} ({type}) switched off."

def deviceError(name, type):
    return f"Device {name} ({type}) encountered error. Switching off... Value: 0."

def print_successfull_message_post(message: dict):
            print(f"""
Message sent successfully:
    deviceId: {message["deviceId"]}
    timestamp: {message["timestamp"]}
    content: {message["content"]}
    deviceMessageType: {message["deviceMessageType"]}
    value: {message["value"]}
""")

# TODO: add requests to endpoint
# TODO: add behaviour of all devices from device type enum

def get_device_message(device, mode):
    t = time.time()
    message_type = "INFO"

    return {
        "deviceId": device["id"],
        "timestamp": int(time.time()),
        "content": f"[{datetime.fromtimestamp(t)}] [{message_type:^6}] {device} sent message in mode {mode}",
        "deviceMessageType": message_type,
        "value": 0
    }
    
def simulate_device(device, messageInterval, regexFilter, mode="normal"):
    global cert_file

    while True:
        if keyboard.is_pressed("q") or keyboard.is_pressed("esc"):
            break
        message = get_device_message(device, mode)

        # post message here

        # r = requests.post(MESSAGE_ENDPOINT_HTTPS, verify=cert_file, json=message)
        r = requests.post(MESSAGE_ENDPOINT, json=message)

        print(r.status_code)

        print_successfull_message_post(message)

        time.sleep(messageInterval)

def simulate(config: dict):
    device_threads = []
    message_interval = config["messageInterval"]
    f = config["filter"]
    devices = config["devices"]
        
    for device in devices:
        t = threading.Thread(target=simulate_device, args=(device, message_interval, f, mode))
        print(f"Launching thread {len(device_threads)}.")
        device_threads.append(t)
        t.start()

    while True:
        if keyboard.is_pressed("q") or keyboard.is_pressed("esc"):
            break

    for index, thread in enumerate(device_threads):
        # logging.info("Main    : before joining thread {index}.")
        print(f"Main    : before joining thread {index}.")
        thread.join()
        # logging.info("Main    : thread {index} done")
        print(f"Main    : thread {index} done")


def main(config_file: str, mode: str):
    config = {}
    with open(config_file) as config_file:
        config = json.load(config_file)["config"]


    simulate(config)
    print("Simulation successfully stopped.\nExiting...")
    exit(0)

if __name__ == "__main__":
    config_file = "mock_config.json" # sys.argv[1]
    mode = "normal" # if len(sys.argv) != 3 else sys.argv[2]
    cert_file = "my-house-certificate.pem"
    main(config_file=config_file, mode=mode)
