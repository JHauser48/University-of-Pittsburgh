# This class calculates a truth table based of inputs
class TruthTable
  def initialize(char_set, size)
    @table_array = Array.new(2**size)
    @table_i = 0
    @size = size
    @char_set = char_set
  end

  # starts the recursive function with an empty string
  def start_table
    n = @char_set.length
    t_size = @size
    create_table(@char_set, '', n, t_size)
  end

  # recursive function for generating every combination of T and F symbols
  def create_table(set, prefix, n, t_size)
  	# base case
    if t_size.zero?
      @table_array[@table_i] = prefix.chars
      @table_i += 1
      return
    end
    
    # recursive case
    set.each do |x|
      new_prefix = prefix + x
      create_table(set, new_prefix, n, t_size - 1)
    end
  end

  # calculates the AND, OR, XOR values based of project requirements
  def calculate_truth_table
    false_s = @char_set[0]
    truth_s = @char_set[1]
    @table_array.each do |x|
      trues = x.count(truth_s)
      trues == @size ? x.push(truth_s) : x.push(false_s)
      trues > 0 ? x.push(truth_s) : x.push(false_s)
      trues.odd? ? x.push(truth_s) : x.push(false_s) 
    end
  end

  # getter for the truth table
  def table_array
    @table_array
  end

  # for testing
  def table_array_print
    @table_array.each do |x|
      puts x.join(' ')
    end
  end
end
