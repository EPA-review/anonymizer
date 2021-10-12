from typing import List, NamedTuple
from analyzer import analyzeText, serializeList

TestCase = NamedTuple('Test', [('text', str), ('names', List[str])])

tests: List[TestCase] = [
    TestCase('Tom thinks this isn\'t a great idea. D/W.', ['Tom']),
    TestCase('Managed the resp distress, approp ddx, proper interp of ABG, Discussed disposition, tx', ['Someone'])
]
for test in tests:
    text = test.text
    names = test.names
    result = serializeList(analyzeText(text, names))
    print(result)