import sys
import csv
import itertools
from itertools import combinations_with_replacement

##############################################################
######################### Functions ##########################
##############################################################

def get_vfi_subsets(vfi_col):
  subsets = []
  # get all subsets of given size (set_size)
  for set_tup in combinations_with_replacement(vfi_col, set_size):
    subsets.append(set_tup)

  # remove tuples with duplicate values
  for value in sorted(subsets):
    if(len(set(value)) != len(value)):
      subsets.remove(value)

  return subsets

def populate_cfi(vfi_sets):
  # fill cfi with values from previous vfi
  for key in item_sets:
    for value in vfi_sets:
      if set(value) <= set(item_sets[key]):
        if value in CFI:
          CFI[value] += 1
        else:
          CFI[value] = 1

def refill_CFI(VFI):
  vfi_list = []
  # create a list of vfi values
  for key in VFI:
  	vfi_list.append(key)
  
  # fill a new list with collapsed tuple values
  collapsed_vfi = []
  for value in vfi_list:
    for item in value:
      collapsed_vfi.append(item)

  # sort the set of the new list and populate the CFI
  collapsed_vfi = set(collapsed_vfi)
  collapsed_vfi = sorted(collapsed_vfi)
  vfi_subsets = get_vfi_subsets(collapsed_vfi)
  populate_cfi(vfi_subsets)

def check_subsets(freq_set):
  set_size = 1
  key_subsets = []

  # get all subsets of the current set from size 1 to freq_set length - 1
  while set_size < len(freq_set):
    for set_tup in combinations_with_replacement(freq_set, set_size):
      key_subsets.append(set_tup)
    set_size += 1

  # get rid of sets with duplicates
  for value in key_subsets:
    if(len(set(value)) != len(value)):
      key_subsets.remove(value)

  for key in key_subsets:
    # get disjoint to isolate other part of the set
    disjoint_set = tuple(set(freq_set) - set(key))

    # calculate occurences of values
    freq = 0
    for val in item_sets:
      if set(key) <= set(item_sets[val]) and set(disjoint_set) < set(item_sets[val]):
        freq += 1
    
    # add to confidence rule set if min confidence is met
    if freq/VFI_counts[key] >= min_conf:
      conf_key = sorted(key)
      conf_key.append('\'=>\'',)
      conf_key += sorted(disjoint_set)
      conf_key = tuple(conf_key)
      conf_sets[conf_key] = freq/VFI_counts[key]

# formats to csv file
def format_to_csv():
  with open(output_file, mode='w', newline='') as f:
    writer = csv.writer(f, delimiter=',')

    # set floats to 4 decimal places and append to list for support rules
    for item_set in final_VFI:
      row = [s_key]
      row.append(str.format('{0:.4f}',final_VFI[item_set]))
      for value in item_set:
      	row.append(value)
      writer.writerow(row)
    
    # set floats, format row for confidence sets
    for item_set in conf_sets:
      row = [r_key]
      set_list = list(item_set)
      set_list.remove('\'=>\'')
      set_list = tuple(sorted(set_list))
      row.append(str.format('{0:.4f}',final_VFI[set_list]))
      row.append(str.format('{0:.4f}',conf_sets[item_set]))
      for value in item_set:
        row.append(value)
      writer.writerow(row)

    f.close()


######################################################################         
########################### Program Start ############################
######################################################################

# check args are correct length
if len(sys.argv) != 5:
  print('Incorrect number of arguments:')
  print('python arma.py <input_file> <output_file> <min_support> <min confidence>')
  sys.exit()

# initialize globals and CFI/VFI
input_file = sys.argv[1]
output_file = sys.argv[2]
min_sup = float(sys.argv[3])
min_conf = float(sys.argv[4])
s_key = 'S'
r_key = 'R'
item_sets = {}  # initial itemsets read in from csv
CFI = {}        # the main CFI
final_VFI = {}  # the final VFI which is added to as program executes
VFI_counts = {} # count of items in VFI
VFI = {}        # temporary VFI, holds one level at a time
conf_sets = {}  # holds confidence rules tuples
set_size = 1    # initial set size to fill VFI with

# open file and preprocess, fill item_set dict, strip spaces
with open(input_file, 'r') as f:
  csv_reader = csv.reader(f, delimiter=',')
  for row in csv_reader:
    set_key = row[0]
    item_sets[set_key] = []
    for value in row[1:]:
      item_sets[set_key].append(value.strip())
  f.close()

total_count = len(item_sets)

# build initial CFI
for key in item_sets:
  for value in item_sets[key]:
  	if tuple(value) in CFI:
  	  CFI[tuple(value)] += 1
  	else:
  	  CFI[tuple(value)] = 1

# while CFI has values, calculate new VFI entries and refill the CFI, increment CFI set size
while(CFI):
  sorted_CFI = sorted(CFI)
  for key in sorted_CFI:
    pct = CFI[key] / total_count
    if pct >= min_sup:
      VFI[key] = pct
      VFI_counts[tuple(key)] = CFI[key]

  # clear cfi, update final_vfi, refill the CFI with the current VFI, clear the current VFI
  CFI.clear()
  final_VFI.update(VFI)
  set_size += 1
  refill_CFI(VFI)
  VFI.clear()

# check subsets of all values in VFI to generate confidence rules
for key in VFI_counts:
  check_subsets(key)

# format to csv file
format_to_csv()