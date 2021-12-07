from SimpleAnonymizer import AnonymizeText
from flask import Flask, jsonify, request, json
from flask_cors import CORS
import sys

app = Flask(__name__)
CORS(app)


@app.route('/analyze', methods=['POST'])
def analyze():
    requestBody = request.json
    text = requestBody['text']
    names = requestBody['names']
    nicknames = {}
    with open('./nicknames.json', 'r') as file:
        nicknames = json.loads(file.read())

    return jsonify(AnonymizeText(text, names, nicknames))


if __name__ == '__main__':
    try:
        port = int(sys.argv[1])  # This is for a command-line input
    except:
        port = 8000
    app.run(port=port, host="0.0.0.0")
