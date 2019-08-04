# map class for gold rush simulator
class Map
  def initialize
    @map = [
      ['Sutter Creek', 'Coloma', 'Angels Camp'],
      ['Coloma', 'Sutter Creek', 'Virginia City'],
      ['Angels Camp', 'Nevada City', 'Sutter Creek', 'Virginia City'],
      ['Nevada City', 'Angels Camp'],
      ['Virginia City', 'Angels Camp', 'Coloma', 'Midas', 'El Dorado Canyon'],
      ['Midas', 'Virginia City', 'El Dorado Canyon'],
      ['El Dorado Canyon', 'Virginia City', 'Midas']
    ]
  end

  def get_path(city_name)
    array =
      case city_name
      when 'Sutter Creek'
        @map[0]
      when 'Coloma'
        @map[1]
      when 'Angels Camp'
        @map[2]
      when 'Nevada City'
        @map[3]
      when 'Virginia City'
        @map[4]
      when 'Midas'
        @map[5]
      when 'El Dorado Canyon'
        @map[6]
      end
    array
  end

  def get_city_index(city_name)
    index =
      case city_name
      when 'Sutter Creek'
        0
      when 'Coloma'
        1
      when 'Angels Camp'
        2
      when 'Nevada City'
        3
      when 'Virginia City'
        4
      when 'Midas'
        5
      when 'El Dorado Canyon'
        6
      end
    index
  end
end
