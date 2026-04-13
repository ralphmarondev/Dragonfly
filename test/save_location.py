import time
import requests

# Dragonfly
# BASE_URL = "https://dragonfly-2026-default-rtdb.firebaseio.com"
BASE_URL = "https://mototrack-ca1f6-default-rtdb.firebaseio.com"
PLATE_NUMBER = "HELLO123"

# Dragonfly
# API_KEY = "AIzaSyAQS99jWftauGrs_-alSed_qGK6jP7IoVI"
API_KEY = "AIzaSyBpJ5-gPVzdSaljPZRikjoS2qHiEWkkq0U"
EMAIL = "admin@gmail.com"
PASSWORD = "adminnimda"

def login():
    url = f"https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key={API_KEY}"

    payload = {
        "email": EMAIL,
        "password": PASSWORD,
        "returnSecureToken": True
    }

    res = requests.post(url, json=payload)
    data = res.json()

    if "error" in data:
        print("Login failed:", data["error"])
        return None

    print("Login success")
    return data["idToken"]

def send_location(id_token, lat, lng):
    timestamp = int(time.time() * 1000)

    url = f"{BASE_URL}/{PLATE_NUMBER}/{timestamp}.json?auth={id_token}"

    data = {
        "latitude": lat,
        "longitude": lng
    }

    res = requests.put(url, json=data)

    print("Sent:", res.status_code, data)

if __name__ == "__main__":
    token = login()

    if not token:
        exit()

    lat = 50.5995
    lng = 504.9842

    send_location(token, lat, lng)

#     while True:
#         send_location(token, lat, lng)
#         # simulate movement
#         lat += 0.0001
#         lng += 0.0001
#
#         time.sleep(5)