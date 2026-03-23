import requests
import time
import json

def load_local_properties(filepath="local.properties"):
    props = {}
    with open(filepath, "r") as f:
        for line in f:
            line = line.strip()
            if line and not line.startswith("#"):
                key, value = line.split("=", 1)
                props[key.strip()] = value.strip()
    return props

# Load config
config = load_local_properties()

API_KEY = config.get("API_KEY")
PROJECT_ID = config.get("PROJECT_ID")
EMAIL = config.get("EMAIL")
PASSWORD = config.get("PASSWORD")
VEHICLE_ID = config.get("VEHICLE_ID")

def get_id_token(email, password):
    url = f"https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key={API_KEY}"

    payload = {
        "email": email,
        "password": password,
        "returnSecureToken": True
    }

    response = requests.post(url, json=payload)

    try:
        resp_json = response.json()
        if response.status_code != 200:
            print("Firebase Sign-In Error:", json.dumps(resp_json, indent=2))
            response.raise_for_status()
    except ValueError:
        print("Non-JSON response:", response.text)
        response.raise_for_status()

    return resp_json["idToken"]

def save_location(id_token):
    lat = float(input("Enter latitude: ").strip())
    lng = float(input("Enter longitude: ").strip())
    timestamp = int(time.time() * 1000)

    url = f"https://firestore.googleapis.com/v1/projects/{PROJECT_ID}/databases/(default)/documents/locations/{VEHICLE_ID}/location?documentId={timestamp}"

    data = {
        "fields": {
            "latitude": {"doubleValue": lat},
            "longitude": {"doubleValue": lng},
            "timestamp": {"integerValue": timestamp}
        }
    }

    headers = {
        "Authorization": f"Bearer {id_token}"
    }

    response = requests.post(url, headers=headers, json=data)

    print("\n--- REQUEST INFO ---")
    print(f"URL: {url}")
    print("Payload:", json.dumps(data, indent=2))

    print("\n--- RESPONSE ---")
    print(f"Status Code: {response.status_code}")
    try:
        print("Response JSON:", json.dumps(response.json(), indent=2))
    except ValueError:
        print("Response Text:", response.text)

if __name__ == "__main__":
    token = get_id_token(EMAIL, PASSWORD)
    save_location(token)