## To retrieve pip packages
```sh
python -m pip install -r requirements.txt
```

## Project Structure
* `SimpleAnonymizer.py` provides the function to analyze and label a single word.
* `analyzer.py` uses `SimpleAnonymizer.py` to provide the function to analyze a whole text. When APIs of `SimpleAnonymizer.py` is changed, modify this file should also be considered. The API schemas in this file should not be changed.
* `server.py` users `analyzer.py` to provide HTTP APIs for web requests.
* `test.py` is used for testing if `analyzer.py` works properly.
* `requirements.txt` is used to store pip packages imported in the project, when installing a new package, this file should also be updated.