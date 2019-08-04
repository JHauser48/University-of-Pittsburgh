require 'minitest/autorun'
require_relative 'map'

class MapTest < Minitest::Test

  # UNIT TESTS FOR METHOD get_city_index(city_name)
  # Equivalence classes:
  # Valid Cities:
  #    Sutter Creek      returns 0 
  #    Coloma			       returns 1
  #    Angels Camp		   returns 2
  #    Nevada City 		   returns 3
  #    Virginia City     returns 4
  #    Midas			       returns 5
  #    El Dorado Canyon  returns 6
  # Invalid Name: returns nil
  # Non-String:   returns nil

  # Sutter Creek
  def test_valid_city1
  	map = Map.new
  	assert_equal 0, map.get_city_index('Sutter Creek')
  end
  
  # Coloma
  def test_valid_city2
    map = Map.new
    assert_equal 1, map.get_city_index('Coloma')
  end

  # Angels Camp
  def test_valid_city3
    map = Map.new
    assert_equal 2, map.get_city_index('Angels Camp')
  end

  # Nevada City
  def test_valid_city4
    map = Map.new
    assert_equal 3, map.get_city_index('Nevada City')
  end

  # Virginia City
  def test_valid_city5
    map = Map.new
    assert_equal 4, map.get_city_index('Virginia City')
  end

  # Midas
  def test_valid_city6
    map = Map.new
    assert_equal 5, map.get_city_index('Midas')
  end
  
  #El Dorado Canyon
  def test_valid_city7
    map = Map.new
    assert_equal 6, map.get_city_index('El Dorado Canyon')
  end

  # invalid city
  # EDGE CASE
  def test_invalid_name
  	map = Map.new
  	assert_nil map.get_city_index('Sooter Crik')
  end

  # non-string city
  # EDGE CASE
  def test_non_string
  	map = Map.new
  	assert_nil map.get_city_index(6)
  end

  # UNIT TESTS FOR METHOD get_path(city_name)
  # Equivalence classes:
  # mostly here for code coverage, but good to double check
  # Valid Cities:
  #    Sutter Creek      returns map[0] 
  #    Coloma            returns map[1]
  #    Angels Camp       returns map[2]
  #    Nevada City       returns map[3]
  #    Virginia City     returns map[4]
  #    Midas             returns map[5]
  #    El Dorado Canyon  returns map[6]
  # Invalid Name: returns nil
  # Non-String:   returns nil

  # Sutter Creek path
  def test_valid_city_sutter
    map = Map.new
    assert_equal  ['Sutter Creek', 'Coloma', 'Angels Camp'], map.get_path('Sutter Creek')
  end
  
  # Coloma path
  def test_valid_city_coloma
    map = Map.new
    assert_equal ['Coloma', 'Sutter Creek', 'Virginia City'], map.get_path('Coloma')
  end

  # Angel Camp path
  def test_valid_city_angels
    map = Map.new
    assert_equal ['Angels Camp', 'Nevada City', 'Sutter Creek', 'Virginia City'], map.get_path('Angels Camp')
  end

  # Nevada path
  def test_valid_city_nevada
    map = Map.new
    assert_equal ['Nevada City', 'Angels Camp'], map.get_path('Nevada City')
  end

  # Virginia City path
  def test_valid_city_virginia
    map = Map.new
    assert_equal ['Virginia City', 'Angels Camp', 'Coloma', 'Midas', 'El Dorado Canyon'], map.get_path('Virginia City')
  end

  # Midas path
  def test_valid_city_midas
    map = Map.new
    assert_equal ['Midas', 'Virginia City', 'El Dorado Canyon'], map.get_path('Midas')
  end

  # El Dorado path
  def test_valid_city_el_dorado
    map = Map.new
    assert_equal ['El Dorado Canyon', 'Virginia City', 'Midas'], map.get_path('El Dorado Canyon')
  end

  # invalid name
  # EDGE CASE
  def test_invalid_name_path
    map = Map.new
    assert_nil map.get_path('Sooter Crik')
  end

  # non-string name
  # EDGE CASE
  def test_non_string_path
    map = Map.new
    assert_nil map.get_path(6)
  end
end