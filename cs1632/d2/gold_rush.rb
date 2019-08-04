# gold_rush.rb
require_relative 'gold_rush_sim'
require_relative 'check_args'

args = CheckArgs.new
valid = args.check_args(ARGV)

if valid
  seed = ARGV[0].to_i
  num_prospectors = ARGV[1].to_i
  sim = GoldRushSim.new(seed, num_prospectors)
  sim.run_sim
else
  args.show_usage
  exit 1
end
