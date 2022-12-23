from typing import List, NamedTuple
from SimpleAnonymizer import AnonymizeText
from flask import json

TestCase = NamedTuple('Test', [('text', str), ('names', List[str])])

tests: List[TestCase] = [
    TestCase('Tom thinks this isn\'t a great idea. D/W.', ['Tom']),
    TestCase('Managed the resp distress, approp ddx, proper interp of ABG, Discussed disposition, tx', [
             'Someone']),
    TestCase('EPA completed by Dr. A. McConnell on ID rotation \r\nSubmitted on Oct 22 for week of Sept 4-7 \r\nNeeded to review the microbiology of the infection and characteristics of the appropriate antibiotic for the infection.',
             ['Grayson', 'Wilson', 'Lynsey', 'Martin']),
    TestCase('Sey successfully managed a first presentation single seizure patient', ['Seyara', 'Shwetz', 'Schaana', 'Van', 'De', 'Kamp']),
    TestCase("Kedra saw a homeless man who was brought in by police after being found passed out in the snow.  The patient was being belligerent and threatening to leave.  The nurses suggested the patient go to the WR or be discharged as they are a 'friendly face' of the ED and they suspected he was behavioral.  Kedra was unsure how to approach this situation and needed help, so I talked her through how to approach it.  In these situations, if patients have capacity to make decisions about their care (they can repeat back to you the consequences of leaving), you can discharge them.  If they don't and try to leave, you can invoke the substitute decision maker act to hold them until they are medically clear or have capacity.", 
        ['Kedra', 'Ann', 'Peterson', 'Robert', 'A', 'Woods']
    )
]
nicknames = {
    'Seyara': ['Sey']
}

for test in tests:
    text = test.text
    names = test.names
    result = AnonymizeText(text, names, nicknames)
    print(result)
