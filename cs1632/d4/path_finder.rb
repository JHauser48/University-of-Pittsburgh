require_relative 'graph_reader'
# main path finding class
class PathFinder
  attr_accessor :all_paths
  attr_accessor :path
  def initialize(graph_reader)
    @graph_reader = graph_reader
    @all_paths = []
    @graph = @graph_reader.graph
    @path = ''
  end

  def find_all_paths
    @graph.each do |n|
      get_paths(n) unless n.nil?
    end
  end

  def print_node(number, letter, neighbors)
    print 'Starting from Node ' + number.to_s + ': ' + letter + ' - '
    !neighbors.nil? ? print(neighbors) : print('[ --- ]')
    print "...\n"
  end

  def get_paths(start_node)
    if start_node.neighbors.nil?
      @path = start_node.letter
      @all_paths.push(@path)
      @path
    else
      start_search(start_node)
    end
  end

  # beginning of the recursive function
  def start_search(start_node)
    start_node.neighbors.each do |i|
      @path = ''
      cur_node = @graph_reader.get_node(i)
      @path.concat(start_node.letter)
      search_path(start_node, cur_node)
    end
  end

  # main recursive function
  def search_path(pre_node, cur_node)
    @path.concat(cur_node.letter)
    if cur_node.neighbors.nil?
      @all_paths.push(@path)
    else cur_node.neighbors.each do |n|
      pre_node = cur_node
      cur_node = @graph_reader.get_node(n)
      search_path(pre_node, cur_node)
      @path = @path.chop
    end
    end
  end

  def print_paths
    @all_paths.each do |path|
      puts path
    end
  end
end