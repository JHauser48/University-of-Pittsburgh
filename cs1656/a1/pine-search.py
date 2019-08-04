# cs1656 Project 1: Jake Hauser
# pine-search program

import os
import json
import nltk
import string
import math

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

# calculates the weights of every word, updates the weight dict
def calculate_weights(keyword, doc_no, index):
  doc_count = index['DOC_COUNT']
  keyword_weights[keyword] = {}
  for doc in index:
    if doc != 'DOC_COUNT':
      # weight calculation equation
      weight = (1+math.log2(index[doc]))*(math.log2(doc_no/doc_count))
      keyword_weights[keyword][doc] = weight

# calculates the final scores for each keyword set, returns a dict of scores
def calculate_scores(keywords):
  scores = {}
  # either updates existing or creates a new dict entry
  for word in keywords:
    for doc in keyword_weights[word]:
      if doc in scores:
        scores[doc] += keyword_weights[word][doc] 
      else:
        scores.update({doc : keyword_weights[word][doc]})
  return scores
   
# prints the scores as specified, some stemming needed. prints to 6 decimal places
def print_scores(keywords, scores):
  stemmer = nltk.stem.PorterStemmer()
  keys = keywords.split()
  rank = 0       # document rank
  prev_score = 0 # tracks last score to check if rank should be incremented
  print('------------------------------------------------------------')
  print('keywords = ' + keywords + '\n')
  for values in scores: # checks if rank should be incremented
    if values[1] != prev_score:
  	  rank += 1
    print('['+str(rank)+']'+'file='+values[0]+' score='+str.format('{0:.6f}',(values[1])))
    for word in keys: # stem keyword to lookup key in the weight dict, then print scores
      stemmed = stemmer.stem(word)
      if values[0] in keyword_weights[stemmed]: 
        print('    weight('+word+')='+str.format('{0:.6f}',keyword_weights[stemmed][values[0]]))
      else:
      	print('    weight('+word+')=0.000000')
    prev_score = values[1] # update preve_score
    print()

#get path
path = os.getcwd()

# init stemmed keys list and regular keys list
key_file = open(path+'/keywords.txt','r')
keys = key_file.readlines()
stemmed_keywords = []
keyword_list = []

# perform the conversion and stemming
for word in keys:
  converted = convert_text(word)
  keyword_list.append(converted.rstrip()) # strip newlines
  stemmed_words = stem_words(converted)
  stemmed_keywords.append(stemmed_words)

# load the .json created by pine-index back into a dict
with open(path+'/inverted-index.json', 'r') as f:
  inv_index = json.loads(f.read())
  f.close()

# get document count into a var, init weights dict
num_docs = inv_index['NUM_DOCS']
keyword_weights = {}

# calculate weight of each word in each set of keywords
for keywords in stemmed_keywords:
  for word in keywords:
    calculate_weights(word, num_docs, inv_index[word])

# prepare information for output
# stem key_list again, this time one set of keywords at a time
# init score_list, convert score_dict to score_list (iist of list pairs)
# sort scores in descending order and then call the print_scores() method
print('Information Retrieval Engine - Jake Hauser (jdh122@pitt.edu)\n')
for words in keyword_list:
  stemmed = stem_words(words) # stem again
  score_list = []
  score_dict = calculate_scores(stemmed)
  for key, value in score_dict.items():
    score_list.append([key, value])
  score_list.sort(key = lambda x: x[1], reverse = True)
  print_scores(words, score_list)
