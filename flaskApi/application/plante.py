import csv
import os
import threading
import time
from datetime import datetime
import schedule
from flask import Flask
from pymongo import MongoClient

#from application.Rest_api import app
from application.planteModel import main
app = Flask(__name__)


client = MongoClient('localhost', 27017) #, username='rou', password='rou'
# client = MongoClient('127.0.0.1', 27017)
db = client.mounir
collection = db.moyenne


@app.route('/first_data', methods=['GET'])
def get_first_data():
    try:
        first_data = collection.find_one()
        if first_data:
            return first_data
        else:
            return None
    except Exception as e:
        print("Error:", e)

def create_csv():
    first_data = get_first_data()
    if first_data:
        with open('plante.csv', 'w+', newline='') as csvfile:
            fieldnames = ['temperature', 'humidity', 'ph']
            writer = csv.DictWriter(csvfile, fieldnames=fieldnames)

            writer.writeheader()
            writer.writerow({
                'temperature': first_data['avgTemperature'],
                'humidity': first_data['avgHumidity'],
                'ph': first_data['avgPh']

            })
            directory_path = os.path.abspath('.')
            csv_file_name = 'plante.csv'
            csv_file_path = os.path.join(directory_path, csv_file_name)
            csv_file_path = os.path.normpath(csv_file_path)
            print("CSV file created successfully at:", csv_file_path)

    else:
        print("No data available")


def execute_predictions():
    predictions = main('C:\\Users\\MICRONET\\PycharmProjects\\flaskApi\\application\\plante.csv')
    pred_plante_collection = db.pred_plante
    if pred_plante_collection.count_documents({}) > 0:
        pred_plante_collection.delete_many({})
        print("donnees supprimer avec succees")

    for pred in predictions:
        for crop_pred in pred['predictions']:
            pred_plante_collection.insert_one(crop_pred)
    print("save avec succes")



schedule.every().day.at("21:24").do(create_csv)
schedule.every().day.at("21:31").do(execute_predictions)
#executer code chaque 2 mois:
#schedule.every(2).months.at("10:47").do(execute_predictions)


# Fonction pour exécuter Flask dans un thread distinct
def run_flask_app():
    app.run(debug=True, use_reloader=False)

# Exécuter Flask dans un thread distinct
flask_thread = threading.Thread(target=run_flask_app)
flask_thread.start()

# Exécuter la planification dans la boucle principale
while True:
    schedule.run_pending()
    time.sleep(60)