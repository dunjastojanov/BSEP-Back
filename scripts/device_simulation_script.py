import sys
import json
import requests
import threading
import time
import keyboard
import random
import re
import os

from datetime import datetime

DEVICE_TYPES = {
    "LIGHT_SWITCH": {
        "unit": "state",
        "values": [0, 1]
    },
    "CAMERA": {
        "unit": "state",
        "values": [0, 1, 2]
    },
    "DOOR_BELL": {
        "unit": "state",
        "values": [1]
    },
    "DOOR_LOCK": {
        "unit": "lock",
        "values": [0, 1]
    },
    "LIGHT": {
        "unit": "state",
        "values": [0, 1]
    },
    "THERMOSTAT": {
        "unit": "temperature",
        "values": [5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23]
    },
    "AC": {
        "unit": "temperature",
        "values": [16, 17, 18, 19, 20, 21, 22, 23, 24, 25]
    },
    "BLINDS": {
        "unit": "state",
        "values": [0, 1]
    },
    "REFRIGERATOR": {
        "unit": "state",
        "values": [0, 1]
    },
    "SHOWER": {
        "unit": "state",
        "values": [0, 1]
    },
    "SMART_PLUG": {
        "unit": "state",
        "values": [0, 1]
    },
    "ALARM_SYSTEM": {
        "unit": "ALARM",
        "values": [1]
    },
    "OTHER" : {
        "unit": "state",
        "values": [0, 1]
    }
}

MESSAGE_TYPES = {
    0: "INFO",
    1: "WARN",
    2: "ERROR",
    3: "ALARM"
}

EXECUTION_MODES = ['normal', 'error']

MESSAGE_ENDPOINT = "http://localhost:8080/api/device/message"
MESSAGE_ENDPOINT_HTTPS = "https://localhost:8080/api/device/message"

def valueChanged(name: str, type: str, unit: str, value: int):
    # match (type, value):
    #     case ("CAMERA", 2): return f"{name} ({type}) detected movement. Value: {value}." case _: 
    return f"{name} ({type}) {unit} changed. Value: {value}."

def deviceOn(name, type):
    return f"{name} ({type}) switched on."

def deviceOff(name, type):
    return f"{name} ({type}) switched off."

def deviceError(name, type):
    return f"Device {name} ({type}) encountered error. Value: -1."

def print_successfull_message_post(message: dict):
            print(f"""Message sent successfully:
    deviceId: {message["deviceId"]}
    timestamp: {message["timestamp"]}
    content: {message["content"]}
    deviceMessageType: {message["type"]}
    value: {message["value"]}
""")

def get_device_message(device, mode):
    t = datetime.now()
    
    device_values_and_units = DEVICE_TYPES[device['type']]

    if mode == 'normal' or random.random() < 0.4:
        val = random.choice(device_values_and_units['values'])
        unit = device_values_and_units['unit']
        message_type = "INFO"
        if random.random() > 0.3:
            content = f"[{t.strftime('%Y-%m-%dT%H:%M:%S')}] [{message_type:^6}] {valueChanged(device['name'], device['type'], unit, val)}"
        elif random.random() > 0.5:
            content = f"[{t.strftime('%Y-%m-%dT%H:%M:%S')}] [{message_type:^6}] {deviceOn(device['name'], device['type'])}"
        else:
            content = f"[{t.strftime('%Y-%m-%dT%H:%M:%S')}] [{message_type:^6}] {deviceOff(device['name'], device['type'])}"

    elif mode == "error" or random.radnom() < 0.025:
        if random.random() < 0.5:
            val = -1
            message_type = "ERROR"
            content = f"[{t.strftime('%Y-%m-%dT%H:%M:%S')}] [ ERROR ] {deviceError(device['name'], device['type'])}"
        else:
            val = 30
            message_type = "INFO"
            unit = device_values_and_units['unit']
            content = f"[{t.strftime('%Y-%m-%dT%H:%M:%S')}] [ INFO ] {valueChanged(device['name'], device['type'], unit, val)}"



    return {
        "deviceId": device["id"],
        "timestamp": t.strftime("%Y-%m-%dT%H:%M:%S"),
        "content": content,
        "type": message_type,
        "value": val
    }

def simulate_device(device, messageInterval, regexFilter, mode, cert_file):

    pattern = re.compile(regexFilter)

    while True:
        if keyboard.is_pressed("q") or keyboard.is_pressed("esc"):
            break
        message = get_device_message(device, mode)

        if regexFilter == "*" or re.match(regexFilter, message['content']):
            
            # post message here

            # r = requests.post(MESSAGE_ENDPOINT_HTTPS, verify=cert_file, json=message)
            r = requests.post(MESSAGE_ENDPOINT, json=message)
            print(f"\n=== Server returned {r.status_code} ===\n")
            if r.status_code == 200:
                print(f"Message content matches regex filter: {regexFilter}")
                print_successfull_message_post(message)

        time.sleep(messageInterval)

def simulate(config: dict, mode, cert_file):
    device_threads = []
    message_interval = config["messageIntervalDuration"]
    f = config["filter"]
    devices = config["devices"]
        
    for device in devices:
        t = threading.Thread(target=simulate_device, args=(device, message_interval, f, mode, cert_file))
        print(f"Launching thread {len(device_threads)}.")
        device_threads.append(t)
        t.start()

    while True:
        if keyboard.is_pressed("q") or keyboard.is_pressed("esc"):
            break

    for index, thread in enumerate(device_threads):
        print(f"Main    : before joining thread {index}.")
        thread.join()
        print(f"Main    : thread {index} done")


def main(config_file_directory: str, mode: str, cert_file):
    configs = []

    config_files = os.listdir(config_file_directory)

    for file in config_files:
        with open(os.path.join(config_file_directory, file)) as f:
            configs.append(json.load(f))

    config_threads = []

    for config in configs:
        t = threading.Thread(target=simulate, args=(config, mode, cert_file))
        print(f"Launching thread {len(config_threads)}.")
        config_threads.append(t)
        t.start()

    # simulate(config, mode, cert_file)
    print("Simulation successfully stopped.\nExiting...")
    exit(0)

if __name__ == "__main__":
    if len(sys.argv) != 4:
        exit("Error, not enough parameters\nCommand execution should look like:\npython device_simulation_script.py <path_to_config> <mode> <path_to_cert>\nWhere mode is either 'normal' or 'error'")

    try:
        config_file_directory = sys.argv[1]
        mode = 'normal' if len(sys.argv) != 4 else sys.argv[2]
        cert_file = sys.argv[3]
        main(config_file_directory=config_file_directory, mode=mode, cert_file=cert_file)
    except KeyboardInterrupt:
        exit("Exiting...")
    #     exit("Error, not enough parameters\nCommand execution should look like:\npython device_simulation_script.py <path_to_config> <mode> <path_to_cert>\nWhere mode is either 'normal' or 'error'")
