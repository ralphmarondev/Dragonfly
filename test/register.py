import requests

API_KEY = "AIzaSyAQS99jWftauGrs_-alSed_qGK6jP7IoVI"


def create_firebase_user():
    url = f"https://identitytoolkit.googleapis.com/v1/accounts:signUp?key={API_KEY}"

    payload = {
        "email": "admin@gmail.com",
        "password": "adminnimda",
        "returnSecureToken": True
    }

    response = requests.post(url, json=payload)
    data = response.json()

    if "error" in data:
        print("Error creating user:")
        print(data["error"])
        return

    print("User created successfully!")
    print("UID:", data["localId"])
    print("ID Token:", data["idToken"])


if __name__ == "__main__":
    create_firebase_user()