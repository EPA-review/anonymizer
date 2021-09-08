from SimpleAnonymizer import AnonymizeWord, SetAnonFlag
from typing import List
import re


class Serializable:
    def serialize(self):
        return {}


class TextSegment(Serializable):
    def __init__(self, start: int, end: int, label: str):
        self.start = start
        self.end = end
        self.label = label

    def serialize(self):
        return {
            "start": self.start,
            "end": self.end,
            "label": self.label
        }


def analyzeText(text: str):
    testResidentFName = "Jason"
    testResidentLName = "Bernard"
    testObserverFName = "Brent"
    testObserverLName = "Thoma"
    flag = False

    textSegments = extractWords(text)
    result: List[TextSegment] = []
    for textSegment in textSegments:
        label = AnonymizeWord(textSegment.label, testResidentFName,
                              testResidentLName, testObserverFName, testObserverLName, flag)
        flag = SetAnonFlag(textSegment.label)
        if label != textSegment.label.lower():
            textSegment.label = label
            result.append(textSegment)

    return result


def extractWords(text: str):
    words: List[TextSegment] = []
    startIndex: int = None
    currentWord: str = ''
    for i in range(len(text)):
        character = text[i]
        match = re.match('\w+', character)
        if match:
            currentWord += character
            if not startIndex:
                startIndex = i
            if i == len(text) - 1:
                words.append(TextSegment(startIndex, i, currentWord))
                currentWord = ''
                startIndex = None
        elif startIndex:
            words.append(TextSegment(startIndex, i, currentWord))
            currentWord = ''
            startIndex = None
    return words


def serializeList(myList: List[Serializable]):
    return list(map(lambda item: item.serialize(), myList))
