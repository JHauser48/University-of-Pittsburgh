require 'minitest/autorun'
require_relative 'gold_rush_sim'

class GoldRushSimTest < Minitest::Test 
    
  # UNIT TEST FOR METHOD run_sim(pros)

  # the run sim returns the number of iterations, which should be the number of prospectors
  # this method stubs the actual methods used and just checks that 1 prospector returns 1 iteration
  def test_run_sim
    sim = GoldRushSim.new(1, 1)
    def sim.pros_sim(a, b); nil; end
    def sim.start_message(i); nil; end
    assert_equal 1, sim.run_sim
  end

  # UNIT TEST FOR METHOD pros_sim(pros, i)

  # this test checks that the simulation for a given prospector executes properly
  # a fake prospector is used, where multiple methods are made into mocks to prevent output
  def test_pros_sim
    sim = GoldRushSim.new(1, 1)
    fakePros = Minitest::Mock.new("fake prospector")
    def fakePros.location; 'Sutter Creek'; end
  	def fakePros.mine_iteration(param); 1; end
  	def fakePros.mine_last_two(param); 1; end
  	def fakePros.convert_resources(); 1; end
  	def fakePros.days; 1; end
  	def fakePros.gold; 1; end
  	def fakePros.silver; 1; end
  	def fakePros.end_messages(a, b, c, d, e); nil; end
    assert_equal true, sim.pros_sim(fakePros, 0)
  end

  # UNIT TESTS FOR METHOD mine(pros)
  # the only test needed is to assert this function runs for 5 iterations
  
  def test_mine_iterations
    sim = GoldRushSim.new(1, 1)
    fakeRand = Minitest::Mock.new ("fake prng")
  	def fakeRand.rand(range); 1; end
  	map = Map.new

  	fakePros = Minitest::Mock.new("fake prospector")
  	def fakePros.location; 'Sutter Creek'; end
  	def fakePros.mine_iteration(param); 1; end
  	def fakePros.mine_last_two(param); 1; end
    assert_equal 5, sim.mine(fakePros)
  end
end