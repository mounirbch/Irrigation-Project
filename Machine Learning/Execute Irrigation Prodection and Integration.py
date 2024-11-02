import csv
import os
import threading
from flask import Flask, jsonify
from pymongo import MongoClient
import schedule
import time
from datetime import datetime


from application.model import faire_predictions

app = Flask(__name__)


@app.route('/')
def welcome():
    return 'rirou'


client = MongoClient('localhost', 27017) #, username='rou', password='rou'
# client = MongoClient('127.0.0.1', 27017)
db = client.mounir
collection = db.final


@app.route('/data', methods=['GET'])
def get_all_data():
    try:
        all_data = list(collection.find())
        for data in all_data:
            data['_id'] = str(data['_id'])
        return jsonify(all_data)
    except Exception as e:
        return jsonify({'error': str(e)}), 500


#@app.route('/last_data', methods=['GET'])
#def get_last_data():
#    try:
#       last_data = collection.find().sort([('_id', -1)]).limit(10)
#        if last_data:
#            return jsonify(list(last_data))
#        else:
#            return jsonify([])
#    except Exception as e:
#        print("Error:", e)

# Fonction pour créer un fichier CSV avec les données de la dernière entrée
def create_csv():
    try:
        last_data = list(collection.find().sort([('_id', -1)]).limit(2))
        if last_data:
            with open('data.csv', 'w+', newline='') as csvfile:
                fieldnames = ['temperature', 'humidity', 'soil_humidity', 'ph', 'water_quantity']
                writer = csv.DictWriter(csvfile, fieldnames=fieldnames)

                writer.writeheader()
                for data in reversed(last_data):
                    writer.writerow({
                        'temperature': data['finalTemperature'],
                        'humidity': data['finalHumidity'],
                        'soil_humidity': data['finalSoilHumidity'],
                        'ph': data['finalPh'],
                        'water_quantity': 0.0
                    })
                directory_path = os.path.abspath('.')
                csv_file_name = 'data.csv'
                csv_file_path = os.path.join(directory_path, csv_file_name)
                csv_file_path = os.path.normpath(csv_file_path)
                print("CSV file created successfully at:", csv_file_path)
        else:
            print("No data available")
    except Exception as e:
        print("Error:", e)

def execute_predictions():
    predictions = faire_predictions('C:\\Users\\MICRONET\\PycharmProjects\\flaskApi\\application\\data.csv')
    max_value = 999.89
    min_value = 1.28

    try:
        collection_quantity = db.quantity
        print("quantity accés" )

        valeur = float(predictions) * (max_value - min_value) + min_value
        document = {
            "timestamp": datetime.now(),
            "valeur": valeur
        }

        collection_quantity.insert_one(document)

        print("New values inserted into the 'quantity' collection successfully.")

    except Exception as e:
        print("Error occurred while inserting new values into the 'quantity' collection:", e)


schedule.every().day.at("06:48").do(create_csv)
schedule.every().day.at("06:49").do(execute_predictions)


def run_flask_app():
    app.run(debug=True, use_reloader=False)

flask_thread = threading.Thread(target=run_flask_app)
flask_thread.start()

while True:
    schedule.run_pending()
    time.sleep(60)