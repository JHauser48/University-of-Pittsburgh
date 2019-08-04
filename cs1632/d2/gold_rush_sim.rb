require_relative 'prospector'
require_relative 'map'

# gold_rush_sim
class GoldRushSim
  def initialize(seed, prospectors)
    @prng = Random.new(seed)
    @prospectors = prospectors
    @map = Map.new
    @pros_array = Array.new(prospectors)
  end

  def run_sim
    i = 0
    @pros_array.each do
      @pros_array[i] = Prospector.new(@prng, @map)
      start_message(i)
      pros_sim(@pros_array[i], i)
      i += 1
    end
    i
  end

  def start_message(itr)
    print "\n\nProspector " + (itr + 1).to_s + " starting in Sutter Creek\n"
  end

  def pros_sim(pros, itr)
    mine(pros)
    cash_total = pros.convert_resources
    pros.end_messages(itr + 1, cash_total, pros.days, pros.gold, pros.silver)
    true
  end

  def mine(pros)
    location_count = 0
    while location_count < 5
      location_count += 1
      if location_count < 3
        pros.mine_iteration(pros.location)
      else
        pros.mine_last_two(pros.location)
      end
    end
    location_count
  end
end
