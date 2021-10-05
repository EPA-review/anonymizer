from analyzer import analyzeText, serializeList

text = 'Tom thinks this isn\'t a great idea.'
names = ['Tom']
result = serializeList(analyzeText(text, names))
print(result)