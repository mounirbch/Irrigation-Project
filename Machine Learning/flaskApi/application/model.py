import numpy as np
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import MinMaxScaler
from tensorflow.keras.layers import LSTM, Dense
from tensorflow.keras.models import Sequential
#from tensorflow.python.keras.layers import LSTM, Dense
#from tensorflow.python.keras.models import Sequential


def faire_predictions(predict_path):
    data1 = pd.read_csv('C:\\Users\\MICRONET\\PycharmProjects\\flaskApi\\application\\MOCK_DATA (3).csv')
    data2 = pd.read_csv('C:\\Users\\MICRONET\\PycharmProjects\\flaskApi\\application\\MOCK_DATA (4).csv')
    data = pd.concat([data1, data2], ignore_index=True)
    data['date'] = pd.to_datetime(data['date'], format='%d/%m/%Y')
    data = data.sort_values('date') #tirer les données par date
    scaler = MinMaxScaler()
    data[['temperature', 'humidity', 'soil_humidity', 'ph', 'water_quantity']] = scaler.fit_transform(data[['temperature', 'humidity', 'soil_humidity', 'ph', 'water_quantity']])
    sequence_length = 2   #10
    forecast_horizon = 1
    X = []
    y = []
    for i in range(len(data) - sequence_length - forecast_horizon + 1):  # +1 pour inclure la dernière séquence
        X.append(data.iloc[i:i+sequence_length][['temperature', 'humidity', 'soil_humidity', 'ph']].values)
        y.append(data.iloc[i+sequence_length:i+sequence_length+forecast_horizon]['water_quantity'].values)#parcourir et pour éviter  les indexations hors limite
    X = np.array(X)
    y = np.array(y)
    X_train_val, X_test, y_train_val, y_test = train_test_split(X, y, test_size=0.1, shuffle=False)
    X_train, X_val, y_train, y_val = train_test_split(X_train_val, y_train_val, test_size=0.1, shuffle=False)
    model = Sequential()
    model.add(LSTM(units=512, return_sequences=True, input_shape=(sequence_length, 4)))
    model.add(LSTM(units=256))
    model.add(Dense(10, activation='relu'))
    model.add(Dense(units=forecast_horizon))
    model.compile(optimizer='adam', loss='mse')
    history = model.fit(X_train, y_train, epochs=80, batch_size=32, validation_data=(X_val, y_val))
    data1 = pd.read_csv(predict_path)
    sequence_length = 1
    X1 = []
    for i in range(len(data1) - sequence_length - forecast_horizon + 1):
      X1.append(data1.iloc[i:i+sequence_length][['temperature', 'humidity', 'soil_humidity', 'ph']].values)
    X1 = np.array(X1)
    p = model.predict(X1)
    return p



#predictions = faire_predictions('C:\\Users\\MICRONET\\PycharmProjects\\flaskApi\\application\\data.csv')
#print(predictions)