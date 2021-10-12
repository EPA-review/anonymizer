from analyzer import analyzeText, serializeList

text = 'Tom thinks this isn\'t a great idea. D/W.'
names = ['Tom']
result = serializeList(analyzeText(text, names))
print(result)