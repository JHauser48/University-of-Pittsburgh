# cs1656 Project 1: Jake Hauser
# program to create inverted index and convert to a .json

import os
import json
import nltk
import string

# removes punctuation and numbers from the string, return as string
def convert_text(file_text):
  file_text = file_text.lower()
  table = str.maketrans('', '', string.punctuation)
  file_text = file_text.translate(table)
  table = str.maketrans('','','1234567890')
  file_text = file_text.translate(table)
  return file_text

# stems the keywords using PorterStemmer algorithm, returns as list
def stem_words(file_text):
  stemmer = nltk.stem.PorterStemmer()
  words = file_text.split()
  for i, word in enumerate(words):
    words[i] = stemmer.stem(word)
  return words

# updates the inv_index appropriately based off input, no return value 
def update_dict(words, filename):
  inv_index['NUM_DOCS'] += 1
  # either adds to existing key or updates dict with a new key and empty dict value
  for word in words:
    if word in inv_index:
      if filename in inv_index[word]:
        inv_index[word][filename] += 1 
      else:
        inv_index[word][filename] = 1
        inv_index[word]['DOC_COUNT'] += 1
    else:
      inv_index[word] = {}
      inv_index[word]['DOC_COUNT'] = 1
      inv_index[word][filename] = 1
  
# first get the path and input, init dict, init doc number to 0
path = os.getcwd() + '/input/'
inv_index = {}
inv_index['NUM_DOCS'] = 0

# iterate through files, convert, stem, and add values to inv_index
for filename in os.listdir(path):
  file = open(path+filename, 'r')
  file_text = file.read()
  file.close()
  file_text = convert_text(file_text)
  words = stem_words(file_text)
  update_dict(words, filename)

# convert inv_index to .json format
with open('inverted-index.json', 'w') as f:
  json.dump(inv_index, f)
  f.close()