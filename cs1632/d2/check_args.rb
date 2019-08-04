# check_args checks for valid command line argumenst when starting the gold_rush simulation
class CheckArgs
  def check_args(args)
    args.count == 2 && args[0].to_i > 0 && args[1].to_i > 0
  end

  def show_usage
    usage = "Usage:\nruby gold_rush.rb"
    cmd = " *seed* *num_prospectors*\n"
    reqs1 = "*seed* should be an non-negative integer\n"
    reqs2 = "*num_prospectors* should be a non-negative integer\n"
    puts usage + cmd + reqs1 + reqs2
  end
end
