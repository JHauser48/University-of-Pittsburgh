require_relative 'truth_table'
require 'sinatra'
require 'sinatra/reloader'

# creates the truth table if inputs are valid
def create_truth_table(truth_s, false_s, size)
  truth_table = TruthTable.new([false_s, truth_s], size)
  truth_table.start_table
  truth_table.calculate_truth_table
  table = truth_table.table_array
  table
end

# checks inputs for validity
def valid_params truth_s, false_s, size
 return false if !truth_s.nil? && truth_s.length > 1
 return false if !false_s.nil? && false_s.length > 1
 return false if !size.nil? && size < 2 || size > 20
 return false if truth_s == false_s
end

# Error message if inputs are invalid
def error_msg
  one = "Please enter:\n\n\t1. A single-character true symbol\n"
  two = "\t2. A single-character false symbol\n"
  three = "\t3. A size greater than or equal to 2 and less than or equal to 20\n"
  four = "\t4. Different Characters for true and false symbols"
  msg = one + two + three + four
  msg
end

get '/' do 

  ts = params['truth_s']
  fs = params['false_s']
  sz = params['size']

  # set to defaults if no input
  ts == '' ? truth_s = 'T' : truth_s = ts
  fs == '' ? false_s = 'F' : false_s = fs
  sz == '' ? size = 3 : size = sz.to_i 
  msg = error_msg

  if valid_params(truth_s, false_s, size) == false
    truth_s.nil? && false_s.nil? ? error = false : error = true
    valid = false
  else
    valid = true
    table = create_truth_table(truth_s, false_s, size)
  end 

  erb :index, :locals => { truth_s: truth_s, false_s: false_s, size: size, table: table, valid: valid, error: error, error_msg: msg }
end

# any page but home will generate this error
not_found do
  status 404
  erb :error
end