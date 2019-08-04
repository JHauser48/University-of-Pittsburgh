require 'minitest/autorun'
require_relative 'check_args'

# test class for check_args.rb
class CheckArgsTest < Minitest::Test

  # UNIT TESTS FOR METHOD check_args(args)
  # Equivalence classes:
  # args[-INFINITY..-1, -INFINITY..-1] -> returns false
  # args[1..INFINITY, 1..INFINITY] -> returns true
  # args[0, 0] -> returns false
  # args[not an integer, not an integer] -> returns false

  # both args are valid
  def test_valid_args
  	args = CheckArgs.new
  	assert_equal true, args.check_args([1, 1])
  end

  # should return false if no args
  def test_no_args
    args = CheckArgs.new
    assert_equal false, args.check_args([])
  end

  # check return false if only one arg
  def test_one_arg
    args = CheckArgs.new
    assert_equal false, args.check_args([1])
  end
  
  # check return false if non-integer arg
  def test_one_non_int_arg
    args = CheckArgs.new
    assert_equal false, args.check_args(['Sixers'])
  end

  # return false if  negative arg
  def test_negative_arg1
    args = CheckArgs.new
    assert_equal false, args.check_args([-1, -1])
  end

  # check if num prospectors is 0
  # EDGE CASE
  def test_zeros
    args = CheckArgs.new
    assert_equal false, args.check_args([0, 0])
  end

  # if both args are non-integers
  # EDGE CASE
  def test_non_integer_args 
    args = CheckArgs.new
    assert_equal false, args.check_args(['Sixers', 6.6])
  end

  # UNIT TEST FOR METHOD show_usage
  # reduntant test, included for increased code coverage

  def test_usage
    args = CheckArgs.new
    assert_output("Usage:\nruby gold_rush.rb *seed* *num_prospectors*\n*seed* should be an non-negative integer\n*num_prospectors* should be a non-negative integer\n") {args.show_usage}
  end
end
