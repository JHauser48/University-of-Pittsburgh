require_relative 'map'
# prospector.rb
class Prospector
  def initialize(prng, map)
    @map = map
    @prng = prng
    @gold_total = 0
    @silver_total = 0
    @cur_location = 'Sutter Creek'
    @days_mining = 0
    @cities_visited = 0
    @resource_chart = resource_chart
  end

  def mine_iteration(city)
    city_i = @map.get_city_index(city)
    return nil if city_i.nil?

    @cities_visited += 1

    loop do
      @days_mining += 1
      gold = mine_gold(city_i)
      silver = mine_silver(city_i)
      display_find(gold, silver, city)
      break if silver.zero? && gold.zero?
    end
    new_location
  end

  def mine_last_two(city)
    city_i = @map.get_city_index(city)
    return nil if city_i.nil?

    @cities_visited += 1

    loop do
      @days_mining += 1
      gold = mine_gold(city_i)
      silver = mine_silver(city_i)
      display_find(gold, silver, city)
      break if silver <= 2 && gold <= 1
    end

    new_location unless @cities_visited == 5
  end

  def mine_gold(city_i)
    gold = 0
    gold = random_val(@resource_chart[city_i][0] + 1) unless @resource_chart[city_i][0].zero?
    @gold_total += gold
    gold
  end

  def mine_silver(city_i)
    silver = 0
    silver = random_val(@resource_chart[city_i][1] + 1) unless @resource_chart[city_i][1].zero?
    @silver_total += silver
    silver
  end

  def new_location
    city_i = @map.get_city_index(@cur_location)
    return nil if city_i < 0 || city_i > 6

    path = @map.get_path(@cur_location)
    range = path.length - 1
    new_city_i = random_val(range) + 1
    new_city = path[new_city_i]

    print 'Heading from ' + @cur_location + ' to ' + new_city
    print ' holding ' + @gold_total.to_s + ' ounces of gold and ' + @silver_total.to_s + " ounces of silver.\n"
    @cur_location = new_city
    new_city
  end

  def display_find(gold, silver, city)
    puts "\tFound no precious metals in " + city if gold.zero? && silver.zero?

    if gold == 1
      print "\tFound 1 ounce of gold in " + city
      print "\n" if silver.zero?
    elsif gold > 1
      print "\tFound " + gold.to_s + ' ounces of gold in ' + city
      print "\n" if silver.zero?
    end

    if gold.zero?
      if silver == 1
        puts "\tFound 1 ounce of silver in " + city
      elsif silver > 1
        puts "\tFound " + silver.to_s + ' ounces of silver in ' + city
      end
    elsif silver == 1
      print ' and 1 ounce of silver in ' + city + "\n"
    elsif silver > 1
      puts ' and ' + silver.to_s + ' ounces of silver in ' + city + "\n"
    end
  end

  def convert_resources
    gold_cash = @gold_total * 20.67
    silver_cash = @silver_total * 1.31
    total_cash = (gold_cash + silver_cash).round(2)
    total_cash
  end

  def end_messages(pros_num, cash_total, days, gold, silver)
    return nil if pros_num < 0 || cash_total < 0 || days < 0 || gold < 0 || silver < 0

    print 'After ' + days.to_s + ' days, Prospector #' + pros_num.to_s + " returned to Pitt with:\n"
    print "\t" + gold.to_s + " ounces of gold\n"
    print "\t" + silver.to_s + " ounces of silver\n"
    print "\tHeading home with $" + format('%0.2f', cash_total.to_s)
  end

  def location
    @cur_location
  end

  def days
    @days_mining
  end

  def gold
    @gold_total
  end

  def silver
    @silver_total
  end

  def random_val(range)
    return nil if (range.is_a? Integer) == false || range < 0

    random_val = @prng.rand(range)
    random_val
  end

  def resource_chart
    [
      [2, 0],
      [3, 0],
      [4, 0],
      [5, 0],
      [3, 3],
      [0, 5],
      [0, 10]
    ]
  end
end
