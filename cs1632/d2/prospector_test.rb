require 'minitest/autorun'
require_relative 'prospector'

class ProspectorTest < Minitest::Test

  # UNIT TESTS FOR METHOD random_val(range)
  # Equivalence classes:
  # Valid range: range >= 0
  # Invalid range: range < 0
  # Non-integer seed or range

  # tests that the function properly returns using a fake random
  def test_random_val
  	fakeRand = Minitest::Mock.new ("fake prng")
  	fakeMap = Minitest::Mock.new ("fake map")
  	def fakeRand.rand(range); 1; end
  	def fakeMap;['Town']; end
	  pros = Prospector.new(fakeRand, fakeMap)
  	assert_equal 1, pros.random_val(1)
  end

  # checks that nil is returned if the given range is negative
  def test_invalid_range
  	fakeRand = Minitest::Mock.new ("fake prng")
  	fakeMap = Minitest::Mock.new ("fake map")
  	def fakeRand.rand(range); 1; end
  	def fakeMap;['Town']; end
    pros = Prospector.new(fakeRand, fakeMap)
    assert_nil pros.random_val(-1)
  end

  #if the value entered is a non-integer, retrun nil
  def test_non_integer
  	fakeRand = Minitest::Mock.new ("fake prng")
  	fakeMap = Minitest::Mock.new ("fake map")
  	def fakeRand.rand(range); 1; end
  	def fakeMap;['Town']; end
  	pros = Prospector.new(fakeRand, fakeMap)
  	assert_nil pros.random_val('a')
  end
  
  # UNIT TESTS FOR METHOD display_find(gold, silver, city)
  # Equivalence classes:
  # No precious metals found
  # Silver == 1, Gold == 1
  # Silver > 1, Gold > 1
  
  # no metals found
  def test_no_metals_found
    fakeRand = Minitest::Mock.new ("fake prng")
    fakeMap = Minitest::Mock.new ("fake map")
    def fakeRand.rand(range); 1; end
    def fakeMap;['Town']; end
    pros = Prospector.new(fakeRand, fakeMap)
    assert_output("\tFound no precious metals in Town\n") {pros.display_find(0, 0, 'Town')}
  end

  # one ounce of each metal found
  def test_one_ounce_metal
    fakeRand = Minitest::Mock.new ("fake prng")
    fakeMap = Minitest::Mock.new ("fake map")
    def fakeRand.rand(range); 1; end
    def fakeMap;['Town']; end
    pros = Prospector.new(fakeRand, fakeMap)
    assert_output("\tFound 1 ounce of gold in Town and 1 ounce of silver in Town\n") {pros.display_find(1, 1, 'Town')}
  end  
  
  # multiple ounces of each metal found 
  def test_multiple_ounces
    fakeRand = Minitest::Mock.new ("fake prng")
    fakeMap = Minitest::Mock.new ("fake map")
    def fakeRand.rand(range); 1; end
    def fakeMap;['Town']; end
    pros = Prospector.new(fakeRand, fakeMap)
    assert_output("\tFound 5 ounces of gold in Town and 5 ounces of silver in Town\n") {pros.display_find(5, 5, 'Town')}
  end  

  # no gold found but multiple ounces of silver are 
  def test_no_gold
     fakeRand = Minitest::Mock.new ("fake prng")
    fakeMap = Minitest::Mock.new ("fake map")
    def fakeRand.rand(range); 1; end
    def fakeMap;['Town']; end
    pros = Prospector.new(fakeRand, fakeMap)
     assert_output("\tFound 5 ounces of silver in Town\n") {pros.display_find(0, 5, 'Town')}
  end

  # no gold found, one ounce of silver found 
  def test_no_gold_one_silver
     fakeRand = Minitest::Mock.new ("fake prng")
    fakeMap = Minitest::Mock.new ("fake map")
    def fakeRand.rand(range); 1; end
    def fakeMap;['Town']; end
    pros = Prospector.new(fakeRand, fakeMap)
     assert_output("\tFound 1 ounce of silver in Town\n") {pros.display_find(0, 1, 'Town')}
  end

  #no silver found but multiple ounces of gold are found 
  def test_no_silver
     fakeRand = Minitest::Mock.new ("fake prng")
    fakeMap = Minitest::Mock.new ("fake map")
    def fakeRand.rand(range); 1; end
    def fakeMap;['Town']; end
    pros = Prospector.new(fakeRand, fakeMap)
     assert_output("\tFound 5 ounces of gold in Town\n") {pros.display_find(5, 0, 'Town')}
  end

  # no silver found, one ounce of gold found 
  def test_no_silver_one_gold
     fakeRand = Minitest::Mock.new ("fake prng")
    fakeMap = Minitest::Mock.new ("fake map")
    def fakeRand.rand(range); 1; end
    def fakeMap;['Town']; end
    pros = Prospector.new(fakeRand, fakeMap)
     assert_output("\tFound 1 ounce of gold in Town\n") {pros.display_find(1, 0, 'Town')}
  end

  # UNIT TESTS FOR METHOD mine_gold(city_i)
  # Equivalence classes:
  # this method simply returns a random value to simulate mining gold
  # included for code coverage

  # mines gold given a fake random of 1
  def test_mine_gold
    fakeRand = Minitest::Mock.new ("fake prng")
    fakeMap = Minitest::Mock.new ("fake map")
    def fakeRand.rand(range); 1; end
    def fakeMap;['Town']; end
    pros = Prospector.new(fakeRand, fakeMap)
    assert_equal 1, pros.mine_gold(0)
  end

  # UNIT TESTS FOR METHOD mine_silver(city_i)
  # Equivalence classes:
  # same as test_mine_gold, included for code coverage
  
  # mines silver given a fake random of 1
  def test_mine_silver
    fakeRand = Minitest::Mock.new ("fake prng")
    fakeMap = Minitest::Mock.new ("fake map")
    def fakeRand.rand(range); 1; end
    def fakeMap;['Town']; end
    pros = Prospector.new(fakeRand, fakeMap)
    assert_equal 0, pros.mine_silver(1)
  end

  # UNIT TESTS FOR METHOD mine_iteration(city_i)
  # This test makes use of other functions which have have their own series of equivalence classes
  # This test just makes sure that a new location is returned (by way of the new location function)
  # also double checks that the method returns nil if no valid city is entered
  # The fakeRand is set to 0, so the prospector should never find any resources and will always move to Coloma

  # makes sure the mine iteration completes the loop and correclty outputs the find
  def test_mine_completes
    fakeRand = Minitest::Mock.new ("fake prng")
    def fakeRand.rand(range); 0; end
    map = Map.new
    pros = Prospector.new(fakeRand, map)
    assert_output("\tFound no precious metals in Sutter Creek\nHeading from Sutter Creek to Coloma holding 0 ounces of gold and 0 ounces of silver.\n") {pros.mine_iteration('Sutter Creek')} 
  end

  # checks that an invalid city does not proceed with a mining iteration
  # EDGE CASE
  def test_invalid_city
    fakeRand = Minitest::Mock.new ("fake prng")
    def fakeRand.rand(range); 0; end
    map = Map.new
    pros = Prospector.new(fakeRand, map)
    assert_nil pros.mine_iteration('Pittsburgh')
  end

  # UNIT TESTS FOR METHOD mine_last_two(city_i)
  # This test makes use of other functions which have have their own series of equivalence classes
  # Equivalence Classes:
  # new location is returnedis locations_visited < 5
  # no new locations is returned if locations_vistited == 5
  # invalid city is entered

  # makes sure the mining iteration completes and correctly outputs value
  def test_mine_Last_two_completes
    fakeRand = Minitest::Mock.new ("fake prng")
    def fakeRand.rand(range); 0; end
    map = Map.new
    pros = Prospector.new(fakeRand, map)
    assert_output("\tFound no precious metals in Sutter Creek\nHeading from Sutter Creek to Coloma holding 0 ounces of gold and 0 ounces of silver.\n") {pros.mine_last_two('Sutter Creek')}
  end

  # Ensures the new_location function is not called when 5 locations are visited
  # the only output will be resources found, which will be 0 due to the fakeRand Mock
  def test_mine_Last_two_ends
    fakeRand = Minitest::Mock.new ("fake prng")
    def fakeRand.rand(range); 0; end
    map = Map.new
    pros = Prospector.new(fakeRand, map)
    pros.instance_variable_set("@cities_visited", 4)
    assert_output("\tFound no precious metals in Sutter Creek\n") {pros.mine_last_two('Sutter Creek')}
  end

  # make sure an invalid city does not proceed with mining
  # EDGE CASE
  def test_invalid_city2
    fakeRand = Minitest::Mock.new ("fake prng")
    def fakeRand.rand(range); 0; end
    map = Map.new
    pros = Prospector.new(fakeRand, map)
    assert_nil pros.mine_last_two('Pittsburgh')
  end

  # UNIT TESTS FOR METHOD convert_resources
  # this method converts the resources found into dollar values
  # we just need a simply test to show correct values are calculated

  # tests proper conversion given arbitrary values of 6 gold and 6 silver
  def test_conversion
    fakeRand = Minitest::Mock.new ("fake prng")
    def fakeRand.rand(range); 0; end
    map = Map.new
    pros = Prospector.new(fakeRand, map)
    pros.instance_variable_set("@gold_total", 6)
    pros.instance_variable_set("@silver_total", 6)
    assert_equal 131.88, pros.convert_resources
  end

  # makes sure if no resources are found, the properly formatted '0' is still output 
  def test_conversion_zero
    fakeRand = Minitest::Mock.new ("fake prng")
    def fakeRand.rand(range); 0; end
    map = Map.new
    pros = Prospector.new(fakeRand, map)
    assert_equal 0.00, pros.convert_resources
  end

  # UNIT TEST FOR METHOD end_messages(pros_num, cash_total, days, gold, silver))
  # Equivalence classes:
  #   all inputs are valid, method executes
  #   any of the five arguments entered are negative, which returns nil

  # tests valid input using 0 as placeholder args
  def test_end_messages
    fakeRand = Minitest::Mock.new ("fake prng")
    def fakeRand.rand(range); 0; end
    map = Map.new
    pros = Prospector.new(fakeRand, map)
    assert_output("After 5 days, Prospector #1 returned to Pitt with:\n\t0 ounces of gold\n\t0 ounces of silver\n\tHeading home with $0.00") {pros.end_messages(1, 0.00, 5, 0, 0)}
  end

  # negative pros_num
  def test_invalid_pros_num
    fakeRand = Minitest::Mock.new ("fake prng")
    def fakeRand.rand(range); 0; end
    map = Map.new
    pros = Prospector.new(fakeRand, map)
    assert_nil pros.end_messages(-1, 0, 0, 0, 0)
  end
  
  # negative cash amount
  def test_invalid_cash
    fakeRand = Minitest::Mock.new ("fake prng")
    def fakeRand.rand(range); 0; end
    map = Map.new
    pros = Prospector.new(fakeRand, map)
    assert_nil pros.end_messages(0, -1.00, 0, 0, 0)
  end

  # negative days
  def test_invalid_days
    fakeRand = Minitest::Mock.new ("fake prng")
    def fakeRand.rand(range); 0; end
    map = Map.new
    pros = Prospector.new(fakeRand, map)
    assert_nil pros.end_messages(0, 0, -1, 0, 0)
  end

  #negative gold
  def test_invalid_gold
    fakeRand = Minitest::Mock.new ("fake prng")
    def fakeRand.rand(range); 0; end
    map = Map.new
    pros = Prospector.new(fakeRand, map)
    assert_nil pros.end_messages(0, 0, 0, -1, 0)
  end
  
  # negative silver
  def test_invalid_silver
    fakeRand = Minitest::Mock.new ("fake prng")
    def fakeRand.rand(range); 0; end
    map = Map.new
    pros = Prospector.new(fakeRand, map)
    assert_nil pros.end_messages(0, 0, 0, -1, 0)
  end
end
