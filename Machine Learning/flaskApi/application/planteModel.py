import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier

def main(predict_csv_file):
    train_data = pd.read_csv('C:\\Users\\MICRONET\\PycharmProjects\\flaskApi\\application\\Crop_recommendation.csv')

    train_data_var = train_data.drop(['N', 'P', 'K', 'rainfall', 'label'], axis=1)
    train_data_result = train_data['label']

    X_train, X_test, y_train, y_test = train_test_split(train_data_var, train_data_result, test_size=0.2, random_state=42)

    # Entraîner le modèle RandomForestClassifier
    #model = RandomForestClassifier(n_estimators=100)
    model = RandomForestClassifier(n_estimators=200, criterion='gini', max_depth=10, min_samples_split=10,
                                   min_samples_leaf=1, max_features=None, bootstrap=True, random_state=0)

    model.fit(X_train, y_train)

    predict_data = pd.read_csv(predict_csv_file)

    predictions = model.predict_proba(predict_data)

    non_zero_predictions = []

    results_string = ""
    for probs in predictions:
        result = {}
        result['predictions'] = []
        for prob_idx, prob in enumerate(probs):
            if prob != 0:
                result['predictions'].append({'crop': model.classes_[prob_idx], 'probability': prob})
        non_zero_predictions.append(result)

    return non_zero_predictions