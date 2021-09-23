from analyzer import analyzeText, serializeList

text = 'Tom thinks this is a great idea.'
names = ['Tom']
result = serializeList(analyzeText(text, names))
print(result)