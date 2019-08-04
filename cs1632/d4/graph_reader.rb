require_relative 'node'
# class to read and create graphs in from .txt files
class GraphReader
  attr_accessor :graph

  def initialize(filename)
    @file = filename
    @graph = []
  end

  def create_graph
    f = File.open(@file)
    f.each do |line|
      read_line(line)
    end
    f.close
    @graph
  rescue SystemCallError
    puts 'Error: file not found'
    -1
  end

  def read_line(line)
    tokens = line.strip.split(';')
    number = tokens[0].to_i
    letter = tokens[1]
    neighbors_s = tokens[2].split(',') unless tokens[2].nil?
    neighbors = neighbors_s.map(&:to_i) unless neighbors_s.nil?
    add_node(number, letter, neighbors)
  end

  def get_node(number)
    return nil if number < 0 || number.nil? || number >= @graph.length

    @graph[number]
  end

  def add_node(number, letter, neighbors)
    return nil if number.nil? || letter.nil?

    n = Node.new(number, letter, neighbors)
    @graph[number] = n
  end

  def print_graph
    i = 0
    @graph.each do
      graph[i].print_node unless graph[i].nil?
      i += 1
    end
  end
end
