import requests

API_KEY = "AIzaSyAQS99jWftauGrs_-alSed_qGK6jP7IoVI"

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