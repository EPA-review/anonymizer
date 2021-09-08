from analyzer import analyzeText, serializeList
from flask import Flask, jsonify, request
from flask_cors import CORS
import sys

app = Flask(__name__)
CORS(app)


@app.route('/analyze', methods=['POST'])
def analyze():
    requestBody = request.json
    text = requestBody['text']

    return jsonify(serializeList(analyzeText(text)))


if __name__ == '__main__':
    try:
        port = int(sys.argv[1])  # This is for a command-line input
    except:
        port = 8000
    app.run(port=port, host="0.0.0.0")
